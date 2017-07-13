package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/11/16.
 */
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.nfvo.Network;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.openbaton.exceptions.NotFoundException;
import org.project.sfc.com.MonitoringAgent.MonitoringManager;
import org.project.sfc.com.openbaton_nfvo.configurations.NfvoConfiguration;
import org.project.sfc.com.openbaton_nfvo.openbaton.OpenbatonEvent;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigReader;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigurationBeans;
import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.catalogue.nfvo.EndpointType;
import org.openbaton.catalogue.nfvo.EventEndpoint;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.sfc.com.openbaton_nfvo.openbaton.Exceptions.NFVORequestorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import org.openbaton.catalogue.security.Project;
/**
 * Created by maa on 11.11.15.
 */
@Service
public class OpenbatonEventSubscription implements CommandLineRunner {

  private NFVORequestor requestor;
  private Logger logger;
  @Autowired private NfvoConfiguration configuration;
  @Autowired private MonitoringManager monitoringManager;
  @Autowired private SFCAllocator creator;
  @Autowired private Gson mapper;
  private List<String> eventIds;
  private Properties properties;
  private String Prev_NSR;
  private String projectId;

  private void init() throws SDKException, IOException, ClassNotFoundException {

    this.logger = LoggerFactory.getLogger(this.getClass());
    this.properties = ConfigReader.readProperties();

    this.requestor =
        new NFVORequestor(
            configuration.getUsername(),
            configuration.getPassword(),
            null,
            false,
            configuration.getIP(),
            configuration.getPort(),
            "1");
    this.eventIds = new ArrayList<String>();
    setProjectId();

    // For NSR Create
    EventEndpoint eventEndpointCreation = new EventEndpoint();
    eventEndpointCreation.setType(EndpointType.RABBIT);
    eventEndpointCreation.setEvent(Action.INSTANTIATE_FINISH);
    eventEndpointCreation.setEndpoint(ConfigurationBeans.queueName_eventInstatiateFinish);
    eventEndpointCreation.setName("SFC-event-NSR-Created");
    eventEndpointCreation = requestor.getEventAgent().create(eventEndpointCreation);

    // For NSR Delete
    EventEndpoint eventEndpointDeletion = new EventEndpoint();
    eventEndpointDeletion.setType(EndpointType.RABBIT);
    eventEndpointDeletion.setEvent(Action.RELEASE_RESOURCES_FINISH);

    eventEndpointDeletion.setEndpoint(ConfigurationBeans.queueName_eventResourcesReleaseFinish);
    eventEndpointDeletion.setName("SFC-event-NSR-Relased");
    eventEndpointDeletion = requestor.getEventAgent().create(eventEndpointDeletion);

    // For Fault SFCdictRepoImpl
    EventEndpoint eventEndpointHealing = new EventEndpoint();
    eventEndpointHealing.setType(EndpointType.RABBIT);
    eventEndpointHealing.setEvent(Action.HEAL);
    eventEndpointHealing.setEndpoint(ConfigurationBeans.queueName_eventHeal);
    eventEndpointHealing.setName("SFC-event-VNF-Healed");
    eventEndpointHealing = requestor.getEventAgent().create(eventEndpointHealing);

    // For Auto-Scaling
    EventEndpoint eventEndpointScaled = new EventEndpoint();
    eventEndpointScaled.setType(EndpointType.RABBIT);
    eventEndpointScaled.setEvent(Action.SCALED);
    eventEndpointScaled.setEndpoint(ConfigurationBeans.queueName_eventScaled);
    eventEndpointScaled.setName("SFC-event-VNF-Scaled");
    eventEndpointScaled = requestor.getEventAgent().create(eventEndpointScaled);

    this.eventIds.add(eventEndpointCreation.getId());
    this.eventIds.add(eventEndpointDeletion.getId());
    this.eventIds.add(eventEndpointHealing.getId());
    this.eventIds.add(eventEndpointScaled.getId());

    Prev_NSR = "";
  }

