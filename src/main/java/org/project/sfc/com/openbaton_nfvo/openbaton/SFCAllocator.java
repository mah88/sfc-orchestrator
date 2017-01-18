package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by maa on 02.12.15.
 */
@Service
public class SFCAllocator {

  private final ScheduledExecutorService qtScheduler = Executors.newScheduledThreadPool(1);
  private Logger logger;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public void addSFtoChain(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsrId) throws IOException {

    logger.info(
        "[Test_SFC-ALLOCATOR] received new set of vnfrs for "
            + nsrId
            + " to create a Test_SFC at time "
            + new Date().getTime());
    logger.debug("Creating ADD Thread");
    AddSFCExecutor aqe = new AddSFCExecutor(vnfrs, nsrId);
    qtScheduler.schedule(aqe, 100, TimeUnit.MILLISECONDS);
    logger.info(
        "[Test_SFC-ALLOCATOR] scheduled thread to handle the NSR"
            + nsrId
            + " to create a Test_SFC at time "
            + new Date().getTime());
    logger.debug("ADD Thread created and scheduled");
  }

  public void ChangeChainPath(VirtualNetworkFunctionRecord vnfr) throws IOException {

    logger.info(
        "[Test_SFC-ALLOCATOR-ChangePath] received new set faulted vnfr "
            + vnfr.getId()
            + " to change SFP at time "
            + new Date().getTime());
    logger.debug("Creating Update Thread");
    UpdateSFPExecutor aqe = new UpdateSFPExecutor(vnfr);
    qtScheduler.schedule(aqe, 100, TimeUnit.MILLISECONDS);
    logger.info(
        "[Test_SFC-ALLOCATOR-ChangePath] scheduled thread to handle the VNFR"
            + vnfr.getParent_ns_id()
            + " to create a Test_SFC at time "
            + new Date().getTime());
    logger.debug("Update Thread created and scheduled");
  }

  public void ScalePaths(VirtualNetworkFunctionRecord vnfr) throws IOException {

    logger.info(
        "[Test_SFC-ALLOCATOR-ScalePaths] received new set Scaled vnfr "
            + vnfr.getId()
            + " to change SFP at time "
            + new Date().getTime());
    logger.debug("Creating Scale Thread");
    ScaleSFPExecutor aqe = new ScaleSFPExecutor(vnfr);
    qtScheduler.schedule(aqe, 100, TimeUnit.MILLISECONDS);
    logger.info(
        "[Test_SFC-ALLOCATOR-ScalePaths] scheduled thread to handle the VNFR"
            + vnfr.getParent_ns_id()
            + " to create a Test_SFC at time "
            + new Date().getTime());
    logger.debug("Scale Thread created and scheduled");
  }

  public void removeSFC(String nsrId) throws IOException {
    logger.info(
        "[Test_SFC-ALLOCATOR] received new set of vnfrs for "
            + nsrId
            + " to remove a Test_SFC at time "
            + new Date().getTime());
    logger.debug("Creating REMOVE Thread");
    RemoveSFCExecutor rqe = new RemoveSFCExecutor(nsrId);
    qtScheduler.schedule(rqe, 10, TimeUnit.SECONDS);
    logger.info(
        "[Test_SFC-ALLOCATOR] scheduled thread to handle the NSR"
            + nsrId
            + " to remove a Test_SFC at time "
            + new Date().getTime());
    logger.debug("REMOVE Thread created and scheduled");
  }
}
