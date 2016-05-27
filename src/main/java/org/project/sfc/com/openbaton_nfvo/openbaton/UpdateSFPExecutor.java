package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */


import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcHandler.SFCcreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Set;

/**
 * Created by maa on 18.01.16.
 */
public class UpdateSFPExecutor implements Runnable{

  private SFCcreator sfchandler;
    private Logger logger;
   private VirtualNetworkFunctionRecord vnfr;


   public UpdateSFPExecutor(VirtualNetworkFunctionRecord vnfr) {
   // public AddSFCExecutor(Set<VNFForwardingGraphRecord> vnfrs, NetworkServiceRecord nsr) {

        this.sfchandler=new SFCcreator();
        this.vnfr = vnfr;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void run() {

        logger.info("[Update-SFP] for NSR " + vnfr.getParent_ns_id() + " at time " + new Date().getTime());
        logger.info("[Update-SFP] vnfr need to be adjust its VNFC instance: " + vnfr + " at time " + new Date().getTime());

      /*  FlowAllocation flows = this.getSFlows(vnfrs);
        logger.debug("adding flows for " + flows.toString()); //print list@id
        List<QoSAllocation> qoses = this.getQoses(vnfrs);
        logger.debug("adding qoses for " + qoses.toString());
*/
        sfchandler.UpdateChainsPaths(vnfr);
       // logger.debug("RESPONSE from Handler " + response);
        logger.info("[Update-SFP-ECUTOREX] ended SFC allocation for " + vnfr.getParent_ns_id() + " at time " + new Date().getTime());

    }




}