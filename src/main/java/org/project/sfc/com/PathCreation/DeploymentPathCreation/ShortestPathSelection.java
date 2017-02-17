package org.project.sfc.com.PathCreation.DeploymentPathCreation;

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

  public ShortestPathSelection(String type) throws IOException {
    NC = new NeutronClient();
    SFC_driver = broker.getSFC(type);

  }
  private VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr,VNFdict prev_vnf) throws IOException {
    List<String> VNF_instances = new ArrayList<String>();
    System.out.println("**** SELECT VNF using Shortest Path ******  ");


    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        VNF_instances.add(vnfc_instance.getHostname());
      }
    }

    VNFdict new_vnf = new VNFdict();

    if (prev_vnf == null) {
      System.out.println("**** Previous VNF is null ***");

      Random randomizer = new Random();
      VNFdict firstHopVNF=new VNFdict();
      String VNF_instance_selected = VNF_instances.get(randomizer.nextInt(VNF_instances.size()));
      System.out.println("**** Selected VNF instance is "+ VNF_instance_selected);

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
    }

    System.out.println("**** Previous VNF is not null *** ");

    int minLength = Integer.MAX_VALUE;
    int length ;
    for(VirtualDeploymentUnit vdu:vnfr.getVdu()){
      for(VNFCInstance vnfc: vdu.getVnfc_instance()){
        String currentVNFName=vnfc.getHostname();
        System.out.println("**** Current VNF NAme: "+ currentVNFName);
        System.out.println("**** The Min distance = "+ minLength);
        String currentIp=null;
        String currentNeutronPortID=null;

        for (Ip ip : vnfc.getIps()) {
          currentIp =ip.getIp();
          currentNeutronPortID=NC.getNeutronPortID(currentIp);

          break;
        }
        length=getDistance(prev_vnf.getNeutronPortId(),currentNeutronPortID);
        System.out.println("**** The distance between it and the prev. node = "+ length);

        if (length < minLength) {
            System.out.println("**** The distance is less than the Min distance ***");

            minLength = length;
            new_vnf.setName(currentVNFName);
            new_vnf.setType(vnfr.getType());

            new_vnf.setIP(currentIp);
            System.out.println("[SF-Selections] Get Neutron Pro " + new Date().getTime());

            new_vnf.setNeutronPortId(currentNeutronPortID);
        }


      }
    }

    System.out.println("**** Selected VNF instance is "+ new_vnf.getName());


    return new_vnf;
  }

  //Calculate the distance between two VNFs instances
  private int getDistance(String prev_VNF_neutron_id,String currentVNF_neutron_id) throws IOException {
    int distance;
    System.out.println("**** Get Distance between ["+prev_VNF_neutron_id+ "] and ["+ currentVNF_neutron_id+"]");

    String currentVNFLocation=SFC_driver.GetHostID(currentVNF_neutron_id);
    System.out.println("**** Location for "+currentVNF_neutron_id+" = "+currentVNFLocation);
    String prevVNFLocation=SFC_driver.GetHostID(prev_VNF_neutron_id);
    System.out.println("**** Location for "+prev_VNF_neutron_id+" = "+prevVNFLocation);

    if(currentVNFLocation.equals(prevVNFLocation)){
      distance=0;
    }else {
      distance=1;
    }

    return distance;
  }

  public HashMap<Integer, VNFdict> CreatePath(
      Set<VirtualNetworkFunctionRecord> vnfrs,
      VNFForwardingGraphRecord vnffgr,
      NetworkServiceRecord nsr) {
    System.out.println("[SFP-Creation] Creating Path (1) started  at time " + new Date().getTime());

    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    List<VNFdict> vnf_test = new ArrayList<VNFdict>();
    List<String> chain = new ArrayList<String>();
    VNFdict prev_vnf=null;

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

                VNFdict new_vnf = null;
                try {
                  new_vnf = SelectVNF(vnfr,prev_vnf);
                } catch (IOException e) {
                  e.printStackTrace();
                }
                if(new_vnf!=null){
                  prev_vnf=new_vnf;
                } else {
                  System.out.println("Couldn't find a reachable SF for ServiceFunctionType: "+vnfr.getType());
                  return null;
                }

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
