package org.project.sfc.com.MonitoringAgent;


import org.openbaton.catalogue.mano.common.AutoScalePolicy;
import org.openbaton.catalogue.mano.common.ScalingAlarm;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Item;
import org.openbaton.catalogue.nfvo.Server;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.openbaton.exceptions.MonitoringException;
import org.openbaton.exceptions.NotFoundException;
import org.openbaton.exceptions.VimDriverException;
import org.openbaton.monitoring.interfaces.MonitoringPlugin;
import org.openbaton.monitoring.interfaces.MonitoringPluginCaller;
import org.openbaton.plugin.utils.RabbitPluginBroker;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.openbaton.vim.drivers.VimDriverCaller;
import org.project.sfc.com.openbaton_nfvo.configurations.NfvoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import org.openbaton.vim.drivers.interfaces.VimDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import org.openbaton.catalogue.security.Project;
/**
 * Created by mah on 11/7/16.
 */
@Service
@Scope("singleton")
public class MonitoringEngine {

  protected Logger log = LoggerFactory.getLogger(this.getClass());
  @Autowired private ConfigurableApplicationContext context;
  private String projectId;
  private MonitoringPlugin monitor;
  private VimDriver Vim;
  @Autowired private Environment env;
  private NFVORequestor requestor;
  @Autowired private NfvoConfiguration configuration;

  private MonitoringManager monitoringManager;

 /* @PostConstruct
  public void init() {
    this.monitoringManager = context.getBean(MonitoringManager.class);
  }*/

  public void initializeMonitor() throws NotFoundException{
    System.out.print("[Initialize Monitoring Engine]");
    try {
      this.monitor = new MonitoringPluginCaller("zabbix", "zabbix-plugin");
    } catch (TimeoutException e) {
      log.error(e.getMessage(), e);
    } catch (NotFoundException e) {
      log.error(e.getMessage(), e);
      throw e;
    }catch( IOException e){
      log.error(e.getMessage(), e);
    }
    log.debug("MonitoringPluginCaller obtained");

      /*  (MonitoringPluginCaller)
            ((RabbitPluginBroker) context.getBean(RabbitPluginBroker.class))
                .getMonitoringPluginCaller(
                    env.getProperty("spring.rabbitmq.host"),
                    env.getProperty("spring.rabbitmq.username"),
                    env.getProperty("spring.rabbitmq.password"),
                    Integer.parseInt(env.getProperty("spring.rabbitmq.port")),
                    "zabbix-plugin",
                    "zabbix",
                    env.getProperty("SFCO.rabbitmq.management.port"));*/

    if (monitor == null) {
      log.warn("MonitoringTask: Monitor was not found. Cannot start Monitoring...");
    }

  }
  public void initializeVimDriver() throws NotFoundException, SDKException, ClassNotFoundException {
    System.out.print("[Initialize VimDriver]");
    try {
      this.Vim=new VimDriverCaller("openstack");
    } catch (TimeoutException e) {
      log.error(e.getMessage(), e);
    } catch (NotFoundException e) {
      log.error(e.getMessage(), e);
      throw e;
    }catch( IOException e){
      log.error(e.getMessage(), e);
    }
    log.debug("VimDriverCaller obtained");
    this.requestor =
        new NFVORequestor(
            configuration.getUsername(),
            configuration.getPassword(),
            null,
            false,
            configuration.getIP(),
            configuration.getPort(),
            "1");
      /*  (MonitoringPluginCaller)
            ((RabbitPluginBroker) context.getBean(RabbitPluginBroker.class))
                .getMonitoringPluginCaller(
                    env.getProperty("spring.rabbitmq.host"),
                    env.getProperty("spring.rabbitmq.username"),
                    env.getProperty("spring.rabbitmq.password"),
                    Integer.parseInt(env.getProperty("spring.rabbitmq.port")),
                    "zabbix-plugin",
                    "zabbix",
                    env.getProperty("SFCO.rabbitmq.management.port"));*/
    setProjectId();


    if( Vim == null){
      log.warn("VimDriverTask: Vim was not found. Cannot get Info about VNF instances Location...");

    }
  }
  private void setProjectId() throws ClassNotFoundException, SDKException {
    if (projectId == null || projectId.isEmpty()) {
      log.debug("Trying to connect to the NFVO...");
      List<Project> projects = requestor.getProjectAgent().findAll();
      log.debug("found " + projects.size() + " projects");

      for (Project project : projects) {
        if (project.getName().equals("default")) {
          projectId = project.getId();
        }
      }
      if (projectId == null) throw new SDKException("Project not found");
      requestor.setProjectId(projectId);
    }
  }

  public List<Item> getRawMeasurementResults(
      VirtualNetworkFunctionRecord vnfr, String metric, String period) throws MonitoringException, NotFoundException {
    if (monitor == null) {
      System.out.print("Monitor is null then Initialize Monitoring Engine");

      initializeMonitor();
    }
    ArrayList<Item> measurementResults = new ArrayList<Item>();
    ArrayList<String> hostnames = new ArrayList<String>();
    ArrayList<String> metrics = new ArrayList<String>();
    metrics.add(metric);
    log.debug(
        "Getting all measurement results for vnfr " + vnfr.getId() + " on metric " + metric + ".");
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance vnfcInstance : vdu.getVnfc_instance()) {
        if (vnfcInstance.getState() == null
            || vnfcInstance.getState().toLowerCase().equals("active")) {
          hostnames.add(vnfcInstance.getHostname());
        }
      }
    }
    log.trace(
        "Getting all measurement results for hostnames "
        + hostnames
        + " on metric "
        + metric
        + ".");
    measurementResults.addAll(monitor.queryPMJob(hostnames, metrics, period));
    log.info(
        "Got all measurement results for vnfr "
        + vnfr.getId()
        + " on metric "
        + metric
        + " -> "
        + measurementResults
        + ".");
    return measurementResults;
  }

  public String getLocation(String vnf_instance_name) throws
                                                                      NotFoundException,
                                                                      ClassNotFoundException,
                                                                      SDKException, VimDriverException {
    if (Vim == null) {
      System.out.print("VimDriver is null then Initialize Monitoring Engine");

      initializeVimDriver();
    }
    String Location="";
    System.out.print("PROJECT ID is"+  requestor.getProjectId());

    List<VimInstance> VimInstances=requestor.getVimInstanceAgent().findAll();
    for (VimInstance Vim_instance:VimInstances){
      List<Server> VNF_instances=Vim.listServer(Vim_instance);
      for (Server VNF_instance:VNF_instances){
        if(VNF_instance.getName().equals(vnf_instance_name)) {
          if(VNF_instance.getHypervisorHostName()!=null){
            Location=VNF_instance.getHypervisorHostName();
          }
        }
      }
    }

    log.info(
        "Got Location for VNF instance "
        + vnf_instance_name
        + " -> "
        + Location
        + ".");
    return Location;


  }
}
