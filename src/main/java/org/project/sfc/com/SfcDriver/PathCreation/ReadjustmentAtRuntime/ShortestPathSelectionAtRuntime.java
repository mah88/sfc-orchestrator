package org.project.sfc.com.SfcDriver.PathCreation.ReadjustmentAtRuntime;

import org.openbaton.catalogue.mano.common.Ip;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

/**
 * Created by mah on 1/26/17.
 */
@Service
@ConfigurationProperties
public class ShortestPathSelectionAtRuntime {
  NeutronClient NC;
  @Autowired private VNFdictRepo vnfManag;
  @Autowired private SFCdictRepo sfcManag;
  @Autowired private SFCCdictRepo classiferManag;
  @Autowired private SFPdictRepo sfpManag;
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  org.project.sfc.com.SfcInterfaces.SFCclassifier SFC_Classifier_driver;
  SfcBroker broker = new SfcBroker();
  private Logger logger;

  @Value("${sfc.driver:ODL}")
  private String type;

  @PostConstruct
  public void init() throws IOException {

    this.SFC_driver = broker.getSFC(type);
    this.SFC_Classifier_driver = broker.getSfcClassifier(type);
    this.NC = new NeutronClient();
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public void ReadjustVNFsAllocation(VirtualNetworkFunctionRecord vnfr) throws IOException {

    HashMap<String, SfcDict> Involved_SFCs = new HashMap<String, SfcDict>();
    long total_size_SFCs;
    logger.info("[Read Just VNFs Allocation] using Shortest Path Selection Algorithm ");
    Iterable<SfcDict> All_SFCs = sfcManag.query();

    if (All_SFCs != null) {
      logger.debug("[ ALL SFCs number ] =  " + sfcManag.count());
      total_size_SFCs = sfcManag.count();
    } else {
      total_size_SFCs = 0;
      logger.debug("SFC Data base is Empty : Can not get the stored chains data  ");
    }
    logger.debug("[Read Just VNFs Allocation] Get Involved SFCs for this VNF ");

    //Get Involved SFCs for this VNF

    Map<String, Boolean> Involved = new HashMap<String, Boolean>();

    for (SfcDict sfcData : All_SFCs) {
      boolean flag = false;

      Map<Integer, VNFdict> VNFs = sfcData.getPaths().get(0).getPath_SFs();
      Iterator vnfs_count = VNFs.entrySet().iterator();
      while (vnfs_count.hasNext()) {
        Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
        if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())) {
          Involved_SFCs.put(sfcData.getInstanceId(), sfcData);
          logger.debug("[Shortest Path Selection] Involved SFCs :  " + sfcData.getInstanceId());
          flag = true;
          logger.debug(" INVOLVED  SFC");
        }
      }
      Involved.put(sfcData.getId(), flag);

      SFC_driver.DeleteSFP(sfcData.getInstanceId(), sfcData.getSymmetrical());
    }

