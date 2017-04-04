package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcDriver.SfcDriverCaller;
//import org.project.sfc.com.openbaton_nfvo.configurations.SfcConfiguration;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 18.01.16.
 */
public class AddSFCExecutor implements Runnable {

  private SfcDriverCaller sfcCaller;
  private Properties properties;

  private Logger logger;
  private Set<VirtualNetworkFunctionRecord> vnfrs;

  private NetworkServiceRecord nsr;

  public AddSFCExecutor(
      Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr, SfcDriverCaller sfcCaller)
      throws IOException {
    this.properties = ConfigReader.readProperties();

    //    this.sfcCaller = new SfcDriverCaller(properties.getProperty("sfc.driver"));
    this.vnfrs = vnfrs;
    this.nsr = nsr;
    this.sfcCaller = sfcCaller;

    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Override
  public void run() {

    logger.info(
        "[ADD-SFC-EXECUTOR] allocating slice for "
            + nsr.getId()
            + " at time "
            + new Date().getTime());
    logger.debug("Received vnfrs with --");

    boolean response = false;
    try {
      response =
          sfcCaller.Create(vnfrs, nsr, properties.getProperty("sfc.sf.deployment.schedulingType"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.debug("RESPONSE from Handler " + response);
    logger.info(
        "[ADD-SFC-EXECUTOR] ended Test_SFC allocation for "
            + nsr.getId()
            + " at time "
            + new Date().getTime());
  }
}
