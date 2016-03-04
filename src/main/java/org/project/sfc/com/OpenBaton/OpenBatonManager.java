package org.project.sfc.com.OpenBaton;

/**
 * Created by mah on 2/25/16.
 */

import org.openbaton.catalogue.mano.common.VNFDeploymentFlavour;
import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.nfvo.*;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.sfc.com.OpenBaton.Configuration.NfvoProperties;
import org.project.sfc.com.OpenBaton.Messages.BuildingStatus;
import org.project.sfc.com.OpenBaton.Configuration.Flavor;
import org.project.sfc.com.OpenBaton.OpenBatonCreateServer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenBatonManager {

    @Autowired private VimInstance vimInstance;
    @Autowired private NfvoProperties nfvoProperties;
    @Autowired private VirtualNetworkFunctionDescriptor cloudRepository;
    @Autowired private NetworkServiceDescriptor nsdFromFile;
    private Logger logger;
    private NFVORequestor nfvoRequestor;
    private String apiPath;
    private Map<String, NetworkServiceRecord> records;

    @PostConstruct
    private void init() throws IOException {

        this.logger = LoggerFactory.getLogger(this.getClass());
        this.nfvoRequestor = new NFVORequestor(nfvoProperties.getOpenbatonUsername(), nfvoProperties.getOpenbatonPasswd(), nfvoProperties.getOpenbatonIP(), nfvoProperties.getOpenbatonPort(), "1");
       this.apiPath = "/api/v1/sfco";

        try {
            vimInstance = this.nfvoRequestor.getVimInstanceAgent().create(vimInstance);
        } catch (SDKException e){
            try {
                List<VimInstance> instances = nfvoRequestor.getVimInstanceAgent().findAll();
                for (VimInstance instance : instances){
                    if (vimInstance.getName().equals(instance.getName())){
                        if (!vimInstance.getAuthUrl().equals(instance.getAuthUrl()) && !vimInstance.getUsername().equals(instance.getUsername()) && !vimInstance.getPassword().equals(instance.getPassword())){
                            nfvoRequestor.getVimInstanceAgent().update(vimInstance,instance.getId());
                        }
                    }
                }
            } catch (SDKException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        this.records = new HashMap<>();
    }

    public OpenBatonCreateServer getServiceFunctionID(Flavor flavorID, String sfID, String callbackUrl,boolean cloudRepositorySet,int scaleInOut) throws SDKException {

        logger.debug("FlavorID " + flavorID + " appID " + sfID + " callbackURL " + callbackUrl +  " scaleInOut " + scaleInOut);

        NetworkServiceDescriptor targetNSD = this.configureDescriptor(nsdFromFile,flavorID, scaleInOut);
        if (cloudRepositorySet){
            Set<VirtualNetworkFunctionDescriptor> vnfds = targetNSD.getVnfd();
            vnfds.add(cloudRepository);
            logger.debug("VNFDS " + vnfds.toString());
            targetNSD.setVnfd(vnfds);
        }

        targetNSD = nfvoRequestor.getNetworkServiceDescriptorAgent().create(targetNSD);

        OpenBatonCreateServer res = new OpenBatonCreateServer();
        res.setNsdID(targetNSD.getId());
        NetworkServiceRecord nsr = null;

        nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(targetNSD.getId());
        logger.debug("NSR " + nsr.toString());
        this.records.put(nsr.getId(), nsr);

        EventEndpoint eventEndpointCreation = new EventEndpoint();
        eventEndpointCreation.setType(EndpointType.REST);
        eventEndpointCreation.setEndpoint(callbackUrl + apiPath + "/openbaton/" + sfID);
        eventEndpointCreation.setEvent(Action.INSTANTIATE_FINISH);

        EventEndpoint eventEndpointError = new EventEndpoint();
        eventEndpointError.setType(EndpointType.REST);
        eventEndpointError.setEndpoint(callbackUrl + apiPath + "/openbaton/" + sfID);
        eventEndpointError.setEvent(Action.ERROR);

        nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(targetNSD.getId());
        logger.debug("NSR " + nsr.toString());
        this.records.put(nsr.getId(), nsr);
        eventEndpointCreation.setNetworkServiceId(nsr.getId());
        eventEndpointError.setNetworkServiceId(nsr.getId());
        res.setServiceFunctionID(nsr.getId());

        eventEndpointCreation = this.nfvoRequestor.getEventAgent().create(eventEndpointCreation);
        res.setEventAllocatedID(eventEndpointCreation.getId());

        eventEndpointError = this.nfvoRequestor.getEventAgent().create(eventEndpointError);
        res.setEventErrorID(eventEndpointError.getId());

        logger.debug("Result " + res.toString());
        return res;
    }

    public BuildingStatus getStatus(String nsrID) {
        NetworkServiceRecord nsr = null;
        BuildingStatus res = BuildingStatus.CREATED;
        try {
            nsr = nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID);
        } catch (SDKException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (nsr != null) {
            switch (nsr.getStatus()) {
                case NULL:
                    res = BuildingStatus.CREATED;
                    break;
                case INITIALIZED:
                    res = BuildingStatus.INITIALIZING;
                    break;
                case ERROR:
                    res = BuildingStatus.FAILED;
                    break;
                case ACTIVE:
                    res = BuildingStatus.INITIALISED;
                    break;
            }
        }

        return res;
    }

    public void deleteRecord(String nsrID) {
        try {
            records.remove(nsrID);
            nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrID);
        } catch (SDKException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(String eventID) {
        try {
            this.nfvoRequestor.getEventAgent().delete(eventID);
        } catch (SDKException e) {
            e.printStackTrace();
        }
    }

    public void deleteDescriptor(String nsdID){
        try {
            this.nfvoRequestor.getNetworkServiceDescriptorAgent().delete(nsdID);
        } catch (SDKException e){
            e.printStackTrace();
        }
    }



    private NetworkServiceDescriptor configureDescriptor(NetworkServiceDescriptor nsd, Flavor flavor, int scaleInOut)  {
        logger.debug("Start configure");
        nsd = this.injectFlavor(flavor.getValue(),scaleInOut,nsd);
        logger.debug("After flavor the nsd is\n" + nsd.toString() + "\n****************************");



        return nsd;
    }








    private NetworkServiceDescriptor injectFlavor(String flavour,int scaleInOut, NetworkServiceDescriptor networkServiceDescriptor){

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : networkServiceDescriptor.getVnfd()) {
            if (vnfd.getEndpoint().equals("generic")){
                Set<VirtualDeploymentUnit> virtualDeploymentUnits = new HashSet<>();
                for (VirtualDeploymentUnit vdu : vnfd.getVdu()){
                    vdu.setScale_in_out(scaleInOut);
                    virtualDeploymentUnits.add(vdu);
                }
                vnfd.setVdu(virtualDeploymentUnits);
                Set<VNFDeploymentFlavour> flavours = new HashSet<>();
                VNFDeploymentFlavour newFlavour = new VNFDeploymentFlavour();
                newFlavour.setFlavour_key(flavour);
                flavours.add(newFlavour);
                vnfd.setDeployment_flavour(flavours);
                vnfds.add(vnfd);
            }
        }

        networkServiceDescriptor.setVnfd(vnfds);
        return networkServiceDescriptor;
    }


//    @PreDestroy
//    private void deleteNSD() throws SDKException {
//        if (!this.records.isEmpty()) {
//            for (String nsrId : records.keySet()) {
//                this.nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrId);
//                this.records.remove(nsrId);
//            }
//        }
//        this.nfvoRequestor.getNetworkServiceDescriptorAgent().delete(nsd.getId());
//        this.nfvoRequestor.getVimInstanceAgent().delete(this.vimInstance.getId());
//
//    }


}
