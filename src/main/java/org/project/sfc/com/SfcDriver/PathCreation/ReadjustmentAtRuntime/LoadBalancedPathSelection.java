package org.project.sfc.com.SfcDriver.PathCreation.ReadjustmentAtRuntime;

import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcImpl.Broker.SfcBroker;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFPdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.project.sfc.com.SfcModel.SFCdict.Status;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.SfcRepository.SFCCdictRepo;
import org.project.sfc.com.SfcRepository.SFCdictRepo;
import org.project.sfc.com.SfcRepository.SFPdictRepo;
import org.project.sfc.com.SfcRepository.VNFdictRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Created by mah on 11/14/16.
 */
@Service
@ConfigurationProperties
public class LoadBalancedPathSelection {
  NeutronClient NC;
  //SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  org.project.sfc.com.SfcInterfaces.SFCclassifier SFC_Classifier_driver;
  SfcBroker broker = new SfcBroker();
  private Logger logger;
  @Autowired private VNFdictRepo vnfManag;
  @Autowired private SFCdictRepo sfcManag;
  @Autowired private SFCCdictRepo classiferManag;
  @Autowired private SFPdictRepo sfpManag;

  @Value("${sfc.driver:ODL}")
  private String type;

  @PostConstruct
  public void init() throws IOException {

    this.SFC_driver = broker.getSFC(type);
    this.SFC_Classifier_driver = broker.getSfcClassifier(type);
    this.NC = new NeutronClient();
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public void ReadjustVNFsAllocation(VirtualNetworkFunctionRecord vnfr) {
    logger.info("[Readjust VNFs Allocation] Start");

    List<SfcDict> Chains_data = new ArrayList<>();
    List<VNFdict> VNF_instance_dicts = new ArrayList<>();
    HashMap<String, Double> PrevVNFTrafficLoad = new HashMap<String, Double>();

    Iterable<SfcDict> All_SFCs = sfcManag.query();
    HashMap<String, SfcDict> Involved_SFCs = new HashMap<String, SfcDict>();
    long total_size_SFCs;
    if (All_SFCs != null) {
      logger.debug("[ ALL SFCs number ] =  " + sfcManag.count());
      total_size_SFCs = sfcManag.count();

    } else {
      total_size_SFCs = 0;
      logger.debug("SFC Data base is Empty : Can not get the stored chains data  ");
    }
    logger.debug("[Read Just VNFs Allocation] Get Involved SFCs for this VNF ");

    //Get Involved SFCs for this VNF
    // Iterator it = All_SFCs.entrySet().iterator();
    //  while (it.hasNext()) {
    //  Map.Entry SFCdata_counter = (Map.Entry) it.next();
    //  HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
    for (SfcDict sfcData : All_SFCs) {
      Map<Integer, VNFdict> VNFs = sfcData.getPaths().get(0).getPath_SFs();
      Iterator vnfs_count = VNFs.entrySet().iterator();
      while (vnfs_count.hasNext()) {
        Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
        if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())) {
          Involved_SFCs.put(sfcData.getInstanceId(), sfcData);
          logger.debug("[LB Path Selection] Involved SFCs :  " + sfcData.getInstanceId());
        }
      }
    }

