package org.project.sfc.com.SfcRepository;

import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mah on 2/20/17.
 */

public interface SFCCdictRepo extends CrudRepository<SFCCdict, String> {
  SFCCdict findFirstById(String id);

}
