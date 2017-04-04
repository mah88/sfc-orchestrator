package org.project.sfc.com.SfcDriver.PathCreation.ReadjustmentAtRuntime;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.SfcImpl.Broker.SfcBroker;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCdict.SFPdict;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by mah on 1/26/17.
 */
public class ShortestPathSelectionAtRuntime {
  NeutronClient NC;
  SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  org.project.sfc.com.SfcInterfaces.SFCclassifier SFC_Classifier_driver;
  SfcBroker broker = new SfcBroker();
  private Logger logger;

  public ShortestPathSelectionAtRuntime(String type) throws IOException {

    this.SFC_driver = broker.getSFC(type);
    this.SFC_Classifier_driver = broker.getSfcClassifier(type);
    this.NC = new NeutronClient();
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public void ReadjustVNFsAllocation(VirtualNetworkFunctionRecord vnfr) throws IOException {

    HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    HashMap<String, SFC.SFC_Data> Involved_SFCs = new HashMap<String, SFC.SFC_Data>();
    int total_size_SFCs;
    logger.info("[Read Just VNFs Allocation] using Shortest Path Selection Algorithm ");

    if (All_SFCs != null) {
      logger.debug("[ ALL SFCs number ] =  " + All_SFCs.size());
      total_size_SFCs = All_SFCs.size();
    } else {
      total_size_SFCs = 0;
      logger.debug("SFC Data base is Empty : Can not get the stored chains data  ");
    }
    logger.debug("[Read Just VNFs Allocation] Get Involved SFCs for this VNF ");

    //Get Involved SFCs for this VNF
    Iterator it = All_SFCs.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator vnfs_count = VNFs.entrySet().iterator();
      while (vnfs_count.hasNext()) {
        Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
        if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())) {
          Involved_SFCs.put(
              All_SFCs.get(SFCdata_counter.getKey()).getRspID(),
              All_SFCs.get(SFCdata_counter.getKey()));
          logger.debug(
              "[Shortest Path Selection] Involved SFCs :  "
                  + All_SFCs.get(SFCdata_counter.getKey()).getRspID());
        }
      }
    }

    if (Involved_SFCs.size() > 0) {
      int SFposition;
      Iterator itr = Involved_SFCs.entrySet().iterator();
      while (itr.hasNext()) {

        Map.Entry Key = (Map.Entry) itr.next();
        logger.debug(
            "[Deleting Involved SFCs ] Involved SFCs :  "
                + Involved_SFCs.get(Key.getKey()).getRspID());
        SFC_driver.DeleteSFP(
            Involved_SFCs.get(Key.getKey()).getRspID(), Involved_SFCs.get(Key.getKey()).isSymm());
      }
      Iterator c = Involved_SFCs.entrySet().iterator();
      while (c.hasNext()) {

        List<VNFdict> VNF_instances = new ArrayList<>();
        Map.Entry SFCKey = (Map.Entry) c.next();
        HashMap<Integer, VNFdict> ChainSFs = Involved_SFCs.get(SFCKey.getKey()).getChainSFs();
        Iterator vnfs_count = ChainSFs.entrySet().iterator();
        VNFdict Previous_vnf = new VNFdict();
        boolean foundVNF = false;

        for (int position : ChainSFs.keySet()) {
          //Continue building the chain path
          if (foundVNF == true) {
            VNFdict SelectedVNFdict = new VNFdict();
            //  List<VNFdict> SFs=getSFs(ChainSFs.get(position).getType());
            List<VNFdict> SFs = sfcc_db.getVNFs(ChainSFs.get(position).getType());
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
                  String currentVNFName = vnfc.getHostname();
                  logger.debug("[Current VNF] Name: " + currentVNFName);

                  VNFdict prev_vnf = ChainSFs.get(SFposition - 1);
                  logger.debug("[Previous VNF] Name: " + prev_vnf.getName());

                  Distance = getDistance(prev_vnf.getName(), currentVNFName);
                  logger.debug("[Distance between Previous VNF and Current VNF ] = " + Distance);

                  if (Distance < minDistance) {
                    minDistance = Distance;
                    SelectedVNFdict.setName(currentVNFName);
                    SelectedVNFdict.setType(vnfr.getType());
                    for (Ip ip : vnfc.getIps()) {
                      SelectedVNFdict.setIP(ip.getIp());
                      logger.debug("[SF-Selections] Get Neutron Pro " + new Date().getTime());

                      SelectedVNFdict.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

                      break;
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
  }

  private List<VNFdict> getSFs(String SF_TYPE) {

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
          firstHopVNF.setName(currentVNFName);
          firstHopVNF.setType(vnfr.getType());

          firstHopVNF.setIP(currentIp);

          firstHopVNF.setNeutronPortId(currentNeutronPortID);
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

  private void UpdateChain(SFC.SFC_Data Chain_Data, List<VNFdict> VNF_instances) {

    logger.debug("[ LB Path Selection < Create Chain > ]  ");

    HashMap<Integer, VNFdict> VNFs = Chain_Data.getChainSFs();
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

    Chain_Data.setChainSFs(VNFs);

    List<SFPdict> newPaths = new ArrayList<>();
    SFPdict newPath = Chain_Data.getSFCdictInfo().getSfcDict().getPaths().get(0);
    newPath.setPath_SFs(VNFs);
    double PathTrafficLoad =
        Chain_Data.getSFCdictInfo().getSfcDict().getPaths().get(0).getPathTrafficLoad();
    newPath.setOldTrafficLoad(PathTrafficLoad);

    newPaths.add(0, newPath);

    Chain_Data.getSFCdictInfo().getSfcDict().setPaths(newPaths);

    //SFC_driver.DeleteSFP(Chain_Data.getRspID(), Chain_Data.isSymm());

    String new_instance_id = SFC_driver.CreateSFP(Chain_Data.getSFCdictInfo(), VNFs);

    String SFCC_name =
        SFC_Classifier_driver.Create_SFC_Classifer(Chain_Data.getClassifierInfo(), new_instance_id);

    logger.debug("[Update SFCC DB] " + Chain_Data.getRspID().substring(5));
    String IDx = Chain_Data.getRspID().substring(5);

    String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);
    logger.debug("[VNFFGR ID] =" + VNFFGR_ID);

    sfcc_db.update(
        VNFFGR_ID,
        new_instance_id,
        Chain_Data.getSfccName(),
        Chain_Data.getSFCdictInfo().getSfcDict().getSymmetrical(),
        VNFs,
        Chain_Data.getSFCdictInfo(),
        Chain_Data.getClassifierInfo());
    logger.info("[Update SFCC DB] is done !!");
  }
}
