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
import org.project.sfc.com.SfcImpl.Broker.SfcBroker;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFPdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.project.sfc.com.SfcModel.SFCdict.Status;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.SfcRepository.SFCCdictRepo;
import org.project.sfc.com.SfcRepository.SFCdictRepo;
import org.project.sfc.com.SfcRepository.SFCdictRepoCustom;
import org.project.sfc.com.SfcRepository.SFCCdictRepoCustom;
import org.project.sfc.com.SfcRepository.SFPdictRepo;
import org.project.sfc.com.SfcRepository.SFPdictRepoCustom;
import org.project.sfc.com.SfcRepository.VNFdictRepo;
import org.project.sfc.com.SfcRepository.VNFdictRepoCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import javax.annotation.PostConstruct;

/**
 * Created by mah on 6/6/16.
 */
@Service
@Scope("prototype")
@ConfigurationProperties
public class SfcDriverCaller {
  Logger log = LoggerFactory.getLogger(this.getClass());

  NeutronClient NC;
  //SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
  SfcBroker broker = new SfcBroker();
  org.project.sfc.com.SfcInterfaces.SFC SFC_driver;
  org.project.sfc.com.SfcInterfaces.SFCclassifier SFC_Classifier_driver;
  @Autowired private LoadBalancedPathSelection LBPath;
  @Autowired private ShortestPathSelectionAtRuntime SPpath;
  @Autowired private TradeOffSpLbSelection TOS;
  String SDN_Controller_driver_type;
  @Autowired private VNFdictRepo vnfManag;
  @Autowired private SFCdictRepo sfcManag;
  @Autowired private SFCCdictRepo classiferManag;
  @Autowired private SFPdictRepo sfpManag;

  @Value("${sfc.driver:ODL}")
  private String sfcDriver;

  @PostConstruct
  public void init() throws IOException {
    log.debug("Entering init method of SdcDriverCaller");
    SFC_driver = broker.getSFC(sfcDriver);
    SFC_Classifier_driver = broker.getSfcClassifier(sfcDriver);
    NC = new NeutronClient();

    //  LBPath = new LoadBalancedPathSelection(sfcDriver);
    //SPpath = new ShortestPathSelectionAtRuntime();
    // TOS = new TradeOffSpLbSelection(sfcDriver);

    SDN_Controller_driver_type = sfcDriver;
    log.debug("Finished init method of SdcDriverCaller");
  }

