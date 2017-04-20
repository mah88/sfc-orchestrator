package org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcImpl.Broker.SfcBroker;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mah on 1/25/17.
 */
public class TradeOffShortestPathLoadBalancingSelection {
  NeutronClient NC;
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  SfcBroker broker = new SfcBroker();
  private Logger logger;

  //mapCountLoad works for the Type of Service Function
  private static Map<String, Map<String, Integer>> mapCountLoad = new HashMap<>();

  public TradeOffShortestPathLoadBalancingSelection(String type) throws IOException {
    NC = new NeutronClient();
    SFC_driver = broker.getSFC(type);
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public void Delete(Map<Integer, VNFdict> VNFs) {

    for (int i = 0; i < VNFs.size(); i++) {
      if (mapCountLoad.containsKey(VNFs.get(i).getType())) {
        logger.info("[Found VNF TYPE in mapCountLoad] " + VNFs.get(i).getType());
        mapCountLoad.remove(VNFs.get(i).getType());
        logger.info("[Removed the VNF from mapCountLoad");
      }
    }
  }

  public VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr, VNFdict prev_vnf, String Chain_QoS)
      throws IOException {
    List<String> VNF_instances = new ArrayList<String>();

    double SfcPriority;
    int countLoad = 0; //Counter for the Load per SF instance
    int LoadSum = 0; // Summation of the Load per SF type
    Map<String, Double> alpha = new HashMap<>(); //Alpha value per  VNF instance

    if (Chain_QoS.equals("gold")) {
      SfcPriority = 3;
    } else if (Chain_QoS.equals("silver")) {
      SfcPriority = 2;
    } else {
      SfcPriority = 1;
    }
    double MaxSfcPriority = 3;
    int MaxDistance = Integer.MIN_VALUE;
    int MaxLoad = Integer.MIN_VALUE;

    logger.info("[SELECT VNF] using tradeoff delay and load algorithm ");
    if (!mapCountLoad.containsKey(vnfr.getType())) {
      logger.debug("[Adding VNF TYPE]  =  [" + vnfr.getType() + " ] to the mapCountLoad");

      Map<String, Integer> mapVNFinstanceCountLoad = new HashMap<>();
      mapCountLoad.put(vnfr.getType(), mapVNFinstanceCountLoad);
    }

    //Summing up the Load of VNF instances
    if (mapCountLoad.size() != 0) {
      logger.debug("[Calculating the Load Sum] for the VNFs ****");
      if (mapCountLoad.containsKey(vnfr.getType())) {
        if (mapCountLoad.get(vnfr.getType()).size() != 0) {
          logger.debug("[Calculating the Load Sum] for the VNF type [" + vnfr.getType() + "]");
          for (String sfInstanceName : mapCountLoad.get(vnfr.getType()).keySet()) {
            LoadSum = LoadSum + mapCountLoad.get(vnfr.getType()).get(sfInstanceName);
          }
        }
      }
    }
    logger.debug("[Load summation] is" + LoadSum);

    // Calculating the Maximum Distance and the alpha value
    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());

        if (prev_vnf != null) {
          String NeutronPortID = null;
          for (Ip ip : vnfc_instance.getIps()) {
            NeutronPortID = NC.getNeutronPortID(ip.getIp());
            break;
          }

          int distance = getDistance(prev_vnf.getName(), vnfc_instance.getHostname());
          if (distance > MaxDistance) {
            MaxDistance = distance;
          }
          logger.debug("[Max Distance] is " + MaxDistance);

          //calculate alpha value and get the Max Load
          logger.debug("[Calculating the Alpha value] for the VNFs  [" + vnfr.getType() + "]");
        }
        double alpha_value;
        int LoadCounter;
        if (mapCountLoad.get(vnfr.getType()).containsKey(vnfc_instance.getHostname())) {
          LoadCounter = mapCountLoad.get(vnfr.getType()).get(vnfc_instance.getHostname());
        } else {
          LoadCounter = 0;
          mapCountLoad.get(vnfr.getType()).put(vnfc_instance.getHostname(), LoadCounter);
        }
        logger.debug(
            "pLoad of SF Instance] : " + vnfc_instance.getHostname() + "  is " + LoadCounter);