  private void setProjectId() throws ClassNotFoundException, SDKException {
    if (projectId == null || projectId.isEmpty()) {
      logger.debug("Trying to connect to the NFVO...");
      List<Project> projects = requestor.getProjectAgent().findAll();
      logger.debug("Found " + projects.size() + " projects");

      for (Project project : projects) {
        if (project.getName().equals("default")) {
          projectId = project.getId();
        }
      }
      if (projectId == null) throw new SDKException("Project not found");
      requestor.setProjectId(projectId);
    }
  }

  public void receiveNewNsr(String message) throws SDKException, ClassNotFoundException {

    logger.debug("[Received NS released Event ] " + message);
    OpenbatonEvent evt;
    setProjectId();

    try {
      logger.debug("Trying to deserialize it");
      evt = getOpenbatonEvent(message);
      logger.debug("Received NFVO event with action: " + evt.getAction());
      NetworkServiceRecord nsr = getNsrFromPayload(evt.getPayload());
      List<VirtualNetworkFunctionRecord> list_vnfrs = new ArrayList<VirtualNetworkFunctionRecord>();
      if (nsr.getId().equals(Prev_NSR)) {
        logger.info(
            "[OPENBATON-EVENT-SUBSCRIPTION] event MESSAGE is repeated at " + new Date().getTime());

      } else {

        creator.addSFtoChain(nsr.getVnfr(), nsr);
        boolean lastvnfr = false;
        int VNFR_counter = 0;
        for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
          try {
            if (VNFR_counter == nsr.getVnfr().size() - 1) {
              lastvnfr = true;
            }
            monitoringManager.start(
                vnfr, properties.getProperty("sf.monitoring.item"), 60, lastvnfr);
          } catch (NotFoundException e) {
            logger.error(e.getMessage(), e);
          }
          VNFR_counter++;
        }

        Prev_NSR = nsr.getId();
        logger.info(
            "[OPENBATON-EVENT-SUBSCRIPTION] Ended message callback function at "
                + new Date().getTime());
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return;
    }
  }

  public void receiveVNFHealEvent(String message) throws SDKException, ClassNotFoundException {

    logger.info("[Received VNF Heal Event ]" + message);
    OpenbatonEvent evt;
    setProjectId();
    try {
      logger.debug("Trying to deserialize it");
      evt = getOpenbatonEvent(message);
      logger.info("Received VNF event with action: " + evt.getAction());
      logger.info("Received VNF event with payload: " + evt.getPayload());
      VirtualNetworkFunctionRecord vnfr = getVnfrFromPayload(evt.getPayload());

      boolean lastvnfr = true;

      if (evt.getAction().ordinal() == Action.HEAL.ordinal()) {

        for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
          for (VNFCInstance vnfc_instance : vdu.getVnfc_instance()) {
            logger.info("VNFR: " + vnfr.getName());

            logger.info(
                "[OPENBATON-EVENT-SUBSCRIPTION] FM - VNFC instance State: "
                    + vnfc_instance.getState()
                    + ", VNFC instance Name "
                    + vnfc_instance.getHostname());
          }
        }
        monitoringManager.stop(vnfr);
        monitoringManager.start(vnfr, properties.getProperty("sf.monitoring.item"), 60, lastvnfr);
        creator.ChangeChainPath(vnfr);
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return;
    }
  }

