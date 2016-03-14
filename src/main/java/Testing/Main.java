package Testing;

import org.openbaton.sdk.api.exception.SDKException;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.VNFdict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.SFCCdict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SFCdict;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SfcDict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mah on 2/8/16.
 */
public class Main {
    public static void main(String[] args) throws SDKException {

        //testing OPENBATON communication and creating NS and VNF
     /*   SecureRandom appIDGenerator = new SecureRandom();
        OpenBatonManager obmanager=new OpenBatonManager();
        String appID = new BigInteger(130,appIDGenerator).toString(64);
System.out.println("APP ID:"+ appID);
String callbackurl="http://localhost:8081";
        try {
            obmanager.init();
            OpenBatonCreateServer openbatonCreateServer = obmanager.getServiceFunctionID(Flavor.TINY, appID, callbackurl, false, 1);
        } catch (IOException x){
            System.out.println("ERROR: IOEXCEPTION");

        }
        */

        //testing the SFC deployment in ODL
        Opendaylight test = new Opendaylight();
        VNFdict vnf1_test = new VNFdict();
       // VNFdict vnf2_test = new VNFdict();
        NeutronClient NC=new NeutronClient();


        vnf1_test.setIP("11.0.0.3");
       // vnf2_test.setIP("10.0.0.7");
        vnf1_test.setName("SF-121");
        //vnf2_test.setName("DPI-1");
        vnf1_test.setNeutronPortId(NC.getNeutronPortID(vnf1_test.getIP()));
       // vnf2_test.setNeutronPortId("22222222222");
        vnf1_test.setType("FW");
        //vnf1_test.setType("DPI");

        SFCdict sfc_test = new SFCdict();
        SfcDict sfc_dict_test=new SfcDict();

        sfc_dict_test.setName("SFC-test");
        List<String> chain = new ArrayList<String>();
        chain.add("SF-121");
     //   chain.add("DPI-1");
        sfc_dict_test.setChain(chain);
        sfc_dict_test.setId("SFC-test:1");

        sfc_dict_test.setInfraDriver("ODL");
        sfc_dict_test.setSymmetrical(false);
        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId(NC.getTenantID());
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
        vnfdicts.put(0, vnf1_test);
        //vnfdicts.put(1, vnf2_test);
        sfc_test.setSfcDict(sfc_dict_test);

       String instance_id= test.CreateSFC(sfc_test, vnfdicts);


        SFC_Classifier classifier_test2=new SFC_Classifier();
        SFCCdict sfcc_dict=new SFCCdict();
        sfcc_dict.setStatus("create");
        sfcc_dict.setTenantId(NC.getTenantID());
        sfcc_dict.setInfraDriver("netvirtsfc");
        sfcc_dict.setId("sfcc-test:1");
        sfcc_dict.setChain(sfc_dict_test.getId());
        sfcc_dict.setName("sfc-classifier-testing");
        AclMatchCriteria acl=new AclMatchCriteria();
        acl.setDestPort(80);
        acl.setProtocol(6);
        List<AclMatchCriteria> list_acl=new ArrayList<AclMatchCriteria>();
        list_acl.add(acl);
        sfcc_dict.setAclMatchCriteria(list_acl);
        classifier_test2.Create_SFC_Classifer(sfcc_dict,instance_id);
        System.out.println("SF PORT ID "+NC.getNeutronPortID(vnf1_test.getIP()));
        System.out.println("TENANT ID "+NC.getTenantID());


        //testing the neutron port client
    /*    NeutronClient NC=new NeutronClient();
        String nc_port=NC.getNeutronPortID("11.0.0.5");
        System.out.println("The NEUTRON PORT ID IS: "+nc_port);
*/
    }
}
