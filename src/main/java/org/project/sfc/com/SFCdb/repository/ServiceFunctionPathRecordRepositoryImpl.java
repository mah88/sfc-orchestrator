package org.project.sfc.com.SFCdb.repository;

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mah on 3/4/16.
 */

@Transactional(readOnly = true)
public class ServiceFunctionPathRecordRepositoryImpl implements ServiceFunctionPathRecordRepositoryCustom {

    @Autowired
    private ServiceFunctionPathRecordRepository sfpRecordRepository;

    @Autowired
    private VNFRRepository vnfrRepository;

    @Override
    @Transactional
    public VirtualNetworkFunctionRecord addVnfr(VirtualNetworkFunctionRecord vnfr, String id) {
        vnfr = vnfrRepository.save(vnfr);
        sfpRecordRepository.findFirstById(id).getVnfr().add(vnfr);
        return vnfr;
    }

    @Override
    @Transactional
    public void deleteVNFRecord(String idSfp, String idVnfd) {
        sfpRecordRepository.findFirstById(idSfp).getVnfr().remove(vnfrRepository.findOne(idVnfd));
        vnfrRepository.delete(idVnfd);
    }




}