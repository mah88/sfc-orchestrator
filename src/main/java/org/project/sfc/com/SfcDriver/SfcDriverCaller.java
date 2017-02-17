package org.project.sfc.com.SfcDriver;

import org.apache.http.HttpResponse;
import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.PathCreation.DeploymentPathCreation.RoundRobinSelection;
import org.project.sfc.com.PathCreation.DeploymentPathCreation.ShortestPathSelection;
import org.project.sfc.com.PathCreation.DeploymentPathCreation.TradeOffShortestPathLoadBalancingSelection;
import org.project.sfc.com.PathCreation.ReadjustmentAtRuntime.LoadBalancedPathSelection;
import org.project.sfc.com.PathCreation.DeploymentPathCreation.RandomPathSelection;
import org.project.sfc.com.PathCreation.ReadjustmentAtRuntime.ShortestPathSelectionAtRuntime;
import org.project.sfc.com.PathCreation.ReadjustmentAtRuntime.TradeOffSpLbSelection;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.SfcImpl.Broker.SfcBroker;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFPdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mah on 6/6/16.
 */
@Service
@Scope("prototype")
public class SfcDriverCaller {
  Logger log = LoggerFactory.getLogger(this.getClass());

  NeutronClient NC;
  SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
  SfcBroker broker = new SfcBroker();
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  org.project.sfc.com.SfcInterfaces.SFCclassifier SFC_Classifier_driver;
  LoadBalancedPathSelection LBPath;
  ShortestPathSelectionAtRuntime SPpath;
  TradeOffSpLbSelection TOS;
  String SDN_Controller_driver_type;

  public SfcDriverCaller(String type) throws IOException{
    SFC_driver = broker.getSFC(type);
    SFC_Classifier_driver = broker.getSfcClassifier(type);
    NC = new NeutronClient();

    LBPath = new LoadBalancedPathSelection(type);
    SPpath= new ShortestPathSelectionAtRuntime(type);
    TOS =new TradeOffSpLbSelection(type);

    SDN_Controller_driver_type=type;

  }

