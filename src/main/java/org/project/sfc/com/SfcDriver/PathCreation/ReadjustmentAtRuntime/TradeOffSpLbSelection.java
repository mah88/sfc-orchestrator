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

/**
 * Created by mah on 1/26/17.
 */
public class TradeOffSpLbSelection {

  NeutronClient NC;
  SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  org.project.sfc.com.SfcInterfaces.SFCclassifier SFC_Classifier_driver;
  SfcBroker broker = new SfcBroker();
  private static Map<String, Map<String, Integer>> mapCountLoad = new HashMap<>();
  private Logger logger;

  public TradeOffSpLbSelection(String type) throws IOException {

    this.SFC_driver = broker.getSFC(type);
    this.SFC_Classifier_driver = broker.getSfcClassifier(type);
    this.NC = new NeutronClient();
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public void ReadjustVNFsAllocation(VirtualNetworkFunctionRecord vnfr) throws IOException {
    logger.info("[ReadJust VNFs Allocation] using tradeoff Selection Algorithm ");

    HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    HashMap<String, SFC.SFC_Data> Involved_SFCs = new HashMap<String, SFC.SFC_Data>();
    int total_size_SFCs;
    Map<String, Double> alpha = new HashMap<>(); //Alpha value per  VNF instance

    double MaxSfcPriority = 3;
    int MaxDistance = Integer.MIN_VALUE;
    double MaxLoad = Double.MIN_VALUE;

    if (All_SFCs != null) {
      logger.debug("[ ALL SFCs number ] =  " + All_SFCs.size());
      total_size_SFCs = All_SFCs.size();
    } else {
      total_size_SFCs = 0;
      logger.debug("SFC Data base is Empty : Can not get the stored chains data  ");
    }
    logger.debug("[ReadJust VNFs Allocation] Get Involved SFCs for this VNF ");

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
              "[Tradeoff Path Selection] Involved SFCs :  "
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
        double SfcPriority =
            Involved_SFCs.get(SFCKey.getKey())
                .getSFCdictInfo()
                .getSfcDict()
                .getPaths()
                .get(0)
                .getQoS();

        for (int position : ChainSFs.keySet()) {
          double LoadSum = 0; // Summation of the Load per SF type

          // Calculating the Maximum Distance
          // List<VNFdict> SF_instances=getSFs(ChainSFs.get(position).getType());
          List<VNFdict> SF_instances = sfcc_db.getVNFs(ChainSFs.get(position).getType());
          logger.debug(
              "(*<**>*) VNFs size is :"
                  + SF_instances.size()
                  + " with type= "
                  + ChainSFs.get(position).getType());

          //Summation of Load

          for (int x = 0; x < SF_instances.size(); x++) {
            LoadSum = LoadSum + SF_instances.get(x).getTrafficLoad();
          }
          logger.debug("**** Load Sum :" + LoadSum);
          if (Previous_vnf.getName() == null && position > 0) {
            Previous_vnf = ChainSFs.get(position - 1);
            logger.debug(
                "(*<**>*) Previous VNF is still NULL and the Position >0 : Get the Old Prev. VNF in the chain "
                    + ChainSFs.get(position - 1).getName()
                    + " with type= "
                    + ChainSFs.get(position).getType());
          }

          for (int i = 0; i < SF_instances.size(); i++) {
            int distance;
            if (Previous_vnf.getName() != null) {
              distance = getDistance(Previous_vnf.getName(), SF_instances.get(i).getName());
            } else {
              distance = getDistanceFirstHOP(SF_instances.get(i).getName());
            }
            logger.debug("**** distance is :" + distance);
            logger.debug("**** Max Distance is :" + MaxDistance);

            if (distance > MaxDistance) {
              MaxDistance = distance;
              logger.debug("(*<**>*) Max Distance is :" + MaxDistance);
            }
          }
          double countLoad = 0; //Counter for the Load per SF instance

          //Calculating alpha
          for (int i = 0; i < SF_instances.size(); i++) {

            double alpha_value;
            countLoad = SF_instances.get(i).getTrafficLoad();
            logger.debug(
                "****  SF Instance: "
                    + SF_instances.get(i).getName()
                    + " = "
                    + countLoad
                    + " and its load = "
                    + countLoad);
            if (MaxLoad < countLoad) {

              MaxLoad = countLoad;
              logger.debug(
                  "**** MAximum Load is SF Instance: "
                      + SF_instances.get(i).getName()
                      + " and it Load = "
                      + MaxLoad);
            }
            if (LoadSum > 0) {
              alpha_value =
                  ((LoadSum - countLoad) / (2 * LoadSum)) + (SfcPriority / (2 * (MaxSfcPriority)));
            } else {
              alpha_value = SfcPriority / (2 * (MaxSfcPriority));
            }

            alpha.put(SF_instances.get(i).getName(), alpha_value);
            logger.debug(
                "**** Load of SF Instance: "
                    + SF_instances.get(i).getName()
                    + " = "
                    + countLoad
                    + " and its alpha value = "
                    + alpha_value);
          }

          //Continue building the chain path
          if (foundVNF == true) {
            VNFdict SelectedVNFdict = new VNFdict();
            //List<VNFdict> SFs=getSFs(ChainSFs.get(position).getType());
            List<VNFdict> SFs = sfcc_db.getVNFs(ChainSFs.get(position).getType());
            double minIndex = Double.MAX_VALUE;
            int Pathlength;
            double Index;

            logger.debug(
                "[ReadJust VNFs Allocation] The Position of the SF in the Chain = " + position);

            for (int i = 0; i < SFs.size(); i++) {

              String currentVNFName = SFs.get(i).getName();
              logger.debug("[Select VNF] Current VNF NAME = " + currentVNFName);
              logger.debug("[Select VNF] Prev VNF NAME = " + Previous_vnf.getName());
              VNFdict prev_vnf = Previous_vnf;
              Pathlength = getDistance(prev_vnf.getName(), currentVNFName);
              double Load = SFs.get(i).getTrafficLoad();

              double alpha_value = alpha.get(currentVNFName);
              logger.debug("==> Load is  " + Load + " and alpha is " + alpha_value);

              if (MaxDistance > 0 && MaxLoad > 0) {
                logger.debug("==> Max Disance > 0 and Max Load > 0 ");
                Index =
                    ((double) ((1 - alpha_value) * Load) / MaxLoad)
                        + ((double) (alpha_value * Pathlength) / MaxDistance);
                logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
              } else if (MaxLoad > 0) {
                logger.debug("==> Max Load > 0 ");
                Index = ((double) ((1 - alpha_value) * Load) / MaxLoad);
                logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
              } else if (MaxDistance > 0) {
                logger.debug("==> Max Disance > 0  ");
                Index = ((double) (alpha_value * Pathlength) / MaxDistance);
                logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
              } else {
                logger.debug("==> Max Disance = 0 and Max Load = 0 ");

                Index = 1 - alpha_value;
                logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
              }
              System.out.println("[Select VNF] Index between them = " + Index);

              if (Index < minIndex) {
                logger.debug("[Select VNF] Index is minimum than the Min Index = " + minIndex);

                minIndex = Index;
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
                "[Readjust VNFs Allocation] Found the Position of the SF in the Chain = "
                    + position);
            SFposition = position;
            double minIndex = Double.MAX_VALUE;
            int Pathlength;
            double Index;

            // if (SFposition > 0) {
            for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
              for (VNFCInstance vnfc : vdu.getVnfc_instance()) {
                String currentVNFName = vnfc.getHostname();
                logger.debug("[Current VNF] Name: " + currentVNFName);

                double Load = 0;

                if (SFposition > 0) {
                  VNFdict prev_vnf = ChainSFs.get(SFposition - 1);
                  logger.debug("[Previous VNF] Name: " + prev_vnf.getName());
                  Pathlength = getDistance(prev_vnf.getName(), currentVNFName);
                } else {
                  Pathlength = getDistanceFirstHOP(currentVNFName);
                }
                // if(ChainSFs.get(SFposition).getName().equals(currentVNFName)){
                int size = sfcc_db.getVNFs(ChainSFs.get(position).getType()).size();
                for (int u = 0; u < size; u++) {
                  if (sfcc_db
                      .getVNFs(ChainSFs.get(position).getType())
                      .get(u)
                      .getName()
                      .equals(vnfc.getHostname())) {
                    logger.debug("[CURRENT VNF] Name is found in the Chains : " + currentVNFName);

                    Load =
                        sfcc_db.getVNFs(ChainSFs.get(position).getType()).get(u).getTrafficLoad();
                  }
                }
                //   }

                logger.debug("[CURRENT VNF] LOAD = : " + Load);

                double alpha_value;
                if (alpha.containsKey(currentVNFName)) {
                  alpha_value = alpha.get(currentVNFName);
                  logger.debug(
                      " The VNF instance  aplha value= "
                          + alpha_value
                          + " for SF Instance : "
                          + currentVNFName);

                } else {
                  alpha_value = 0.5 + (SfcPriority / (2 * (MaxSfcPriority)));
                  logger.debug(
                      " The New VNF instance  aplha value= "
                          + alpha_value
                          + " for SF Instance : "
                          + currentVNFName);
                }

                if (MaxDistance > 0 && MaxLoad > 0) {
                  logger.debug("==> Max Disance > 0 and Max Load > 0 ");
                  Index =
                      ((double) ((1 - alpha_value) * Load) / MaxLoad)
                          + ((double) (alpha_value * Pathlength) / MaxDistance);
                  logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
                } else if (MaxLoad > 0) {
                  logger.debug("==> Max Load > 0 ");
                  Index = ((double) ((1 - alpha_value) * Load) / MaxLoad);
                  logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
                } else if (MaxDistance > 0) {
                  logger.debug("==> Max Disance > 0  ");
                  Index = ((double) (alpha_value * Pathlength) / MaxDistance);
                  logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
                } else {
                  logger.debug("==> Max Disance = 0 and Max Load = 0 ");

                  Index = 1 - alpha_value;
                  logger.debug(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
                }
                if (Index < minIndex && (Load <= 10000000 || SfcPriority == MaxSfcPriority)) {
                  logger.debug(
                      " LOAD = "
                          + Load
                          + "and SFC PRIORITY = "
                          + SfcPriority
                          + " --> for SF Instance : "
                          + currentVNFName);
                  minIndex = Index;
                  SelectedVNFdict.setName(currentVNFName);
                  SelectedVNFdict.setType(vnfr.getType());
                  for (Ip ip : vnfc.getIps()) {
                    SelectedVNFdict.setIP(ip.getIp());

                    SelectedVNFdict.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
                    break;
                  }
                }
              }
            }

            VNF_instances.add(SelectedVNFdict);

            foundVNF = true;
            Previous_vnf = SelectedVNFdict;
            logger.info("[SF-Selections] Selected VNF : " + SelectedVNFdict.getName());

            /*   } else {
              System.out.println("[Read Just VNFs Allocation] Select first HOP ]");
              System.out.println(
                  "[SF-Selections]  SFposition: "
                      + SFposition
                      + "  VNF instance : "
                      + SelectedVNFdict.getName());

              SelectedVNFdict = SelectFirstHop(vnfr);
              VNF_instances.add(SelectedVNFdict);
              Previous_vnf = SelectedVNFdict;
              foundVNF = true;
            }*/
          }
          logger.debug("[ReadJust VNFs Allocation] FOUND is true? ]" + foundVNF);
        }

        UpdateChain(Involved_SFCs.get(SFCKey.getKey()), VNF_instances);
        logger.info("[UPDATING Chain Paths finished]");
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
          if (!SFsList.contains(VNFs.get(VNFcounter.getKey()))) {
            SFsList.add(VNFs.get(VNFcounter.getKey()));
            logger.debug(
                "[trade off Path Selection] Added VNF instance to the list :  "
                    + VNFs.get(VNFcounter.getKey()).getName());
          }
        }
      }
    }

