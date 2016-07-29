package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */


        import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
        import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
        import org.project.sfc.com.SfcDriver.SfcDriverCaller;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.util.*;

/**
 * Created by maa on 18.01.16.
 */
public class AddSFCExecutor implements Runnable{

  //private SFCcreator sfchandler;
  private SfcDriverCaller sfcCaller;

    private Logger logger;
   private Set<VirtualNetworkFunctionRecord> vnfrs;

    private NetworkServiceRecord nsr;

   public AddSFCExecutor(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr)  {
   // public AddSFCExecutor(Set<VNFForwardingGraphRecord> vnfrs, NetworkServiceRecord nsr) {

       this.sfcCaller=new SfcDriverCaller("opendaylight");
        this.vnfrs = vnfrs;
        this.nsr = nsr;

        this.logger = LoggerFactory.getLogger(this.getClass());
    }
/*
    public AddSFCExecutor(Set<VirtualNetworkFunctionRecord> vnfrs,VNFForwardingGraphRecord vnffgr, NetworkServiceRecord nsr) {
        // public AddSFCExecutor(Set<VNFForwardingGraphRecord> vnfrs, NetworkServiceRecord nsr) {

        this.sfchandler=new SFCcreator();
        this.vnfrs = vnfrs;
        this.nsr = nsr;
        this.vnffgr=vnffgr;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
*/
    @Override
    public void run() {

        logger.info("[ADD-SFC-EXECUTOR] allocating slice for " + nsr.getId() + " at time " + new Date().getTime());
        logger.debug("Received vnfrs with --");

      /*  FlowAllocation flows = this.getSFlows(vnfrs);
        logger.debug("adding flows for " + flows.toString()); //print list@id
        List<QoSAllocation> qoses = this.getQoses(vnfrs);
        logger.debug("adding qoses for " + qoses.toString());
*/
      //  boolean response = sfchandler.Create(vnfrs,nsr);
        boolean response=sfcCaller.Create(vnfrs,nsr);
        logger.debug("RESPONSE from Handler " + response);
        logger.info("[ADD-SFC-EXECUTOR] ended SFC allocation for " + nsr.getId() + " at time " + new Date().getTime());

    }




}