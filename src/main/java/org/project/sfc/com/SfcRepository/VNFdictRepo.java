package org.project.sfc.com.SfcRepository;

/**
 * Created by mah on 2/20/17.
 */
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VNFdictRepo extends CrudRepository<VNFdict, String>, VNFdictRepoCustom {
  VNFdict findFirstById(String id);
}
