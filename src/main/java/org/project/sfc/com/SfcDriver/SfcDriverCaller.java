package org.project.sfc.com.SfcDriver;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation.RoundRobinSelection;
import org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation.ShortestPathSelection;
import org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation.TradeOffShortestPathLoadBalancingSelection;
import org.project.sfc.com.SfcDriver.PathCreation.ReadjustmentAtRuntime.LoadBalancedPathSelection;
import org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation.RandomPathSelection;
import org.project.sfc.com.SfcDriver.PathCreation.ReadjustmentAtRuntime.ShortestPathSelectionAtRuntime;
import org.project.sfc.com.SfcDriver.PathCreation.ReadjustmentAtRuntime.TradeOffSpLbSelection;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.SfcImpl.Broker.SfcBroker;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFPdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.SfcRepository.SfcManagement.SfcManagement;
import org.project.sfc.com.SfcRepository.SfcManagement.SfcManagementInterface;
import org.project.sfc.com.SfcRepository.SfcManagement.SfccManagement;
import org.project.sfc.com.SfcRepository.SfcManagement.SfccManagementInterface;
import org.project.sfc.com.SfcRepository.SfcManagement.SfpManagement;
import org.project.sfc.com.SfcRepository.SfcManagement.SfpManagementInterface;
import org.project.sfc.com.SfcRepository.SfcManagement.VnfManagement;
import org.project.sfc.com.SfcRepository.SfcManagement.VnfManagementInterface;
import org.project.sfc.com.SfcRepository.VNFdictRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired   VnfManagementInterface vnfManag;
  @Autowired  SfcManagementInterface sfcManag;
  @Autowired  SfccManagementInterface classiferManag;
  @Autowired  SfpManagementInterface sfpManag;

  public SfcDriverCaller(String type) throws IOException {
    SFC_driver = broker.getSFC(type);
    SFC_Classifier_driver = broker.getSfcClassifier(type);
    NC = new NeutronClient();

    LBPath = new LoadBalancedPathSelection(type);
    SPpath = new ShortestPathSelectionAtRuntime(type);
    TOS = new TradeOffSpLbSelection(type);

    SDN_Controller_driver_type = type;
  }

  public void UpdateFailedPaths(VirtualNetworkFunctionRecord vnfr, String sfscheduler)
      throws IOException {

    log.info("[Failed SFC-Path-UPDATE] Start");

    HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    if (All_SFCs != null) {
     log.debug("[ ALL SFCs number ] =  " + All_SFCs.size());
    }
    List<HashMap<Integer, VNFdict>> list_vnf_dicts = new ArrayList<HashMap<Integer, VNFdict>>();

    Iterator it = All_SFCs.entrySet().iterator();
    boolean Found_flag = false;
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      log.debug(
          "[Adding SFs  for Chain : ]  "
              + All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo().getSfcDict().getName());

      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator count = VNFs.entrySet().iterator();
      while (count.hasNext()) {


        Map.Entry VNFcounter = (Map.Entry) count.next();


        if (getFaildVNFname(vnfr) != null) {
          List<VNFdict> vnfs_list = new ArrayList<>();

          log.debug(
              "[Found Failed SF] -->" + getFaildVNFname(vnfr) + " at time " + new Date().getTime());

          if (VNFs.get(VNFcounter.getKey()).getName().equals(getFaildVNFname(vnfr))) {
            Integer position = (Integer) VNFcounter.getKey();
            System.out.println("[position of Failed SF] -->" + position);
            Found_flag = true;
            if (sfcc_db.existVNF(VNFs.get(position))) {
              log.debug(
                  "[REMOVE FAILED VNF from the Database] -->" + VNFs.get(position).getName());

              sfcc_db.removeVNF(VNFs.get(position));
            }

            VNFs.remove(position);

            if (getActiveVNF(vnfr) != null) {
              log.debug("[NEW SF will be added  ] " + getActiveVNF(vnfr).getHostname());

              VNFdict newVnf = new VNFdict();
              newVnf.setName(getActiveVNF(vnfr).getHostname());
              newVnf.setType(vnfr.getType());
              for (Ip ip : getActiveVNF(vnfr).getIps()) {
                newVnf.setIP(ip.getIp());
                newVnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
                System.out.println(
                    "[Adjustted the IP and Neutron port ID of new SF ] "
                        + newVnf.getIP()
                        + " , "
                        + newVnf.getNeutronPortId());

                break;
              }
              VNFs.put(position, newVnf);
              log.debug("[ADDed the NEW SF ] " + getActiveVNF(vnfr).getHostname());
              if (vnfs_list != null) {
                sfcc_db.addVNFs(vnfs_list);
                log.debug(
                    "[ADDed the NEW SF to data base] " + getActiveVNF(vnfr).getHostname());
              }
              break;
            }

          } else {
            log.debug("[Not equal to the name of the failed VNF]");
          }
        }
      }
      log.debug(
          "[Finished ALLOCATION OF SFs]");
      SFC_driver.CreateSFs(VNFs);
      for (int a = 0; a < VNFs.size(); a++) {
        VNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(VNFs.get(a).getName()));
      }
      list_vnf_dicts.add(VNFs);
    }

    if (Found_flag == false) {
      log.debug("[The ODL SF was not used in any Chain path ]  ");
      VNFdict vnf_dict = new VNFdict();
      vnf_dict.setName(getFaildVNFname(vnfr));
      sfcc_db.removeVNF(vnf_dict);
      log.debug("[REMOVE the SF from the data base ]  ");
    }
    if (sfscheduler.equals("roundrobin")) {

      Iterator it_ = All_SFCs.entrySet().iterator();
      int counter = 0;
      if (All_SFCs != null) {
        log.debug("[Creating Paths - ALL SFCs number ] =  " + All_SFCs.size());
      }
      while (it_.hasNext()) {
        log.debug("[Counter] =  " + counter);

        Map.Entry SFCdata_counter_ = (Map.Entry) it_.next();
        log.debug(
            "[Create new Path ] for Chain  "
                + All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getName());
        log.debug("[VNF dicts list ]  " + list_vnf_dicts.get(counter));
        List<SFPdict> newPaths = new ArrayList<>();
        double PathTrafficLoad =
            All_SFCs.get(SFCdata_counter_.getKey())
                .getSFCdictInfo()
                .getSfcDict()
                .getPaths()
                .get(0)
                .getPathTrafficLoad();

        SFPdict newPath =
            All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0);
        newPath.setPath_SFs(list_vnf_dicts.get(counter));
        newPath.setQoS(
            All_SFCs.get(SFCdata_counter_.getKey())
                .getSFCdictInfo()
                .getSfcDict()
                .getPaths()
                .get(0)
                .getQoS());
        newPaths.add(0, newPath);
        log.debug("[OLD Traffic load ]  " + PathTrafficLoad);

        newPath.setOldTrafficLoad(PathTrafficLoad);
        All_SFCs.get(SFCdata_counter_.getKey()).setChainSFs(list_vnf_dicts.get(counter));

        All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().setPaths(newPaths);
        SFC_driver.DeleteSFP(
            All_SFCs.get(SFCdata_counter_.getKey()).getRspID(),
            All_SFCs.get(SFCdata_counter_.getKey()).isSymm());

        String new_instance_id =
            SFC_driver.CreateSFP(
                All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(),
                list_vnf_dicts.get(counter));
        log.debug("[NEW Path Updated ] " + new_instance_id);

        String SFCC_name =
            SFC_Classifier_driver.Create_SFC_Classifer(
                All_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo(), new_instance_id);

        log.debug("[NEW Classifier Updated ] " + SFCC_name);

        log.debug("[Key of the Test_SFC ] =  " + (String) SFCdata_counter_.getKey());
        log.debug("[new instance id ] =  " + new_instance_id);

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
    } else if (sfscheduler.equals("tradeoff")) {
      TOS.ReadjustVNFsAllocation(vnfr);
    } else if (sfscheduler.equals("shortestpath")) {
      SPpath.ReadjustVNFsAllocation(vnfr);
    } else {
      log.debug("ERROR : The SF Scheduler is not detected ");
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
        VNFdict vnf_dict = new VNFdict();
        vnf_dict.setName(vnf_instance.getHostname());
        if (vnf_instance.getState().equals("ACTIVE") && !sfcc_db.existVNF(vnf_dict)) {
          log.debug(" [NEW SF instance] Found  " + vnf_instance.getHostname());

          found = true;

          VNF_instance = vnf_instance;
        }
      }
    }

    if (found == false) {
      log.warn("[Not finding the new SF instance]  ");

      return null;
    } else {
      return VNF_instance;
    }
  }

  public VNFCInstance getVNFInstanceName(VirtualNetworkFunctionRecord vnfr, int order) {
    boolean found = false;
    int counter = 0;
    VNFCInstance VNF_instance = new VNFCInstance();
    log.info("[get VNF instance NAME] order: "+ order);

    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      if (order < vdu.getVnfc_instance().size()) {
        for (VNFCInstance vnf_instance : vdu.getVnfc_instance()) {
          log.info("[get VNF instance NAME] counter: "+ counter);

          if (counter == order) {
            log.info("[Found the SF instance]");

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
      log.warn("[Not finding the SF instance]  ");

      return null;
    } else {
      return VNF_instance;
    }
  }

  public void UpdateScaledPaths(VirtualNetworkFunctionRecord vnfr, String sfscheduler)
      throws IOException {

    log.debug("[Update SFC-Path Scaling]  " );




    if (!sfscheduler.equals("roundrobin")) {

      List<VNFdict> vnf_list = new ArrayList<>();
      HashMap<Integer, VNFdict> NewVNFs = new HashMap<>();
      int Counter = 0;

      for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
        for (VNFCInstance vnfc : vdu.getVnfc_instance()) {
          VNFdict VnfDict = new VNFdict();
          VnfDict.setName(vnfc.getHostname());
          VnfDict.setType(vnfr.getType());
          for (Ip ip : vnfc.getIps()) {
            VnfDict.setIP(ip.getIp());
            VnfDict.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
            log.debug("[Adjustted the IP and Neutron port ID of new scaled SF ] " +
                               VnfDict.getIP() +
                               " , " +
                               VnfDict.getNeutronPortId());

            break;
          }

          if (!sfcc_db.existVNF(VnfDict)) {
            vnf_list.add(VnfDict);

            vnf_list.add(VnfDict);
            NewVNFs.put(Counter, VnfDict);
            Counter++;
            log.debug("[Update Scaled VNF] the SFC DB has not this VNF instance = " + VnfDict.getName());
          }
        }
      }
      if (NewVNFs.size() > 0) {
        log.debug("[---------------- NEW VNFs Should be Created --------------]");

        SFC_driver.CreateSFs(NewVNFs);

        for (int a = 0; a < NewVNFs.size(); a++) {
          NewVNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(NewVNFs.get(a).getName()));
        }
      }
    }



    if (sfscheduler.equals("roundrobin")) {
      HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();

      HashMap<String, SFC.SFC_Data> Involved_SFCs=new HashMap<>();
      //Get Involved SFCs for this VNF
      Iterator itr = All_SFCs.entrySet().iterator();
      while (itr.hasNext()) {
        Map.Entry SFCdata_counter = (Map.Entry) itr.next();
        HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
        Iterator vnfs_count = VNFs.entrySet().iterator();
        while (vnfs_count.hasNext()) {
          Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
          if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())) {
            Involved_SFCs.put(
                All_SFCs.get(SFCdata_counter.getKey()).getRspID(),
                All_SFCs.get(SFCdata_counter.getKey()));
            log.debug(
                "[Shortest Path Selection] Involved SFCs :  "
                + All_SFCs.get(SFCdata_counter.getKey()).getRspID());
          }
        }
      }
      if (Involved_SFCs != null) {
        log.debug("[Involved SFCs number ] =  " + Involved_SFCs.size());
      }
      List<HashMap<Integer, VNFdict>> list_vnf_dicts = new ArrayList<HashMap<Integer, VNFdict>>();

      Iterator it = Involved_SFCs.entrySet().iterator();
      int scale_counter = 0;
      while (it.hasNext()) {
        Map.Entry SFCdata_counter = (Map.Entry) it.next();
        log.info(
            "[Adding SFs  for Chain : ]  "
            + Involved_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo().getSfcDict().getName());

        HashMap<Integer, VNFdict> VNFs = Involved_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
        Iterator count = VNFs.entrySet().iterator();

        if (getVNFInstanceName(vnfr, scale_counter) == null) {
          log.info("[ The number of SF instances is smaller than the involved  Chains ]");

          scale_counter = 0;
        }
        while (count.hasNext()) {
          log.debug("[NEW SF Prepared] ");
          List<VNFdict> vnfs_list=new ArrayList<>();

          Map.Entry VNFcounter = (Map.Entry) count.next();
          log.debug("[OK] ");

          log.info("[SF counter ] " + VNFcounter.getKey());
          log.info("[NEW SF will be added  ] " +  getVNFInstanceName(vnfr, scale_counter).getHostname());
          log.info("[type of VNF  ] " +  VNFs.get(VNFcounter.getKey()).getType() + " VNF type: "+vnfr.getType());
          log.info("[IP of the SF instance ] " + VNFs.get(VNFcounter.getKey()).getIP());
          log.info("[NAME of the SF instance ] " + VNFs.get(VNFcounter.getKey()).getName());

          if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())
              && getVNFInstanceName(vnfr, scale_counter) != null) {
            System.out.println("[NEW SF will be added  ] " +  getVNFInstanceName(vnfr, scale_counter).getHostname());

            if (!VNFs.get(VNFcounter.getKey())
                     .getName()
                     .equals(getVNFInstanceName(vnfr, scale_counter).getHostname())) {
              Integer position = (Integer) VNFcounter.getKey();
              log.info("[position of old SF instance] -->" + position);
              VNFs.remove(position);
              log.info("[REMOVED old SF instance ] ");
              String Scaled_SF_instance_name = getVNFInstanceName(vnfr, scale_counter).getHostname();

              log.info("[Scaled SF instance will be added  ] " + Scaled_SF_instance_name);

              VNFdict newVnf = new VNFdict();
              newVnf.setName(Scaled_SF_instance_name);
              newVnf.setType(vnfr.getType());
              for (Ip ip : getVNFInstanceName(vnfr, scale_counter).getIps()) {
                newVnf.setIP(ip.getIp());
                newVnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
                log.info(
                    "[Adjustted the IP and Neutron port ID of new scaled SF ] "
                    + newVnf.getIP()
                    + " , "
                    + newVnf.getNeutronPortId());

                break;
              }
              log.info("[Update Scaled VNF] Is the SFC DB has this VNF instance = "+ newVnf.getName()+"  ?  "+sfcc_db.existVNF(newVnf));

              if(!sfcc_db.existVNF(newVnf)){
                vnfs_list.add(newVnf);

                log.info("[Update Scaled VNF] the SFC DB has not this VNF instance = "+ newVnf.getName());

              }
              VNFs.put(position, newVnf);
              log.info("[ADDed the Scaled SF instance ] " + Scaled_SF_instance_name);
              if(vnfs_list!=null) {
                sfcc_db.addVNFs(vnfs_list);
              }
              break;
            }
          }

        }
        log.info(
            "[Finished ALLOCATION OF SFs]");
        SFC_driver.CreateSFs(VNFs);

        for(int a=0;a<VNFs.size();a++){
          VNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(VNFs.get(a).getName()));
        }

        list_vnf_dicts.add(VNFs);
        scale_counter++;
      }

      Iterator it_ = Involved_SFCs.entrySet().iterator();
      int counter = 0;
      if (Involved_SFCs != null) {
        log.info("[ SCALING Paths - ALL SFCs number ] =  " + Involved_SFCs.size());
      }
      while (it_.hasNext()) {
        Map.Entry SFCdata_counter_ = (Map.Entry) it_.next();
        log.info(
            "[SCALING new Path ] for Chain  "
                + Involved_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getName());
        log.info("[VNF dicts list ]  " + list_vnf_dicts.get(counter));
        List<SFPdict> newPaths = new ArrayList<>();
        double PathTrafficLoad =
            Involved_SFCs.get(SFCdata_counter_.getKey())
                .getSFCdictInfo()
                .getSfcDict()
                .getPaths()
                .get(0)
                .getPathTrafficLoad();
        SFPdict newPath =
            Involved_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0);
        newPath.setPath_SFs(list_vnf_dicts.get(counter));
        newPath.setOldTrafficLoad(PathTrafficLoad);
        newPath.setQoS(
            Involved_SFCs.get(SFCdata_counter_.getKey())
                .getSFCdictInfo()
                .getSfcDict()
                .getPaths()
                .get(0)
                .getQoS());
        newPaths.add(0, newPath);
        Involved_SFCs.get(SFCdata_counter_.getKey()).setChainSFs(list_vnf_dicts.get(counter));
        Involved_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().setPaths(newPaths);
        SFC_driver.DeleteSFP(
            Involved_SFCs.get(SFCdata_counter_.getKey()).getRspID(),
            Involved_SFCs.get(SFCdata_counter_.getKey()).isSymm());
        String new_instance_id =
            SFC_driver.CreateSFP(
                Involved_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(),
                list_vnf_dicts.get(counter));
        log.info("[SCALED NEW Path ] " + new_instance_id);
        String SFCC_name =
            SFC_Classifier_driver.Create_SFC_Classifer(
                Involved_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo(), new_instance_id);
        log.info("[NEW Classifier Updated ] " + SFCC_name);
        log.info("[Key of the SFC ] =  " + (String) SFCdata_counter_.getKey());
        log.info("[new instance id ] =  " + new_instance_id);
        log.info("[Update SFCC DB] " +((String) SFCdata_counter_.getKey()).substring(5));
        String IDx = ((String) SFCdata_counter_.getKey()).substring(5);

        String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);
        log.info("[VNFFGR ID] =" + VNFFGR_ID);
        sfcc_db.update(
            VNFFGR_ID,
            new_instance_id,
            Involved_SFCs.get(SFCdata_counter_.getKey()).getSfccName(),
            Involved_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getSymmetrical(),
            list_vnf_dicts.get(counter),
            Involved_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(),
            Involved_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo());
        log.info("[SFC DB updated ]  Counter= "+counter);

        counter++;
      }


    } else if (sfscheduler.equals("qos-aware-loadbalancer")) { //QoS aware Load Balancing Algorithm
      LBPath.ReadjustVNFsAllocation(vnfr);
    } else if (sfscheduler.equals("tradeoff")) {
      TOS.ReadjustVNFsAllocation(vnfr);
    } else if (sfscheduler.equals("shortestpath")) {
      SPpath.ReadjustVNFsAllocation(vnfr);
    } else {
      System.out.println("ERROR : The SF Scheduler is not detected ");
    }
  }

  public boolean Create(
      Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr, String SfSchedulingType)
      throws IOException {

    log.info("[SFC-Creation] start");

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
      HashMap<Integer, VNFdict> vnf_dicts = CreateSFs(vnf_members, nsr, vnffgr, SfSchedulingType);

      list_vnf_dicts.add(vnf_dicts);
    }
    int counter = 0;
    for (VNFForwardingGraphRecord vnffgr : nsr.getVnffgr()) {

      log.info(
          "[Size of VNF MEMBERS for creating CHAIN] "
              + list_vnf_dicts.size()
              + " for Test_SFC allocation to nsr handler at time "
              + new Date().getTime());
      log.info(
          "[ VNFFGR ID for creating CHAIN] "
              + vnffgr.getId()
              + " for Test_SFC allocation to nsr handler at time "
              + new Date().getTime());
      boolean Response = CreateChain(list_vnf_dicts.get(counter), vnffgr, nsr);
      if (Response == false) {
        log.error("[CHAIN IS NOT CREATED] ");

        return false;
      }
      counter++;
    }

    return true;
  }


  public HashMap<Integer, VNFdict> CreateSFs(
      Set<VirtualNetworkFunctionRecord> vnfrs,
      NetworkServiceRecord nsr,
      VNFForwardingGraphRecord vnffgr,
      String SfSchedulingType)
      throws IOException {

    // Create SFs and add them to the Data base
    HashMap<Integer, VNFdict> list_vnfs = new HashMap<>();
    List<VNFdict> vnfs = new ArrayList<>();
    int counter = 0;

    for (VirtualNetworkFunctionRecord vnfr : vnfrs) {
      for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
        for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
          VNFdict new_vnf = new VNFdict();
          new_vnf.setName(vnfc_instance.getHostname());
          new_vnf.setType(vnfr.getType());
          new_vnf.setId(vnfc_instance.getId());

          for (Ip ip : vnfc_instance.getIps()) {
            new_vnf.setIP(ip.getIp());
            log.debug("[SFP-Creation] Get Neutron Pro " + new Date().getTime());

            new_vnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

            break;
          }
          list_vnfs.put(counter, new_vnf);
          if (!sfcc_db.existVNF(new_vnf)) {
            vnfs.add(new_vnf);

            log.info(
                "[Create VNF] the SFC DB has not this VNF instance = " + new_vnf.getName());
          }
          counter++;
        }
      }
    }



    SFC_driver.CreateSFs(list_vnfs);
    if (vnfs != null) {
      sfcc_db.addVNFs(vnfs);

    }
    //adding to Repository
    for(int i=0;i<list_vnfs.size();i++){
      log.info(
          "[ADD VNF to REPO] " + list_vnfs.get(i).getId());
      //if(vnfManag.query(list_vnfs.get(i).getId())!=null){

        vnfManag.add(list_vnfs.get(i));
      log.info(
          "[Added this VNF] ");
    //  }

    }
    for (int a = 0; a < list_vnfs.size(); a++) {
      list_vnfs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(list_vnfs.get(a).getName()));
    }
    // Create Paths for the Chains

    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    log.info("[SFs-Creation] ");
    if (SfSchedulingType.equals("roundrobin")) {
      log.debug("[Path-Selection-Algorithm]  Round Robin");

      RoundRobinSelection RRS = new RoundRobinSelection();
      vnfdicts = RRS.CreatePath(vnfrs, vnffgr, nsr);

    } else if (SfSchedulingType.equals("shortestpath")) {
      log.debug("[Path-Selection-Algorithm] Shortest Path");

      ShortestPathSelection SPS = new ShortestPathSelection(SDN_Controller_driver_type);
      vnfdicts = SPS.CreatePath(vnfrs, vnffgr, nsr);

    } else if (SfSchedulingType.equals("tradeoff")) {
      log.debug("[Path-Selection-Algorithm]  Trade Off Shortest Path and Load Balancing");

      TradeOffShortestPathLoadBalancingSelection TOS =
          new TradeOffShortestPathLoadBalancingSelection(SDN_Controller_driver_type);
      vnfdicts = TOS.CreatePath(vnfrs, vnffgr, nsr);

    } else {
      log.debug("[Path-Selection-Algorithm]  Random");

      RandomPathSelection RPS = new RandomPathSelection();

      vnfdicts = RPS.CreatePath(vnfrs, vnffgr, nsr);
    }

    log.info("[SFs-Creation-Finsihed] ");

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

    log.info(
        "[SFC-Creation] Creating Path Finished  ");

    for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {

      for (int counter = 0; counter < nfp.getConnection().size(); counter++) {
        for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {
          Integer k = Integer.valueOf(entry.getKey());

          int x = k.intValue();

          if (counter == x) {


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
    //sfc_dict_test.setStatus("Active");
    sfc_dict_test.setTenantId(NC.getTenantID());
    List<SFPdict> Paths = new ArrayList<SFPdict>();
    SFPdict Path = new SFPdict();

    Path.setPath_SFs(vnfdicts);
    Path.setId("Path-" + nsr.getName() + "-" + vnffgr.getId());
    Path.setParentChainId(vnffgr.getId());
    if (vnffgr.getVendor().equals("gold")) {
      Path.setQoS(3);
    } else if (vnffgr.getVendor().equals("silver")) {
      Path.setQoS(2);
    } else {
      Path.setQoS(1);
    }

    Paths.add(0, Path);
    sfc_dict_test.setPaths(Paths);
    sfc_test.setSfcDict(sfc_dict_test);

    SFC_driver.CreateSFC(sfc_test, vnfdicts);
    String instance_id = SFC_driver.CreateSFP(sfc_test, vnfdicts);
    log.debug(
        " ADD VNFFGR ID as key to Test_SFC DB:  "
            + vnffgr.getId()
            + " at time "
            + new Date().getTime());
    log.debug(
        " ADD to it Instance  ID:  " + instance_id + " at time " + new Date().getTime());

    //sfcc_dict.setStatus("create");
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
    sfcManag.add(sfc_test.getSfcDict());
    sfpManag.add(Path);
    classiferManag.add(sfcc_dict);

    log.debug(
        " [GET  Instance  ID: ]  "
            + sfcc_db.getRspID(vnffgr.getId())
            + " at time "
            + new Date().getTime());
    log.debug(
        "[GET SFC NAME : ]  "
            + sfcc_db.getAllSFCs().get(vnffgr.getId()).getSFCdictInfo().getSfcDict().getName());

    if (SFCC_name != null && instance_id != null) {
      log.info("[ Chain Created successfully] , instance id= " + instance_id);

      return true;
    } else {
      log.error(" [Chain Not Created at time] " );

      return false;
    }
  }

  public boolean Delete(String vnffgID,String SfSchedulingType) throws IOException {
    if(SfSchedulingType.equals("roundrobin")){
      RoundRobinSelection RRS = new RoundRobinSelection();
      RRS.Delete(sfcc_db.getChain(vnffgID));
      log.info("Remove the mapCOUNT  RoundRobin " + vnffgID );


    }else if (SfSchedulingType.equals("tradeoff")){
      TradeOffShortestPathLoadBalancingSelection  TOSPLB = new TradeOffShortestPathLoadBalancingSelection(SDN_Controller_driver_type);
      TOSPLB.Delete(sfcc_db.getChain(vnffgID));
      log.info("Remove the mapCOUNT Tradeoff  " + vnffgID );

    }
    log.info("delete NSR ID:  " + vnffgID );
    String rsp_id = sfcc_db.getRspID(vnffgID);

    String sffc_name = sfcc_db.getSfccName(vnffgID);
    System.out.println("instance id to be deleted:  " + sffc_name);

    String IDx = rsp_id.substring(5);
    log.debug(" [ADD Instance] " + rsp_id + " - with IDx = " + IDx);
    String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);
    log.debug("[VNFFGR ID] =" + VNFFGR_ID);

    HashMap<Integer, VNFdict> Chain = sfcc_db.getChain(VNFFGR_ID);

    if (Chain == null) {
      log.error("[Could not find this chain in Data Base] ");

    } else {
      for (int x = 0; x < Chain.size(); x++) {

        log.debug("[Chain in DB] SF in Position " + x + "  is " + Chain.get(x).getName());
      }
    }
    sfcManag.delete(sfcManag.query(vnffgID));
    sfpManag.delete(sfpManag.query("Path-"+sfcManag.query(vnffgID).getName()));
    classiferManag.delete(classiferManag.query("sfcc-" + vnffgID));

    ResponseEntity<String> result = SFC_Classifier_driver.Delete_SFC_Classifier(sffc_name);
    log.info("Delete SFC Classifier :  " + result.getStatusCode().is2xxSuccessful());
    ResponseEntity<String> sfc_result = SFC_driver.DeleteSFC(rsp_id, sfcc_db.isSymmSFC(vnffgID));
    log.info("Delete SFC   :  " + sfc_result.getStatusCode().is2xxSuccessful());

    if (result != null && sfc_result != null) {

      if (result.getStatusCode().is2xxSuccessful()
          && sfc_result.getStatusCode().is2xxSuccessful()) {
        sfcc_db.remove(vnffgID);
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}
