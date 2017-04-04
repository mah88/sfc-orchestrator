package org.project.sfc.com.SfcRepository;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCdict.SFPdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mah on 3/21/17.
 */
@Transactional(readOnly = true)
public class SFPdictRepoImpl implements SFPdictRepoCustom {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired private SFPdictRepo sfpRepository;

  @Override
  @Transactional
  public SFPdict add(SFPdict sfpDict) {
    log.debug("Creating SFP :" + sfpDict.getName());
    //Define Network if values are null or empty
    if (sfpDict.getName() == null || sfpDict.getName().isEmpty())
      sfpDict.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfpRepository.save(sfpDict);
    //Add network to VimInstance
    log.debug("Created Path details: " + sfpDict);
    return sfpDict;
  }

  @Override
  @Transactional
  public void remove(SFPdict sfp) {
    log.debug("Delete SFP :" + sfp.getName());

    sfpRepository.delete(sfp);
    log.debug("Deleted SFP ");
  }

  @Override
  @Transactional
  public SFPdict update(SFPdict new_sfp) {
    log.debug("[ Updating SFP ] " + new_sfp.getName());

    SFPdict newSFP = sfpRepository.save(new_sfp);
    log.info("[ Updated SFP ] " + new_sfp);

    return newSFP;
  }

  @Override
  @Transactional
  public Iterable<SFPdict> query() {

    return sfpRepository.findAll();
  }

  @Override
  @Transactional
  public SFPdict query(String id) {
    return sfpRepository.findFirstById(id);
  }
}