  public void UpdateFailedPaths(VirtualNetworkFunctionRecord vnfr,String sfscheduler) throws IOException {

    System.out.println("[Test_SFC-Path-UPDATE] (1) at time " + new Date().getTime());

    HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    if (All_SFCs != null) {
      System.out.println("[ ALL SFCs number ] =  " + All_SFCs.size());
    }
    List<HashMap<Integer, VNFdict>> list_vnf_dicts = new ArrayList<HashMap<Integer, VNFdict>>();

    Iterator it = All_SFCs.entrySet().iterator();
    boolean Found_flag=false;
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      System.out.println(
          "[Adding SFs  for Chain : ]  "
              + All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo().getSfcDict().getName());

      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator count = VNFs.entrySet().iterator();
      while (count.hasNext()) {
        System.out.println("[NEW SF Prepared] ");

        Map.Entry VNFcounter = (Map.Entry) count.next();
        System.out.println("[OK] ");

        System.out.println("[SF counter ] " + VNFcounter.getKey());

        if (getFaildVNFname(vnfr) != null) {
          List<VNFdict> vnfs_list=new ArrayList<>();

          System.out.println(
              "[Found Failed SF] -->" + getFaildVNFname(vnfr) + " at time " + new Date().getTime());

          if (VNFs.get(VNFcounter.getKey()).getName().equals(getFaildVNFname(vnfr))) {
            Integer position = (Integer) VNFcounter.getKey();
            System.out.println("[position of Failed SF] -->" + position);
            Found_flag =true;
            if(sfcc_db.existVNF(VNFs.get(position))) {
              System.out.println("[REMOVE FAILED VNF from the Database] -->" + VNFs.get(position).getName());

              sfcc_db.removeVNF(VNFs.get(position));
            }

            VNFs.remove(position);
            System.out.println("[REMOVED failed SF ] ");

            if (getActiveVNF(vnfr) != null) {
                System.out.println("[NEW SF will be added  ] " + getActiveVNF(vnfr).getHostname());

                VNFdict newVnf = new VNFdict();
                newVnf.setName(getActiveVNF(vnfr).getHostname());
                newVnf.setType(vnfr.getType());
                for (Ip ip : getActiveVNF(vnfr).getIps()) {
                  newVnf.setIP(ip.getIp());
                  newVnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
                  System.out.println("[Adjustted the IP and Neutron port ID of new SF ] " +
                                     newVnf.getIP() +
                                     " , " +
                                     newVnf.getNeutronPortId());

                  break;
                }
                VNFs.put(position, newVnf);
                System.out.println("[ADDed the NEW SF ] " + getActiveVNF(vnfr).getHostname());
                if (vnfs_list != null) {
                  sfcc_db.addVNFs(vnfs_list);
                  System.out.println("[ADDed the NEW SF to data base] " + getActiveVNF(vnfr).getHostname());

                }
                break;

            }

          } else {
            System.out.println("[Not equal to the name of the failed VNF]");
          }
        }
      }
      System.out.println(
          "[---------------------------Finished ALLOCATION OF SFs----------------------------------]");
      SFC_driver.CreateSFs(VNFs);
      for(int a=0;a<VNFs.size();a++){
        VNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(VNFs.get(a).getName()));
      }
      list_vnf_dicts.add(VNFs);
    }

    if(Found_flag==false){
      System.out.println("[The ODL SF was not used in any Chain path ]  ");
      VNFdict vnf_dict=new VNFdict();
      vnf_dict.setName(getFaildVNFname(vnfr));
      sfcc_db.removeVNF(vnf_dict);
      System.out.println("[REMOVE the SF from the data base ]  ");

    }
    if(sfscheduler.equals("roundrobin")) {

      Iterator it_ = All_SFCs.entrySet().iterator();
      int counter = 0;
      if (All_SFCs != null) {
        System.out.println("[Creating Paths - ALL SFCs number ] =  " + All_SFCs.size());
      }
      while (it_.hasNext()) {
        System.out.println("[Counter] =  " + counter);

        Map.Entry SFCdata_counter_ = (Map.Entry) it_.next();
        System.out.println("[Create new Path ] for Chain  " +
                           All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getName());
        System.out.println("[VNF dicts list ]  " + list_vnf_dicts.get(counter));
        List<SFPdict> newPaths = new ArrayList<>();
        double
            PathTrafficLoad =
            All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getPathTrafficLoad();

        SFPdict newPath = All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0);
        newPath.setPath_SFs(list_vnf_dicts.get(counter));
        newPath.setQoS(All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getQoS());
        newPaths.add(0, newPath);
        System.out.println("[OLD Traffic load ]  " + PathTrafficLoad);

        newPath.setOldTrafficLoad(PathTrafficLoad);
        All_SFCs.get(SFCdata_counter_.getKey()).setChainSFs(list_vnf_dicts.get(counter));

        All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().setPaths(newPaths);

        String
            new_instance_id =
            SFC_driver.CreateSFP(All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(), list_vnf_dicts.get(counter));
        System.out.println("[NEW Path Updated ] " + new_instance_id);

        String
            SFCC_name =
            SFC_Classifier_driver.Create_SFC_Classifer                (All_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo(),
                                                       new_instance_id);

        System.out.println("[NEW Classifier Updated ] " + SFCC_name);

        System.out.println("[Key of the Test_SFC ] =  " + (String) SFCdata_counter_.getKey());
        System.out.println("[new instance id ] =  " + new_instance_id);

        sfcc_db.update                                                            ((String) SFCdata_counter_.getKey(),
                       new_instance_id,
                       All_SFCs.get(SFCdata_counter_.getKey()).getSfccName(),
                       All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getSymmetrical(),
                       list_vnf_dicts.get(counter),
                       All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(),
                       All_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo());

        counter++;
      }
    } else if (sfscheduler.equals("tradeoff")){
    TOS.ReadjustVNFsAllocation(vnfr);
  } else if (sfscheduler.equals("shortestpath")){
    SPpath.ReadjustVNFsAllocation(vnfr);
  } else{
    System.out.println("ERROR : The SF Scheduler is not detected ");

  }
  }

  public String getFaildVNFname(VirtualNetworkFunctionRecord vnfr) {
    boolean found = false;
    String VNF_name = "";
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance vnf_instance : vdu.getVnfc_instance()) {

        if (vnf_instance.getState().equals("FAILED")) {

          found = true;

          VNF_name = vnf_instance.getHostname();
        }
      }
    }

    if (found == false) {
      return null;
    } else {
      return VNF_name;
    }
  }

  public VNFCInstance getActiveVNF(VirtualNetworkFunctionRecord vnfr) {
    boolean found = false;
    VNFCInstance VNF_instance = new VNFCInstance();
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance vnf_instance : vdu.getVnfc_instance()) {
        VNFdict vnf_dict=new VNFdict();
        vnf_dict.setName(vnf_instance.getHostname());
        if (vnf_instance.getState().equals("ACTIVE") && !sfcc_db.existVNF(vnf_dict)) {
          System.out.println("::: Found ::: [NEW SF instance]  "+ vnf_instance.getHostname());

          found = true;

          VNF_instance=vnf_instance;
        }
      }
    }

    if (found == false) {
      System.out.println("::: WARNING ::: [Not finding the new SF instance]  ");

      return null;
    } else {
      return VNF_instance;
    }
  }

  public VNFCInstance getVNFInstanceName(VirtualNetworkFunctionRecord vnfr, int order) {
    boolean found = false;
    int counter = 0;
    VNFCInstance VNF_instance = new VNFCInstance();

    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      if (order < vdu.getVnfc_instance().size()) {
        for (VNFCInstance vnf_instance : vdu.getVnfc_instance()) {

          if (counter == order) {

            found = true;
            VNF_instance = vnf_instance;
          }
          counter++;
        }
      } else {
        break;
      }
    }

    if (found == false) {
      System.out.println("::: WARNING ::: [Not finding the SF instance]  ");

      return null;
    } else {
      return VNF_instance;
    }
  }

  public void UpdateScaledPaths(VirtualNetworkFunctionRecord vnfr,String sfscheduler) throws IOException {

    System.out.println("[Test_SFC-Path-Scaling] (1) at time " + new Date().getTime());

    HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    if (All_SFCs != null) {
      System.out.println("[ ALL SFCs number ] =  " + All_SFCs.size());
    }
    List<HashMap<Integer, VNFdict>> list_vnf_dicts = new ArrayList<HashMap<Integer, VNFdict>>();

    Iterator it = All_SFCs.entrySet().iterator();
    int scale_counter = 0;
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      System.out.println(
          "[Adding SFs  for Chain : ]  "
              + All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo().getSfcDict().getName());

      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator count = VNFs.entrySet().iterator();

      if (getVNFInstanceName(vnfr, scale_counter) == null) {
        System.out.println("[ The number of SF instances is smaller than the involved  Chains ]");

        scale_counter = 0;
      }
      while (count.hasNext()) {
        System.out.println("[NEW SF Prepared] ");
        List<VNFdict> vnfs_list=new ArrayList<>();

        Map.Entry VNFcounter = (Map.Entry) count.next();
        System.out.println("[OK] ");

        System.out.println("[SF counter ] " + VNFcounter.getKey());

        if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())
            && getVNFInstanceName(vnfr, scale_counter) != null) {

          if (!VNFs.get(VNFcounter.getKey())
              .getName()
              .equals(getVNFInstanceName(vnfr, scale_counter).getHostname())) {
            Integer position = (Integer) VNFcounter.getKey();
            System.out.println("[position of old SF instance] -->" + position);
            VNFs.remove(position);
            System.out.println("[REMOVED old SF instance ] ");
            String Scaled_SF_instance_name = getVNFInstanceName(vnfr, scale_counter).getHostname();

            System.out.println("[Scaled SF instance will be added  ] " + Scaled_SF_instance_name);

            VNFdict newVnf = new VNFdict();
            newVnf.setName(Scaled_SF_instance_name);
            newVnf.setType(vnfr.getType());
            for (Ip ip : getVNFInstanceName(vnfr, scale_counter).getIps()) {
              newVnf.setIP(ip.getIp());
              newVnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
              System.out.println(
                  "[Adjustted the IP and Neutron port ID of new scaled SF ] "
                      + newVnf.getIP()
                      + " , "
                      + newVnf.getNeutronPortId());

              break;
            }
            System.out.println("[Update Scaled VNF] Is the SFC DB has this VNF instance = "+ newVnf.getName()+"  ?  "+sfcc_db.existVNF(newVnf));

            if(!sfcc_db.existVNF(newVnf)){
              vnfs_list.add(newVnf);

              System.out.println("[Update Scaled VNF] the SFC DB has not this VNF instance = "+ newVnf.getName());

            }
            VNFs.put(position, newVnf);
            System.out.println("[ADDed the Scaled SF instance ] " + Scaled_SF_instance_name);
            if(vnfs_list!=null) {
              sfcc_db.addVNFs(vnfs_list);
            }
            break;
          }
        }

      }
      System.out.println(
          "[---------------------------Finished ALLOCATION OF SFs----------------------------------]");
      SFC_driver.CreateSFs(VNFs);

      for(int a=0;a<VNFs.size();a++){
        VNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(VNFs.get(a).getName()));
      }

      list_vnf_dicts.add(VNFs);
      scale_counter++;
    }


    if(sfscheduler.equals("roundrobin")){
      Iterator it_ = All_SFCs.entrySet().iterator();
      int counter = 0;
      if (All_SFCs != null) {
        System.out.println("[ SCALING Paths - ALL SFCs number ] =  " + All_SFCs.size());
      }
      while (it_.hasNext()) {
        System.out.println("[Counter] =  " + counter);
        Map.Entry SFCdata_counter_ = (Map.Entry) it_.next();
        System.out.println(
            "[SCALING new Path ] for Chain  "
            + All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getName());
        System.out.println("[VNF dicts list ]  " + list_vnf_dicts.get(counter));
        List<SFPdict> newPaths=new ArrayList<>();
        double PathTrafficLoad=All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getPathTrafficLoad();
        SFPdict newPath= All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0);
        newPath.setPath_SFs(list_vnf_dicts.get(counter));
        newPath.setOldTrafficLoad(PathTrafficLoad);
        newPath.setQoS(All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getQoS());
        newPaths.add(0,newPath);
        All_SFCs.get(SFCdata_counter_.getKey()).setChainSFs(list_vnf_dicts.get(counter));
        All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().setPaths(newPaths);
        String new_instance_id =
            SFC_driver.CreateSFP(
                All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(),
                list_vnf_dicts.get(counter));
        System.out.println("[SCALED NEW Path ] " + new_instance_id);
        String SFCC_name =
            SFC_Classifier_driver.Create_SFC_Classifer(
                All_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo(), new_instance_id);
        System.out.println("[NEW Classifier Updated ] " + SFCC_name);
        System.out.println("[Key of the SFC ] =  " + (String) SFCdata_counter_.getKey());
        System.out.println("[new instance id ] =  " + new_instance_id);
        sfcc_db.update(
            (String) SFCdata_counter_.getKey(),
            new_instance_id,
            All_SFCs.get(SFCdata_counter_.getKey()).getSfccName(),
            All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getSymmetrical(),
            list_vnf_dicts.get(counter),
            All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(),
            All_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo());
        counter++;
      }
    }else if (sfscheduler.equals("qos-aware-loadbalancer")) {  //QoS aware Load Balancing Algorithm
      LBPath.ReadjustVNFsAllocation(vnfr);
    } else if (sfscheduler.equals("tradeoff")){
      TOS.ReadjustVNFsAllocation(vnfr);
    } else if (sfscheduler.equals("shortestpath")){
      SPpath.ReadjustVNFsAllocation(vnfr);
    } else{
      System.out.println("ERROR : The SF Scheduler is not detected ");

    }
  }

  public boolean Create(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr, String SfSchedulingType) throws IOException {

    System.out.println("[Test_SFC-Creation] (1) at time " + new Date().getTime());

    List<HashMap<Integer, VNFdict>> list_vnf_dicts = new ArrayList<HashMap<Integer, VNFdict>>();
    for (VNFForwardingGraphRecord vnffgr : nsr.getVnffgr()) {
      Set<VirtualNetworkFunctionRecord> vnf_members = new HashSet<VirtualNetworkFunctionRecord>();
      for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {
        for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {

          for (VirtualNetworkFunctionRecord vnfr : vnfrs) {

            if (vnfr.getName().equals(entry.getValue())) {
              vnf_members.add(vnfr);
            }
          }
        }
      }
      HashMap<Integer, VNFdict> vnf_dicts = CreateSFs(vnf_members, nsr, vnffgr,SfSchedulingType);

      list_vnf_dicts.add(vnf_dicts);
    }
    int counter = 0;
    for (VNFForwardingGraphRecord vnffgr : nsr.getVnffgr()) {

      System.out.println(
          "[Size of VNF MEMBERS for creating CHAIN] "
              + list_vnf_dicts.size()
              + " for Test_SFC allocation to nsr handler at time "
              + new Date().getTime());
      System.out.println(
          "[ VNFFGR ID for creating CHAIN] "
              + vnffgr.getId()
              + " for Test_SFC allocation to nsr handler at time "
              + new Date().getTime());
      boolean Response = CreateChain(list_vnf_dicts.get(counter), vnffgr, nsr);
      if (Response == false) {
        System.out.println("[CHAIN IS NOT CREATED] ");

        return false;
      }
      counter++;
    }

    return true;
  }

  public HashMap<Integer, VNFdict> CreateSFs(
      Set<VirtualNetworkFunctionRecord> vnfrs,
      NetworkServiceRecord nsr,
      VNFForwardingGraphRecord vnffgr, String SfSchedulingType) throws IOException {

    // Create SFs and add them to the Data base
    HashMap<Integer, VNFdict> list_vnfs=new HashMap<>();
    List<VNFdict> vnfs=new ArrayList<>();
    int counter=0;

    for(VirtualNetworkFunctionRecord vnfr:vnfrs){
      for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
        for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
          VNFdict new_vnf = new VNFdict();
          new_vnf.setName(vnfc_instance.getHostname());
          new_vnf.setType(vnfr.getType());
          for (Ip ip : vnfc_instance.getIps()) {
            new_vnf.setIP(ip.getIp());
            System.out.println("[SFP-Creation] Get Neutron Pro " + new Date().getTime());

            new_vnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

            break;
          }
          list_vnfs.put(counter,new_vnf);
          if(!sfcc_db.existVNF(new_vnf)){
            vnfs.add(new_vnf);

            System.out.println("[Create VNF] the SFC DB has not this VNF instance = "+ new_vnf.getName());

          }
          counter++;

        }

      }

    }

    SFC_driver.CreateSFs(list_vnfs);
    if(vnfs!=null) {
      sfcc_db.addVNFs(vnfs);
    }
    for(int a=0;a<list_vnfs.size();a++){
      list_vnfs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(list_vnfs.get(a).getName()));
    }



    // Create Paths for the Chains

    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    System.out.println("[SFs-Creation] at time " + new Date().getTime());
    if(SfSchedulingType.equals("roundrobin")){
      System.out.println("[Path-Selection-Algorithm]  Round Robin" );

      RoundRobinSelection RRS=new RoundRobinSelection();
      vnfdicts = RRS.CreatePath(vnfrs,vnffgr,nsr);

    }else if(SfSchedulingType.equals("shortestpath")) {
      System.out.println("[Path-Selection-Algorithm] Shortest Path" );

      ShortestPathSelection SPS = new ShortestPathSelection(SDN_Controller_driver_type);
      vnfdicts = SPS.CreatePath(vnfrs,vnffgr,nsr);


    }else if(SfSchedulingType.equals("tradeoff")) {
      System.out.println("[Path-Selection-Algorithm]  Trade Off Shortest Path and Load Balancing" );

      TradeOffShortestPathLoadBalancingSelection TOS= new TradeOffShortestPathLoadBalancingSelection(SDN_Controller_driver_type);
      vnfdicts = TOS.CreatePath(vnfrs,vnffgr,nsr);


    }else{
      System.out.println("[Path-Selection-Algorithm]  Random" );

          RandomPathSelection RPS = new RandomPathSelection();

          vnfdicts = RPS.CreatePath(vnfrs, vnffgr, nsr);
        }

    System.out.println("[SFs-Creation-Finsihed] " + new Date().getTime());

    return vnfdicts;

  }

  public boolean CreateChain(
      HashMap<Integer, VNFdict> vnfdicts,
      VNFForwardingGraphRecord vnffgr,
      NetworkServiceRecord nsr) {

    List<String> chain = new ArrayList<String>();
    SFCdict sfc_test = new SFCdict();
    SfcDict sfc_dict_test = new SfcDict();
    SFCCdict sfcc_dict = new SFCCdict();

    System.out.println("[Test_SFC-Creation] Creating Path Finished  at time " + new Date().getTime());

    for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {

      for (int counter = 0; counter < nfp.getConnection().size(); counter++) {
        for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {
          Integer k = Integer.valueOf(entry.getKey());

          int x = k.intValue();

          if (counter == x) {

            System.out.println("[Test_SFC-Creation] integer vlaue " + counter);

            chain.add(entry.getValue());
          }
        }
      }
    }

    sfc_dict_test.setSymmetrical(vnffgr.isSymmetrical());

    sfc_dict_test.setName(nsr.getName() + "-" + vnffgr.getId());
    sfc_dict_test.setId(vnffgr.getId());

    sfc_dict_test.setChain(chain);
    sfc_dict_test.setInfraDriver("ODL");

    sfc_dict_test.setStatus("Active");
    sfc_dict_test.setTenantId(NC.getTenantID());
    List<SFPdict> Paths=new ArrayList<SFPdict>();
    SFPdict Path=new SFPdict();

    Path.setPath_SFs(vnfdicts);
    Path.setId("Path-"+nsr.getName()+"-"+vnffgr.getId());
    Path.setParentChainId(vnffgr.getId());
    if(vnffgr.getVendor().equals("gold")){
    Path.setQoS(3);
    } else if(vnffgr.getVendor().equals("silver")){
      Path.setQoS(2);
    }else {
      Path.setQoS(1);
    }

    Paths.add(0, Path);
    sfc_dict_test.setPaths(Paths);
    sfc_test.setSfcDict(sfc_dict_test);

    SFC_driver.CreateSFC(sfc_test, vnfdicts);
    String instance_id = SFC_driver.CreateSFP(sfc_test, vnfdicts);
    System.out.println(
        " ADD VNFFGR ID as key to Test_SFC DB:  " + vnffgr.getId() + " at time " + new Date().getTime());
    System.out.println(
        " ADD to it Instance  ID:  " + instance_id + " at time " + new Date().getTime());

    sfcc_dict.setStatus("create");
    sfcc_dict.setTenantId(NC.getTenantID());
    sfcc_dict.setInfraDriver("netvirtsfc");
    sfcc_dict.setId("sfcc-" + vnffgr.getId());
    sfcc_dict.setChain(sfc_dict_test.getId());
    AclMatchCriteria acl = new AclMatchCriteria();

    sfcc_dict.setName("sfc-classifier-" + nsr.getName() + "-" + vnffgr.getId());
    for (NetworkForwardingPath nsp : vnffgr.getNetwork_forwarding_path()) {

      acl.setDestPort(nsp.getPolicy().getMatchingCriteria().getDestinationPort());
      acl.setSrcPort(nsp.getPolicy().getMatchingCriteria().getSourcePort());
      acl.setProtocol(nsp.getPolicy().getMatchingCriteria().getProtocol());
      acl.setDestIpv4(nsp.getPolicy().getMatchingCriteria().getDestinationIP());
      acl.setSourceIpv4(nsp.getPolicy().getMatchingCriteria().getSourceIP());
    }

    List<AclMatchCriteria> list_acl = new ArrayList<AclMatchCriteria>();
    list_acl.add(acl);
    sfcc_dict.setAclMatchCriteria(list_acl);
    String SFCC_name = SFC_Classifier_driver.Create_SFC_Classifer(sfcc_dict, instance_id);

    sfcc_db.add(
        vnffgr.getId(),
        instance_id,
        sfcc_dict.getName(),
        sfc_dict_test.getSymmetrical(),
        vnfdicts,
        sfc_test,
        sfcc_dict);

    System.out.println(
        " [GET  Instance  ID: ]  "
            + sfcc_db.getRspID(vnffgr.getId())
            + " at time "
            + new Date().getTime());
    System.out.println(
        "[GET Test_SFC NAME : ]  "
            + sfcc_db.getAllSFCs().get(vnffgr.getId()).getSFCdictInfo().getSfcDict().getName());

    if (SFCC_name != null && instance_id != null) {
      System.out.println(" Chain Created successfully, instance id= " + instance_id);

      return true;
    } else {
      System.out.println(" Chain Not Created at time " + new Date().getTime());

      return false;
    }
  }

  public boolean Delete(String nsrID) {
    System.out.println("delete NSR ID:  " + nsrID + " at time " + new Date().getTime());
    String rsp_id = sfcc_db.getRspID(nsrID);

    String sffc_name = sfcc_db.getSfccName(nsrID);
    System.out.println("instance id to be deleted:  " + sffc_name);

    ResponseEntity<String> result = SFC_Classifier_driver.Delete_SFC_Classifier(sffc_name);
    System.out.println("Delete Test_SFC Classifier :  " + result.getStatusCode().is2xxSuccessful());
    ResponseEntity<String> sfc_result = SFC_driver.DeleteSFC(rsp_id, sfcc_db.isSymmSFC(nsrID));
    System.out.println("Delete Test_SFC   :  " + sfc_result.getStatusCode().is2xxSuccessful());

    if (result != null && sfc_result != null) {

      if (result.getStatusCode().is2xxSuccessful() && sfc_result.getStatusCode().is2xxSuccessful()) {
        sfcc_db.remove(nsrID);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}
