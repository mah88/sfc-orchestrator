package org.project.sfc.com.SfcRepository.SfcManagement;

import org.project.sfc.com.SfcModel.SFCdict.SFPdict;

/**
 * Created by mah on 3/21/17.
 */
public interface SfpManagementInterface {

  //  This operation allows adding new SFP in the SFP repository. */
  SFPdict add(SFPdict sfp) ;

  /** This operation allows deleting an existing SFP from the SFP repository. */
  void delete(SFPdict sfp) ;

  /** This operation allows updating the SFP  in the SFP repository. */
  SFPdict update(SFPdict new_sfp) ;

  /**
   * This operation allows querying the information of the SFP in the SFP repository.
   */
  Iterable<SFPdict> query();

  /**
   * This operation allows querying the information of SFP in the SFP repository.
   */
  SFPdict query(String id);
}
