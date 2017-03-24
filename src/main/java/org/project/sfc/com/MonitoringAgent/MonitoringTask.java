package org.project.sfc.com.MonitoringAgent;

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Item;
import org.openbaton.exceptions.MonitoringException;
import org.openbaton.exceptions.NotFoundException;

import org.openbaton.exceptions.VimDriverException;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.SfcImpl.Broker.SfcBroker;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by mah on 11/7/16.
 */
@Service
@Scope("prototype")
public class MonitoringTask implements Runnable {

  protected Logger log = LoggerFactory.getLogger(this.getClass());
  private MonitoringEngine monitoringEngine;

  private VirtualNetworkFunctionRecord vnfr;
  private String Metric;
  private int Period;
  private Properties properties;
  private boolean lastVNFR;

  public MonitoringTask(
      VirtualNetworkFunctionRecord vnfr,
      String Metric,
      int Period,
      MonitoringEngine monitoringEngine,
      boolean LastVNFR)
      throws NotFoundException, IOException {
    this.vnfr = vnfr;
    this.monitoringEngine = monitoringEngine;
    this.Metric = Metric;
    this.Period = Period;
    System.out.println("[MonitoringTask] Initializing ");
    this.properties = ConfigReader.readProperties();
    this.lastVNFR = LastVNFR;
  }

  public void RegisterMonitoringData(
      VirtualNetworkFunctionRecord vnfr, List<Item> measurementResults)
      throws ClassNotFoundException, SDKException, NotFoundException, VimDriverException {

    SFC sfc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
    HashMap<String, SFC.SFC_Data> All_SFCs = sfc_db.getAllSFCs();
    List<VNFdict> vnfs = sfc_db.getVNFs(vnfr.getType());
    List<String> Assigned = new ArrayList<String>();

    for (int i = 0; i < vnfs.size(); i++) {
      for (Item Measurment : measurementResults) {
        if (vnfs.get(i).getName().equals(Measurment.getHostname())
            && !Assigned.contains(vnfs.get(i).getName())) {
          vnfs.get(i).setTrafficLoad(Double.parseDouble(Measurment.getValue()));
          vnfs.get(i).setHostNode(monitoringEngine.getLocation(vnfs.get(i).getName()));
          /* log.info("[Register Measurments is done to the VNFs] VNF instance - " +
          vnfs.get(i).getName() +
          " -, Traffic Load= " +
          vnfs.get(i).getTrafficLoad() +
          " -, Host Node= " +
          vnfs.get(i).getHostNode());*/
          Assigned.add(vnfs.get(i).getName());
        }
      }
    }
    boolean flag = false;
    Iterator it = All_SFCs.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator count = VNFs.entrySet().iterator();
      while (count.hasNext()) {

        Map.Entry VNFcounter = (Map.Entry) count.next();
        for (Item Measurment : measurementResults) {
          if (VNFs.get(VNFcounter.getKey()).getName().equals(Measurment.getHostname())) {

            VNFs.get(VNFcounter.getKey()).setTrafficLoad(Double.parseDouble(Measurment.getValue()));
            VNFs.get(VNFcounter.getKey())
                .setHostNode(monitoringEngine.getLocation(VNFs.get(VNFcounter.getKey()).getName()));
            log.debug(
                "[Register Measurments is done] VNF instance - "
                    + VNFs.get(VNFcounter.getKey()).getName()
                    + " -, Traffic Load= "
                    + VNFs.get(VNFcounter.getKey()).getTrafficLoad()
                    + " -, Host Node= "
                    + VNFs.get(VNFcounter.getKey()).getHostNode());
            flag = true;
            break;
          }
        }
      }
    }
    if (flag != true) {
      log.error(
          "[Register Measurments Failed] Can not find Measurments for instance: " + vnfr.getName());
    }
  }

  public void SetSfcTrafficLoad(String SDN_controller_type) throws IOException {
    SfcBroker broker = new SfcBroker();
    org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
    SFC_driver = broker.getSFC(SDN_controller_type);

    SFC sfc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
    HashMap<String, SFC.SFC_Data> All_SFCs = sfc_db.getAllSFCs();
    Iterator it = All_SFCs.entrySet().iterator();

    HashMap<String, List<String>> InvolvedServices = new HashMap<String, List<String>>();
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      if (All_SFCs.get(SFCdata_counter.getKey()) == null) {
        log.error(" NO Test_SFC Found");

      } else if (All_SFCs.get(SFCdata_counter.getKey()).getClassifierInfo() == null) {
        log.error(" NO Test_SFC CLASSIFIER INFO EXIST");
      }
      /*String BytesCount=SFC_driver.GetBytesCount(All_SFCs.get(SFCdata_counter.getKey()).getClassifierInfo());

            log.info("Test_SFC with RSP ID ( "+All_SFCs.get(SFCdata_counter.getKey()).getRspID()+" ) has traffic Load  = "+BytesCount+ " Bytes");
            double oldtafficLoad=All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getOldTrafficLoad();
            log.info("OLD TRAFFIC LOAD ==>  " + oldtafficLoad);


            All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).setPathTrafficLoad(Double.parseDouble(BytesCount)+oldtafficLoad);
            log.info("Updated Path TRAFFIC LOAD ==>  " + All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getPathTrafficLoad());
      */
    }
  }

  @Override
  public void run() {
    log.debug("[MonitoringTask] running ");

    if (vnfr != null) {
     log.debug("[MonitoringTask] ");

      List<Item> measurementResults = null;
      try {
           log.debug("Request measurements for VNFR " + vnfr.getId());
        measurementResults =
            monitoringEngine.getRawMeasurementResults(
                vnfr, Metric, Integer.toString(Period)); //get results every 30 sec
        RegisterMonitoringData(vnfr, measurementResults);
        if (lastVNFR == true) {
          SetSfcTrafficLoad(properties.getProperty("sfc.driver"));
        }

      } catch (MonitoringException e) {
        log.warn(
            "Not found all the measurement results for VNFR "
                + vnfr.getId()
                + ". Trying next time again");

      } catch (NotFoundException e) {
        e.printStackTrace();
      } catch (SDKException e) {
        e.printStackTrace();
      } catch (VimDriverException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
