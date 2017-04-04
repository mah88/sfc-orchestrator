package org.project.sfc.com.MonitoringAgent;

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.exceptions.NotFoundException;
import org.project.sfc.com.SfcRepository.VNFdictRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

/**
 * Created by mah on 11/7/16.
 */
@Service
@Scope("singleton")
public class MonitoringManager {

  protected Logger log = LoggerFactory.getLogger(this.getClass());

  private ThreadPoolTaskScheduler taskScheduler;

  private Map<String, Map<String, ScheduledFuture>> monitoringTasks;

  @Autowired private MonitoringEngine monitoringEngine;
  @Autowired private VNFdictRepo vnfManag;

  @PostConstruct
  public void init() {
    log.debug("[ Initialize Monitoring Manager ] ");
    this.monitoringTasks = new HashMap<String, Map<String, ScheduledFuture>>();
    this.taskScheduler = new ThreadPoolTaskScheduler();
    this.taskScheduler.setPoolSize(10);
    this.taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
    this.taskScheduler.setRemoveOnCancelPolicy(true);
    this.taskScheduler.setErrorHandler(
        new ErrorHandler() {
          protected Logger log = LoggerFactory.getLogger(this.getClass());

          @Override
          public void handleError(Throwable t) {
            log.error(t.getMessage(), t);
          }
        });
    this.taskScheduler.initialize();
  }

  public void start(VirtualNetworkFunctionRecord vnfr, String Metric, int Period, boolean LastVNFR)
      throws NotFoundException, IOException {

    log.debug("Creating new MonitoringTask " + vnfr.getName() + " with id: " + vnfr.getId());
    log.debug("Creating new MonitoringTask " + vnfr.getName() + " with id: " + vnfr.getId());
    if (!monitoringTasks.containsKey(vnfr.getParent_ns_id())) {
      monitoringTasks.put(vnfr.getParent_ns_id(), new HashMap<String, ScheduledFuture>());
    }

    if (monitoringTasks.get(vnfr.getParent_ns_id()).containsKey(vnfr.getId())) {
      /*  log.warn(
      "Got new request for starting MonitoringTask for VNFR "
      + vnfr.getId()
      + " but it was already running. So do nothing");*/
      return;
    }

    MonitoringTask monitoringTask =
        new MonitoringTask(vnfr, Metric, Period, monitoringEngine, LastVNFR, vnfManag);

    ScheduledFuture scheduledFuture =
        taskScheduler.scheduleAtFixedRate(monitoringTask, Period * 1000);
    monitoringTasks.get(vnfr.getParent_ns_id()).put(vnfr.getId(), scheduledFuture);
    log.debug(
        "Activated Monitoring of VNFR with ID: " + vnfr.getId() + " and Name " + vnfr.getName());
    log.debug(
        "Activated Monitoring of VNFR with ID: " + vnfr.getId() + " and Name " + vnfr.getName());
  }

  public void stop(VirtualNetworkFunctionRecord vnfr) throws NotFoundException {
    log.debug("Deactivating Monitoring for VNFR with id: " + vnfr.getId());
    if (monitoringTasks.containsKey(vnfr.getParent_ns_id())) {
      if (monitoringTasks.get(vnfr.getParent_ns_id()).containsKey(vnfr.getId())) {

        monitoringTasks.get(vnfr.getParent_ns_id()).get(vnfr.getId()).cancel(false);

        monitoringTasks.get(vnfr.getParent_ns_id()).remove(vnfr.getId());
        log.debug("Deactivated Monitoring for VNFR with id: " + vnfr.getId());
      } else {
        log.warn("Not Found MonitoringTask for VNFR with id: " + vnfr.getId());
      }
    } else {
      log.warn("Not Found any MonitoringTasks for NSR with id: " + vnfr.getParent_ns_id());
    }
  }
}