  public SfcDriverCaller() {};
  /*
    public SfcDriverCaller(String type) throws IOException {
      log.info("Creating of SdcDriverCaller");
      SFC_driver = broker.getSFC(type);
      SFC_Classifier_driver = broker.getSfcClassifier(type);
      NC = new NeutronClient();

      LBPath = new LoadBalancedPathSelection(type);
      SPpath = new ShortestPathSelectionAtRuntime(type);
      TOS = new TradeOffSpLbSelection(type);

      SDN_Controller_driver_type = type;
    }
  */
  public void UpdateFailedPaths(VirtualNetworkFunctionRecord vnfr, String sfscheduler)
      throws IOException {

    log.info("[Failed SFC-Path-UPDATE] Start");

    // HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    Iterable<SfcDict> All_SFCs = sfcManag.query();

    if (sfcManag.query() != null) {
      log.debug("[ ALL SFCs number ] =  " + sfcManag.count());
    }
    List<Map<Integer, VNFdict>> list_vnf_dicts = new ArrayList<Map<Integer, VNFdict>>();

    VNFCInstance Active_VNFCI = getActiveVNF(vnfr);

    // Iterator it = All_SFCs.iterator();
    boolean Found_flag = false;
    // while (it.hasNext()) {
    for (SfcDict sfcData : All_SFCs) {
      //Map.Entry SFCdata_counter = (Map.Entry) it.next();
      // SfcDict sfcData = (SfcDict) SFCdata_counter.getValue();
      log.debug("[Adding SFs  for Chain : ]  " + sfcData.getName());

      Map<Integer, VNFdict> VNFs = sfcData.getPaths().get(0).getPath_SFs();
      Iterator count = VNFs.entrySet().iterator();
      while (count.hasNext()) {

        Map.Entry VNFcounter = (Map.Entry) count.next();

        if (getFaildVNFname(vnfr) != null) {
          // List<VNFdict> vnfs_list = new ArrayList<>();

          log.debug(
              "[Found Failed SF] -->" + getFaildVNFname(vnfr) + " at time " + new Date().getTime());

          if (VNFs.get(VNFcounter.getKey()).getName().equals(getFaildVNFname(vnfr))) {
            Integer position = (Integer) VNFcounter.getKey();
            System.out.println("[position of Failed SF] -->" + position);
            Found_flag = true;
            if (vnfManag.exists(VNFs.get(position).getId())) {
              log.debug("[REMOVE FAILED VNF from the Database] -->" + VNFs.get(position).getName());
              VNFs.get(position).setStatus(Status.INACTIVE);
              vnfManag.update(VNFs.get(position));
            }

            VNFs.remove(position);

            if (Active_VNFCI != null) {
              log.debug("[NEW SF will be added  ] " + Active_VNFCI.getHostname());

              VNFdict newVnf = new VNFdict();
              newVnf.setName(Active_VNFCI.getHostname());
              newVnf.setType(vnfr.getType());
              newVnf.setId(Active_VNFCI.getId());
              for (Ip ip : Active_VNFCI.getIps()) {
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
              log.debug("[ADDed the NEW SF ] " + Active_VNFCI.getHostname());
              if (newVnf != null) {
                //sfcc_db.addVNFs(vnfs_list);
                vnfManag.add(newVnf);
                log.debug("[ADDed the NEW SF to data base] " + Active_VNFCI.getHostname());
              }
              break;
            }

          } else {
            log.debug("[Not equal to the name of the failed VNF]");
          }
        }
      }
      log.debug("[Finished ALLOCATION OF SFs]");
      SFC_driver.CreateSFs(VNFs);
      for (int a = 0; a < VNFs.size(); a++) {
        VNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(VNFs.get(a).getName()));
        vnfManag.update(VNFs.get(a));
        log.debug("[Update OF SFs with connected SFF]");
      }
      list_vnf_dicts.add(VNFs);
    }

    if (Found_flag == false) {
      log.debug("[The ODL SF was not used in any Chain path ]  ");
      VNFdict vnf_dict = vnfManag.findbyName(getFaildVNFname(vnfr));
      if (vnfManag.exists(vnf_dict.getId())) {
        log.debug("[REMOVE FAILED VNF from the Database] -->" + getFaildVNFname(vnfr));
        vnf_dict.setStatus(Status.INACTIVE);

        vnfManag.update(vnf_dict);
      }
      log.debug("[REMOVE the SF from the data base ]  ");
    }
    if (sfscheduler.equals("roundrobin")) {

      //  Iterator it_ = All_SFCs.iterator();
      int counter = 0;
      if (sfcManag.query() != null) {
        log.debug("[Creating Paths - ALL SFCs number ] =  " + sfcManag.count());
      }

      for (SfcDict sfcData : All_SFCs) {

        log.debug("[Counter] =  " + counter);

        //    Map.Entry SFCdata_counter_ = (Map.Entry) it_.next();
        //    SfcDict sfcData = (SfcDict) SFCdata_counter_.getValue();

        log.debug("[Create new Path ] for Chain  " + sfcData.getName());
        log.debug("[VNF dicts list ]  " + list_vnf_dicts.get(counter));
        List<SFPdict> newPaths = new ArrayList<>();
        double PathTrafficLoad = sfcData.getPaths().get(0).getPathTrafficLoad();

        SFPdict newPath = sfcData.getPaths().get(0);
        newPath.setPath_SFs(list_vnf_dicts.get(counter));
        newPath.setQoS(sfcData.getPaths().get(0).getQoS());
        newPaths.add(0, newPath);
        log.debug("[OLD Traffic load ]  " + PathTrafficLoad);

        newPath.setOldTrafficLoad(PathTrafficLoad);
        //sfcData.setChain(list_vnf_dicts.get(counter));

        sfcData.setPaths(newPaths);
        SFC_driver.DeleteSFP(sfcData.getInstanceId(), sfcData.getSymmetrical());

        SFCdict sfc_info = new SFCdict();
        sfc_info.setSfcDict(sfcData);

        String new_instance_id = SFC_driver.CreateSFP(sfc_info, list_vnf_dicts.get(counter));
        log.debug("[NEW Path Updated ] " + new_instance_id);
        String IDx = sfcData.getInstanceId().substring(5);

        String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);
        log.debug("[VNFFGR ID] " + VNFFGR_ID);

        String SFCC_name =
            SFC_Classifier_driver.Create_SFC_Classifer(
                classiferManag.query("sfcc-" + VNFFGR_ID), new_instance_id);

        log.debug("[NEW Classifier Updated ] " + SFCC_name);

        log.debug("[Key of the SFC ] =  " + sfcData.getId());
        log.debug("[new instance id ] =  " + new_instance_id);
        SFCCdict updated_classifier = classiferManag.query("sfcc-" + VNFFGR_ID);
        sfcData.setInstanceId(new_instance_id);

        updated_classifier.setInstanceId(new_instance_id);
        classiferManag.update(updated_classifier);
        sfpManag.update(newPath);
        sfcManag.update(sfcData);

        /*

        sfcc_db.update(
            (String) SFCdata_counter_.getKey(),
            new_instance_id,
            All_SFCs.get(SFCdata_counter_.getKey()).getSfccName(),
            All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo().getSfcDict().getSymmetrical(),
            list_vnf_dicts.get(counter),
            All_SFCs.get(SFCdata_counter_.getKey()).getSFCdictInfo(),
            All_SFCs.get(SFCdata_counter_.getKey()).getClassifierInfo());
            */

        counter++;
      }
      List<VNFdict> vnf_list = vnfManag.findAllbyType(vnfr.getType());

      for (int i = 0; i < vnf_list.size(); i++) {
        if (vnf_list.get(i).getStatus() == Status.INACTIVE) {
          vnfManag.delete(vnf_list.get(i));
          log.debug("Removed the VNF instance: " + vnf_list.get(i) + " from the data base");
        }
      }
    } else if (sfscheduler.equals("tradeoff")) {
      TOS.ReadjustVNFsAllocation(vnfr);
    } else if (sfscheduler.equals("shortestpath")) {
      SPpath.ReadjustVNFsAllocation(vnfr);
    } else {
      log.debug("ERROR : The SF Scheduler is not detected ");
    }

