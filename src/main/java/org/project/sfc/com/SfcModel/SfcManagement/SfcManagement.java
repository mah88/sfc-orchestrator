package org.project.sfc.com.SfcModel.SfcManagement;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
//import org.project.sfc.com.SfcRepository.SFCdictRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by mah on 2/20/17.
 */

/*
@Service
@Scope
public class SfcManagement implements  org.project.sfc.com.SfcModel.SfcManagement.SfcManagementInterface {

  private final Logger log = LoggerFactory.getLogger(this.getClass());


  @Autowired private SFCdictRepo sfcRepository;

  @Override
  public SFCdict add(SFCdict sfcDict){
    log.info("Creating SFC " + sfcDict.getSfcDict().getName() + " on Infra Driver" + sfcDict.getSfcDict().getInfraDriver());
    //Define Network if values are null or empty
    if (sfcDict.getSfcDict().getName()  == null || sfcDict.getSfcDict().getName().isEmpty())
      sfcDict.getSfcDict().setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfcRepository.save(sfcDict);
    //Add network to VimInstance
    log.info("Created SFC " + sfcDict.getSfcDict().getName());
    log.debug("Network details: " + sfcDict);
    return sfcDict;
  }

  @Override
  public void delete(SFCdict sfc) {
    sfcRepository.delete(sfc);
  }

  @Override
  public SFCdict update(SFCdict new_sfc){

    sfcRepository.delete(new_sfc.getSfcDict().getId());
    SFCdict newSFC=add(new_sfc);

    return newSFC;

  }

  @Override
  public   Iterable<SFCdict> query(){

    return sfcRepository.findAll();
  }

  @Override
  public   SFCdict query(String id){
    return sfcRepository.findFirstById(id);

  }



}
*/