    int total_size_VNF_instances = 0;

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        if (vnfc_instance.getState().equals("ACTIVE")) {

          PrevVNFTrafficLoad.put(vnfc_instance.getHostname(), 0.0);
          logger.debug(
              "[PrevVNFTAFFICload] VNF instance name : "
                  + vnfc_instance.getHostname()
                  + " Load: "
                  + PrevVNFTrafficLoad.get(vnfc_instance.getHostname()));

          total_size_VNF_instances++;
        }
      }
    }
    logger.debug("[LB Path Selection] Total Size of VNF instances : " + total_size_VNF_instances);

    HashMap<String, SfcDict> SelectedSFCs = new HashMap<String, SfcDict>();

    logger.debug(
        "[LB Path Selection] Levels of Selections = "
            + String.valueOf(Math.floor(total_size_SFCs / total_size_VNF_instances)));

    for (int counter1 = 0;
        counter1 <= Math.floor(total_size_SFCs / total_size_VNF_instances);
        counter1++) {

      for (int x = 0; x < total_size_VNF_instances; x++) {

        if (Involved_SFCs.size() > 0) {
          double MaxLoad = 0;
          String RSPID = null;

          Iterator c = Involved_SFCs.entrySet().iterator();
          while (c.hasNext()) {
            Map.Entry SFCKey = (Map.Entry) c.next();

            if (Involved_SFCs.get(SFCKey.getKey()).getPaths().get(0).getPathTrafficLoad()
                >= MaxLoad) {
              logger.debug(
                  "This Test_SFC:  "
                      + Involved_SFCs.get(SFCKey.getKey()).getInstanceId()
                      + " has Load = "
                      + Involved_SFCs.get(SFCKey.getKey()).getPaths().get(0).getPathTrafficLoad()
                      + " higher than the Max Load < "
                      + MaxLoad
                      + " >");

              if (RSPID != null) {
                logger.debug("[LB Path Selection]    RSPID is not empty  ");

                if (SelectedSFCs.containsKey(RSPID)) {
                  SelectedSFCs.remove(RSPID);
                  logger.debug(
                      "[LB Path Selection] Selected SFC is Removed, Test_SFC RSP ID:  " + RSPID);
                }
              }
              SelectedSFCs.put(
                  Involved_SFCs.get(SFCKey.getKey()).getInstanceId(),
                  Involved_SFCs.get(SFCKey.getKey()));
              MaxLoad = Involved_SFCs.get(SFCKey.getKey()).getPaths().get(0).getPathTrafficLoad();
              RSPID = SFCKey.getKey().toString();
              logger.debug(
                  "[LB Path Selection] Selected SFC at round "
                      + counter1
                      + "  is  "
                      + Involved_SFCs.get(SFCKey.getKey())
                      + " with Max Load = "
                      + MaxLoad
                      + " and RSP ID = "
                      + RSPID);
            }
          }
          Involved_SFCs.remove(RSPID);
          logger.debug("[ Remove from the Involved SFCs ]  The RSP ID is  " + RSPID);
        }
      }

      // Get which Test_SFC should be selected
      int size_selected_sfcs = SelectedSFCs.size();

      for (int i = 0; i < size_selected_sfcs; i++) {
        Iterator counter = SelectedSFCs.entrySet().iterator();

        double Load = 0;
        int recentQoS = 0;
        String RSPID = null;
        String SelectedVNFinstance = null;
        SfcDict selectedChain = null;
        logger.debug("[ Selected Chains ]  size:  " + SelectedSFCs.size());

        while (counter.hasNext()) {
          Map.Entry involved_SFC_data_counter = (Map.Entry) counter.next();
          System.out.println(
              "[ Selected Chain ]  QoS:  "
                  + SelectedSFCs.get(involved_SFC_data_counter.getKey())
                      .getPaths()
                      .get(0)
                      .getQoS());
          logger.debug("[ Recent   QoS ] :  " + recentQoS);

          if (SelectedSFCs.get(involved_SFC_data_counter.getKey()).getPaths().get(0).getQoS()
              > recentQoS) {
            logger.debug(
                "[ SFC Selection ] :  "
                    + SelectedSFCs.get(involved_SFC_data_counter.getKey()).getInstanceId()
                    + " has QoS Priority= "
                    + SelectedSFCs.get(involved_SFC_data_counter.getKey())
                        .getPaths()
                        .get(0)
                        .getQoS()
                    + " HIGHER than the best QoS Priority < "
                    + recentQoS
                    + " >");

            Load =
                SelectedSFCs.get(involved_SFC_data_counter.getKey())
                    .getPaths()
                    .get(0)
                    .getPathTrafficLoad();
            selectedChain = SelectedSFCs.get(involved_SFC_data_counter.getKey());
            recentQoS =
                SelectedSFCs.get(involved_SFC_data_counter.getKey()).getPaths().get(0).getQoS();
            RSPID = involved_SFC_data_counter.getKey().toString();

            logger.debug(
                "[ Selected Chain ]  [Current Qos > Last QoS] Selected Chain:  "
                    + selectedChain
                    + " Last QoS= "
                    + recentQoS
                    + " RSP ID= "
                    + RSPID);

          } else if (SelectedSFCs.get(involved_SFC_data_counter.getKey()).getPaths().get(0).getQoS()
              == recentQoS) {
            logger.debug(
                "[SFC Selection] :  "
                    + SelectedSFCs.get(involved_SFC_data_counter.getKey()).getInstanceId()
                    + " has QoS Priority= "
                    + SelectedSFCs.get(involved_SFC_data_counter.getKey())
                        .getPaths()
                        .get(0)
                        .getQoS()
                    + " EQUAL to the best QoS Priority < "
                    + recentQoS
                    + " >");
            if (SelectedSFCs.get(involved_SFC_data_counter.getKey())
                    .getPaths()
                    .get(0)
                    .getPathTrafficLoad()
                > Load) {
              selectedChain = SelectedSFCs.get(involved_SFC_data_counter.getKey());
              logger.debug(
                  "[SFC Selection] :  "
                      + SelectedSFCs.get(involved_SFC_data_counter.getKey()).getInstanceId()
                      + " has Load= "
                      + SelectedSFCs.get(involved_SFC_data_counter.getKey())
                          .getPaths()
                          .get(0)
                          .getPathTrafficLoad()
                      + " HIGHER than the MaxLoad < "
                      + Load
                      + " >");
              Load =
                  SelectedSFCs.get(involved_SFC_data_counter.getKey())
                      .getPaths()
                      .get(0)
                      .getPathTrafficLoad();
              recentQoS =
                  SelectedSFCs.get(involved_SFC_data_counter.getKey()).getPaths().get(0).getQoS();
              RSPID = involved_SFC_data_counter.getKey().toString();
            }
          }
        }
        logger.debug(
            "[ Selected Chain ]   Selected Chain:  "
                + selectedChain
                + " Best QoS= "
                + recentQoS
                + " RSP ID= "
                + RSPID);

        SelectedSFCs.remove(RSPID);

        double MinLoad = Double.MAX_VALUE;

        //Allocate the selected Test_SFC to lowest Load of the VNF instances
        Iterator vnf_TL_counter = PrevVNFTrafficLoad.entrySet().iterator();
        boolean Selected = false;
        while (vnf_TL_counter.hasNext()) {

          Map.Entry VNF_key = (Map.Entry) vnf_TL_counter.next();
          logger.debug(
              "[Allocate the selected Test_SFC to lowest VNF instance Load ]  Load="
                  + PrevVNFTrafficLoad.get(VNF_key.getKey())
                  + " Min Load= "
                  + MinLoad);

          if (PrevVNFTrafficLoad.get(VNF_key.getKey()) < MinLoad) {

            MinLoad = PrevVNFTrafficLoad.get(VNF_key.getKey());

            SelectedVNFinstance = VNF_key.getKey().toString();

            Selected = true;
            logger.debug(
                "[Min Load ]=" + MinLoad + " [Selected VNF instance]= " + SelectedVNFinstance);
          }
        }
        if (Selected == true) {
          double prev_load = PrevVNFTrafficLoad.get(SelectedVNFinstance);
          PrevVNFTrafficLoad.put(SelectedVNFinstance, prev_load + Load);
          logger.debug(
              "[ Alocate Selected Chain to lowest VNF load ]  Min Load:  "
                  + MinLoad
                  + " VNF instance= "
                  + SelectedVNFinstance);
        }

        //Iterator it_new = All_SFCs.entrySet().iterator();
        VNFdict SelectedVNFdict = null;
        for (SfcDict sfcData : All_SFCs) {

          // Map.Entry SFCdata_counter = (Map.Entry) it_new.next();
          Map<Integer, VNFdict> VNFs = sfcData.getPaths().get(0).getPath_SFs();
          Iterator vnfs_count = VNFs.entrySet().iterator();
          while (vnfs_count.hasNext()) {

            Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
            logger.debug(
                "VNF name: "
                    + VNFs.get(VNFcounter.getKey()).getName()
                    + " [Selected VNF instance]"
                    + SelectedVNFinstance);

            if (VNFs.get(VNFcounter.getKey()).getName().equals(SelectedVNFinstance)) {

              SelectedVNFdict = vnfManag.findbyName(VNFs.get(VNFcounter.getKey()).getName());
              logger.debug("[Selected VNFdict]" + SelectedVNFdict);

              break;
            }
          }
        }
        logger.info(
            "[ CreatChain ]  chain RSPID  "
                + selectedChain.getInstanceId()
                + " VSelected VNF instance= "
                + SelectedVNFdict.getName());

        Chains_data.add(selectedChain);
        VNF_instance_dicts.add(SelectedVNFdict);
      }
    }

    for (int x = 0; x < Chains_data.size(); x++) {
      UpdateChain(Chains_data.get(x), VNF_instance_dicts.get(x));
    }
    logger.info("[Readjust VNFs Allocation] END");
    List<VNFdict> vnf_list = vnfManag.findAllbyType(vnfr.getType());

    for (int i = 0; i < vnf_list.size(); i++) {
      if (vnf_list.get(i).getStatus() == Status.INACTIVE) {
        vnfManag.delete(vnf_list.get(i));
        logger.debug("Removed the VNF instance: " + vnf_list.get(i) + " from the data base");
      }
    }
  }

  private void UpdateChain(SfcDict Chain_Data, VNFdict VNF_instance) {

    logger.debug("[ LB Path Selection < Create Chain > ]  ");

    Map<Integer, VNFdict> VNFs = Chain_Data.getPaths().get(0).getPath_SFs();
    Iterator count = VNFs.entrySet().iterator();
    logger.debug("[ LB Path Selection < Create Chain > ] SIZE of VNFs  " + VNFs.size());

    while (count.hasNext()) {

      Map.Entry VNFcounter = (Map.Entry) count.next();
      logger.debug(
          "[ LB Path Selection < Create Chain > ] chain data VNF type= "
              + VNFs.get(VNFcounter.getKey()).getType()
              + "VNF instance TYPE= "
              + VNF_instance.getType());

      if (VNFs.get(VNFcounter.getKey()).getType().equals(VNF_instance.getType())) {
        logger.debug("[ LB Path Selection < Create Chain > Found the same TYPE  ");

        int position = Integer.valueOf(VNFcounter.getKey().toString()).intValue();

        VNFs.remove(position);

        VNFs.put(position, VNF_instance);
        logger.debug("[ LB Path Selection < Create Chain > ] DONE  ");

        break;
      }
    }

    Chain_Data.getPaths().get(0).setPath_SFs(VNFs);

    List<SFPdict> newPaths = new ArrayList<>();
    SFPdict newPath = Chain_Data.getPaths().get(0);
    newPath.setPath_SFs(VNFs);
    double PathTrafficLoad = Chain_Data.getPaths().get(0).getPathTrafficLoad();
    newPath.setOldTrafficLoad(PathTrafficLoad);

    newPaths.add(0, newPath);

    Chain_Data.setPaths(newPaths);

    SFC_driver.DeleteSFP(Chain_Data.getInstanceId(), Chain_Data.getSymmetrical());
    SFCdict sfc_info = new SFCdict();
    sfc_info.setSfcDict(Chain_Data);
    String new_instance_id = SFC_driver.CreateSFP(sfc_info, VNFs);
    logger.debug("[NEW Path readjusted ] " + new_instance_id);

    logger.debug("[Update SFCC DB] " + Chain_Data.getInstanceId().substring(5));
    String IDx = Chain_Data.getInstanceId().substring(5);

    String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);

    String SFCC_name =
        SFC_Classifier_driver.Create_SFC_Classifer(
            classiferManag.query("sfcc-" + VNFFGR_ID), new_instance_id);

    logger.debug("[NEW Classifier updated ] " + SFCC_name);
    logger.debug("[NEW Classifier Updated ] " + SFCC_name);

    logger.debug("[new instance id ] =  " + new_instance_id);
    SFCCdict updated_classifier = classiferManag.query("sfcc-" + VNFFGR_ID);
    updated_classifier.setInstanceId(new_instance_id);
    Chain_Data.setInstanceId(new_instance_id);
    classiferManag.update(updated_classifier);
    sfpManag.update(newPath);
    sfcManag.update(Chain_Data);

    logger.info("[NEW Classifier Updated ] " + SFCC_name);
    logger.info("[new instance id ] =  " + new_instance_id);

    logger.info("[Update DB] is done !!");
  }
}
