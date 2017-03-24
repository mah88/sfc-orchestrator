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
import java.util.Set;

/**
 * Created by maa on 18.01.16.
 */
public class RemoveSFCExecutor implements Runnable {
  private SfcDriverCaller sfcCaller;
  private Logger logger;
  private Set<VirtualNetworkFunctionRecord> vnfrs;
  private String nsrID;
  private Properties properties;

  public RemoveSFCExecutor(String nsrID) throws IOException {
    //sfchandler=new SFCcreator();
    this.properties = ConfigReader.readProperties();

    this.sfcCaller = new SfcDriverCaller(properties.getProperty("sfc.driver"));
    this.vnfrs = vnfrs;
    this.nsrID = nsrID;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Override
  public void run() {
    logger.info(
        "[REMOVE-SFC-EXECUTOR] deleting Test_SFC for "
            + nsrID
            + " at time "
            + new Date().getTime());
    logger.debug("remmoving Test_SFC for nsr " + nsrID + " with vnfrs: " + vnfrs);
    // boolean response = sfchandler.Delete(nsrID);
    boolean response = false;
    try {
      response = sfcCaller.Delete(nsrID, properties.getProperty("sfc.sf.deployment.schedulingType"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.debug("Response from handler " + response);
    logger.info(
        "[REMOVE-SFC-EXECUTOR] ended Test_SFC removal for "
            + nsrID
            + " at time "
            + new Date().getTime());
  }
}
