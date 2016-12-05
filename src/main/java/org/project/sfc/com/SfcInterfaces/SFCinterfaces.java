package org.project.sfc.com.SfcInterfaces;

import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by mah on 6/10/16.
 */
public interface SFCinterfaces {
  String interfaceVersion = "1.0";

  void CreateSFC(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict);

  void CreateSFs(HashMap<Integer, VNFdict> vnf_dict);

  String CreateSFP(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict);

  ResponseEntity<String> DeleteSFC(String instance_id, boolean isSymmetric);

  ResponseEntity<String> DeleteSFP(String instance_id, boolean isSymmetric);

  String GetBytesCount(SFCCdict SFCC_dict);

  }
