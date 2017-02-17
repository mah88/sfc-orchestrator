package org.project.sfc.com.SfcInterfaces;

import org.apache.http.HttpResponse;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.springframework.http.ResponseEntity;

/**
 * Created by mah on 6/10/16.
 */
public interface SfcClassifierInter {

  String Create_SFC_Classifer(SFCCdict sfcc_dict, String Chain_instance_id);

  ResponseEntity<String> Delete_SFC_Classifier(String classifier_name);
}
