package org.project.sfc.com.SfcRepository.SfcManagement;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.SfcRepository.VNFdictRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mah on 3/21/17.
 */

@Transactional(readOnly = true)
public class VnfManagement implements  VnfManagementInterface{


  private final Logger log = LoggerFactory.getLogger(this.getClass());


  @Autowired  VNFdictRep sfRepository;

  @Override
  @Transactional
  public VNFdict add(VNFdict vnfDict){
    log.info("Creating SF : " + vnfDict);
    //Define Network if values are null or empty
    if (vnfDict.getName()  == null || vnfDict.getName().isEmpty())
      vnfDict.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfRepository.save(vnfDict);
    //Add network to VimInstance
    log.info("Created SFC " + vnfDict.getName());
    log.debug("Chain details: " + vnfDict);
    return vnfDict;
  }

  @Override
  @Transactional
  public void delete(VNFdict sf) {
    log.info("Deleting SF :" + sf);

    sfRepository.delete(sf);
  }

  @Override
  @Transactional
  public VNFdict update(VNFdict new_sf){

    sfRepository.delete(new_sf.getId());
    VNFdict newSF=add(new_sf);

    return newSF;

  }

  @Override
  @Transactional
  public   Iterable<VNFdict> query(){

    return sfRepository.findAll();
  }

  @Override
  @Transactional
  public   VNFdict query(String id){

    if(sfRepository.exists(id)) {
      log.info(
          "[Query VNF] the SFC DB has this VNF instance with ID = " + id + " ?  "+sfRepository.findFirstById(id));
      return sfRepository.findFirstById(id);
    }else {
      return null;
    }

  }
}
