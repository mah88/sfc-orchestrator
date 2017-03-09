package org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;

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

  public RoundRobinSelection() throws IOException {
    NC = new NeutronClient();
  }
  public VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr) {
    List<String> VNF_instances = new ArrayList<String>();
    int countRoundRobin = 0;
    System.out.println("**** SELECT VNF using Round Robin ******  ");

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());
      }
    }

    VNFdict new_vnf = new VNFdict();
    if (mapCountRoundRobin.size() != 0) {
      for (String sfType : mapCountRoundRobin.keySet()) {
        if (sfType.equals(vnfr.getType())) {
          System.out.println("**** SF TYPE: "+ sfType);

          countRoundRobin = mapCountRoundRobin.get(sfType);
          System.out.println("**** countRoundRobin: "+ countRoundRobin);
          break;
        }
      }
    }
    String VNF_instance_selected = VNF_instances.get(countRoundRobin);
    System.out.println("**** Selected VNF instance: "+ VNF_instance_selected +" and its count round robin = "+countRoundRobin);

    countRoundRobin = (countRoundRobin + 1) % VNF_instances.size();
    mapCountRoundRobin.put(vnfr.getType(), countRoundRobin);
    System.out.println("**** CountRoundRobin becomes: "+ countRoundRobin + " for the SF Type: "+ vnfr.getType());

    new_vnf.setName(VNF_instance_selected);
    new_vnf.setType(vnfr.getType());
    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        if (vnfc_instance.getHostname() == VNF_instance_selected) {
          for (Ip ip : vnfc_instance.getIps()) {
            new_vnf.setIP(ip.getIp());
            System.out.println("[SFP-Creation] Get Neutron Pro " + new Date().getTime());

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
    System.out.println("[SFP-Creation] Creating Path (1) started  at time " + new Date().getTime());

    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    List<VNFdict> vnf_test = new ArrayList<VNFdict>();
    List<String> chain = new ArrayList<String>();

    int i = 0;
    // for getting the VNF instance NAME
    String VNF_NAME;

    System.out.println("[SFP-Creation] Creating Path started  at time " + new Date().getTime());
    for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {

      for (int counter = 0; counter < nfp.getConnection().size(); counter++) {
        System.out.println("[COUNTER] " + counter);

        for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {

          Integer k = Integer.valueOf(entry.getKey());

          int x = k.intValue();
          System.out.println("[entry key] " + x);

          //need to be adjusted again (use put(entry.key()
          if (counter == x) {

            for (VirtualNetworkFunctionRecord vnfr : vnfrs) {
              if (vnfr.getName().equals(entry.getValue())) {
                System.out.println("[entry Value] " + entry.getValue());

                VNFdict new_vnf = SelectVNF(vnfr);

                vnf_test.add(new_vnf);
                System.out.println("[-------] integer vlaue " + counter);

                vnfdicts.put(counter, vnf_test.get(counter));

                System.out.println("[VNFdicts] integer vlaue " + counter);
              }
            }
          }
        }
      }
    }

    System.out.println("[SFP-Created] Creating Path Finished  at time " + new Date().getTime());

    return vnfdicts;
  }
}

