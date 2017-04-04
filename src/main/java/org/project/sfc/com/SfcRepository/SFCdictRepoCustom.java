package org.project.sfc.com.SfcRepository;

import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;

/**
 * Created by mah on 2/20/17.
 */
public interface SFCdictRepoCustom {

  //  This operation allows adding new SFC in the SFC repository. */
  SfcDict add(SfcDict add);

  /** This operation allows deleting an existing SFC from the SFC repository. */
  void remove(SfcDict sfc);

  /** This operation allows updating the SFC  in the SFC repository. */
  SfcDict update(SfcDict new_sfc);

  /**
   * This operation allows querying the information of the SFC in the SFC repository.
   */
  Iterable<SfcDict> query();

  /**
   * This operation allows querying the information of SFC in the SFC repository.
   */
  SfcDict query(String id);
}
