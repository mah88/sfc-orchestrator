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
// A DB for NSR created and their relative classifiers

public class SFC {

    private static SFC instance;

    private SFC() {}

    public static SFC getInstance() {
        if (instance == null) {
            instance = new SFC();
        }
        return instance;
    }

    HashMap<String,SFC_Data> SFC_MAP=new HashMap<>();
    public void add(String nsr_id, String rsp_id, String sfcc_name){
        SFC_Data sfc_data=new SFC_Data();
        sfc_data.setRspID(rsp_id);
        sfc_data.setSfccName(sfcc_name);
        SFC_MAP.put(nsr_id,sfc_data);


    }

    public void remove(String nsr){
        SFC_MAP.remove(nsr);
    }

    public void update(String nsr_id,String new_rsp_id, String new_sfcc_name){
        SFC_MAP.remove(nsr_id);
        SFC_Data sfc_data=new SFC_Data();
        sfc_data.setRspID(new_rsp_id);
        sfc_data.setSfccName(new_sfcc_name);
        SFC_MAP.put(nsr_id,sfc_data);
    }

    public String getRspID(String nsr_id){
        String chain_id=SFC_MAP.get(nsr_id).getRspID();
        return chain_id;
    }
    public String getSfccName(String nsr_id){
        String sfcc_name=SFC_MAP.get(nsr_id).getSfccName();
        return sfcc_name;
    }
    public class SFC_Data{
        String rsp_id;
        String SFCC_name;

        public String getRspID() {
            return rsp_id;
        }

        /**
         *
         * @param rsp
         *     The rsp-id
         */
        public void setRspID(String rsp) {
            this.rsp_id = rsp;
        }

        public String getSfccName() {
            return SFCC_name;
        }


        public void setSfccName(String name) {
            this.SFCC_name = name;
        }

    }
}