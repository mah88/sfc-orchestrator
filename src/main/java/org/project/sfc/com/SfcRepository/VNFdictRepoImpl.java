package org.project.sfc.com.SfcRepository;

import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcModel.SFCdict.Status;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mah on 3/21/17.
 */
@Transactional(readOnly = true)
public class VNFdictRepoImpl implements VNFdictRepoCustom {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired private VNFdictRepo sfRepository;

  @Override
  @Transactional
  public VNFdict add(VNFdict vnfDict) {
    log.debug("Creating SF : " + vnfDict.getName());
    //Define Network if values are null or empty
    if (vnfDict.getName() == null || vnfDict.getName().isEmpty())
      vnfDict.setName(IdGenerator.createUUID());

    //Create Network in NetworkRepository
    sfRepository.save(vnfDict);
    //Add network to VimInstance
    log.debug("Created SF details: " + vnfDict);
    return vnfDict;
  }

  @Override
  @Transactional
  public void remove(VNFdict sf) {
    log.debug("Deleting SF :" + sf);

    sfRepository.delete(sf);
  }

  @Override
  @Transactional
  public VNFdict update(VNFdict new_sf) {
    log.debug("Updating SF : " + new_sf.getName());

    VNFdict newSF = sfRepository.save(new_sf);
    log.debug("Updated SF details: " + new_sf);

    return newSF;
  }

  @Override
  @Transactional
  public Iterable<VNFdict> query() {

    return sfRepository.findAll();
  }

  @Override
  @Transactional
  public VNFdict query(String id) {

    if (sfRepository.exists(id)) {
      log.debug(
          "[Query VNF] the SFC DB has this VNF instance with ID = "
              + id
              + " ?  "
              + sfRepository.findFirstById(id));
      return sfRepository.findFirstById(id);
    } else {
      return null;
    }
  }

  @Override
  @Transactional
  public VNFdict findbyName(String name) {
    Iterable<VNFdict> vnfs = sfRepository.findAll();
    VNFdict found_VNF = null;

    for (VNFdict vnf : vnfs) {
      log.debug("Finding the  VNF NAME: " + name);
      log.debug("vnf name: " + vnf.getName());

      if (vnf.getName().equals(name)) {
        found_VNF = vnf;
        log.debug("found the SF : " + vnf.getName());

        break;
      }
    }

    log.debug("return the SF : " + found_VNF);

    return found_VNF;
  }

  @Override
  @Transactional
  public List<VNFdict> findbyType(String type) {
    Iterable<VNFdict> vnfs = sfRepository.findAll();
    boolean found_VNF = false;
    List<VNFdict> vnfsType = new ArrayList<>();

    for (VNFdict vnf : vnfs) {
      log.debug("Finding the  VNF type: " + type);
      log.debug("vnf name: " + vnf.getName());

      if (vnf.getType().equals(type) && vnf.getStatus() != Status.INACTIVE) {
        log.debug("add the SF instance: " + vnf.getName());
        vnfsType.add(vnf);
        found_VNF = true;
      }
    }
    if (found_VNF == true) {
      return vnfsType;

    } else {
      return null;
    }
  }

  @Override
  @Transactional
  public List<VNFdict> findAllbyType(String type) {
    Iterable<VNFdict> vnfs = sfRepository.findAll();
    boolean found_VNF = false;
    List<VNFdict> vnfsType = new ArrayList<>();

    for (VNFdict vnf : vnfs) {
      log.debug("Finding the  VNF NAME: " + type);
      log.debug("vnf name: " + vnf.getName());

      if (vnf.getType().equals(type)) {
        log.debug("add the SF instance: " + vnf.getName());
        vnfsType.add(vnf);
        found_VNF = true;
      }
    }
    if (found_VNF == true) {
      return vnfsType;

    } else {
      return null;
    }
  }
}
