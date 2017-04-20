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
import java.util.*;

/**
 * Created by mah on 4/22/16.
 */
public class RandomPathSelection {

  NeutronClient NC;
  private Logger logger;

  public RandomPathSelection() throws IOException {
    NC = new NeutronClient();
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  public VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr) {
    List<String> VNF_instances = new ArrayList<String>();

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());
      }
    }

    VNFdict new_vnf = new VNFdict();

    Random randomizer = new Random();
    String VNF_instance_selected = VNF_instances.get(randomizer.nextInt(VNF_instances.size()));
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
    logger.info("[SFP-Create] Creating Path started ");

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

    logger.info("[SFP-Create] Creating Path Finished ");

    return vnfdicts;
  }
}
