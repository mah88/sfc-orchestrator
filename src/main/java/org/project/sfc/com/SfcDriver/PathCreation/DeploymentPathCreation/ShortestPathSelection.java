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
import org.project.sfc.com.SfcModel.SFCdict.Status;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by mah on 1/24/17.
 */
public class ShortestPathSelection {
  NeutronClient NC;
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  SfcBroker broker = new SfcBroker();
  private Logger logger;

  public ShortestPathSelection(String type) throws IOException {
    NC = new NeutronClient();
    SFC_driver = broker.getSFC(type);
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  private VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr, VNFdict prev_vnf)
      throws IOException {
    List<String> VNF_instances = new ArrayList<String>();
    logger.info("[SELECT VNF] using Shortest Path Algorithm ");

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());
      }
    }

    VNFdict new_vnf = new VNFdict();

    if (prev_vnf == null) {
      System.out.println("**** Previous VNF is null ***");

      Random randomizer = new Random();
      VNFdict firstHopVNF = new VNFdict();
      String VNF_instance_selected = VNF_instances.get(randomizer.nextInt(VNF_instances.size()));
      System.out.println("**** Selected VNF instance is " + VNF_instance_selected);

      firstHopVNF.setName(VNF_instance_selected);
      firstHopVNF.setType(vnfr.getType());
      for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
        for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
          if (vnfc_instance.getHostname() == VNF_instance_selected) {
            firstHopVNF.setId(vnfc_instance.getId());
            firstHopVNF.setStatus(Status.ACTIVE);
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
    }

    logger.debug("[Previous VNF is not null]");

    int minLength = Integer.MAX_VALUE;
    int length;
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance vnfc : vdu.getVnfc_instance()) {
        String currentVNFName = vnfc.getHostname();
        String currentIp = null;
        String currentNeutronPortID = null;

        for (Ip ip : vnfc.getIps()) {
          currentIp = ip.getIp();
          currentNeutronPortID = NC.getNeutronPortID(currentIp);

          break;
        }
        /*  if (prev_vnf == null) {
          length = getDistanceFirstHOP(currentNeutronPortID);

        } else {*/
        length = getDistance(prev_vnf.getNeutronPortId(), currentNeutronPortID);
        // }

        if (length < minLength) {

          minLength = length;
          new_vnf.setName(currentVNFName);
          new_vnf.setType(vnfr.getType());
          new_vnf.setId(vnfc.getId());

          new_vnf.setIP(currentIp);

          new_vnf.setNeutronPortId(currentNeutronPortID);
        }
      }
    }

    logger.debug("[Selected VNF instance] is " + new_vnf.getName());

    return new_vnf;
  }

  //Calculate the distance between two VNFs instances
  private int getDistance(String prev_VNF_neutron_id, String currentVNF_neutron_id)
      throws IOException {
    int distance;
    logger.debug(
        "[Get Distance] between [" + prev_VNF_neutron_id + "] and [" + currentVNF_neutron_id + "]");

    String currentVNFLocation = SFC_driver.GetHostID(currentVNF_neutron_id);
    logger.debug("[Location] for " + currentVNF_neutron_id + " is " + currentVNFLocation);
    String prevVNFLocation = SFC_driver.GetHostID(prev_VNF_neutron_id);
    logger.debug("[Location] for " + prev_VNF_neutron_id + " is " + prevVNFLocation);

    if (currentVNFLocation.equals(prevVNFLocation)) {
      distance = 0;
    } else {
      distance = 1;
    }

    return distance;
  }

  private int getDistanceFirstHOP(String currentVNF_neutron_id) throws IOException {
    int distance;
    logger.debug("[Get Distance for First HOP between] [" + currentVNF_neutron_id + "]");

    String currentVNFLocation = SFC_driver.GetHostID(currentVNF_neutron_id);
    logger.debug("[Location] for " + currentVNF_neutron_id + " = " + currentVNFLocation);
    String prevVNFLocation = "006f3e16-571e-4d13-a840-d66832c9b985";

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
    logger.info("[SFP-Create] Creating Path  started  ");

    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    List<VNFdict> vnf_test = new ArrayList<VNFdict>();
    List<String> chain = new ArrayList<String>();
    VNFdict prev_vnf = null;

    int i = 0;
    // for getting the VNF instance NAME
    String VNF_NAME;

    for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {

      for (int counter = 0; counter < nfp.getConnection().size(); counter++) {

        for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {

          Integer k = Integer.valueOf(entry.getKey());

          int x = k.intValue();

          //need to be adjusted again (use put(entry.key()
          if (counter == x) {

            for (VirtualNetworkFunctionRecord vnfr : vnfrs) {
              if (vnfr.getName().equals(entry.getValue())) {
                System.out.println("[entry Value] " + entry.getValue());

                VNFdict new_vnf = null;
                try {
                  new_vnf = SelectVNF(vnfr, prev_vnf);
                } catch (IOException e) {
                  e.printStackTrace();
                }
                if (new_vnf != null) {
                  prev_vnf = new_vnf;
                } else {
                  logger.warn(
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
