package OpenBaton;

/**
 * Created by mah on 3/1/16.
 */
import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.catalogue.nfvo.Configuration;
import org.openbaton.catalogue.nfvo.ConfigurationParameter;
import org.openbaton.sdk.api.exception.SDKException;
import OpenBaton.Configuration.OpenBatonProperties;
import OpenBaton.exceptions.*;
import OpenBaton.Messages.*;


import OpenBaton.persistence.Application;
import OpenBaton.persistence.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;


/**
 * Created by maa on 28.09.15.
 */

@RestController
@RequestMapping("/api/v1/sfco")
public class ServiceFunctionAppManager {

    private static Map<String, OpenBatonCreateServer> deploymentMap = new HashMap<>();
    private Logger logger;
    private SecureRandom appIDGenerator;


    @Autowired private OpenBatonManager obmanager;
    @Autowired private ApplicationRepository appRepo;
   @Autowired private OpenBatonProperties openbatonProperties;

    @PostConstruct
    private void init() {
     //   System.setProperty("javax.net.ssl.trustStore", "/opt/nubomedia/nubomedia-paas/resource/openshift-keystore");
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.appIDGenerator = new SecureRandom();
    }

    @RequestMapping(value = "/SFapp",  method = RequestMethod.POST)
    public @ResponseBody SFcreateAppResponse createApp(@RequestBody SFcreateAppRequest request) throws SDKException, DuplicatedException, NameStructureException {



        if(request.getAppName().length() > 18){

            throw new NameStructureException("name is too long");

        }

        if(request.getAppName().contains(".")){

            throw new NameStructureException("name can't contains dots");

        }

        if(!appRepo.findByAppName(request.getAppName()).isEmpty()){
            throw new DuplicatedException("Application with " + request.getAppName() + " already exist");
        }

        logger.debug("REQUEST" + request.toString());

     /*   List<String> protocols = new ArrayList<>();
        List<Integer> targetPorts = new ArrayList<>();
        List<Integer> ports = new ArrayList<>();

        for (int i = 0; i < request.getPorts().length; i++){

            protocols.add(request.getPorts()[i].getProtocol());
            targetPorts.add(request.getPorts()[i].getTargetPort());
            ports.add(request.getPorts()[i].getPort());

        }
*/
        SFcreateAppResponse res = new SFcreateAppResponse();
        String appID = new BigInteger(130,appIDGenerator).toString(64);
        logger.debug("App ID " + appID + "\n");

        logger.debug("request params " + request.getAppName() + " " + request.getGitURL() + " " + request.getProjectName() + " " + request.getReplicasNumber());

        //Openbaton SF Request
        logger.info("[SF]: EVENT_APP_CREATE " + new Date().getTime());
        OpenBatonCreateServer openbatonCreateServer = obmanager.getServiceFunctionID(request.getFlavor(),appID, openbatonProperties.getInternalURL(),request.isCloudRepository(), request.getScaleInOut());
        //openbatonCreateServer.setToken(token);

        deploymentMap.put(appID,openbatonCreateServer);

        Application persistApp = new Application(appID,request.getFlavor(),request.getAppName(),request.getProjectName(),"",openbatonCreateServer.getNsdID(), request.getGitURL(), request.getReplicasNumber(),false);
        appRepo.save(persistApp);

        res.setApp(persistApp);
        res.setCode(200);
        return res;
    }

    @RequestMapping(value = "/SFapp/{id}", method =  RequestMethod.GET)
    public @ResponseBody Application getApp(@PathVariable("id") String id) throws ApplicationNotFoundException {

        logger.info("Request status for " + id + " app");


        if(!appRepo.exists(id)){
            throw new ApplicationNotFoundException("Application with ID not found");
        }

        Application app = appRepo.findFirstByAppID(id);
        logger.debug("Retrieving status for " + app.toString());

        switch (app.getStatus()){
            case CREATED:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case INITIALIZING:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case INITIALISED:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case BUILDING:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case DEPLOYNG:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case FAILED:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case RUNNING:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case PAAS_RESOURCE_MISSING:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
        }

        appRepo.save(app);

        return app;

    }

