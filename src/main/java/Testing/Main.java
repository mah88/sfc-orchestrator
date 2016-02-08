package Testing;

import org.project.sfc.com.ODL_SFC.Opendaylight;
import org.project.sfc.com.ODL_SFC.VNFdict;
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
        VNFdict vnf2_test = new VNFdict();

        vnf1_test.setIP("10.0.0.7");
        vnf2_test.setIP("10.0.0.7");
        vnf1_test.setName("FW-1");
        vnf2_test.setName("DPI-1");
        vnf1_test.setNeutronPortId("11111111111");
        vnf2_test.setNeutronPortId("22222222222");
        vnf1_test.setType("FW");
        vnf1_test.setType("DPI");

        SFCdict sfc_test = new SFCdict();
        SfcDict sfc_dict_test=new SfcDict();

        sfc_dict_test.setName("SFC TEST");
        List<String> chain = new ArrayList<String>();
        chain.add("FW-1");
        chain.add("DPI-1");
        sfc_dict_test.setChain(chain);
        sfc_dict_test.setId("SFC-test1");

        sfc_dict_test.setInfraDriver("ODL");
        sfc_dict_test.setSymmetrical(false);
        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId("1111122222");
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
        vnfdicts.put(0, vnf1_test);
        vnfdicts.put(1, vnf2_test);
        sfc_test.setSfcDict(sfc_dict_test);

        test.CreateSFC(sfc_test, vnfdicts);
    }
}
