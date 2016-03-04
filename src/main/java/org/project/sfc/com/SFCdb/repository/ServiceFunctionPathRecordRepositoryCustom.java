package org.project.sfc.com.SFCdb.repository;

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;


/**
 * Created by mah on 3/4/16.
 */

public interface ServiceFunctionPathRecordRepositoryCustom {

    VirtualNetworkFunctionRecord addVnfr(VirtualNetworkFunctionRecord vnfr, String id);

    void deleteVNFRecord(String idSfp, String idVnfd);


}