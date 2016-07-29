package Testing;

import org.openbaton.sdk.api.exception.SDKException;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;

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
 //test.DeleteSFC("Path-SFC-demo-", false);
      VNFdict vnf2_test = new VNFdict();
       NeutronClient NC=new NeutronClient();

     /*   if(test.Check_Configuration_NETVIRT()==false) {
            ResponseEntity<String> netvirt = test.Configure_NETVIRT();
            System.out.println("NETVIRT status code "+ netvirt.getStatusCode());

        }

        if(test.Check_Configuration_SfcOfRenderer()==false) {
            ResponseEntity<String> sfcodrender = test.Configure_SfcOfRenderer();
            System.out.println("SFC OF Render status code " + sfcodrender.getStatusCode());
            //----------------------------------
        }
        */
        //   ResponseEntity<String> netvirt= test.Configure_NETVIRT();
     //   ResponseEntity<String> sfcodrender= test.Configure_SfcOfRenderer();
    //    System.out.println("NETVIRT status code "+ netvirt.getStatusCode());
      //  System.out.println("SFC OF Render status code "+ sfcodrender.getStatusCode());


        vnf1_test.setIP("12.0.0.5");
        vnf2_test.setIP("12.0.0.6");
       vnf2_test.setName("http-SF");
       vnf1_test.setName("FW-SF");
        System.out.println("Config. : Neutron Port ID of VNF 1 "+NC.getNeutronPortID(vnf1_test.getIP()));

        vnf1_test.setNeutronPortId(NC.getNeutronPortID(vnf1_test.getIP())); //NC.getNeutronPortID(vnf1_test.getIP()));
       System.out.println("Config. : Neutron Port ID of VNF 2 "+NC.getNeutronPortID(vnf2_test.getIP()));
       vnf2_test.setNeutronPortId(NC.getNeutronPortID(vnf2_test.getIP()));
        vnf2_test.setType("HTTP");
       vnf1_test.setType("FW");

        SFCdict sfc_test = new SFCdict();
        SfcDict sfc_dict_test=new SfcDict();

        sfc_dict_test.setName("SFC-demo_test");
        List<String> chain = new ArrayList<String>();
        chain.add("FW-SF");

        chain.add("http-SF");

        sfc_dict_test.setChain(chain);
        sfc_dict_test.setId("SFC-demo:222122141");

        sfc_dict_test.setInfraDriver("ODL");
        sfc_dict_test.setSymmetrical(false);
        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId(NC.getTenantID());//NC.getTenantID());
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
        vnfdicts.put(0, vnf1_test);
        vnfdicts.put(1, vnf2_test);
        sfc_test.setSfcDict(sfc_dict_test);
        test.CreateSFC(sfc_test, vnfdicts);
       String instance_id= test.CreateSFP(sfc_test, vnfdicts);

System.out.println("INSTANCE ID "+ instance_id);
        SFC_Classifier classifier_test2=new SFC_Classifier();
        SFCCdict sfcc_dict=new SFCCdict();
        sfcc_dict.setStatus("create");
        sfcc_dict.setTenantId(NC.getTenantID());
        sfcc_dict.setInfraDriver("netvirtsfc");
        sfcc_dict.setId("sfcc-demo-test:x");
        sfcc_dict.setChain(sfc_dict_test.getId());
        sfcc_dict.setName("sfc-classifier-demo-x");
        AclMatchCriteria acl=new AclMatchCriteria();
        acl.setDestPort(0);
        acl.setSrcPort(0);
       // acl.setDestMAC("fa:16:3e:3c:5c:d3");
        acl.setProtocol(1);
        acl.setDestIpv4("12.0.0.4/32");
        List<AclMatchCriteria> list_acl=new ArrayList<AclMatchCriteria>();
        list_acl.add(acl);
        sfcc_dict.setAclMatchCriteria(list_acl);
        classifier_test2.Create_SFC_Classifer(sfcc_dict,instance_id);


        //testing the neutron port client
    /*    NeutronClient NC=new NeutronClient();
        String nc_port=NC.getNeutronPortID("11.0.0.5");
        System.out.println("The NEUTRON PORT ID IS: "+nc_port);
*/
    }
}
