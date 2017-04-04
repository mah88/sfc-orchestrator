package org.project.sfc.com.SfcRepository;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mah on 3/21/17.
 */
@Transactional(readOnly = true)
public class SFCCdictRepoImpl implements SFCCdictRepoCustom {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired private SFCCdictRepo sfccRepository;

  @Override
  @Transactional
  public SFCCdict add(SFCCdict SFCC) {
    log.debug("Creating SFC Classifer " + SFCC.getName());
    //Define Network if values are null or empty
    if (SFCC.getName() == null || SFCC.getName().isEmpty()) SFCC.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfccRepository.save(SFCC);
    //Add network to VimInstance
    log.debug("Classifier details: " + SFCC);
    return SFCC;
  }

  @Override
  @Transactional
  public void remove(SFCCdict SFCC) {
    log.info("Deleting SFC Classifer " + SFCC.getName());

    sfccRepository.delete(SFCC);
    log.info("Deleted SFC Classifer ");
  }

  @Override
  @Transactional
  public SFCCdict update(SFCCdict SFCC) {
    log.debug("Updating SFC Classifer " + SFCC.getName());

    SFCCdict newSFCC = sfccRepository.save(SFCC);

    log.debug("Updated SFC Classifier " + SFCC);

    return newSFCC;
  }

  @Override
  @Transactional
  public Iterable<SFCCdict> query() {

    return sfccRepository.findAll();
  }

  @Override
  @Transactional
  public SFCCdict query(String id) {
    return sfccRepository.findFirstById(id);
  }
}
