package OpenBaton.SFCdb.RecordManagement;

/**
 * Created by mah on 3/4/16.
 */



import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import OpenBaton.SFCdb.catalogue.ServiceFunctionChainRecord;
import OpenBaton.SFCdb.catalogue.Status;
import OpenBaton.SFCdb.repository.ServiceFunctionChainRecordRepository;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcModel.SFCdict.SfcDict;
import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lto on 11/05/15.
 */
@Service
@Scope("prototype")
@ConfigurationProperties
public class SFCRmanagement implements OpenBaton.SFCdb.RecordManagementInterfaces.SFCRmanInterface {

    private Logger log = LoggerFactory.getLogger(this.getClass());

   private Set<Status> ACTIVE_UPDATE=new HashSet<Status>(Arrays.asList(Status.ACTIVE,Status.PENDING_UPDATE));
    private Set<Status> ACTIVE_UPDATE_ERROR_DEAD=new HashSet<Status>(Arrays.asList(Status.PENDING_CREATE,Status.ACTIVE,Status.PENDING_UPDATE,Status.ERROR,Status.DEAD));


    @Autowired
    private ServiceFunctionChainRecordRepository sfcrRepository;




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

    public ServiceFunctionChainRecord get_SFC_Record(String sfc_id, Set<Status> Current_status,Status new_status){
      ServiceFunctionChainRecord sfcr=sfcrRepository.findFirstById(sfc_id);
        if(ACTIVE_UPDATE.contains(sfcr.getStatus())==true){
            log.info("SFC is Found");

        }
        if(sfcr.getStatus().equals(Status.PENDING_UPDATE)){
            log.info("SFC In Use");

        }
        sfcr.setStatus(new_status);
        sfcr=sfcrRepository.save(sfcr);

        return sfcr;
    }


    public SFCdict SFC_update(ServiceFunctionChainRecord newSFCR, String sfc_id) {
        SFCdict sfcd=SFC_update_pre(sfc_id);
        SFC_update_post(sfc_id,Status.ACTIVE);
        return sfcd;
    }

    public SFCdict SFC_update_pre( String sfc_id) {
       ServiceFunctionChainRecord sfcr=get_SFC_Record(sfc_id,ACTIVE_UPDATE,Status.PENDING_UPDATE);
        sfcr=sfcrRepository.save(sfcr);
        SFCdict sfc=new SFCdict();
        return sfc;
    }
    public void SFC_update_post( String sfc_id, Status new_status) {
        if (sfcrRepository.findFirstById(sfc_id).getId()==sfc_id && sfcrRepository.findFirstById(sfc_id).getStatus().equals(Status.PENDING_UPDATE)){
            sfcrRepository.findFirstById(sfc_id).setStatus(new_status);
        }
        sfcrRepository.save(sfcrRepository.findFirstById(sfc_id));
    }



    public ServiceFunctionChainRecord update(ServiceFunctionChainRecord newRsr, String idNsr) {
        sfcrRepository.exists(idNsr);
        newRsr = sfcrRepository.save(newRsr);
        return newRsr;
    }


    public Iterable<ServiceFunctionChainRecord> SFCs_query() {
        return sfcrRepository.findAll();
    }




    public ServiceFunctionChainRecord SFC_query(String id) {
        return sfcrRepository.findFirstById(id);
    }




}