    if (Involved_SFCs.size() > 0) {
      int SFposition;
      Iterator itr = Involved_SFCs.entrySet().iterator();
      while (itr.hasNext()) {

        Map.Entry Key = (Map.Entry) itr.next();
        logger.debug(
            "[Deleting Involved SFCs ] Involved SFCs :  "
                + Involved_SFCs.get(Key.getKey()).getInstanceId());
      }
      Iterator c = Involved_SFCs.entrySet().iterator();
      while (c.hasNext()) {

        List<VNFdict> VNF_instances = new ArrayList<>();
        Map.Entry SFCKey = (Map.Entry) c.next();
        Map<Integer, VNFdict> ChainSFs =
            Involved_SFCs.get(SFCKey.getKey()).getPaths().get(0).getPath_SFs();
        Iterator vnfs_count = ChainSFs.entrySet().iterator();
        VNFdict Previous_vnf = new VNFdict();
        boolean foundVNF = false;

        for (int position : ChainSFs.keySet()) {
          //Continue building the chain path
          if (foundVNF == true) {
            VNFdict SelectedVNFdict = new VNFdict();
            //  List<VNFdict> SFs=getSFs(ChainSFs.get(position).getType());
            List<VNFdict> SFs = vnfManag.findbyType(ChainSFs.get(position).getType());
            logger.debug("[Read Just VNFs Allocation] Continue the Chain building");

            logger.debug(
                "[Read Just VNFs Allocation] The Position of the SF in the Chain = " + position);
            int minDistance = Integer.MAX_VALUE;
            int Distance;
            for (int i = 0; i < SFs.size(); i++) {

              String currentVNFName = SFs.get(i).getName();
              logger.debug("[Select VNF] Current VNF NAME = " + currentVNFName);
              logger.debug("[Select VNF] Prev VNF NAME = " + Previous_vnf.getName());

              VNFdict prev_vnf = Previous_vnf;
              Distance = getDistance(prev_vnf.getName(), currentVNFName);
              logger.debug("[Select VNF] Distance between them = " + Distance);

              if (Distance < minDistance) {
                logger.debug(
                    "[Select VNF] Distance is minimum than the Min distance = " + minDistance);

                minDistance = Distance;
                SelectedVNFdict = SFs.get(i);
              }
            }
            VNF_instances.add(SelectedVNFdict);

            Previous_vnf = SelectedVNFdict;

            logger.debug("[Selected VNF] NAME = " + SelectedVNFdict.getName());
          }
          // Searching for the updated VNF
          if (ChainSFs.get(position).getType().equals(vnfr.getType())) {
            VNFdict SelectedVNFdict = new VNFdict();

            logger.debug(
                "[Read Just VNFs Allocation] Found the Position of the SF in the Chain = "
                    + position);
            SFposition = position;
            int minDistance = Integer.MAX_VALUE;
            int Distance;
            if (SFposition > 0) {
              for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
                for (VNFCInstance vnfc : vdu.getVnfc_instance()) {
                  if (vnfc.getState().equals("ACTIVE")) {

                    String currentVNFName = vnfc.getHostname();
                    logger.debug("[Current VNF] Name: " + currentVNFName);

                    VNFdict prev_vnf = ChainSFs.get(SFposition - 1);
                    logger.debug("[Previous VNF] Name: " + prev_vnf.getName());

                    Distance = getDistance(prev_vnf.getName(), currentVNFName);
                    logger.debug("[Distance between Previous VNF and Current VNF ] = " + Distance);

                    if (Distance < minDistance) {
                      minDistance = Distance;
                      SelectedVNFdict = vnfManag.findbyName(currentVNFName);
                      /*SelectedVNFdict.setType(vnfr.getType());
                      for (Ip ip : vnfc.getIps()) {
                        SelectedVNFdict.setIP(ip.getIp());
                        logger.debug("[SF-Selections] Get Neutron Pro " + new Date().getTime());

                        SelectedVNFdict.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

                        break;
                      }*/
                      logger.debug("[SF-Selected Candidate] " + SelectedVNFdict.getName());
                    }
                  }
                }
              }
              VNF_instances.add(SelectedVNFdict);

              foundVNF = true;
              Previous_vnf = SelectedVNFdict;

            } else {
              logger.debug("[Readjust VNFs Allocation] Select first HOP ]");

              SelectedVNFdict = SelectFirstHop(vnfr);
              VNF_instances.add(SelectedVNFdict);
              Previous_vnf = SelectedVNFdict;
              foundVNF = true;
            }
          }
          logger.debug("[Readjust VNFs Allocation] FOUND is true? ]" + foundVNF);
        }

        UpdateChain(Involved_SFCs.get(SFCKey.getKey()), VNF_instances);
        logger.info("[Readjust VNFs Allocation] END]");
      }
    }
    logger.debug("[Update the not Involved SFCs]");

    Iterator inv_itr = Involved.entrySet().iterator();
    while (inv_itr.hasNext()) {

      Map.Entry Key = (Map.Entry) inv_itr.next();

      String SFC_ID = (String) Key.getKey();
      Boolean involved = Involved.get(Key.getKey());
      logger.debug("[Involved SFCs ? ] SFC ID " + SFC_ID + " ? " + involved);

      if (involved == false) {
        Map<Integer, VNFdict> Path_SFs =
            sfcManag.findFirstById(SFC_ID).getPaths().get(0).getPath_SFs();
        Iterator path_itr = Path_SFs.entrySet().iterator();
        List<VNFdict> pathVNFs = new ArrayList<>();
        while (path_itr.hasNext()) {
          Map.Entry PathKey = (Map.Entry) path_itr.next();
          pathVNFs.add(Path_SFs.get(PathKey.getKey()));
        }
        logger.debug("==> Do the Update to the chain with  ID " + SFC_ID);

        UpdateChain(sfcManag.findFirstById(SFC_ID), pathVNFs);
      }
    }

    List<VNFdict> vnf_list = vnfManag.findAllbyType(vnfr.getType());

    for (int i = 0; i < vnf_list.size(); i++) {
      if (vnf_list.get(i).getStatus() == Status.INACTIVE) {
        vnfManag.delete(vnf_list.get(i));
        logger.debug("Removed the VNF instance: " + vnf_list.get(i) + " from the data base");
      }
    }
  }

  /* private List<VNFdict> getSFs(String SF_TYPE) {

    HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    List<VNFdict> SFsList = new ArrayList<>();
    Iterator it = All_SFCs.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator vnfs_count = VNFs.entrySet().iterator();
      while (vnfs_count.hasNext()) {
        Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
        if (VNFs.get(VNFcounter.getKey()).getType().equals(SF_TYPE)) {
          logger.debug(
              "[Shortest Path Selection] Added VNF instance to the list :  "
                  + VNFs.get(VNFcounter.getKey()).getName());
          logger.debug(
              "[Shortest Path Selection] SFs List contains it ? :  "
                  + SFsList.contains(VNFs.get(VNFcounter.getKey())));

          if (!SFsList.contains(VNFs.get(VNFcounter.getKey()))) {
            SFsList.add(VNFs.get(VNFcounter.getKey()));
            logger.debug(
                "[Shortest Path Selection] Added VNF instance to the list :  "
                    + VNFs.get(VNFcounter.getKey()).getName());
          }
        }
      }
    }

    return SFsList;
  }
  */

  private VNFdict SelectFirstHop(VirtualNetworkFunctionRecord vnfr) throws IOException {
    logger.info("[Shortest Path Selection] SELECT first HOP  ");
    VNFdict firstHopVNF = new VNFdict();

    /*
    List<String> VNF_instances = new ArrayList<String>();

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());
        System.out.println(
            "[Shortest Path Selection] added VNF instance   "
                + vnfc_instance.getHostname()
                + " to the list of VNF instances with type "
                + vnfr.getType());
      }
    }



    Random randomizer = new Random();
    String VNF_instance_selected = VNF_instances.get(randomizer.nextInt(VNF_instances.size()));
    System.out.println(
        "[Shortest Path Selection] RANDOM Selection of First HOP is  " + VNF_instance_selected);

    firstHopVNF.setName(VNF_instance_selected);
    firstHopVNF.setType(vnfr.getType());
    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        if (vnfc_instance.getHostname() == VNF_instance_selected) {
          for (Ip ip : vnfc_instance.getIps()) {
            firstHopVNF.setIP(ip.getIp());
            System.out.println("[SF-Selections] Get Neutron Pro " + new Date().getTime());

            firstHopVNF.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

            break;
          }
        }
      }
    }
    */

    int minLength = Integer.MAX_VALUE;
    int length;
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance vnfc : vdu.getVnfc_instance()) {
        if (vnfc.getState().equals("ACTIVE")) {

          String currentVNFName = vnfc.getHostname();
          logger.debug("[ Current VNF NAme] =" + currentVNFName);
          logger.debug("[ The Min distance ] = " + minLength);
          String currentIp = null;
          String currentNeutronPortID = null;

          for (Ip ip : vnfc.getIps()) {
            currentIp = ip.getIp();
            currentNeutronPortID = NC.getNeutronPortID(currentIp);

            break;
          }

          length = getDistanceFirstHOP(currentVNFName);

          logger.debug("[ The distance between it and the prev. node ] = " + length);

          if (length < minLength) {
            logger.debug("[The distance is less than the Min distance ]");

            minLength = length;
            firstHopVNF = vnfManag.findbyName(currentVNFName);
            /*  firstHopVNF.setName(currentVNFName);
            firstHopVNF.setType(vnfr.getType());

            firstHopVNF.setIP(currentIp);

            firstHopVNF.setNeutronPortId(currentNeutronPortID);*/
          }
        }
      }
    }
    logger.info(
        "[First HOP Selected]  NAME: "
            + firstHopVNF.getName()
            + " IP: "
            + firstHopVNF.getIP()
            + " Traffic Load: "
            + firstHopVNF.getTrafficLoad()
            + " Connected SFF: "
            + firstHopVNF.getConnectedSFF());

    return firstHopVNF;
  }
  //Calculate the distance between two VNFs instances
  private int getDistance(String prev_VNF, String currentVNF) throws IOException {
    int distance;
    String currentVNFLocation = SFC_driver.GetConnectedSFF(currentVNF);
    String prevVNFLocation = SFC_driver.GetConnectedSFF(prev_VNF);
    if (currentVNFLocation == null || prevVNFLocation == null) {
      logger.warn("[ Could not get the connected SFF to one of the VNFs ]  ");
      return 1000;

    } else if (currentVNFLocation.equals(prevVNFLocation)) {
      distance = 0;
    } else {
      distance = 1;
    }

    return distance;
  }

  private int getDistanceFirstHOP(String currentVNF) throws IOException {
    int distance;
    String currentVNFLocation = SFC_driver.GetConnectedSFF(currentVNF);
    String prevVNFLocation = "SFF-192.168.0.16";
    if (currentVNFLocation == null) {
      logger.warn(" [ Could not get the connected SFF to one of the VNFs ]  ");
      return 1000;

    } else if (currentVNFLocation.equals(prevVNFLocation)) {
      distance = 0;
    } else {
      distance = 1;
    }

    return distance;
  }

  private void UpdateChain(SfcDict Chain_Data, List<VNFdict> VNF_instances) {

    logger.debug("[ LB Path Selection < Create Chain > ]  ");

    Map<Integer, VNFdict> VNFs = Chain_Data.getPaths().get(0).getPath_SFs();
    Iterator count = VNFs.entrySet().iterator();
    logger.debug(
        "[ Shortest Path Selection < Create Chain > ] SIZE of VNFs in the original Chain:  "
            + VNFs.size());
    logger.debug(
        "[ Shortest Path Selection < Create Chain > ] SIZE of VNF instances  : "
            + VNF_instances.size());

    while (count.hasNext()) {

      Map.Entry VNFcounter = (Map.Entry) count.next();
      for (int i = 0; i < VNF_instances.size(); i++) {
        logger.debug(
            "[ TYPE of CHAIN DATA SFs "
                + VNFs.get(VNFcounter.getKey()).getType()
                + "  and the instances: "
                + VNF_instances.get(i).getType());

        if (VNFs.get(VNFcounter.getKey()).getType().equals(VNF_instances.get(i).getType())) {
          logger.debug("[ Shortest Path Selection < Create Chain > Found the same TYPE  ");

          int position = Integer.valueOf(VNFcounter.getKey().toString()).intValue();

          //VNFs.remove(position);

          VNFs.put(position, VNF_instances.get(i));
          logger.debug("[ Shortest Path Selection < Create Chain > ] DONE  ");

          break;
        }
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

    //SFC_driver.DeleteSFP(Chain_Data.getRspID(), Chain_Data.isSymm());
    SFCdict sfc_info = new SFCdict();
    sfc_info.setSfcDict(Chain_Data);
    String new_instance_id = SFC_driver.CreateSFP(sfc_info, VNFs);

    logger.debug("[Update SFCC DB] " + Chain_Data.getInstanceId().substring(5));
    String IDx = Chain_Data.getInstanceId().substring(5);

    String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);

    String SFCC_name =
        SFC_Classifier_driver.Create_SFC_Classifer(
            classiferManag.query("sfcc-" + VNFFGR_ID), new_instance_id);

    logger.debug("[NEW Classifier Updated ] " + SFCC_name);

    logger.debug("[new instance id ] =  " + new_instance_id);
    SFCCdict updated_classifier = classiferManag.query("sfcc-" + VNFFGR_ID);
    updated_classifier.setInstanceId(new_instance_id);
    Chain_Data.setInstanceId(new_instance_id);
    classiferManag.update(updated_classifier);
    logger.debug("[new path id ] =  " + newPath.getId());
    logger.debug("[new path info ] =  " + newPath.toString());

    sfpManag.update(newPath);
    sfcManag.update(Chain_Data);

    logger.info("[NEW Classifier Updated ] " + SFCC_name);
    logger.info("[new instance id ] =  " + new_instance_id);

    logger.info("[Update DB] is done !!");
  }
}
