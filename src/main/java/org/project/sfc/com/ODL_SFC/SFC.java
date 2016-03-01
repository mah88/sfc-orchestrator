package org.project.sfc.com.ODL_SFC;

import org.project.sfc.com.SFCdict.SFCdict;
import org.project.sfc.com.SFCdict.SfcDict;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mah on 2/29/16.
 */
public class SFC {

    Opendaylight SFC = new Opendaylight();
    SFCdict sfc_dict=new SFCdict();
    SfcDict sfc_dict_test=new SfcDict();
    int counter=1;
    public String Create(List<String> chain, String chain_name, String tenant_id, boolean Symm){

        sfc_dict_test.setName(chain_name);
        sfc_dict_test.setChain(chain);
        sfc_dict_test.setId(chain_name+"-test"+counter);
        sfc_dict_test.setInfraDriver("ODL");
        sfc_dict_test.setSymmetrical(Symm);
        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId(tenant_id);
        sfc_dict.setSfcDict(sfc_dict_test);
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();

        //get info from openstack about each instantiated SF instance
        //if there are multiple of sfs have the same type, we have to select only one and put it inside the vnfdicts
        for(int x=0;x<chain.size();x++){



        }
        counter++;
        String instance_id= SFC.CreateSFC(sfc_dict, vnfdicts);
        return instance_id;
    }

}
