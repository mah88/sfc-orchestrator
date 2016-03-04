package org.project.sfc.com.SFCdb.RecordManagementInterfaces;

/**
 * Created by mah on 3/4/16.
 */
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VNFComponent;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.messages.Interfaces.NFVMessage;
import org.project.sfc.com.SFCdb.catalogue.ServiceFunctionChainRecord;

import java.util.concurrent.ExecutionException;

/**
 * Created by mpa on 30/04/15.
 */

public interface SFCRmanInterface {

    /**
     * This operation allows submitting and
     * validating a Network Service	Descriptor (NSD),
     * including any related VNFFGD and VLD.
     */
    NetworkServiceRecord onboard(String nsd_id) throws InterruptedException, ExecutionException, VimException, NotFoundException, BadFormatException, VimDriverException, QuotaExceededException;

    /**
     * This operation allows submitting and
     * validating a Network Service	Descriptor (NSD),
     * including any related VNFFGD and VLD.
     */
    NetworkServiceRecord onboard(NetworkServiceDescriptor networkServiceDescriptor) throws ExecutionException, InterruptedException, VimException, NotFoundException, NotFoundException, BadFormatException, VimDriverException, QuotaExceededException;

    /**
     * This operation allows updating a Network
     * Service Descriptor (NSD), including any
     * related VNFFGD and VLD.This update might
     * include creating/deleting new VNFFGDs
     * and/or new VLDs.
     *
     * @param new_nsd
     * @param old_id
     */
    NetworkServiceRecord update(NetworkServiceRecord new_nsd, String old_id);

    /**
     * This operation is used to query the
     * information of the Network Service
     * Descriptor (NSD), including any
     * related VNFFGD and VLD.
     */
    Iterable<ServiceFunctionChainRecord> query();

    void executeAction(NFVMessage nfvMessage,String nsrId,String idVnf,String idVdu, String idVNFCI) ;

    NetworkServiceRecord query(String id);

    /**
     * This operation is used to remove a
     * disabled Network Service Descriptor.
     *
     * @param id
     */
    void delete(String id) throws  ExecutionException, InterruptedException;


}
