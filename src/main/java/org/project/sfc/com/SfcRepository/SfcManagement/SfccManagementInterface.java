package org.project.sfc.com.SfcRepository.SfcManagement;

import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;

/**
 * Created by mah on 3/21/17.
 */
public interface SfccManagementInterface {

  //  This operation allows adding new SFC Classifier in the SFC Classifier repository. */
  SFCCdict add(SFCCdict add) ;

  /** This operation allows deleting an existing SFC Classifier from the SFC Classifier repository. */
  void delete(SFCCdict sfc) ;

  /** This operation allows updating the SFC Classifier  in the SFC Classifier repository. */
  SFCCdict update(SFCCdict new_sfc) ;

  /**
   * This operation allows querying the information of the SFC Classifier in the SFC Classifier repository.
   */
  Iterable<SFCCdict> query();

  /**
   * This operation allows querying the information of SFC Classifier in the SFC Classifier repository.
   */
  SFCCdict query(String id);
}
