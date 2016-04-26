package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/11/16.
 */

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.openbaton_nfvo.configurations.NfvoConfiguration;
import org.project.sfc.com.openbaton_nfvo.openbaton.OpenbatonEvent;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigReader;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigurationBeans;
import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.catalogue.nfvo.EndpointType;
import org.openbaton.catalogue.nfvo.EventEndpoint;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by maa on 11.11.15.
 */
@Service
public class OpenbatonEventSubscription {

    private NFVORequestor requestor;
    private Logger logger;
    @Autowired private NfvoConfiguration configuration;
    @Autowired
    private SFCAllocator creator;
    @Autowired
    private Gson mapper;
    private List<String> eventIds;
    private Properties properties;

    @PostConstruct
    private void init() throws SDKException, IOException {

        this.logger = LoggerFactory.getLogger(this.getClass());
        this.properties=ConfigReader.readProperties();
/*
        if (configuration.isSecurity()){
             this.requestor = new NFVORequestor(configuration.getUsername(), configuration.getPassword(), configuration.getBaseURL(), configuration.getBasePort(), "1");
        }
        else{
            this.requestor = new NFVORequestor("","",configuration.getBaseURL(),configuration.getBasePort(),"1");
        }
*/
        this.requestor = new NFVORequestor(properties.getProperty("nfvo.username"),properties.getProperty("nfvo.password"),properties.getProperty("nfvo.baseURL"),properties.getProperty("nfvo.basePort"),"1");
        this.eventIds = new ArrayList<>();

        EventEndpoint eventEndpointCreation = new EventEndpoint();
        eventEndpointCreation.setType(EndpointType.RABBIT);
        eventEndpointCreation.setEvent(Action.INSTANTIATE_FINISH);
        eventEndpointCreation.setEndpoint(ConfigurationBeans.queueName_eventInstatiateFinish);
        eventEndpointCreation.setName("eventNSRCreated");
        eventEndpointCreation = requestor.getEventAgent().create(eventEndpointCreation);

        EventEndpoint eventEndpointDeletion = new EventEndpoint();
        eventEndpointDeletion.setType(EndpointType.RABBIT);
        eventEndpointDeletion.setEvent(Action.RELEASE_RESOURCES_FINISH);
        eventEndpointDeletion.setEndpoint(ConfigurationBeans.queueName_eventResourcesReleaseFinish);
        eventEndpointDeletion.setName("eventNSRCreated");
        eventEndpointDeletion = requestor.getEventAgent().create(eventEndpointDeletion);

        this.eventIds.add(eventEndpointCreation.getId());
        this.eventIds.add(eventEndpointDeletion.getId());



    }

    public void receiveNewNsr(String message) {
        logger.debug("received new event " + message);
        OpenbatonEvent evt;

        try {
            logger.debug("Trying to deserialize it");
            evt = mapper.fromJson(message, OpenbatonEvent.class);
        } catch (JsonParseException e) {
            if (logger.isDebugEnabled() || logger.isTraceEnabled())
                logger.warn("Error in payload, expected NSR ", e);
            else
                logger.warn("Error in payload, expected NSR " + e.getMessage());
            return;
        }

        logger.info("[OPENBATON-EVENT-SUBSCRIPTION] received new NSR " + evt.getPayload().getId() + "for slice allocation at time " + new Date().getTime());

        logger.debug("Deserialized!!!");
        logger.debug("ACTION: " + evt.getAction() + " PAYLOAD: " + evt.getPayload().toString());
        NetworkServiceRecord nsr = evt.getPayload();
        logger.debug("NSR is " + nsr.toString());
        List<VirtualNetworkFunctionRecord> list_vnfrs=new ArrayList<>();

        //vnfrloop:
        for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
            logger.debug("VNFR: " + vnfr.toString());
            logger.info("[OPENBATON-EVENT-SUBSCRIPTION] sending the NSR " + evt.getPayload().getId() + "for SFC allocation to nsr handler at time " + new Date().getTime());
            list_vnfrs.add(vnfr);
           // break vnfrloop;
        }
      /*  if(nsr.getVnffgr()!=null){
            creator.addSFtoChain(nsr.getVnffgr(), nsr);

        }else {
            logger.error(">>>>> VNF FG is EMPTY !!!!");

        }*/

        creator.addSFtoChain(nsr.getVnfr(), nsr);

        logger.info("[OPENBATON-EVENT-SUBSCRIPTION] Ended message callback function at " + new Date().getTime());
    }

    public void deleteNsr(String message){

        logger.debug("received new event " + message);
        OpenbatonEvent evt;

        try {
            logger.debug("Trying to deserialize it");
            evt = mapper.fromJson(message, OpenbatonEvent.class);
        } catch (JsonParseException e) {
            if (logger.isDebugEnabled() || logger.isTraceEnabled())
                logger.warn("Error in payload, expected NSR ", e);
            else
                logger.warn("Error in payload, expected NSR " + e.getMessage());
            return;
        }


        logger.info("[OPENBATON-EVENT-SUBSCRIPTION] received new NSR " + evt.getPayload().getId() + "for SFC removal at time " + new Date().getTime());
        logger.debug("Deserialized!!!");
        logger.debug("ACTION: " + evt.getAction() + " PAYLOAD " + evt.getPayload().toString());
        NetworkServiceRecord nsr = evt.getPayload();
        creator.removeSFC(nsr.getId());

    }

    @PreDestroy
    private void dispose() throws SDKException {
        for (String id : this.eventIds) {
            requestor.getEventAgent().delete(id);
        }
    }
}
