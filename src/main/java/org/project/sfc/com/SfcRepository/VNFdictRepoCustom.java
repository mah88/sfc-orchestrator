package org.project.sfc.com.SfcRepository;

import org.project.sfc.com.SfcModel.SFCdict.VNFdict;

import java.util.List;

/**
 * Created by mah on 3/21/17.
 */
public interface VNFdictRepoCustom {

  //  This operation allows adding new VNF in the VNF repository. */
  VNFdict add(VNFdict vnfDict);

  /** This operation allows deleting an existing VNF from the VNF repository. */
  void remove(VNFdict vnfDict);

  /** This operation allows updating the VNF  in the VNF repository. */
  VNFdict update(VNFdict new_vnf);

  /**
   * This operation allows querying the information of the VNF in the VNF repository.
   */
  Iterable<VNFdict> query();

  /**
   * This operation allows querying the information of VNF in the VNF repository.
   */
  VNFdict query(String id);

  VNFdict findbyName(String name);

  List<VNFdict> findbyType(String type);

  List<VNFdict> findAllbyType(String type);
}
