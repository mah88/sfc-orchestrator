package org.project.sfc.com.SfcRepository;

import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mah on 2/20/17.
 */
@Repository
public interface SFCdictRepo extends CrudRepository<SfcDict, String>, SFCdictRepoCustom {
  SfcDict findFirstById(String id);
}
