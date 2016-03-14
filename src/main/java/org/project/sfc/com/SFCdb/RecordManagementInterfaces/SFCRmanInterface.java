package org.project.sfc.com.SFCdb.RecordManagementInterfaces;

/**
 * Created by mah on 3/4/16.
 */

import org.project.sfc.com.SFCdb.catalogue.ServiceFunctionChainRecord;
import org.project.sfc.com.SFCdb.catalogue.Status;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SFCdict;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SfcDict;

import java.util.Set;

/**
 * Created by mpa on 30/04/15.
 */

public interface SFCRmanInterface {

    /**
     * This operation allows submitting and
     * validating a Network Service	Descriptor (NSD),
     * including any related VNFFGD and VLD.
     */
    ServiceFunctionChainRecord Create_SFC_pre(SFCdict sfcd);

    /**
     * This operation allows submitting and
     * validating a Network Service	Descriptor (NSD),
     * including any related VNFFGD and VLD.
     */
    SfcDict Create_SFC_(SFCdict sfcd);
    /**
     * This operation allows updating a Network
     * Service Descriptor (NSD), including any
     * related VNFFGD and VLD.This update might
     * include creating/deleting new VNFFGDs
     * and/or new VLDs.
     *
     */
    SfcDict create_device_pre(SFCdict sfcd);
    /**
     * This operation is used to query the
     * information of the Network Service
     * Descriptor (NSD), including any
     * related VNFFGD and VLD.
     */
    Iterable<ServiceFunctionChainRecord> SFCs_query();

    void Create_SFC_post(String sfc_id, String instance_id,SfcDict sfcd_) ;
    void Create_SFC_status(String sfc_id, Status new_status);
    ServiceFunctionChainRecord get_SFC_Record(String sfc_id, Set<Status> Current_status, Status new_status);
    SFCdict SFC_update(ServiceFunctionChainRecord newSFCR, String sfc_id);
    SFCdict SFC_update_pre( String sfc_id);
    void SFC_update_post( String sfc_id, Status new_status);
    ServiceFunctionChainRecord update(ServiceFunctionChainRecord newRsr, String idNsr);

    /**
     * This operation is used to remove a
     * disabled Network Service Descriptor.
     *
     * @param id
     */
    ServiceFunctionChainRecord SFC_query(String id);

}
