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
import org.project.sfc.com.SfcHandler.SFC;
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

/**
 * Created by maa on 11.11.15.
 */
@Service
public class OpenbatonEventSubscription implements CommandLineRunner {

    private NFVORequestor requestor;
    private Logger logger;
    @Autowired private NfvoConfiguration configuration;
    @Autowired
    private SFCAllocator creator;
    @Autowired
    private Gson mapper;
    private List<String> eventIds;
    private Properties properties;
    private String Prev_NSR;

    private void init() throws SDKException, IOException {

        this.logger = LoggerFactory.getLogger(this.getClass());
        this.properties=ConfigReader.readProperties();
/*
        if (configuration.isSecurity()){
             this.requestor = new NFVORequestor(configuration.getUsername(), configuration.getPassword(), configuration.getBaseURL(), configuration.getBasePort(), "1");
        }
        else{
            this.requestor = new NFVORequestor("","",configuration.getBaseURL(),configuration.getBasePort(),"1");
        }
*/
        this.requestor = new NFVORequestor(properties.getProperty("nfvo.username"),properties.getProperty("nfvo.password"),properties.getProperty("nfvo.baseURL"),properties.getProperty("nfvo.basePort"),"1");
        this.eventIds = new ArrayList<String>();
        // For NSR Create
        EventEndpoint eventEndpointCreation = new EventEndpoint();
        eventEndpointCreation.setType(EndpointType.RABBIT);
        eventEndpointCreation.setEvent(Action.INSTANTIATE_FINISH);
        eventEndpointCreation.setEndpoint(ConfigurationBeans.queueName_eventInstatiateFinish);
        eventEndpointCreation.setName("eventNSRCreated");
        eventEndpointCreation = requestor.getEventAgent().create(eventEndpointCreation);


        // For NSR Delete
        EventEndpoint eventEndpointDeletion = new EventEndpoint();
        eventEndpointDeletion.setType(EndpointType.RABBIT);
        eventEndpointDeletion.setEvent(Action.RELEASE_RESOURCES_FINISH);

        eventEndpointDeletion.setEndpoint(ConfigurationBeans.queueName_eventResourcesReleaseFinish);
        eventEndpointDeletion.setName("eventNSRRelased");
        eventEndpointDeletion = requestor.getEventAgent().create(eventEndpointDeletion);

        // For Fault Management
        EventEndpoint eventEndpointHealing = new EventEndpoint();
        eventEndpointHealing.setType(EndpointType.RABBIT);
        eventEndpointHealing.setEvent(Action.HEAL);
        eventEndpointHealing.setEndpoint(ConfigurationBeans.queueName_eventHeal);
        eventEndpointHealing.setName("eventVNFHealed");
        eventEndpointHealing = requestor.getEventAgent().create(eventEndpointHealing);

        this.eventIds.add(eventEndpointCreation.getId());
        this.eventIds.add(eventEndpointDeletion.getId());
        this.eventIds.add(eventEndpointHealing.getId());

        Prev_NSR="";



    }

    public void receiveNewNsr(String message) {

        logger.debug("received new event " + message);
        OpenbatonEvent evt;

        try {
            logger.debug("Trying to deserialize it");
            evt = getOpenbatonEvent(message);
            logger.debug("Received nfvo event with action: " + evt.getAction());
            NetworkServiceRecord nsr = getNsrFromPayload(evt.getPayload());
            List<VirtualNetworkFunctionRecord> list_vnfrs=new ArrayList<VirtualNetworkFunctionRecord>();
            if(nsr.getId().equals(Prev_NSR)){
                logger.info("[OPENBATON-EVENT-SUBSCRIPTION] event MESSAGE is repeated at " + new Date().getTime());


            }else {


                creator.addSFtoChain(nsr.getVnfr(), nsr);
                Prev_NSR=nsr.getId();
                logger.info("[OPENBATON-EVENT-SUBSCRIPTION] Ended message callback function at " + new Date().getTime());

            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return;
        }


    }


    public void receiveVNFHealEvent(String message) {


        logger.debug("received new event " + message);
        OpenbatonEvent evt;

        try {
            logger.debug("Trying to deserialize it");
            evt = getOpenbatonEvent(message);
            logger.debug("Received VNF event with action: " + evt.getAction());
            VirtualNetworkFunctionRecord vnfr = getVnfrFromPayload(evt.getPayload());
            if (evt.getAction().ordinal() == Action.HEAL.ordinal()){
                for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
                    for (VNFCInstance vnfc_instance : vdu.getVnfc_instance()) {

                        logger.info("[OPENBATON-EVENT-SUBSCRIPTION] VNFC instance State: " + vnfc_instance.getState() + ", VNFC instance Name " + vnfc_instance.getHostname());
                        //list_vnfrs.add(vnfr);

                        // break vnfrloop;
                    }
                }
                creator.ChangeChainPath(vnfr);
            }

        } catch (Exception e) {
                logger.error(e.getMessage(),e);
            return;
        }
    }

    public void deleteNsr(String message){
        logger.debug("received new event " + message);
        OpenbatonEvent evt;

        try {
            logger.debug("Trying to deserialize it");
            evt = getOpenbatonEvent(message);
            logger.debug("Received nfvo event with action: " + evt.getAction());
            NetworkServiceRecord nsr = getNsrFromPayload(evt.getPayload());
            for(VNFForwardingGraphRecord vnffgr:nsr.getVnffgr()) {
                creator.removeSFC(vnffgr.getId());
      /*      try {
                Thread.sleep(100);                 //100 milliseconds is 0.1 second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
            return;
        }

    }


    private OpenbatonEvent getOpenbatonEvent(String message) throws NFVORequestorException {
        OpenbatonEvent openbatonEvent;

        try {
            openbatonEvent = mapper.fromJson(message, OpenbatonEvent.class);
        } catch (JsonParseException e) {
            throw new NFVORequestorException(e.getMessage(),e);
        }
        return openbatonEvent;
    }

    private NetworkServiceRecord getNsrFromPayload(JsonObject nsrJson) throws NFVORequestorException {
        NetworkServiceRecord networkServiceRecord;
        try {
            networkServiceRecord = mapper.fromJson(nsrJson, NetworkServiceRecord.class);
        } catch (JsonParseException e) {
            throw new NFVORequestorException(e.getMessage(),e);
        }
        return networkServiceRecord;
    }
    private VirtualNetworkFunctionRecord getVnfrFromPayload(JsonObject vnfrJson) throws NFVORequestorException {
        VirtualNetworkFunctionRecord vnfr;
        try {
            vnfr = mapper.fromJson(vnfrJson, VirtualNetworkFunctionRecord.class);
        } catch (JsonParseException e) {
            throw new NFVORequestorException(e.getMessage(),e);
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