        if (MaxLoad < LoadCounter) {

          MaxLoad = LoadCounter;
          logger.debug(
              "[Maximum Load] is SF Instance: "
                  + vnfc_instance.getHostname()
                  + " and it Load = "
                  + MaxLoad);
        }
        if (LoadSum > 0) {
          alpha_value =
              ((double) (LoadSum - LoadCounter) / (2 * LoadSum))
                  + (SfcPriority / (2 * (MaxSfcPriority)));

        } else {
          alpha_value = SfcPriority / (2 * (MaxSfcPriority));
        }
        alpha.put(vnfc_instance.getHostname(), alpha_value);
        logger.debug(
            "[Load of SF Instance] : "
                + vnfc_instance.getHostname()
                + " = "
                + LoadCounter
                + " and its alpha value = "
                + alpha_value);
      }
    }

    VNFdict new_vnf = new VNFdict();

    int minLoad = Integer.MAX_VALUE;
    if (prev_vnf == null) {
      logger.debug("[ Previous VNF in the Chain is NULL]");

      VNFdict firstHopVNF = new VNFdict();
      String VNF_instance_selected = null;
      if (mapCountLoad.size() != 0) {
        logger.debug("[ mapCountLoad ] is not EMPTY *****");
        if (mapCountLoad.containsKey(vnfr.getType())) {
          logger.debug("[ mapCountLoad ] for the VNF [" + vnfr.getType() + "] is not EMPTY *****");
          if (mapCountLoad.get(vnfr.getType()).size() != 0) {
            for (String sfInstance : mapCountLoad.get(vnfr.getType()).keySet()) {
              if (mapCountLoad.get(vnfr.getType()).get(sfInstance) < minLoad) {
                logger.debug(
                    "[ MinLOAD ] --> "
                        + minLoad
                        + " <-- is greater than the VNF instance Load ["
                        + sfInstance
                        + "], it load -->"
                        + mapCountLoad.get(vnfr.getType()).get(sfInstance)
                        + " <-- *****");

                VNF_instance_selected = sfInstance;
                logger.debug("**** VNF instance Selected is [" + VNF_instance_selected + "] ");

                minLoad = mapCountLoad.get(vnfr.getType()).get(sfInstance);
              }
            }
          } else {
            logger.warn(
                "*[mapCountLoad] for the VNF instance of type ["
                    + vnfr.getType()
                    + "] is EMPTY *****");
          }
        } else {
          logger.warn("[VNF type] " + vnfr.getType() + " is not exist in the mapCountLoad ###");
        }
      } else {
        logger.warn("[mapCountLoad] Size is ZERO ");
      }

      if (VNF_instance_selected != null) {
        logger.debug("[ VNF instance] is Selected *****");

        firstHopVNF.setName(VNF_instance_selected);
        firstHopVNF.setType(vnfr.getType());
        for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
          for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
            if (vnfc_instance.getHostname() == VNF_instance_selected) {
              firstHopVNF.setId(vnfc_instance.getId());

              for (Ip ip : vnfc_instance.getIps()) {
                firstHopVNF.setIP(ip.getIp());

                firstHopVNF.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

                break;
              }
            }
          }
        }
        if (mapCountLoad.containsKey((vnfr.getType()))) {
          if (mapCountLoad.get(vnfr.getType()).size() != 0) {
            if (mapCountLoad.get(vnfr.getType()).containsKey(firstHopVNF.getName())) {
              countLoad = mapCountLoad.get(vnfr.getType()).get(firstHopVNF.getName());
              System.out.println(" First HOP --> Count Load OLD=" + countLoad);
            }
          }
          countLoad = countLoad + 1;
          logger.debug(" First HOP --> Count Load NEW=" + countLoad);
          mapCountLoad.get(vnfr.getType()).put(firstHopVNF.getName(), countLoad);
        } else {
          logger.error(" Could not select VNF instance: VNF instance name is Null");
          return null;
        }
      }

      return firstHopVNF;
    }
    double minIndex = Double.MAX_VALUE;

    int Pathlength;
    double Index;

    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance vnfc : vdu.getVnfc_instance()) {
        String currentVNFName = vnfc.getHostname();
        String CurrentVNF_NeutronPortID = null;
        String CurrentVNF_IP = null;

        for (Ip ip : vnfc.getIps()) {
          CurrentVNF_IP = ip.getIp();
          CurrentVNF_NeutronPortID = NC.getNeutronPortID(ip.getIp());
          break;
        }
        Pathlength = getDistance(prev_vnf.getName(), currentVNFName);
        logger.debug(
            "[ Path Length] = "
                + Pathlength
                + " for SF Instance : "
                + prev_vnf.getName()
                + " and  SF instance: "
                + currentVNFName);

        int Load = mapCountLoad.get(vnfr.getType()).get(currentVNFName);
        System.out.println(" SF Load = " + Load + " for SF Instance : " + currentVNFName);

        double alpha_value = alpha.get(currentVNFName);
        logger.debug(" [SF alpha Value] = " + alpha_value + " for SF Instance : " + currentVNFName);

        if (MaxDistance > 0 && MaxLoad > 0) {
          logger.debug("[ Max Disance] > 0 and Max Load > 0 ");
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
          System.out.println(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
        }
        if (Index < minIndex) {
          minIndex = Index;
          new_vnf.setId(vnfc.getId());

          new_vnf.setName(currentVNFName);
          new_vnf.setType(vnfr.getType());
          new_vnf.setNeutronPortId(CurrentVNF_NeutronPortID);
          new_vnf.setIP(CurrentVNF_IP);
          logger.debug("[ Index Value ] is less than the Min Index ** ");
        }
      }
    }
    if (mapCountLoad.containsKey(vnfr.getType())) {
      if (mapCountLoad.get(vnfr.getType()).size() != 0) {

        countLoad = mapCountLoad.get(vnfr.getType()).get(new_vnf.getName());
      }
    }
    countLoad = countLoad + 1;

    mapCountLoad.get(vnfr.getType()).put(new_vnf.getName(), countLoad);
    logger.debug(" [ SELECTED VNF instance ] = " + new_vnf.getName());

    return new_vnf;
  }

  //Calculate the distance between two VNFs instances
  public int getDistance(String prev_VNF, String currentVNF) throws IOException {
    int distance;
    logger.debug("[Get Distance] between [" + prev_VNF + "] and [" + currentVNF + "]");

    String currentVNFLocation = SFC_driver.GetConnectedSFF(currentVNF);
    logger.debug("[Location] for " + currentVNF + " = " + currentVNFLocation);

    String prevVNFLocation = SFC_driver.GetConnectedSFF(prev_VNF);
    logger.debug("[Location] for " + prev_VNF + " = " + prevVNFLocation);

    if (currentVNFLocation.equals(prevVNFLocation)) {
      distance = 0;
    } else {
      distance = 1;
    }

    return distance;
  }

  public HashMap<Integer, VNFdict> CreatePath(
      Set<VirtualNetworkFunctionRecord> vnfrs,
      VNFForwardingGraphRecord vnffgr,
      NetworkServiceRecord nsr) {
    logger.info("[SFP-Create] Creating Path started  ");

    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    List<VNFdict> vnf_test = new ArrayList<VNFdict>();
    List<String> chain = new ArrayList<String>();
    VNFdict prev_vnf = null;

    int i = 0;
    // for getting the VNF instance NAME
    String VNF_NAME;

    for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {
      String QoS_level = nfp.getPolicy().getQoSLevel();
      if (QoS_level == null) {
        logger.warn(" SFC QoS LEVEL is null");
        QoS_level = "bronze";
      }
      for (int counter = 0; counter < nfp.getConnection().size(); counter++) {

        for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {

          Integer k = Integer.valueOf(entry.getKey());

          int x = k.intValue();

          //need to be adjusted again (use put(entry.key()
          if (counter == x) {

            for (VirtualNetworkFunctionRecord vnfr : vnfrs) {
              if (vnfr.getName().equals(entry.getValue())) {

                VNFdict new_vnf = null;
                try {

                  logger.debug("QoS Level for the SFC: " + QoS_level);
                  new_vnf = SelectVNF(vnfr, prev_vnf, QoS_level);
                } catch (IOException e) {
                  e.printStackTrace();
                }
                if (new_vnf != null) {
                  prev_vnf = new_vnf;
                } else {
                  logger.error(
                      "Couldn't find a reachable SF for ServiceFunctionType: " + vnfr.getType());
                  return null;
                }

                vnf_test.add(new_vnf);

                vnfdicts.put(counter, vnf_test.get(counter));
              }
            }
          }
        }
      }
    }

    logger.info("[SFP-Create] Creating Path Finished  ");

    return vnfdicts;
  }
}
