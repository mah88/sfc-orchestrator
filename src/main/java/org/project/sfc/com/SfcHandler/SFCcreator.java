package org.project.sfc.com.SfcHandler;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.DynamicPathCreation.RandomPathSelection;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SFCdict;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SfcDict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.VNFdict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.SFCCdict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * Created by mah on 3/14/16.
 */

public class SFCcreator {
    Opendaylight SFC = new Opendaylight();
    SFCdict sfc_test=new SFCdict();
    SfcDict sfc_dict_test=new SfcDict();
    NeutronClient NC=new NeutronClient();
    SFC_Classifier classifier_test2=new SFC_Classifier();
    SFCCdict sfcc_dict=new SFCCdict();
    SFC sfcc_db= org.project.sfc.com.SfcHandler.SFC.getInstance();





    int counter=1;

    public boolean Create(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr){
  // public boolean Create(Set<VNFForwardingGraphRecord> vnfrs, NetworkServiceRecord nsr){
//FIX ME change the place of this configuration
        if(SFC.Check_Configuration_NETVIRT()==false) {
            ResponseEntity<String> netvirt = SFC.Configure_NETVIRT();
            System.out.println("NETVIRT status code "+ netvirt.getStatusCode());

        }

        if(SFC.Check_Configuration_SfcOfRenderer()==false) {
            ResponseEntity<String> sfcodrender = SFC.Configure_SfcOfRenderer();
            System.out.println("SFC OF Render status code " + sfcodrender.getStatusCode());
            //----------------------------------
        }

        List<VNFdict> vnf_test =new ArrayList<>();
        List<String> chain = new ArrayList<String>();
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();

        RandomPathSelection RPS=new RandomPathSelection();
        vnfdicts=RPS.CreatePath(vnfrs,nsr);

        for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
            chain.add(vnfr.getName());
        }

        sfc_dict_test.setName(nsr.getName());
        sfc_dict_test.setChain(chain);
        sfc_dict_test.setId(nsr.getId());
        sfc_dict_test.setInfraDriver("ODL");
        sfc_dict_test.setSymmetrical(true);
        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId(NC.getTenantID());
        sfc_test.setSfcDict(sfc_dict_test);

        SFC.CreateSFC(sfc_test, vnfdicts);
        String instance_id=SFC.CreateSFP(sfc_test, vnfdicts);
        System.out.println(" ADD NSR ID as key to SFC DB:  " + nsr.getId() + " at time " + new Date().getTime());
        System.out.println(" ADD to it Instance  ID:  " + instance_id + " at time " + new Date().getTime());


        sfcc_dict.setStatus("create");
        sfcc_dict.setTenantId(NC.getTenantID());
        sfcc_dict.setInfraDriver("netvirtsfc");
        sfcc_dict.setId("sfcc-"+nsr.getId());
        sfcc_dict.setChain(sfc_dict_test.getId());
        sfcc_dict.setName("sfc-classifier-"+nsr.getName());
        AclMatchCriteria acl=new AclMatchCriteria();
        acl.setDestPort(80);
        acl.setSrcPort(0);
        acl.setProtocol(6);
        List<AclMatchCriteria> list_acl=new ArrayList<AclMatchCriteria>();
        list_acl.add(acl);
        sfcc_dict.setAclMatchCriteria(list_acl);
        String SFCC_name=classifier_test2.Create_SFC_Classifer(sfcc_dict,instance_id);

        sfcc_db.add(nsr.getId(),instance_id,sfcc_dict.getName());
        System.out.println(" GET  Instance  ID:  " + sfcc_db.getRspID(nsr.getId()) + " at time " + new Date().getTime());

        if (SFCC_name!=null && instance_id!=null){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean Delete(String nsrID){
        System.out.println("delete NSR ID:  " + nsrID + " at time " + new Date().getTime());
        String rsp_id=sfcc_db.getRspID(nsrID);

        String sffc_name=sfcc_db.getSfccName(nsrID);
        System.out.println("instance id to be deleted:  " + sffc_name );

        ResponseEntity<String> sfc_result=SFC.DeleteSFC(rsp_id,false);
        System.out.println("Delete SFC   :  " + sfc_result.getStatusCode().is2xxSuccessful() );
        ResponseEntity<String> result= classifier_test2.Delete_SFC_Classifier(sffc_name);
        System.out.println("Delete SFC Classifier :  " + result.getStatusCode().is2xxSuccessful() );

        if(result!=null && sfc_result!=null) {


            if (result.getStatusCode().is2xxSuccessful() && sfc_result.getStatusCode().is2xxSuccessful()) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
}