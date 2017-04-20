package org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
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
import java.util.Set;

/**
 * Created by mah on 1/24/17.
 */
public class RoundRobinSelection {

  //mapCountRoundRobin works for the Type of Service Function
  private static Map<String, Integer> mapCountRoundRobin = new HashMap<>();
  NeutronClient NC;
  private Logger logger;

  public RoundRobinSelection() throws IOException {
    NC = new NeutronClient();
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public void Delete(Map<Integer, VNFdict> VNFs) {

    for (int i = 0; i < VNFs.size(); i++) {
      if (mapCountRoundRobin.containsKey(VNFs.get(i).getType())) {
        logger.info("[Found VNF TYPE in mapCountLoad] " + VNFs.get(i).getType());
        mapCountRoundRobin.remove(VNFs.get(i).getType());
        logger.info("[Removed the VNF from mapCountLoad");
      }
    }
  }

  public VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr) {
    List<String> VNF_instances = new ArrayList<String>();
    int countRoundRobin = 0;
    logger.info("[SELECT VNF] using Round Robin Algorithm");

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());
      }
    }

    VNFdict new_vnf = new VNFdict();
    if (mapCountRoundRobin.size() != 0) {
      for (String sfType : mapCountRoundRobin.keySet()) {
        if (sfType.equals(vnfr.getType())) {
          logger.debug("**** SF TYPE: " + sfType);

          countRoundRobin = mapCountRoundRobin.get(sfType);
          logger.debug("**** countRoundRobin: " + countRoundRobin);
          break;
        }
      }
    }
    String VNF_instance_selected = VNF_instances.get(countRoundRobin);
    logger.debug(
        "**** Selected VNF instance: "
            + VNF_instance_selected
            + " and its count round robin = "
            + countRoundRobin);

    countRoundRobin = (countRoundRobin + 1) % VNF_instances.size();
    mapCountRoundRobin.put(vnfr.getType(), countRoundRobin);
    logger.debug(
        "**** CountRoundRobin becomes: " + countRoundRobin + " for the SF Type: " + vnfr.getType());

    new_vnf.setName(VNF_instance_selected);
    new_vnf.setType(vnfr.getType());
    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        if (vnfc_instance.getHostname() == VNF_instance_selected) {
          new_vnf.setId(vnfc_instance.getId());
          new_vnf.setStatus(Status.ACTIVE);

          for (Ip ip : vnfc_instance.getIps()) {
            new_vnf.setIP(ip.getIp());
            logger.debug(
                "[Select-VNF] Setting the IP  for " + new_vnf.getName() + " : " + new_vnf.getIP());

            new_vnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

            break;
          }
        }
      }
    }

    return new_vnf;
  }

  public HashMap<Integer, VNFdict> CreatePath(
      Set<VirtualNetworkFunctionRecord> vnfrs,
      VNFForwardingGraphRecord vnffgr,
      NetworkServiceRecord nsr) {
    logger.info("[SFP-Create] Creating Path started");

    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    List<VNFdict> vnf_test = new ArrayList<VNFdict>();
    List<String> chain = new ArrayList<String>();

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

                VNFdict new_vnf = SelectVNF(vnfr);

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