    @RequestMapping(value = "/SFapp/{id}/buildlogs", method = RequestMethod.GET)
    public @ResponseBody SFBuildlogs getBuildLogs( @PathVariable("id") String id) {


        SFBuildlogs res = new SFBuildlogs();

        if(!appRepo.exists(id)){
            return null;
        }

        Application app = appRepo.findFirstByAppID(id);

        if(!app.isResourceOK()){

            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("Something wrong on retrieving resources");

        }

        if(app.getStatus().equals(BuildingStatus.CREATED) || app.getStatus().equals(BuildingStatus.INITIALIZING)){
            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("The application is retrieving resources " + app.getStatus());

            return res;
        }

        if (app.getStatus().equals(BuildingStatus.PAAS_RESOURCE_MISSING)){
            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("PaaS components are missing, send an email to the administrator to chekc the PaaS status");

            return res;
        }

        res.setId(id);
        res.setAppName(app.getAppName());
        res.setProjectName(app.getProjectName());
       // res.setLog(osmanager.getBuildLogs(token,app.getAppName(),app.getProjectName()));
        return res;
    }

    @RequestMapping(value = "/SFapp/{id}/logs", method = RequestMethod.GET)
    public @ResponseBody String getApplicationLogs( @PathVariable("id") String id) throws  ApplicationNotFoundException {


        if(!appRepo.exists(id)){
            throw new ApplicationNotFoundException("Application with ID not found");
        }

        Application app = appRepo.findFirstByAppID(id);

        if(!app.getStatus().equals(BuildingStatus.RUNNING)){
            return "Application Status " + app.getStatus() + ", logs not available until the status is RUNNING";
        }

        return app.toString();

    }

    @RequestMapping(value = "/SFapp", method = RequestMethod.GET)
    public @ResponseBody Iterable<Application> getApps() throws ApplicationNotFoundException {


        //BETA
        Iterable<Application> applications = this.appRepo.findAll();

        for (Application app : applications){
            app.setStatus(this.getStatus(app));
        }

        this.appRepo.save(applications);

        return applications;
    }

    @RequestMapping(value = "/SFapp/{id}", method = RequestMethod.DELETE)
    public @ResponseBody SFdeleteAppResponse deleteApp(@PathVariable("id") String id)  {



        logger.debug("id " + id);

        if(!appRepo.exists(id)){
            return new SFdeleteAppResponse(id,"Application not found","null",404);
        }

        Application app = appRepo.findFirstByAppID(id);
        app.setStatus(this.getStatus(app));
        logger.debug("Deleting " + app.toString());

        if (!app.isResourceOK()){

            String name = app.getAppName();
            String projectName = app.getProjectName();

            if(app.getStatus().equals(BuildingStatus.CREATED) || app.getStatus().equals(BuildingStatus.INITIALIZING)){
                OpenBatonCreateServer server = deploymentMap.get(id);
                obmanager.deleteDescriptor(server.getNsdID());
                obmanager.deleteEvent(server.getEventAllocatedID());
                obmanager.deleteEvent(server.getEventErrorID());
                obmanager.deleteRecord(app.getNsrID());
                deploymentMap.remove(server);

            }

            appRepo.delete(app);
            return new SFdeleteAppResponse(id,name,projectName,200);

        }

        if (app.getStatus().equals(BuildingStatus.PAAS_RESOURCE_MISSING)){
            obmanager.deleteRecord(app.getNsrID());
            appRepo.delete(app);

            return new SFdeleteAppResponse(id,app.getAppName(),app.getProjectName(),200);
        }

//        if (app.getStatus().equals(BuildingStatus.CREATED) || app.getStatus().equals(BuildingStatus.INITIALIZING)){
//
//            obmanager.deleteRecord(app.getNsrID());
//            return new NubomediaDeleteAppResponse(id,app.getAppName(),app.getProjectName(),200);
//
//        }


        obmanager.deleteRecord(app.getNsrID());
        HttpStatus resDelete = HttpStatus.BAD_REQUEST;

        appRepo.delete(app);

        return new SFdeleteAppResponse(id,app.getAppName(),app.getProjectName(),resDelete.value());
    }





