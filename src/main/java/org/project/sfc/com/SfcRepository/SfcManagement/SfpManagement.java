package org.project.sfc.com.SfcRepository.SfcManagement;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCdict.SFPdict;
import org.project.sfc.com.SfcRepository.SFPdictRepo;
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
public class SfpManagement implements SfpManagementInterface  {

  private final Logger log = LoggerFactory.getLogger(this.getClass());


  @Autowired  SFPdictRepo sfpRepository;

  @Override
  @Transactional
  public SFPdict add(SFPdict sfpDict){
    log.info("Creating SFP :" + sfpDict.getName() );
    //Define Network if values are null or empty
    if (sfpDict.getName()  == null || sfpDict.getName().isEmpty())
      sfpDict.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfpRepository.save(sfpDict);
    //Add network to VimInstance
    log.info("Created SFP " + sfpDict);
    log.debug("Path details: " + sfpDict);
    return sfpDict;
  }

  @Override
  @Transactional
  public void delete(SFPdict sfp) {
    log.info("Delete SFP :" + sfp.getName() );

    sfpRepository.delete(sfp);
  }

  @Override
  @Transactional
  public SFPdict update(SFPdict new_sfp){

    sfpRepository.delete(new_sfp.getId());
    SFPdict newSFP=add(new_sfp);

    return newSFP;

  }

  @Override
  @Transactional
  public   Iterable<SFPdict> query(){

    return sfpRepository.findAll();
  }

  @Override
  @Transactional
  public   SFPdict query(String id){
    return sfpRepository.findFirstById(id);

  }

}
