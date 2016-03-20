package org.project.sfc.com.SfcHandler;

import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SFCdict;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SfcDict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.VNFdict;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mah on 2/29/16.
 */
public class SFC {

    HashMap<String,String> SFC_MAP=new HashMap<>();
    public void add(String nsr_id, String chain_id){
        SFC_MAP.put(nsr_id,chain_id);

    }

    public void remove(String nsr){
        SFC_MAP.remove(nsr);
    }

    public void update(String nsr_id,String new_chain_id){
        SFC_MAP.remove(nsr_id);
        SFC_MAP.put(nsr_id,new_chain_id);
    }

    public String get(String nsr_id){
        String chain_id=SFC_MAP.get(nsr_id);
        return chain_id;
    }

}