    @RequestMapping(value = "/openbaton/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void startServiceFunctionBuild(@RequestBody OpenBatonEvent evt, @PathVariable("id") String id) {
        logger.debug("starting callback for appId" + id);
        logger.info("Received event " + evt);
        Application app = appRepo.findFirstByAppID(id);
        logger.debug(deploymentMap.toString());
        OpenBatonCreateServer server = deploymentMap.get(id);

        if(evt.getAction().equals(Action.INSTANTIATE_FINISH) && server.getNsdID().equals(evt.getPayload().getId())){
            logger.info("[SF]: EVENT_FINISH " + new Date().getTime());
            app.setStatus(BuildingStatus.INITIALISED);
            app.setResourceOK(true);
            appRepo.save(app);

            String vnfrID ="";
            String cloudRepositoryIp = null;
            String cloudRepositoryPort = null;

            for(VirtualNetworkFunctionRecord record : evt.getPayload().getVnfr()){

                if(record.getEndpoint().equals("generic"))
                    vnfrID = record.getId();

                if(record.getName().contains("service-function")){
                    cloudRepositoryIp = this.getCloudRepoIP(record);

                    Configuration configuration = record.getConfigurations();
                    for (ConfigurationParameter parameter : configuration.getConfigurationParameters()){
                        if (parameter.getConfKey().equals("PORT")){
                            cloudRepositoryPort = parameter.getValue();
                        }
                    }
                }

            }
/*
            logger.debug("retrieved session for " + server.getToken());
            String route = null;
            try {
                int[] ports = new int[app.getPorts().size()];
                int[] targetPorts = new int[app.getTargetPorts().size()];

                for(int i = 0; i < ports.length; i++){
                    ports[i] = app.getPorts().get(i);
                    targetPorts[i] = app.getTargetPorts().get(i);
                }

                logger.info("[PAAS]: CREATE_APP_OS " + new Date().getTime());
                logger.debug("cloudRepositoryPort "+ cloudRepositoryPort + " IP " + cloudRepositoryIp);

                try {
                    route = osmanager.buildApplication(server.getToken(), app.getAppID(), app.getAppName(), app.getProjectName(), app.getGitURL(), ports, targetPorts, app.getProtocols().toArray(new String[0]), app.getReplicasNumber(), app.getSecretName(), vnfrID, paaSProperties.getVnfmIP(), paaSProperties.getVnfmPort(), cloudRepositoryIp, cloudRepositoryPort);

                } catch (ResourceAccessException e){
                    obmanager.deleteDescriptor(server.getNsdID());
                    obmanager.deleteEvent(server.getEventAllocatedID());
                    obmanager.deleteEvent(server.getEventErrorID());
                    app.setStatus(BuildingStatus.FAILED);
                    appRepo.save(app);
                    deploymentMap.remove(app.getAppID());
                }
                logger.info("[PAAS]: SCHEDULED_APP_OS " + new Date().getTime());
            } catch (DuplicatedException e) {
                app.setRoute(e.getMessage());
                app.setStatus(BuildingStatus.DUPLICATED);
                appRepo.save(app);
                return;
            }
            */

            //something wrong here :why delete here ?
            obmanager.deleteDescriptor(server.getNsdID());

            obmanager.deleteEvent(server.getEventAllocatedID());
            obmanager.deleteEvent(server.getEventErrorID());
         //   app.setRoute(route);
            appRepo.save(app);
            deploymentMap.remove(app.getAppID());
        }
        else if (evt.getAction().equals(Action.ERROR)){

            obmanager.deleteDescriptor(server.getNsdID());
            obmanager.deleteEvent(server.getEventErrorID());
            obmanager.deleteEvent(server.getEventAllocatedID());
            obmanager.deleteRecord(server.getNsdID());
            app.setStatus(BuildingStatus.FAILED);
            appRepo.save(app);
            deploymentMap.remove(app.getAppID());
        }

    }

    private String getCloudRepoIP(VirtualNetworkFunctionRecord record) {

        for (VirtualDeploymentUnit vdu : record.getVdu()){
            for (VNFCInstance instance : vdu.getVnfc_instance()){
                for (Ip ip : instance.getFloatingIps()){
                    if (ip != null){
                        return ip.getIp();
                    }
                }
            }
        }

        return null;
    }

    private BuildingStatus getStatus( Application app)  {

        BuildingStatus res = null;

        switch (app.getStatus()){
            case CREATED:
                res =  obmanager.getStatus(app.getNsrID());
                break;
            case INITIALIZING:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case INITIALISED:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case BUILDING:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case DEPLOYNG:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case FAILED:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case RUNNING:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case PAAS_RESOURCE_MISSING:
                res = obmanager.getStatus(app.getNsrID());
                break;
        }

        return res;
    }

//    @Scheduled(initialDelay = 0,fixedDelay = 200)
//    public void refreshStatus() throws ApplicationNotFoundException, UnauthorizedException {
//
//        for (String id : deploymentMap.keySet()){
//            boolean writed = false;
//            OpenbatonCreateServer ocs = deploymentMap.get(id);
//            Application app = this.getApp(ocs.getToken(),id);
//            if(app.getStatus() == BuildingStatus.RUNNING && !writed){
//                logger.info("[PAAS]: APP_RUNNING " + new Date().getTime());
//                writed = true;
//            }
//        }
//
//    }

}
