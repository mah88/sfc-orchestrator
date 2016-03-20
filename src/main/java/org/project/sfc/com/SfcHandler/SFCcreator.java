package org.project.sfc.com.SfcHandler;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
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
    SFC sfc_db=new SFC();

    int counter=1;

    public boolean Create(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr){
        List<VNFdict> vnf_test =new ArrayList<>();
        List<String> chain = new ArrayList<String>();
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();

        int i=0;
        for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
            VNFdict new_vnf=new VNFdict();
            vnf_test.add(new_vnf);
            Set<String> ip_address=vnfr.getVnf_address();
            for ( String ip:vnfr.getVnf_address()) {
                vnf_test.get(i).setIP(ip);
                break;
            }
            vnf_test.get(i).setType(vnfr.getType());
            vnf_test.get(i).setName(vnfr.getName());
            vnf_test.get(i).setNeutronPortId(NC.getNeutronPortID(vnf_test.get(i).getIP()));
            chain.add(vnfr.getName());
            vnfdicts.put(i,vnf_test.get(i));

            i++;
        }


        sfc_dict_test.setName(nsr.getName());
        sfc_dict_test.setChain(chain);
        sfc_dict_test.setId(nsr.getId());
        sfc_dict_test.setInfraDriver("ODL");
        sfc_dict_test.setSymmetrical(false);
        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId(NC.getTenantID());
        sfc_test.setSfcDict(sfc_dict_test);

        String instance_id= SFC.CreateSFC(sfc_test, vnfdicts);

        sfc_db.add(nsr.getId(),instance_id);

        sfcc_dict.setStatus("create");
        sfcc_dict.setTenantId(NC.getTenantID());
        sfcc_dict.setInfraDriver("netvirtsfc");
        sfcc_dict.setId("sfcc-"+nsr.getId());
        sfcc_dict.setChain(sfc_dict_test.getId());
        sfcc_dict.setName("sfc-classifier-"+nsr.getName());
        AclMatchCriteria acl=new AclMatchCriteria();
        acl.setDestPort(80);
        acl.setProtocol(6);
        List<AclMatchCriteria> list_acl=new ArrayList<AclMatchCriteria>();
        list_acl.add(acl);
        sfcc_dict.setAclMatchCriteria(list_acl);
        String SFCC_name=classifier_test2.Create_SFC_Classifer(sfcc_dict,instance_id);
        if (SFCC_name!=null && instance_id!=null){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean Delete(String nsrID){
        String instance_id=sfc_db.get(nsrID);
        ResponseEntity<String> sfc_result=SFC.DeleteSFC(nsrID,false);
        ResponseEntity<String> result= classifier_test2.Delete_SFC_Classifier(instance_id);
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