    // Deleting the failed VNF from database
    /* List<VNFdict> vnf_list=vnfManag.findAllbyType(vnfr.getType());

    for(int i=0;i<vnf_list.size();i++){
      if(vnf_list.get(i).getStatus()==Status.INACTIVE){
        vnfManag.delete(vnf_list.get(i));
        log.debug("Removed the VNF instance: "+vnf_list.get(i)+" from the data base");
      }
    }*/
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
        vnf_dict.setId(vnf_instance.getId());
        if (vnf_instance.getState().equals("ACTIVE") && !vnfManag.exists(vnf_dict.getId())) {
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
    log.info("[get VNF instance NAME] order: " + order);

    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      if (order < vdu.getVnfc_instance().size()) {
        for (VNFCInstance vnf_instance : vdu.getVnfc_instance()) {
          log.info("[get VNF instance NAME] counter: " + counter);

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

    log.debug("[Update SFC-Path Scaling]  ");

    if (!sfscheduler.equals("roundrobin")) {

      List<VNFdict> vnf_list = new ArrayList<>();
      HashMap<Integer, VNFdict> NewVNFs = new HashMap<>();
      int Counter = 0;

      for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
        for (VNFCInstance vnfc : vdu.getVnfc_instance()) {
          VNFdict VnfDict = new VNFdict();
          VnfDict.setId(vnfc.getId());
          VnfDict.setName(vnfc.getHostname());
          VnfDict.setType(vnfr.getType());
          VnfDict.setStatus(Status.ACTIVE);
          for (Ip ip : vnfc.getIps()) {
            VnfDict.setIP(ip.getIp());
            VnfDict.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
            log.debug(
                "[Adjustted the IP and Neutron port ID of new scaled SF ] "
                    + VnfDict.getIP()
                    + " , "
                    + VnfDict.getNeutronPortId());

            break;
          }

          if (!vnfManag.exists(VnfDict.getId())) {
            vnf_list.add(VnfDict);
            vnfManag.add(VnfDict);

            NewVNFs.put(Counter, VnfDict);
            Counter++;
            log.debug(
                "[Update Scaled VNF] the SFC DB has not this VNF instance = " + VnfDict.getName());
          }
        }
      }
      if (NewVNFs.size() > 0) {
        log.debug("[---------------- NEW VNFs Should be Created --------------]");

        SFC_driver.CreateSFs(NewVNFs);

        for (int a = 0; a < NewVNFs.size(); a++) {
          //NewVNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(NewVNFs.get(a).getName()));
          VNFdict vnf = vnfManag.findbyName(NewVNFs.get(a).getName());
          vnf.setConnectedSFF(SFC_driver.GetConnectedSFF(NewVNFs.get(a).getName()));
          vnfManag.update(vnf);
          log.debug("[Update OF SFs with connected SFF]");
        }
      }
    }

    if (sfscheduler.equals("roundrobin")) {
      Iterable<SfcDict> All_SFCs = sfcManag.query();

      if (sfcManag.query() != null) {
        log.debug("[ ALL SFCs number ] =  " + sfcManag.count());
      }

      HashMap<String, SfcDict> Involved_SFCs = new HashMap<>();
      //Get Involved SFCs for this VNF
      //Iterator itr = All_SFCs.iterator();
      //while (itr.hasNext()) {
      for (SfcDict sfcData : All_SFCs) {
        // Map.Entry SFCdata_counter = (Map.Entry) itr.next();
        // SfcDict sfcData = (SfcDict) SFCdata_counter.getValue();

        Map<Integer, VNFdict> VNFs = sfcData.getPaths().get(0).getPath_SFs();
        Iterator vnfs_count = VNFs.entrySet().iterator();
        while (vnfs_count.hasNext()) {
          Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
          if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())) {

            Involved_SFCs.put(sfcData.getInstanceId(), sfcData);
            log.debug("[Shortest Path Selection] Involved SFCs :  " + sfcData.getInstanceId());
          }
        }
      }
      if (Involved_SFCs != null) {
        log.debug("[Involved SFCs number ] =  " + Involved_SFCs.size());
      }
      List<Map<Integer, VNFdict>> list_vnf_dicts = new ArrayList<Map<Integer, VNFdict>>();

      Iterator it = Involved_SFCs.entrySet().iterator();
      int scale_counter = 0;
      while (it.hasNext()) {
        Map.Entry SFCdata_counter = (Map.Entry) it.next();
        log.info(
            "[Adding SFs  for Chain : ]  " + Involved_SFCs.get(SFCdata_counter.getKey()).getName());

        Map<Integer, VNFdict> VNFs =
            Involved_SFCs.get(SFCdata_counter.getKey()).getPaths().get(0).getPath_SFs();
        Iterator count = VNFs.entrySet().iterator();

        if (getVNFInstanceName(vnfr, scale_counter) == null) {
          log.info("[ The number of SF instances is smaller than the involved  Chains ]");

          scale_counter = 0;
        }
        while (count.hasNext()) {
          log.debug("[NEW SF Prepared] ");
          List<VNFdict> vnfs_list = new ArrayList<>();

          Map.Entry VNFcounter = (Map.Entry) count.next();
          log.debug("[OK] ");

          log.info("[SF counter ] " + VNFcounter.getKey());
          log.info(
              "[NEW SF will be added  ] " + getVNFInstanceName(vnfr, scale_counter).getHostname());
          log.info(
              "[type of VNF  ] "
                  + VNFs.get(VNFcounter.getKey()).getType()
                  + " VNF type: "
                  + vnfr.getType());
          log.info("[IP of the SF instance ] " + VNFs.get(VNFcounter.getKey()).getIP());
          log.info("[NAME of the SF instance ] " + VNFs.get(VNFcounter.getKey()).getName());

          if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())
              && getVNFInstanceName(vnfr, scale_counter) != null) {
            System.out.println(
                "[NEW SF will be added  ] "
                    + getVNFInstanceName(vnfr, scale_counter).getHostname());

            if (!VNFs.get(VNFcounter.getKey())
                .getName()
                .equals(getVNFInstanceName(vnfr, scale_counter).getHostname())) {
              Integer position = (Integer) VNFcounter.getKey();
              log.info("[position of old SF instance] -->" + position);
              VNFs.remove(position);
              log.info("[REMOVED old SF instance ] ");
              String Scaled_SF_instance_name =
                  getVNFInstanceName(vnfr, scale_counter).getHostname();

              log.info("[Scaled SF instance will be added  ] " + Scaled_SF_instance_name);

              VNFdict newVnf = new VNFdict();
              newVnf.setName(Scaled_SF_instance_name);
              newVnf.setType(vnfr.getType());
              newVnf.setId(getVNFInstanceName(vnfr, scale_counter).getId());
              newVnf.setStatus(Status.ACTIVE);
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
              log.info(
                  "[Update Scaled VNF] Is the SFC DB has this VNF instance = "
                      + newVnf.getName()
                      + "  ?  "
                      + vnfManag.exists(newVnf.getId()));

              if (!vnfManag.exists(newVnf.getId())) {
                vnfs_list.add(newVnf);
                vnfManag.add(newVnf);

                log.info(
                    "[Update Scaled VNF] the SFC DB has not this VNF instance = "
                        + newVnf.getName());
              }
              VNFs.put(position, newVnf);
              log.info("[ADDed the Scaled SF instance ] " + Scaled_SF_instance_name);
              /*   if (vnfs_list != null) {
                sfcc_db.addVNFs(vnfs_list);
              }*/
              break;
            }
          }
        }
        log.info("[Finished ALLOCATION OF SFs]");
        SFC_driver.CreateSFs(VNFs);