  public void receiveVNFScaledEvent(String message) throws SDKException, ClassNotFoundException {

    logger.info("[Received VNF Scaled Event ]" + message);
    OpenbatonEvent evt;
    setProjectId();
    boolean flag = false;
    try {
      logger.debug("Trying to deserialize it");
      evt = getOpenbatonEvent(message);
      logger.info("[Received VNF event with action]: " + evt.getAction());
      VirtualNetworkFunctionRecord vnfr = getVnfrFromPayload(evt.getPayload());

      if (evt.getAction().ordinal() == Action.SCALED.ordinal()) {
        boolean NotAS = false;
        for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
          for (VNFCInstance vnfc_instance : vdu.getVnfc_instance()) {

            logger.info(
                "[OPENBATON-EVENT-SUBSCRIPTION] VNFC instance State: "
                    + vnfc_instance.getState()
                    + ", VNFC instance Name "
                    + vnfc_instance.getHostname());

            try {
              if (!vnfc_instance.getState().equals("ACTIVE")) {
                NotAS = true;
                logger.info("::: It is not AutoScaling");
                break;
              }
            } catch (NullPointerException e) {
              logger.info(
                  "::: The state is null for this VNF instance: " + vnfc_instance.getHostname());
            }
          }
          if (NotAS) {
            break;
          }
        }

        if (!NotAS) {
          logger.info("::: It is an Auto-Scaling Event :::");

          boolean lastvnfr = true;
          monitoringManager.stop(vnfr);
          monitoringManager.start(vnfr, properties.getProperty("sf.monitoring.item"), 60, lastvnfr);
          creator.ScalePaths(vnfr);

        } else {
          logger.info("::: It is a Fault Sfc Management Event :::");
        }
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return;
    }
  }

  public void deleteNsr(String message) throws SDKException, ClassNotFoundException {
    logger.debug("[Received New Event ] " + message);
    OpenbatonEvent evt;
    setProjectId();

    try {
      logger.debug("Trying to deserialize it");
      evt = getOpenbatonEvent(message);
      logger.debug("Received nfvo event with action: " + evt.getAction());
      NetworkServiceRecord nsr = getNsrFromPayload(evt.getPayload());

      for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
        try {
          monitoringManager.stop(vnfr);
        } catch (NotFoundException e) {
          logger.error(e.getMessage(), e);
        }
      }
      int counter = 0;
      for (VNFForwardingGraphRecord vnffgr : nsr.getVnffgr()) {
        counter++;
        if (counter == nsr.getVnffgr().size()) {
          logger.debug("Deleting Last SFC ");

          creator.removeSFC(vnffgr.getId(), true);
        } else {
          creator.removeSFC(vnffgr.getId(), false);
        }
      }
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
      return;
    }
  }

  private OpenbatonEvent getOpenbatonEvent(String message)
      throws NFVORequestorException, SDKException, ClassNotFoundException {
    OpenbatonEvent openbatonEvent;
    setProjectId();

    try {
      openbatonEvent = mapper.fromJson(message, OpenbatonEvent.class);
    } catch (JsonParseException e) {
      throw new NFVORequestorException(e.getMessage(), e);
    }
    return openbatonEvent;
  }

  private NetworkServiceRecord getNsrFromPayload(JsonObject nsrJson)
      throws NFVORequestorException, SDKException, ClassNotFoundException {
    NetworkServiceRecord networkServiceRecord;
    setProjectId();
    try {
      networkServiceRecord = mapper.fromJson(nsrJson, NetworkServiceRecord.class);
    } catch (JsonParseException e) {
      throw new NFVORequestorException(e.getMessage(), e);
    }
    return networkServiceRecord;
  }

  private VirtualNetworkFunctionRecord getVnfrFromPayload(JsonObject vnfrJson)
      throws NFVORequestorException, SDKException, ClassNotFoundException {
    setProjectId();
    VirtualNetworkFunctionRecord vnfr;
    try {
      vnfr = mapper.fromJson(vnfrJson, VirtualNetworkFunctionRecord.class);
    } catch (JsonParseException e) {
      throw new NFVORequestorException(e.getMessage(), e);
    }
    return vnfr;
  }

  @PreDestroy
  private void dispose() throws SDKException {
    for (String id : this.eventIds) {
      requestor.getEventAgent().delete(id);
    }
  }

  @Override
  public void run(String... args) throws Exception {
    init();
  }
}
