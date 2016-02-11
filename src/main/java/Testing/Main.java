package Testing;

import org.project.sfc.com.ODL_SFC.Opendaylight;
import org.project.sfc.com.ODL_SFC.VNFdict;
import org.project.sfc.com.ODL_SFC_Classifier.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.ODL_SFC_Classifier.SFCCdict.SFCCdict;
import org.project.sfc.com.ODL_SFC_Classifier.SFC_Classifier;
import org.project.sfc.com.SFCdict.SFCdict;
import org.project.sfc.com.SFCdict.SfcDict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mah on 2/8/16.
 */
public class Main {
    public static void main(String[] args) {
        Opendaylight test = new Opendaylight();
        VNFdict vnf1_test = new VNFdict();
       // VNFdict vnf2_test = new VNFdict();

        vnf1_test.setIP("11.0.0.3");
       // vnf2_test.setIP("10.0.0.7");
        vnf1_test.setName("FW-1");
        //vnf2_test.setName("DPI-1");
        vnf1_test.setNeutronPortId("5a3a7bf8-9f43-4a07-b258-a317600577c4");
       // vnf2_test.setNeutronPortId("22222222222");
        vnf1_test.setType("FW");
        //vnf1_test.setType("DPI");

        SFCdict sfc_test = new SFCdict();
        SfcDict sfc_dict_test=new SfcDict();

        sfc_dict_test.setName("SFC-TEST");
        List<String> chain = new ArrayList<String>();
        chain.add("FW-1");
     //   chain.add("DPI-1");
        sfc_dict_test.setChain(chain);
        sfc_dict_test.setId("SFC-test1");

        sfc_dict_test.setInfraDriver("ODL");
        sfc_dict_test.setSymmetrical(false);
        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId("357e82e10a084733b693d079f8e82737");
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
        vnfdicts.put(0, vnf1_test);
        //vnfdicts.put(1, vnf2_test);
        sfc_test.setSfcDict(sfc_dict_test);

       String instance_id= test.CreateSFC(sfc_test, vnfdicts);

        SFC_Classifier classifier_test2=new SFC_Classifier();
        SFCCdict sfcc_dict=new SFCCdict();
        sfcc_dict.setStatus("create");
        sfcc_dict.setTenantId("357e82e10a084733b693d079f8e82737");
        sfcc_dict.setInfraDriver("netvirtsfc");
        sfcc_dict.setId("sfcc-test1");
        sfcc_dict.setChain(sfc_dict_test.getId());
        sfcc_dict.setName("sfc-classifier-test");
        AclMatchCriteria acl=new AclMatchCriteria();
        acl.setDestPort(80);
        acl.setProtocol(6);
        List<AclMatchCriteria> list_acl=new ArrayList<AclMatchCriteria>();
        list_acl.add(acl);
        sfcc_dict.setAclMatchCriteria(list_acl);
        classifier_test2.Create_SFC_Classifer(sfcc_dict,instance_id);



    }
}