        for (int a = 0; a < VNFs.size(); a++) {
          //  VNFs.get(a).setConnectedSFF(SFC_driver.GetConnectedSFF(VNFs.get(a).getName()));
          VNFdict vnf = vnfManag.findbyName(VNFs.get(a).getName());
          vnf.setConnectedSFF(SFC_driver.GetConnectedSFF(VNFs.get(a).getName()));
          vnfManag.update(vnf);
          log.debug("[Update of SFs with connected SFF]");
        }

        list_vnf_dicts.add(VNFs);
        SFC_driver.DeleteSFP(
            Involved_SFCs.get(SFCdata_counter.getKey()).getInstanceId(),
            Involved_SFCs.get(SFCdata_counter.getKey()).getSymmetrical());

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
                + Involved_SFCs.get(SFCdata_counter_.getKey()).getName());
        log.info("[VNF dicts list ]  " + list_vnf_dicts.get(counter));
        List<SFPdict> newPaths = new ArrayList<>();
        double PathTrafficLoad =
            Involved_SFCs.get(SFCdata_counter_.getKey()).getPaths().get(0).getPathTrafficLoad();
        SFPdict newPath = Involved_SFCs.get(SFCdata_counter_.getKey()).getPaths().get(0);
        newPath.setPath_SFs(list_vnf_dicts.get(counter));
        newPath.setOldTrafficLoad(PathTrafficLoad);
        newPath.setQoS(Involved_SFCs.get(SFCdata_counter_.getKey()).getPaths().get(0).getQoS());
        newPaths.add(0, newPath);
        //  Involved_SFCs.get(SFCdata_counter_.getKey()).setChainSFs(list_vnf_dicts.get(counter));
        Involved_SFCs.get(SFCdata_counter_.getKey()).setPaths(newPaths);

        SFCdict sfc_info = new SFCdict();
        sfc_info.setSfcDict(Involved_SFCs.get(SFCdata_counter_.getKey()));

        String new_instance_id = SFC_driver.CreateSFP(sfc_info, list_vnf_dicts.get(counter));
        log.debug("[NEW Path Updated ] " + new_instance_id);
        String IDx = ((String) SFCdata_counter_.getKey()).substring(5);

        String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);
        log.debug("[VNFFGR ID] " + VNFFGR_ID);

        String SFCC_name =
            SFC_Classifier_driver.Create_SFC_Classifer(
                classiferManag.query("sfcc-" + VNFFGR_ID), new_instance_id);

        log.debug("[NEW Classifier Updated ] " + SFCC_name);

        log.debug("[Key of the Test_SFC ] =  " + (String) SFCdata_counter_.getKey());
        log.debug("[new instance id ] =  " + new_instance_id);
        SFCCdict updated_classifier = classiferManag.query("sfcc-" + VNFFGR_ID);
        updated_classifier.setInstanceId(new_instance_id);
        Involved_SFCs.get(SFCdata_counter_.getKey()).setInstanceId(new_instance_id);
        classiferManag.update(updated_classifier);
        log.debug("[new Path id ] =  " + newPath.getId());
        log.debug("[new Path  ] =  " + newPath.toString());

        sfpManag.update(newPath);
        sfcManag.update(Involved_SFCs.get(SFCdata_counter_.getKey()));

        log.info("[NEW Classifier Updated ] " + SFCC_name);
        log.info("[Key of the SFC ] =  " + (String) SFCdata_counter_.getKey());
        log.info("[new instance id ] =  " + new_instance_id);
        log.info("[Update SFCC DB] " + ((String) SFCdata_counter_.getKey()).substring(5));

        log.info("[SFC DB updated ]  Counter= " + counter);

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
          new_vnf.setStatus(Status.ACTIVE);

          for (Ip ip : vnfc_instance.getIps()) {
            new_vnf.setIP(ip.getIp());
            log.debug("[SFP-Creation] Get Neutron Pro " + new Date().getTime());

            new_vnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

            break;
          }
          list_vnfs.put(counter, new_vnf);
          if (!vnfManag.exists(new_vnf.getId())) {
            vnfs.add(new_vnf);
            vnfManag.add(new_vnf);
            log.info("[Create VNF] the SFC DB has not this VNF instance = " + new_vnf.getName());
          }
          counter++;
        }
      }
    }

    SFC_driver.CreateSFs(list_vnfs);
    /*if (vnfs != null) {
      for(int i=0;i<vnfs.size();i++) {
        vnfManag.add(vnfs.get(i));
        log.info("[ADD VNF to DB]  VNF instance Name= " + vnfs.get(i).getName());

      }
    }*/
    //adding to Repository
    /* for (int i = 0; i < list_vnfs.size(); i++) {
      log.info("[ADD VNF to REPO] " + list_vnfs.get(i).getId());

      if(vnfManag.query(list_vnfs.get(i).getId())==null){
      //vnfManag.save(list_vnfs.get(i));
      vnfManag.add(list_vnfs.get(i));
      log.info("[Added this VNF] ");
        }

    }*/
    for (int a = 0; a < list_vnfs.size(); a++) {
      VNFdict vnf = vnfManag.findbyName(list_vnfs.get(a).getName());
      vnf.setConnectedSFF(SFC_driver.GetConnectedSFF(list_vnfs.get(a).getName()));
      vnfManag.update(vnf);
      log.info(
          "[Update VNF DB] by the connected VNF instance = "
              + vnf.getName()
              + "  connected SFF= "
              + vnf.getConnectedSFF());
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

    log.info("[SFC-Creation] Creating Path Finished  ");

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
    sfc_dict_test.setInfraDriver(sfcDriver);
    sfc_dict_test.setStatus(Status.ACTIVE);
    sfc_dict_test.setTenantId(NC.getTenantID());
    List<SFPdict> Paths = new ArrayList<SFPdict>();
    SFPdict Path = new SFPdict();

    Map<Integer, VNFdict> path = new HashMap<>();

    Iterator count = vnfdicts.entrySet().iterator();
    while (count.hasNext()) {

      Map.Entry VNFcounter = (Map.Entry) count.next();
      log.debug(
          " 1- [ Add VNF to the Path ] ID of VNF is : "
              + vnfdicts.get(VNFcounter.getKey()).getId());

      VNFdict vnf = vnfManag.query(vnfdicts.get(VNFcounter.getKey()).getId());
      log.debug(" 2- [ Add VNF to the Path ] ID of VNF is : " + vnf.getId());
      Integer position = (Integer) VNFcounter.getKey();
      log.debug("[ Add VNF to the Path ] in the position = " + position);

      path.put(position, vnf);
      log.debug("[ Done ]");
    }

    //Path.setPath_SFs(vnfdicts);
    Path.setPath_SFs(path);
    Path.setId("Path-" + nsr.getName() + "-" + vnffgr.getId());
    Path.setName("Path-" + nsr.getName() + "-" + vnffgr.getId());
    Path.setParentChainId(vnffgr.getId());

    for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {
      String QoS_level = nfp.getPolicy().getQoSLevel();

      if (QoS_level == null) {
        log.warn(" QoS level is null");
        QoS_level = "bronze";
      }

      if (QoS_level.equals("gold")) {
        Path.setQoS(3);
      } else if (QoS_level.equals("silver")) {
        Path.setQoS(2);
      } else {
        Path.setQoS(1);
      }
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
    log.debug(" ADD to it Instance  ID:  " + instance_id + " at time " + new Date().getTime());
    sfc_test.getSfcDict().setInstanceId(instance_id);
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
    sfcc_dict.setInstanceId(instance_id);

    String SFCC_name = SFC_Classifier_driver.Create_SFC_Classifer(sfcc_dict, instance_id);
    /*
        sfcc_db.add(
            vnffgr.getId(),
            instance_id,
            sfcc_dict.getName(),
            sfc_dict_test.getSymmetrical(),
            vnfdicts,
            sfc_test,
            sfcc_dict);
    */
    log.info(" [Adding SFP to Database ]  ");
    sfpManag.add(Path);
    log.info(" [SFP Added ]  ");
    log.info(" [Adding SFC to Database ]  ");
    sfcManag.add(sfc_test.getSfcDict());
    log.info(" [SFC Added ]  ");
    log.info(" [Adding Classifier to Database ]  ");

    classiferManag.add(sfcc_dict);
    log.info(" [Classifier Added ]  ");
    /*  log.debug(
            " [GET  Instance  ID: ]  "
                + sfcc_db.getRspID(vnffgr.getId())
                + " at time "
                + new Date().getTime());
        log.debug(
            "[GET SFC NAME : ]  "
                + sfcc_db.getAllSFCs().get(vnffgr.getId()).getSFCdictInfo().getSfcDict().getName());
    */
    if (SFCC_name != null && instance_id != null) {
      log.info("[ Chain Created successfully] , instance id= " + instance_id);

      return true;
    } else {
      log.error(" [Chain Not Created at time] ");

      return false;
    }
  }

  public boolean Delete(String vnffgID, String SfSchedulingType, boolean lastSFC)
      throws IOException {
    if (SfSchedulingType.equals("roundrobin")) {
      RoundRobinSelection RRS = new RoundRobinSelection();
      RRS.Delete(sfcManag.query(vnffgID).getPaths().get(0).getPath_SFs());
      log.info("Remove the mapCOUNT  RoundRobin " + vnffgID);

    } else if (SfSchedulingType.equals("tradeoff")) {
      TradeOffShortestPathLoadBalancingSelection TOSPLB =
          new TradeOffShortestPathLoadBalancingSelection(SDN_Controller_driver_type);
      TOSPLB.Delete(sfcManag.query(vnffgID).getPaths().get(0).getPath_SFs());
      log.info("Remove the mapCOUNT Tradeoff  " + vnffgID);
    }
    log.info("delete NSR ID:  " + vnffgID);

    SfcDict sfc = sfcManag.query(vnffgID);
    SFCCdict sfcc = classiferManag.query("sfcc-" + vnffgID);

    String rsp_id = sfc.getInstanceId();

    String sffc_name = sfcc.getName();
    log.debug("SFC  id to be deleted:  " + rsp_id);
    log.debug("Classifier  id to be deleted:  " + sffc_name);

    String IDx = rsp_id.substring(5);
    log.debug(" [ADD Instance] " + rsp_id + " - with IDx = " + IDx);
    String VNFFGR_ID = IDx.substring(IDx.indexOf('-') + 1);
    log.debug("[VNFFGR ID] =" + VNFFGR_ID);

    Map<Integer, VNFdict> Chain = sfc.getPaths().get(0).getPath_SFs();

    if (Chain == null) {
      log.error("[Could not find this chain in Data Base] ");

    } else {
      for (int x = 0; x < Chain.size(); x++) {

        log.debug("[Chain in DB] SF in Position " + x + "  is " + Chain.get(x).getName());
      }
    }

    ResponseEntity<String> result = SFC_Classifier_driver.Delete_SFC_Classifier(sffc_name);
    log.info("Delete SFC Classifier :  " + result.getStatusCode().is2xxSuccessful());
    ResponseEntity<String> sfc_result = SFC_driver.DeleteSFC(rsp_id, sfc.getSymmetrical());
    log.info("Delete SFC   :  " + sfc_result.getStatusCode().is2xxSuccessful());

    if (result != null && sfc_result != null) {

      if (result.getStatusCode().is2xxSuccessful()
          && sfc_result.getStatusCode().is2xxSuccessful()) {
        classiferManag.remove(classiferManag.query("sfcc-" + vnffgID));
        log.info("[ Removed classifier from DB  ]  ");
        sfcManag.remove(sfcManag.query(vnffgID));
        log.info("[ Removed SFC from DB  ]  ");
        if (lastSFC == true) {
          SFC_driver.DeleteSFs();
          vnfManag.removeAll();
          log.info("[ Removed All SFs from DB  ]  ");
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}
