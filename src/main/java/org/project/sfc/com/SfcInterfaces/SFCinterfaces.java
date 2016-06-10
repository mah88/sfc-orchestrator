package org.project.sfc.com.SfcInterfaces;

import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by mah on 6/10/16.
 */
public interface SFCinterfaces {
    String interfaceVersion="1.0";
    void CreateSFC(SFCdict sfc_dict, HashMap<Integer,VNFdict> vnf_dict);


    String CreateSFP(SFCdict sfc_dict, HashMap<Integer,VNFdict> vnf_dict);

    ResponseEntity<String> DeleteSFC(String instance_id, boolean isSymmetric);

    ResponseEntity<String> DeleteSFP(String instance_id,boolean isSymmetric);



    // boolean DeletePath(String SFC_driver,String Chain_ID);
    //  SFCdict getChain(String SFC_driver,String Chain_ID);
    //  List<SFPdict> getPaths(String SFC_driver,String Chain_id);
    //  SFPdict getPath(String SFC_driver,String Chain_id,String Path_id);

}
