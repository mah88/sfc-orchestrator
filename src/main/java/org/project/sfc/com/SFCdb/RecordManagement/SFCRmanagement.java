package org.project.sfc.com.SFCdb.RecordManagement;

/**
 * Created by mah on 3/4/16.
 */



import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.ODL_SFC.NeutronClient;
import org.project.sfc.com.SFCdb.catalogue.ServiceFunctionChainRecord;
import org.project.sfc.com.SFCdb.catalogue.Status;
import org.project.sfc.com.SFCdb.repository.ServiceFunctionChainRecordRepository;
import org.project.sfc.com.SFCdict.SFCdict;
import org.project.sfc.com.SFCdict.SfcDict;
import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.beans.factory.annotation.Value;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by lto on 11/05/15.
 */
@Service
@Scope("prototype")
@ConfigurationProperties
public class SFCRmanagement implements org.project.sfc.com.SFCdb.RecordManagementInterfaces.SFCRmanInterface {

    private Logger log = LoggerFactory.getLogger(this.getClass());



    @Autowired
    private ServiceFunctionChainRecordRepository sfcrRepository;



    @Value("${nfvo.delete.all-status:}")
    private String deleteInAllStatus;

    @Value("${nfvo.delete.vnfr:false}")
    private String waitForDelete;

    @Value("${nfvo.delete.vnfr:false}")
    private boolean removeAfterTimeout;




    public ServiceFunctionChainRecord Create_SFC_pre(SFCdict sfcd) {
        log.info("Create Pre SFC : " + sfcd.getSfcDict().getName());
        NeutronClient NC=new NeutronClient();
      String  tenant_id = NC.getTenantID();
        SfcDict sfc=sfcd.getSfcDict();
        String Infra_driver=sfc.getInfraDriver();

        String name = sfc.getName();
       String description = sfc.getDescription();
        ServiceFunctionChainRecord SFCR=new ServiceFunctionChainRecord();
        SFCR.ensureId();
        String sfc_id = SFCR.getId();
       boolean symmetrical = sfc.getSymmetrical();

       List<String> chain = sfc.getChain();

        SFCR.setDescription(description);
        SFCR.setId(sfc_id);
        SFCR.setName(name);
        SFCR.setSFC(chain);
        SFCR.setSymmetrical(symmetrical);
        SFCR.setStatus(Status.PENDING_CREATE);
        SFCR.setInstanceID(null);
        SFCR=sfcrRepository.save(SFCR);


        return SFCR;
    }

    public SfcDict Create_SFC_(SFCdict sfcd) {

        SfcDict sfcd_= create_device_pre(sfcd);
        String Instance_id= IdGenerator.createUUID();
        sfcd_.setInstanceId(Instance_id);
        Create_SFC_post(sfcd_.getId(), Instance_id, sfcd_);
        Create_SFC_status(sfcd_.getId(),Status.ACTIVE);
        return sfcd_;

    }

    public SfcDict create_device_pre(SFCdict sfcd) {
        //Instantiating and creating the SFs
        SfcDict sfcd_=null;
        return sfcd_;
    }

    public void Create_SFC_post(String sfc_id, String instance_id,SfcDict sfcd_) {


        String Instance_id= IdGenerator.createUUID();
        sfcd_.setInstanceId(Instance_id);
        if (sfcrRepository.findFirstById(sfc_id).getId()==sfc_id && sfcrRepository.findFirstById(sfc_id).getStatus()==Status.PENDING_CREATE){
            sfcrRepository.findFirstById(sfc_id).setInstanceID(instance_id);
        }

        if(instance_id==null){
            sfcrRepository.findFirstById(sfc_id).setStatus(Status.ERROR);
        }



    }

    public void Create_SFC_status(String sfc_id, Status new_status){
        if (sfcrRepository.findFirstById(sfc_id).getId()==sfc_id && sfcrRepository.findFirstById(sfc_id).getStatus()==Status.PENDING_CREATE){
            sfcrRepository.findFirstById(sfc_id).setStatus(new_status);
        }

    }

    //start work from here need to adjust update,delete

    public ServiceFunctionChainRecord get_SFC_Record(String sfc_id, Status Current_status,Status new_status){
      ServiceFunctionChainRecord sfcr=sfcrRepository.findFirstById(sfc_id);

        return sfcr;
    }
    public ServiceFunctionChainRecord SFC_update(ServiceFunctionChainRecord newSFCR, String sfc_id) {
        SFCdict sfcd=SFC_update_pre(sfc_id);
        return sfcd;
    }

    public ServiceFunctionChainRecord SFC_update_pre( String sfc_id) {
       ServiceFunctionChainRecord sfcr=get_SFC_Record(sfc_id,)
    }




    @Override
    public ServiceFunctionChainRecord update(ServiceFunctionChainRecord newRsr, String idNsr) {
        sfcrRepository.exists(idNsr);
        newRsr = sfcrRepository.save(newRsr);
        return newRsr;
    }

    @Override
    public Iterable<ServiceFunctionChainRecord> SFCs_query() {
        return sfcrRepository.findAll();
    }



    @Override
    public ServiceFunctionChainRecord SFC_query(String id) {
        return sfcrRepository.findFirstById(id);
    }




}