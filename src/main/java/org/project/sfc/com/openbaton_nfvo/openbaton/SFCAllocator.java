package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */



        import org.openbaton.catalogue.mano.common.Ip;
        import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
        import org.openbaton.catalogue.mano.descriptor.VNFDConnectionPoint;
        import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
        import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
        import org.openbaton.catalogue.mano.record.VNFCInstance;
        import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import javax.annotation.PostConstruct;
        import java.util.*;
        import java.util.concurrent.*;

/**
 * Created by maa on 02.12.15.
 */
@Service
public class SFCAllocator {

    private final ScheduledExecutorService qtScheduler = Executors.newScheduledThreadPool(1);
    private Logger logger;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void addSFtoChain(Set<VirtualNetworkFunctionRecord> vnfrs,NetworkServiceRecord nsrId){
        logger.info("[SFC-ALLOCATOR] received new set of vnfrs for " + nsrId + " to create a SFC at time " + new Date().getTime());
        logger.debug("Creating ADD Thread");
        AddSFCExecutor aqe = new AddSFCExecutor(vnfrs,nsrId);
        qtScheduler.schedule(aqe,100, TimeUnit.MILLISECONDS);
        logger.info("[SFC-ALLOCATOR] scheduled thread to handle the NSR" + nsrId + " to create a SFC at time " + new Date().getTime());
        logger.debug("ADD Thread created and scheduled");
    }

    public void removeSFC(String nsrId){
        logger.info("[SFC-ALLOCATOR] received new set of vnfrs for " + nsrId + " to remove a SFC at time " + new Date().getTime());
        logger.debug("Creating REMOVE Thread");
        RemoveSFCExecutor rqe = new RemoveSFCExecutor(nsrId);
        qtScheduler.schedule(rqe,10,TimeUnit.SECONDS);
        logger.info("[QOS-ALLOCATOR] scheduled thread to handle the NSR" + nsrId + " to remove a SFC at time " + new Date().getTime());
        logger.debug("REMOVE Thread created and scheduled");

    }

}