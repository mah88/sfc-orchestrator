package org.project.sfc.com.SfcImpl.Test_SFC_driver;

import org.apache.http.HttpResponse;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFCJSON.SFCJSON;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.project.sfc.com.SfcInterfaces.SFC;

import java.util.HashMap;

/**
 * Created by mah on 1/13/17.
 */
@Service
@Scope(value = "prototype")
public class Test_SFC extends SFC {

  @Override
  public void CreateSFC(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict) {
    //create Test_SFC

    }
  @Override
  public void CreateSFs(HashMap<Integer, VNFdict> vnf_dict) {
    //create SFs
  }
  @Override
  public String CreateSFP(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict) {
    return "created_new_sfp";
  }
  @Override
  public String GetBytesCount(SFCCdict SFCC_dict) {
    return "Bytes_count";
  }

  @Override
  public ResponseEntity<String> DeleteSFC(String instance_id, boolean isSymmetric) {
  return null;
  }

  @Override
  public ResponseEntity<String> DeleteSFP(String instance_id, boolean isSymmetric) {
  return null;
  }

  @Override
  public String GetConnectedSFF(String SF_name) {
    return "SFF-x";
  }

  @Override
  public String GetHostID(String neutron_port_id){ return "OVS-181881182";}

}
