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
 * Created by mah on 1/25/17.
 */
public class TradeOffShortestPathLoadBalancingSelection {
  NeutronClient NC;
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  SfcBroker broker = new SfcBroker();

  //mapCountLoad works for the Type of Service Function
  private static Map<String, Map<String,Integer>> mapCountLoad = new HashMap<>();

  public TradeOffShortestPathLoadBalancingSelection(String type) throws IOException {
    NC = new NeutronClient();
    SFC_driver = broker.getSFC(type);

  }

  public VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr,VNFdict prev_vnf,String Chain_QoS) throws IOException {
    List<String> VNF_instances = new ArrayList<String>();

    double SfcPriority;
    int countLoad = 0; //Counter for the Load per SF instance
    int LoadSum=0; // Summation of the Load per SF type
    Map<String,Double> alpha=new HashMap<>(); //Alpha value per  VNF instance

    if(Chain_QoS.equals("gold")){
      SfcPriority=3;
    }else if (Chain_QoS.equals("silver")){
      SfcPriority=2;
    }else {
      SfcPriority=1;
    }
    double MaxSfcPriority=3;
    int MaxDistance=Integer.MIN_VALUE;
    int MaxLoad=Integer.MIN_VALUE;

    System.out.println("**** SELECT VNF using tradeoff algorithm ******  ");
    if(!mapCountLoad.containsKey(vnfr.getType())) {
      System.out.println("****Adding VNF TYPE =  ["+vnfr.getType()+" ] to the mapCountLoad");

      Map<String, Integer> mapVNFinstanceCountLoad = new HashMap<>();
      mapCountLoad.put(vnfr.getType(), mapVNFinstanceCountLoad);
    }


    //Summing up the Load of VNF instances
    if(mapCountLoad.size()!=0) {
      System.out.println("****Calculating the Load Sum for the VNFs ****");
      if(mapCountLoad.containsKey(vnfr.getType())) {
        if (mapCountLoad.get(vnfr.getType()).size() != 0) {
          System.out.println("Calculating the Load Sum for the VNF type [" + vnfr.getType() + "]");
          for (String sfInstanceName : mapCountLoad.get(vnfr.getType()).keySet()) {
            LoadSum = LoadSum + mapCountLoad.get(vnfr.getType()).get(sfInstanceName);
            System.out.println("**** Load summation:" + LoadSum);
          }
        }
      }
    }
    System.out.println("**** Load Summation = "+LoadSum);

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
            System.out.println("****Max Distance becomes :" + MaxDistance);
          }

          //calculate alpha value and get the Max Load
          System.out.println("****Calculating the Alpha value for the VNFs  [" + vnfr.getType() + "]");
        }
        double alpha_value;
        int LoadCounter;
        if(mapCountLoad.get(vnfr.getType()).containsKey(vnfc_instance.getHostname())) {
          LoadCounter = mapCountLoad.get(vnfr.getType()).get(vnfc_instance.getHostname());
        }else {
          LoadCounter=0;
          mapCountLoad.get(vnfr.getType()).put(vnfc_instance.getHostname(),LoadCounter);
        }
        System.out.println("**** Load of SF Instance: " +vnfc_instance.getHostname()+"  = "+ LoadCounter);

          if(MaxLoad<LoadCounter){

            MaxLoad=LoadCounter;
            System.out.println("**** MAximum Load is SF Instance: " +vnfc_instance.getHostname()+" and it Load = "+ MaxLoad);

          }
          if(LoadSum>0){
            alpha_value=((double)(LoadSum-LoadCounter)/(2*LoadSum))+(SfcPriority/(2*(MaxSfcPriority+1)));

          }else {
            alpha_value=SfcPriority/(2*(MaxSfcPriority+1));
            System.out.println("**** LoadSUmmation =0 ");

          }
          alpha.put(vnfc_instance.getHostname(),alpha_value);
          System.out.println("**** Load of SF Instance: " +vnfc_instance.getHostname()+" = "+ LoadCounter+" and its alpha value = "+alpha_value);

      }
    }

    System.out.println("**** Max Distance = "+MaxDistance);

    VNFdict new_vnf = new VNFdict();


    int minLoad = Integer.MAX_VALUE;
    if (prev_vnf == null) {
      System.out.println("**** Previous VNF in the Chain is NULL *****");

      VNFdict firstHopVNF = new VNFdict();
      String VNF_instance_selected = null;
      if (mapCountLoad.size() != 0) {
        System.out.println("**** mapCountLoad is not EMPTY *****");
        if (mapCountLoad.containsKey(vnfr.getType())) {
          System.out.println("**** mapCountLoad for the VNF [" + vnfr.getType() + "] is not EMPTY *****");
          if (mapCountLoad.get(vnfr.getType()).size() != 0) {
            for (String sfInstance : mapCountLoad.get(vnfr.getType()).keySet()) {
              if (mapCountLoad.get(vnfr.getType()).get(sfInstance) < minLoad) {
                System.out.println("**** MinLOAD --> " +
                                   minLoad +
                                   " <-- is greater than the VNF instance Load [" +
                                   sfInstance +
                                   "], it load -->" +
                                   mapCountLoad.get(vnfr.getType()).get(sfInstance) +
                                   " <-- *****");

                VNF_instance_selected = sfInstance;
                System.out.println("**** VNF instance Selected is [" + VNF_instance_selected + "] ");

                minLoad = mapCountLoad.get(vnfr.getType()).get(sfInstance);
              }
            }
          }else {
            System.out.println("*ERROR*** mapCountLoad for the VNF instance of type [" + vnfr.getType() + "] is EMPTY *****");

          }
        } else {
          System.out.println("**ERROR** VNF type "+vnfr.getType()+" is not exist in the mapCountLoad ###");

        }
      } else {
        System.out.println("*ERROR*** mapCountLoad Size is ZERO ###");

      }

      if (VNF_instance_selected != null) {
        System.out.println("**** VNF instance is Selected *****");

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
        if (mapCountLoad.containsKey((vnfr.getType()))) {
          if (mapCountLoad.get(vnfr.getType()).size() != 0) {
            if(mapCountLoad.get(vnfr.getType()).containsKey(firstHopVNF.getName())) {
              countLoad = mapCountLoad.get(vnfr.getType()).get(firstHopVNF.getName());
              System.out.println(" First HOP --> Count Load OLD=" + countLoad);
            }
          }
          countLoad = countLoad + 1;
          System.out.println(" First HOP --> Count Load NEW=" + countLoad);
          mapCountLoad.get(vnfr.getType()).put(firstHopVNF.getName(), countLoad);
        } else {
          System.out.println(" Could not select VNF instance: VNF instance name is Null");
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
        for (Ip ip : vnfc.getIps()) {
          new_vnf.setIP(ip.getIp());
          System.out.println("[SF-Selections] Get Neutron Pro " + new Date().getTime());
          CurrentVNF_NeutronPortID = NC.getNeutronPortID(ip.getIp());
          break;
        }
        Pathlength = getDistance(prev_vnf.getName(), currentVNFName);
        System.out.println(" Path Length = " +
                           Pathlength +
                           " for SF Instance : " +
                           prev_vnf.getName() +
                           " and  SF instance: " +
                           currentVNFName);


        int Load = mapCountLoad.get(vnfr.getType()).get(currentVNFName);
        System.out.println(" SF Load = " + Load + " for SF Instance : " + currentVNFName);

        double alpha_value = alpha.get(currentVNFName);
        System.out.println(" SF alpha Value = " + alpha_value + " for SF Instance : " + currentVNFName);

        if (MaxDistance > 0 && MaxLoad >0) {
          System.out.println("==> Max Disance > 0 and Max Load > 0 " );
          Index = ((double)((1 - alpha_value) * Load) / MaxLoad) + ((double)(alpha_value * Pathlength) / MaxDistance);
          System.out.println(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
        } else if(MaxLoad > 0){
          System.out.println("==> Max Load > 0 " );
          Index = ((double)((1 - alpha_value) * Load) / MaxLoad);
          System.out.println(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
        }else if(MaxDistance > 0){
          System.out.println("==> Max Disance > 0  " );
          Index = ((double)(alpha_value * Pathlength) / MaxDistance);
          System.out.println(" Index Value = " + Index + " for SF Instance : " + currentVNFName);
        }else {
          System.out.println("==> Max Disance = 0 and Max Load = 0 " );

          Index = 1-alpha_value;
          System.out.println(" Index Value = " + Index + " for SF Instance : " + currentVNFName);

        }
        if (Index < minIndex) {
          minIndex = Index;
          new_vnf.setName(currentVNFName);
          new_vnf.setType(vnfr.getType());
          new_vnf.setNeutronPortId(CurrentVNF_NeutronPortID);
          System.out.println("** Index Value less than the Min Index ** ");
        }
      }
    }
    if (mapCountLoad.containsKey(vnfr.getType())) {
      if (mapCountLoad.get(vnfr.getType()).size() != 0) {

        countLoad = mapCountLoad.get(vnfr.getType()).get(new_vnf.getName());
        System.out.println(" --> Count Load OLD=" + countLoad);
      }
    }
    countLoad = countLoad + 1;
    System.out.println(" --> Count Load NEW=" + countLoad);

    mapCountLoad.get(vnfr.getType()).put(new_vnf.getName(), countLoad);
    System.out.println(" #####  SELECTED VNF instance= " + new_vnf.getName());

    return new_vnf;
  }


  //Calculate the distance between two VNFs instances
  public int getDistance(String prev_VNF,String currentVNF) throws IOException {
    int distance;
    System.out.println("**** Get Distance between ["+prev_VNF+ "] and ["+ currentVNF+"]");

    String currentVNFLocation=SFC_driver.GetConnectedSFF(currentVNF);
    System.out.println("**** Location for "+currentVNF+" = "+currentVNFLocation);

    String prevVNFLocation=SFC_driver.GetConnectedSFF(prev_VNF);
    System.out.println("**** Location for "+prev_VNF+" = "+prevVNFLocation);


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
                  new_vnf = SelectVNF(vnfr,prev_vnf,vnffgr.getVendor());
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
