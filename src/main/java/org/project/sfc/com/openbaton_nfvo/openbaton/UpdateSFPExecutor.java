package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */
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
import java.util.Date;
import java.util.Properties;

/**
 * Created by maa on 18.01.16.
 */

public class UpdateSFPExecutor implements Runnable {

  private SfcDriverCaller sfcCaller;
  private Logger logger;
  private VirtualNetworkFunctionRecord vnfr;
  private Properties properties;

  public UpdateSFPExecutor(VirtualNetworkFunctionRecord vnfr) throws IOException{


    this.properties = ConfigReader.readProperties();

    this.sfcCaller = new SfcDriverCaller(properties.getProperty("sfc.driver"));

    this.vnfr = vnfr;

    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Override
  public void run() {

    logger.info(
        "[Update-SFP] for NSR " + vnfr.getParent_ns_id() + " at time " + new Date().getTime());
    logger.info(
        "[Update-SFP] vnfr need to be adjust its VNFC instance: "
            + vnfr
            + " at time "
            + new Date().getTime());

    sfcCaller.UpdateFailedPaths(vnfr);
    logger.info(
        "[Update-SFP-ECUTOREX] ended SFC allocation for "
            + vnfr.getParent_ns_id()
            + " at time "
            + new Date().getTime());
  }
}
