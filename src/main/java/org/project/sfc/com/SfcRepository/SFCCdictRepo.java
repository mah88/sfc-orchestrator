package org.project.sfc.com.SfcRepository;

import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mah on 2/20/17.
 */
@Repository
public interface SFCCdictRepo extends CrudRepository<SFCCdict, String>, SFCCdictRepoCustom {
  SFCCdict findFirstById(String id);
}
