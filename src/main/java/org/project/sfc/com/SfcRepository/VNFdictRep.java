package org.project.sfc.com.SfcRepository;

/**
 * Created by mah on 2/20/17.
 */
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.springframework.data.repository.CrudRepository;

public interface VNFdictRep extends CrudRepository<VNFdict, String> {
  VNFdict findFirstById(String id);
}




