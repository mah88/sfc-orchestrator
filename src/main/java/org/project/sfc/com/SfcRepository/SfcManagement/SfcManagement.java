package org.project.sfc.com.SfcRepository.SfcManagement;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.project.sfc.com.SfcRepository.SFCdictRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mah on 2/20/17.
 */


@Transactional(readOnly = true)
public class SfcManagement implements SfcManagementInterface {

  private final Logger log = LoggerFactory.getLogger(this.getClass());


  @Autowired  SFCdictRepo sfcRepository;

  @Override
  @Transactional
  public SfcDict add(SfcDict sfcDict){
    log.info("Creating SFC " + sfcDict.getName() + " on Infra Driver" + sfcDict.getInfraDriver());
    //Define Network if values are null or empty
    if (sfcDict.getName()  == null || sfcDict.getName().isEmpty())
      sfcDict.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfcRepository.save(sfcDict);
    //Add network to VimInstance
    log.info("Created SFC " + sfcDict);
    log.debug("Chain details: " + sfcDict);
    return sfcDict;
  }

  @Override
  @Transactional
  public void delete(SfcDict sfc) {
    log.info("Deleted SFC " + sfc.getName());

    sfcRepository.delete(sfc);
  }

  @Override
  @Transactional
  public SfcDict update(SfcDict new_sfc){

    sfcRepository.delete(new_sfc.getId());
    SfcDict newSFC=add(new_sfc);

    return newSFC;

  }

  @Override
  @Transactional
  public   Iterable<SfcDict> query(){

    return sfcRepository.findAll();
  }

  @Override
  @Transactional
  public   SfcDict query(String id){
    return sfcRepository.findFirstById(id);

  }



}

