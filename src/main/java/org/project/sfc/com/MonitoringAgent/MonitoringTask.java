package org.project.sfc.com.MonitoringAgent;



import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Item;
import org.openbaton.exceptions.MonitoringException;
import org.openbaton.exceptions.NotFoundException;

import org.openbaton.exceptions.VimDriverException;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


  public MonitoringTask(VirtualNetworkFunctionRecord vnfr, String Metric,int Period,
                        MonitoringEngine monitoringEngine) throws NotFoundException {
    this.vnfr = vnfr;
    this.monitoringEngine = monitoringEngine;
    this.Metric=Metric;
    this.Period=Period;
    System.out.println("[MonitoringTask] Initializing ");


  }
  public void RegisterMonitoringData(VirtualNetworkFunctionRecord vnfr, List<Item> measurementResults) throws
                                                                                                       ClassNotFoundException,
                                                                                                       SDKException,
                                                                                                       NotFoundException,
                                                                                                       VimDriverException {

    SFC sfc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
    HashMap<String, SFC.SFC_Data> All_SFCs=sfc_db.getAllSFCs();
    boolean flag=false;
    Iterator it = All_SFCs.entrySet().iterator();
    List<String> Registered=new ArrayList<String>();
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator count = VNFs.entrySet().iterator();
      while (count.hasNext()) {

        Map.Entry VNFcounter = (Map.Entry) count.next();
        System.out.println("[OK] ");
        for(Item Measurment:measurementResults){
          if(VNFs.get(VNFcounter.getKey()).getName().equals(Measurment.getHostname()) && !Registered.contains(VNFs.get(VNFcounter.getKey()).getName())){

            VNFs.get(VNFcounter.getKey()).setTrafficLoad(Double.parseDouble(Measurment.getLastValue()));
            VNFs.get(VNFcounter.getKey()).setHostNode(monitoringEngine.getLocation(VNFs.get(VNFcounter.getKey()).getName()));
            log.info("[Register Measurments is done] VNF instance - " + VNFs.get(VNFcounter.getKey()).getName()+" -, Traffic Load= "+VNFs.get(VNFcounter.getKey()).getTrafficLoad()+" -, Host Node= "+VNFs.get(VNFcounter.getKey()).getHostNode());
            Registered.add(VNFs.get(VNFcounter.getKey()).getName());
            flag=true;
            break;

          }
        }
      }
    }
    if (flag!=true){
      log.error("[Register Measurments Failed] Can not find Measurments for instance: "+ vnfr.getName());

    }
  }
  @Override
  public void run() {
    System.out.println("[MonitoringTask] running ");

    if (vnfr != null) {
      log.info("[MonitoringTask] ");


        List<Item> measurementResults = null;
        try {
          //log.info("[DETECTOR] REQUEST_MEASUREMENTS " + new Date().getTime());
          log.info("Request measurements for VNFR " + vnfr.getId());
          measurementResults =
              monitoringEngine.getRawMeasurementResults(vnfr, Metric, Integer.toString(Period)); //get results every 30 sec
          RegisterMonitoringData(vnfr,measurementResults);

         log.info("[DETECTOR] GOT_MEASUREMENT_RESULTS " + new Date().getTime());

        } catch (MonitoringException e) {
          //log.error(e.getMessage(), e);
          log.warn("Not found all the measurement results for VNFR " + vnfr.getId() + ". Trying next time again");

        } catch (NotFoundException e) {
          e.printStackTrace();
        } catch (SDKException e) {
          e.printStackTrace();
        } catch (VimDriverException e) {
          e.printStackTrace();
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
    }
  }
}