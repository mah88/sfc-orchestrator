package org.project.sfc.com.SfcRepository;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mah on 2/20/17.
 */
@Transactional(readOnly = true)
public class SFCdictRepoImpl implements SFCdictRepoCustom {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired private SFCdictRepo sfcRepository;

  @Override
  @Transactional
  public SfcDict add(SfcDict sfcDict) {
    log.debug("Creating SFC " + sfcDict.getName() + " on Infra Driver " + sfcDict.getInfraDriver());
    //Define Network if values are null or empty
    if (sfcDict.getName() == null || sfcDict.getName().isEmpty())
      sfcDict.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfcRepository.save(sfcDict);
    //Add network to VimInstance
    log.debug("Created Chain details: " + sfcDict);
    return sfcDict;
  }

  @Override
  @Transactional
  public void remove(SfcDict sfc) {
    log.debug("Delete SFC " + sfc.getName());

    sfcRepository.delete(sfc);
    log.debug("Deleted SFC ");
  }

  @Override
  @Transactional
  public SfcDict update(SfcDict new_sfc) {
    log.info("Update SFC " + new_sfc.getName());

    SfcDict newSFC = sfcRepository.save(new_sfc);
    log.info("Updated SFC : " + new_sfc);

    return newSFC;
  }

  @Override
  @Transactional
  public Iterable<SfcDict> query() {

    return sfcRepository.findAll();
  }

  @Override
  @Transactional
  public SfcDict query(String id) {
    return sfcRepository.findFirstById(id);
  }
}
