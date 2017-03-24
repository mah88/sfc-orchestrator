package org.project.sfc.com.SfcRepository.SfcManagement;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcRepository.SFCCdictRepo;
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
public class SfccManagement implements  SfccManagementInterface {

  private final Logger log = LoggerFactory.getLogger(this.getClass());


  @Autowired  SFCCdictRepo sfccRepository;

  @Override
  @Transactional
  public SFCCdict add(SFCCdict SFCC){
    log.info("Creating SFC Classifer " + SFCC.getName() );
    //Define Network if values are null or empty
    if (SFCC.getName()  == null || SFCC.getName().isEmpty())
      SFCC.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfccRepository.save(SFCC);
    //Add network to VimInstance
    log.info("Created SFC Classifier " + SFCC);
    log.debug("Classifier details: " + SFCC);
    return SFCC;
  }

  @Override
  @Transactional
  public void delete(SFCCdict SFCC) {
    log.info("Deleting SFC Classifer " + SFCC.getName() );

    sfccRepository.delete(SFCC);
  }

  @Override
  @Transactional
  public SFCCdict update(SFCCdict SFCC){

    sfccRepository.delete(SFCC.getId());
    SFCCdict newSFCC=add(SFCC);

    return newSFCC;

  }

  @Override
  @Transactional
  public   Iterable<SFCCdict> query(){

    return sfccRepository.findAll();
  }

  @Override
  @Transactional
  public   SFCCdict query(String id){
    return sfccRepository.findFirstById(id);

  }
}
