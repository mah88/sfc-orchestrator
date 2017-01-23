package testing;

import org.apache.http.HttpResponse;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

/**
 * Created by mah on 8/5/16.
 */
public class Test {

  public static void main(String[] args) throws SDKException, IOException {

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

    //testing the Test_SFC deployment in ODL

    Opendaylight test = new Opendaylight();
    VNFdict vnf1_test = new VNFdict();
    //test.DeleteSFC("Path-Test_SFC-demo-", false);
    VNFdict vnf2_test = new VNFdict();
    NeutronClient NC = new NeutronClient();
    SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();

    /*   if(test.Check_Configuration_NETVIRT()==false) {
        ResponseEntity<String> netvirt = test.Configure_NETVIRT();
        System.out.println("NETVIRT status code "+ netvirt.getStatusCode());

    }
/*
    if(test.Check_Configuration_SfcOfRenderer()==false) {
        ResponseEntity<String> sfcodrender = test.Configure_SfcOfRenderer();
        System.out.println("Test_SFC OF Render status code " + sfcodrender.getStatusCode());
        //----------------------------------
    }
    */
    //   ResponseEntity<String> netvirt= test.Configure_NETVIRT();
    //   ResponseEntity<String> sfcodrender= test.Configure_SfcOfRenderer();
    //    System.out.println("NETVIRT status code "+ netvirt.getStatusCode());
    //  System.out.println("Test_SFC OF Render status code "+ sfcodrender.getStatusCode());

      vnf1_test.setIP("12.0.0.8");
    vnf2_test.setIP("12.0.0.7");
    vnf2_test.setName("FW-SF");
        vnf1_test.setName("http-SF");
    // System.out.println("Config. : Neutron Port ID of VNF 1 " + NC.getNeutronPortID(vnf1_test.getIP()));

       vnf1_test.setNeutronPortId(NC.getNeutronPortID(vnf1_test.getIP())); //NC.getNeutronPortID(vnf1_test.getIP()));
    // System.out.println("Config. : Neutron Port ID of VNF 2 " + NC.getNeutronPortID(vnf2_test.getIP()));

    vnf2_test.setNeutronPortId(NC.getNeutronPortID(vnf2_test.getIP()));
   // vnf2_test.setNeutronPortId("dd34837e-4693-4b1a-a42a-b30cc7388c09");

    vnf2_test.setType("FW");
    vnf1_test.setType("HTTP");


    // Chain 1
    SFCdict sfc_test = new SFCdict();
    SfcDict sfc_dict_test = new SfcDict();

    sfc_dict_test.setName("Test_SFC-demo_test");
    List<String> chain = new ArrayList<String>();
    chain.add("http-SF");

    chain.add("FW-SF");


    sfc_dict_test.setChain(chain);
    sfc_dict_test.setId("Test_SFC-demo:222122141");

    sfc_dict_test.setInfraDriver("ODL");
    sfc_dict_test.setSymmetrical(false);
    sfc_dict_test.setStatus("create");
    // sfc_dict_test.setTenantId(NC.getTenantID());//NC.getTenantID());
    HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
    vnfdicts.put(0, vnf1_test);
    vnfdicts.put(1, vnf2_test);
    sfc_test.setSfcDict(sfc_dict_test);
    test.CreateSFC(sfc_test, vnfdicts);
    test.CreateSFs(vnfdicts);
    String instance_id = test.CreateSFP(sfc_test, vnfdicts);

    System.out.println("INSTANCE ID " + instance_id);
    SFC_Classifier classifier_test = new SFC_Classifier();
    SFCCdict sfcc_dict = new SFCCdict();
    sfcc_dict.setStatus("create");
    //sfcc_dict.setTenantId(NC.getTenantID());
    // sfcc_dict.setTenantId("11010100101");
    sfcc_dict.setInfraDriver("netvirtsfc");
    sfcc_dict.setId("sfcc-demo-test:x");
    sfcc_dict.setChain(sfc_dict_test.getId());
    sfcc_dict.setName("sfc-classifier-demo-x");
    AclMatchCriteria acl = new AclMatchCriteria();
    acl.setDestPort(80);
    acl.setSrcPort(2000);
    // acl.setDestMAC("fa:16:3e:3c:5c:d3");
    acl.setProtocol(6);
    // acl.setDestIpv4("12.0.0.7/32");
    List<AclMatchCriteria> list_acl = new ArrayList<AclMatchCriteria>();
    list_acl.add(acl);
    sfcc_dict.setAclMatchCriteria(list_acl);
    classifier_test.Create_SFC_Classifer(sfcc_dict, instance_id);

    // Chain 2

            SFCdict sfc_test_2 = new SFCdict();
            SfcDict sfc_dict_test_2 = new SfcDict();

            sfc_dict_test_2.setName("Test_SFC-demo_test-2");
            List<String> chain_2 = new ArrayList<String>();
          //  chain_2.add("FW-SF");
            chain_2.add("http-SF");


            sfc_dict_test_2.setChain(chain_2);
            sfc_dict_test_2.setId("Test_SFC-demo:1111122141");

            sfc_dict_test_2.setInfraDriver("ODL");
            sfc_dict_test_2.setSymmetrical(false);
            sfc_dict_test_2.setStatus("create");
            sfc_dict_test_2.setTenantId(NC.getTenantID());//NC.getTenantID());
            HashMap<Integer, VNFdict> vnfdicts_2 = new HashMap<Integer, VNFdict>();
            vnfdicts_2.put(0, vnf1_test);
           // vnfdicts_2.put(1, vnf2_test);

            sfc_test_2.setSfcDict(sfc_dict_test_2);
            test.CreateSFC(sfc_test_2, vnfdicts_2);
            test.CreateSFs(vnfdicts_2);

            String instance_id_2 = test.CreateSFP(sfc_test_2, vnfdicts_2);

            System.out.println("INSTANCE ID 2: " + instance_id_2);
            SFC_Classifier classifier_test_2 = new SFC_Classifier();
            SFCCdict sfcc_dict_2 = new SFCCdict();
            sfcc_dict_2.setStatus("create");
            sfcc_dict_2.setTenantId(NC.getTenantID());
            sfcc_dict_2.setInfraDriver("netvirtsfc");
            sfcc_dict_2.setId("sfcc-demo-test:22222");
            sfcc_dict_2.setChain(sfc_dict_test_2.getId());
            sfcc_dict_2.setName("sfc-classifier-demo-2");
            AclMatchCriteria acl_2 = new AclMatchCriteria();
            acl_2.setDestPort(80);
            acl_2.setSrcPort(2002);
            // acl.setDestMAC("fa:16:3e:3c:5c:d3");
            acl_2.setProtocol(6);
            //acl_2.setDestIpv4("12.0.0.7/32");
            List<AclMatchCriteria> list_acl_2 = new ArrayList<AclMatchCriteria>();
            list_acl_2.add(acl_2);
            sfcc_dict_2.setAclMatchCriteria(list_acl_2);
            classifier_test_2.Create_SFC_Classifer(sfcc_dict_2, instance_id_2);
          //  sfcc_db.add(sfc_dict_test_2.getId(), instance_id_2, sfcc_dict_2.getName(),sfc_dict_test_2.getSymmetrical(),vnfdicts_2,sfc_test_2,sfcc_dict_2);


/*
   sfcc_db.add(
        sfc_dict_test.getId(),
        instance_id,
        sfcc_dict.getName(),
        sfc_dict_test.getSymmetrical(),
        vnfdicts,
        sfc_test,
        sfcc_dict);

    int size = sfcc_db.getAllSFCs().size();
    System.out.println("Size of Data base: " + size);

      HttpResponse result= classifier_test2.Delete_SFC_Classifier("sfc-classifier-demo-x");
            System.out.println("Delete Test_SFC Classifier :  " + result.getStatusLine().getStatusCode()  );
            HttpResponse sfc_result=test.DeleteSFC("Path-Test_SFC-demo_test", false);
            System.out.println("Delete Test_SFC   :  " + sfc_result.getStatusLine().getStatusCode() );
            //testing the neutron port client
        /*    NeutronClient NC=new NeutronClient();
            String nc_port=NC.getNeutronPortID("11.0.0.5");
            System.out.println("The NEUTRON PORT ID IS: "+nc_port);
    */
  }
}
