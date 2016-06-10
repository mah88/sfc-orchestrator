package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */



        import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
        import org.project.sfc.com.SfcDriver.SfcDriverCaller;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.util.Date;
        import java.util.Set;

/**
 * Created by maa on 18.01.16.
 */
public class RemoveSFCExecutor implements Runnable{
   // private SFCcreator sfchandler;
     private SfcDriverCaller sfcCaller;
    private Logger logger;
    private Set<VirtualNetworkFunctionRecord> vnfrs;
    private String nsrID;

    public RemoveSFCExecutor(String nsrID)  {
        //sfchandler=new SFCcreator();
        this.sfcCaller=new SfcDriverCaller("opendaylight");
        this.vnfrs = vnfrs;
        this.nsrID = nsrID;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }


    @Override
    public void run() {
        logger.info("[REMOVE-SFC-EXECUTOR] deleting SFC for " + nsrID + " at time " + new Date().getTime());
        logger.debug("remmoving SFC for nsr " + nsrID + " with vnfrs: " + vnfrs);
       // boolean response = sfchandler.Delete(nsrID);
        boolean response=sfcCaller.Delete(nsrID);
        logger.debug("Response from handler " + response);
        logger.info("[REMOVE-SFC-EXECUTOR] ended SFC removal for " + nsrID + " at time " + new Date().getTime());
    }


}