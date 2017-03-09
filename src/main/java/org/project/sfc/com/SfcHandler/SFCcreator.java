package org.project.sfc.com.SfcHandler;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcDriver.PathCreation.DeploymentPathCreation.RandomPathSelection;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.project.sfc.com.SfcHandler.SFC.SFC_Data;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

/**
 * Created by mah on 3/14/16.
 */











// This class is not used





public class SFCcreator {
  Opendaylight SFC;
  SFCdict sfc_test = new SFCdict();
  SfcDict sfc_dict_test = new SfcDict();
  NeutronClient NC;
  SFC_Classifier classifier_test2;
  SFCCdict sfcc_dict = new SFCCdict();
  SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();
  private org.slf4j.Logger logger;


  public SFCcreator() throws IOException {
    SFC = new Opendaylight();
    NC = new NeutronClient();
    classifier_test2 = new SFC_Classifier();
  }

  public void UpdateChainsPaths(VirtualNetworkFunctionRecord vnfr) {

    logger.info("[Test_SFC-Path-UPDATE] (1) at time " + new Date().getTime());

    HashMap<String, SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    logger.info("===========================================");
    if (All_SFCs != null) {
      logger.info("[ ALL SFCs number ] =  " + All_SFCs.size());
    }
    Iterator it = All_SFCs.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator count = VNFs.entrySet().iterator();
      while (count.hasNext()) {
        System.out.println(
            "[NEW SF Prepared] the ID in data base for the Test_SFC is " + SFCdata_counter.getKey());

        Map.Entry VNFcounter = (Map.Entry) count.next();
        System.out.println("[OK] ");

        System.out.println("[SF counter ] " + VNFcounter.getKey());

        if (getFaildVNFname(vnfr) != null) {
          System.out.println(
              "[Found Failed SF] -->" + getFaildVNFname(vnfr) + " at time " + new Date().getTime());

          if (VNFs.get(VNFcounter.getKey()).getName().equals(getFaildVNFname(vnfr))) {
            Integer position = (Integer) VNFcounter.getKey();
            System.out.println("[position of Failed SF] -->" + position);
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
                System.out.println(
                    "[Adjustted the IP and Neutron port ID of new SF ] "
                        + newVnf.getIP()
                        + " , "
                        + newVnf.getNeutronPortId());

                break;
              }
              VNFs.put(position, newVnf);
              logger.info("[ADDed the NEW SF ] " + getActiveVNF(vnfr).getHostname());
              logger.info(
                  "[Create new Path ] for Chain  "
                      + All_SFCs.get(SFCdata_counter.getKey())
                          .getSFCdictInfo()
                          .getSfcDict()
                          .getName());

              String new_instance_id =
                  SFC.CreateSFP(All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo(), VNFs);
              logger.info("[NEW Path Updated ] " + new_instance_id);
              logger.info("[NEW VNF in path ] " + VNFs.get(0).getName());





              String SFCC_name =
                  classifier_test2.Create_SFC_Classifer(
                      All_SFCs.get(SFCdata_counter.getKey()).getClassifierInfo(), new_instance_id);
              logger.info("[NEW Classifier Updated ] " + SFCC_name);
              sfcc_db.update(
                  (String) SFCdata_counter.getKey(),
                  new_instance_id,
                  All_SFCs.get(SFCdata_counter.getKey()).getSfccName(),
                  All_SFCs.get(SFCdata_counter.getKey())
                      .getSFCdictInfo()
                      .getSfcDict()
                      .getSymmetrical(),
                  VNFs,
                  All_SFCs.get(SFCdata_counter.getKey()).getSFCdictInfo(),
                  All_SFCs.get(SFCdata_counter.getKey()).getClassifierInfo());

              break;
            }

          } else {
            System.out.println("[Not equal to the name of the failed VNF]");
          }
        }
      }
      System.out.println(
          "[---------------------------Finished ALLOCATION OF SFs----------------------------------]");
    }
  }

  public String getFaildVNFname(VirtualNetworkFunctionRecord vnfr) {
    boolean found = false;
    String VNF_name = "";
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance vnf_instance : vdu.getVnfc_instance()) {

        if (vnf_instance.getState().equals("failed")) {

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

        if (vnf_instance.getState().equals("active")) {

          found = true;

          VNF_instance = vnf_instance;
        }
      }
    }

    if (found == false) {
      return null;
    } else {
      return VNF_instance;
    }
  }

  public boolean Create(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr) throws IOException {

    System.out.println("[Test_SFC-Creation] (1) at time " + new Date().getTime());

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

      System.out.println(
          "[Size of VNF MEMBERS for creating CHAIN] "
              + vnf_members.size()
              + " for Test_SFC allocation to nsr handler at time "
              + new Date().getTime());
      System.out.println(
          "[ VNFFGR ID for creating CHAIN] "
              + vnffgr.getId()
              + " for Test_SFC allocation to nsr handler at time "
              + new Date().getTime());
      boolean Response = CreateChain(vnf_members, vnffgr, nsr);
      if (Response == false) {
        System.out.println("[CHAIN IS NOT CREATED] ");

        return false;
      }
    }

    return true;
  }

  public boolean CreateChain(
      Set<VirtualNetworkFunctionRecord> vnfrs,
      VNFForwardingGraphRecord vnffgr,
      NetworkServiceRecord nsr) throws IOException {

    List<VNFdict> vnf_test = new ArrayList<VNFdict>();
    List<String> chain = new ArrayList<String>();
    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    System.out.println("[Test_SFC-Creation] (2) at time " + new Date().getTime());

    RandomPathSelection RPS = new RandomPathSelection();
    System.out.println("[Test_SFC-Creation] (3) at time " + new Date().getTime());

    vnfdicts = RPS.CreatePath(vnfrs, vnffgr, nsr);

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

    //sfc_dict_test.setStatus("create");
    sfc_dict_test.setTenantId(NC.getTenantID());
    sfc_test.setSfcDict(sfc_dict_test);

    SFC.CreateSFC(sfc_test, vnfdicts);

    String instance_id = SFC.CreateSFP(sfc_test, vnfdicts);



    System.out.println(
        " ADD NSR ID as key to Test_SFC DB:  " + nsr.getId() + " at time " + new Date().getTime());
    System.out.println(
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
    String SFCC_name = classifier_test2.Create_SFC_Classifer(sfcc_dict, instance_id);

    sfcc_db.add(
        vnffgr.getId(),
        instance_id,
        sfcc_dict.getName(),
        sfc_dict_test.getSymmetrical(),
        vnfdicts,
        sfc_test,
        sfcc_dict);
    System.out.println(
        " GET  Instance  ID:  "
            + sfcc_db.getRspID(vnffgr.getId())
            + " at time "
            + new Date().getTime());

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

    ResponseEntity<String> result = classifier_test2.Delete_SFC_Classifier(sffc_name);
    System.out.println("Delete Test_SFC Classifier :  " + result.getStatusCode().is2xxSuccessful());
    ResponseEntity<String> sfc_result = SFC.DeleteSFC(rsp_id, sfcc_db.isSymmSFC(nsrID));
    System.out.println("Delete Test_SFC   :  " + sfc_result.getStatusCode().is2xxSuccessful());

    if (result != null && sfc_result != null) {

      if (result.getStatusCode().is2xxSuccessful()
          && sfc_result.getStatusCode().is2xxSuccessful()) {
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