    return SFsList;
  }

  private VNFdict SelectFirstHop(VirtualNetworkFunctionRecord vnfr) {
    List<VNFdict> SFs = sfcc_db.getVNFs(vnfr.getType());
    double minLoad = Double.MAX_VALUE;
    double Load = 0;
    VNFdict FirstHop = new VNFdict();

    for (int i = 0; i < SFs.size(); i++) {
      logger.debug("[tradeoff Path Selection] VNF instance Name:  " + SFs.get(i).getName());

      Load = SFs.get(i).getTrafficLoad();
      logger.debug("[tradeoff Path Selection] VNF Load :  " + Load);

      if (Load < minLoad) {

        minLoad = Load;
        FirstHop = SFs.get(i);
      }
    }

    logger.debug(
        "[tradeoff Path Selection] Selected VNF  for the First Hop :  " + FirstHop.getName());

    return FirstHop;

    /*
    List<String> VNF_instances = new ArrayList<String>();
    System.out.println("[Tradeoff Path Selection] SELECT first HOP  " );

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());
        System.out.println("[Tradeoff Path Selection] added VNF instance   " + vnfc_instance.getHostname()+" to the list of VNF instances with type "+vnfr.getType());

      }
    }
    VNFdict firstHopVNF=new VNFdict();
    Random randomizer=new Random();
    String VNF_instance_selected = VNF_instances.get(randomizer.nextInt(VNF_instances.size()));
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
    return firstHopVNF;
    */
  }

  private int getDistanceFirstHOP(String currentVNF) throws IOException {
    int distance;
    String currentVNFLocation = SFC_driver.GetConnectedSFF(currentVNF);
    String prevVNFLocation = "SFF-192.168.0.16";
    if (currentVNFLocation == null) {
      logger.warn("ERROR [ Could not get the connected SFF to one of the VNFs ]  ");
      return 1000;

    } else if (currentVNFLocation.equals(prevVNFLocation)) {
      distance = 0;
    } else {
      distance = 1;
    }

    return distance;
  }
  //Calculate the distance between two VNFs instances
  private int getDistance(String prev_VNF, String currentVNF) throws IOException {
    logger.debug("**** Get Distance between [" + prev_VNF + "] and [" + currentVNF + "]");

    int distance;
    String currentVNFLocation = SFC_driver.GetConnectedSFF(currentVNF);
    logger.debug("**** Location for " + currentVNF + " = " + currentVNFLocation);

    String prevVNFLocation = SFC_driver.GetConnectedSFF(prev_VNF);
    logger.debug("**** Location for " + prev_VNF + " = " + prevVNFLocation);

    if (currentVNFLocation.equals(prevVNFLocation)) {
      distance = 0;
    } else {
      distance = 1;
    }
    logger.debug("**** DISTANCE = " + distance);

    return distance;
  }

  private void UpdateChain(SFC.SFC_Data Chain_Data, List<VNFdict> VNF_instances) {

    logger.info("[ TDL Path Selection < Create Chain > ]  ");

    HashMap<Integer, VNFdict> VNFs = Chain_Data.getChainSFs();
    Iterator count = VNFs.entrySet().iterator();
    logger.debug("[ TDL Path Selection < Create Chain > ] SIZE of VNFs  " + VNFs.size());

    while (count.hasNext()) {

      Map.Entry VNFcounter = (Map.Entry) count.next();
      for (int i = 0; i < VNF_instances.size(); i++) {
        logger.debug(
            "[ Shortest Path Selection < Create Chain > ] chain data VNF type= "
                + VNFs.get(VNFcounter.getKey()).getType()
                + "VNF instance TYPE= "
                + VNF_instances.get(i).getType());

        if (VNFs.get(VNFcounter.getKey()).getType().equals(VNF_instances.get(i).getType())) {
          logger.debug("[ Shortest Path Selection < Create Chain > Found the same TYPE  ");

          int position = Integer.valueOf(VNFcounter.getKey().toString()).intValue();

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

    logger.debug("[NEW Classifier updated ] " + SFCC_name);
    logger.debug("[Update SFCC DB] " + Chain_Data.getRspID().substring(5));
    String IDx = Chain_Data.getRspID().substring(5);

    String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);

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
