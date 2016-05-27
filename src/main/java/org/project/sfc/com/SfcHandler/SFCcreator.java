package org.project.sfc.com.SfcHandler;
import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.DynamicPathCreation.RandomPathSelection;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SFCdict;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SfcDict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.VNFdict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.SFCCdict;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.springframework.http.ResponseEntity;
import org.project.sfc.com.SfcHandler.SFC.SFC_Data;
import java.util.*;


/**
 * Created by mah on 3/14/16.
 */

public class SFCcreator {
    Opendaylight SFC = new Opendaylight();
    SFCdict sfc_test=new SFCdict();
    SfcDict sfc_dict_test=new SfcDict();
    NeutronClient NC=new NeutronClient();
    SFC_Classifier classifier_test2=new SFC_Classifier();
    SFCCdict sfcc_dict=new SFCCdict();
    SFC sfcc_db= org.project.sfc.com.SfcHandler.SFC.getInstance();


    public void UpdateChainsPaths(VirtualNetworkFunctionRecord vnfr){

        System.out.println("[SFC-Path-UPDATE] (1) at time " + new Date().getTime());

        HashMap<String, SFC_Data> All_SFCs=sfcc_db.getAllSFCs();

        Iterator it = All_SFCs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry SFCdata_counter = (Map.Entry)it.next();
            HashMap<Integer,VNFdict> VNFs=All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
            Iterator count=VNFs.entrySet().iterator();
            while(count.hasNext()){
                Map.Entry VNFcounter=(Map.Entry)count.next();
                if(getFaildVNFname(vnfr)!=null){
                    System.out.println("[Found Failed VNF] -->"+getFaildVNFname(vnfr)+" at time " + new Date().getTime());

                    if(VNFs.get(VNFcounter.getKey()).getName().equals(getFaildVNFname(vnfr))){
                        int position=(int)VNFcounter.getKey();
                        System.out.println("[position of Failed VNF] -->"+position);
                        VNFs.remove(position);
                        if(getActiveVNF(vnfr)!=null){
                            VNFdict newVnf=new VNFdict();
                            newVnf.setName(getActiveVNF(vnfr).getHostname());
                            newVnf.setType(vnfr.getType());
                            for ( Ip ip:getActiveVNF(vnfr).getIps()) {
                                newVnf.setIP(ip.getIp());
                                newVnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));

                                break;
                            }
                            VNFs.put(position,newVnf);
                        }

                    }
                }
            }

            String new_instance_id=SFC.CreateSFP(sfc_test, VNFs);
            System.out.println("[NEW Path Updated ] " + new_instance_id );

            String SFCC_name=classifier_test2.Create_SFC_Classifer(sfcc_dict,new_instance_id);
            System.out.println("[NEW Classifier Updated ] " + new_instance_id );

            sfcc_db.update(new_instance_id.substring(5), new_instance_id, sfcc_dict.getName(),sfc_dict_test.getSymmetrical(),VNFs,sfc_dict_test,sfcc_dict);

        }

        SFC.DeleteSF(getFaildVNFname(vnfr));

    }

    public String getFaildVNFname(VirtualNetworkFunctionRecord vnfr){
        boolean found=false;
        String VNF_name="";
        for(VirtualDeploymentUnit vdu:vnfr.getVdu()){
            for(VNFCInstance vnf_instance:vdu.getVnfc_instance()){

                if(vnf_instance.getState().equals("failed")){

                    found=true;

                    VNF_name= vnf_instance.getHostname();
                }
            }
        }

        if(found==false){
            return null;
        }else {
            return VNF_name;
        }
    }
    public VNFCInstance getActiveVNF(VirtualNetworkFunctionRecord vnfr){
        boolean found=false;
        VNFCInstance VNF_instance=new VNFCInstance();
        for(VirtualDeploymentUnit vdu:vnfr.getVdu()){
            for(VNFCInstance vnf_instance:vdu.getVnfc_instance()){

                if(vnf_instance.getState().equals("active")){

                    found=true;

                    VNF_instance= vnf_instance;
                }
            }
        }

        if(found==false){
            return null;
        }else {
            return VNF_instance;
        }
    }
    public boolean Create(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr){

        System.out.println("[SFC-Creation] (1) at time " + new Date().getTime());

        for(VNFForwardingGraphRecord vnffgr:nsr.getVnffgr()){
            Set<VirtualNetworkFunctionRecord> vnf_members=new HashSet<VirtualNetworkFunctionRecord>();
            for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {
                for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {

                    for (VirtualNetworkFunctionRecord vnfr : vnfrs) {

                        if (vnfr.getName().equals(entry.getValue())) {
                            vnf_members.add(vnfr);

                        }


                    }
                }
            }

            System.out.println("[Size of VNF MEMBERS for creating CHAIN] " + vnf_members.size() + " for SFC allocation to nsr handler at time " + new Date().getTime());
            System.out.println("[ VNFFGR ID for creating CHAIN] " + vnffgr.getId() + " for SFC allocation to nsr handler at time " + new Date().getTime());
            boolean Response =CreateChain(vnf_members,vnffgr,nsr);
            if (Response==false){
                System.out.println("[CHAIN IS NOT CREATED] " );

                return false;
            }
      /*      try {
                Thread.sleep(100);                 //100 milliseconds is 0.1 second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
        }


        return true;
    }


    public boolean CreateChain(Set<VirtualNetworkFunctionRecord> vnfrs,VNFForwardingGraphRecord vnffgr, NetworkServiceRecord nsr){

        // public boolean Create(Set<VNFForwardingGraphRecord> vnfrs, NetworkServiceRecord nsr){
//FIX ME change the place of this configuration
     /*   if(SFC.Check_Configuration_NETVIRT()==false) {
            ResponseEntity<String> netvirt = SFC.Configure_NETVIRT();
            System.out.println("NETVIRT status code "+ netvirt.getStatusCode());

      }
 */
      /*  if(SFC.Check_Configuration_SfcOfRenderer()==false) {
            ResponseEntity<String> sfcodrender = SFC.Configure_SfcOfRenderer();
            System.out.println("SFC OF Render status code " + sfcodrender.getStatusCode());
            //----------------------------------
        }


   */


        List<VNFdict> vnf_test =new ArrayList<>();
        List<String> chain = new ArrayList<String>();
        HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
      System.out.println("[SFC-Creation] (2) at time " + new Date().getTime());

        RandomPathSelection RPS=new RandomPathSelection();
        System.out.println("[SFC-Creation] (3) at time " + new Date().getTime());


        vnfdicts = RPS.CreatePath(vnfrs,vnffgr,nsr);

        System.out.println("[SFC-Creation] Creating Path Finished  at time " + new Date().getTime());


      /*  if(vnffgr==null) {
            for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
                chain.add(vnfr.getName());
            }
            sfc_dict_test.setSymmetrical(false);
        }else{

*/



        for (NetworkForwardingPath nfp : vnffgr.getNetwork_forwarding_path()) {

            for(int counter=0;counter<nfp.getConnection().size();counter++) {
                for (Map.Entry<String, String> entry : nfp.getConnection().entrySet()) {
                    Integer k = Integer.valueOf(entry.getKey());

                      int  x = k.intValue();


                    if(counter==x) {


                        System.out.println("[SFC-Creation] integer vlaue " + counter);

                        chain.add(entry.getValue());
                    }


                }
            }
        }


        sfc_dict_test.setSymmetrical(vnffgr.isSymmetrical());

        //   }

    /*    if(vnffgr==null) {
            sfc_dict_test.setName(nsr.getName()+"-"+nsr.getId());
            sfc_dict_test.setId(nsr.getId());


        }else{*/


        sfc_dict_test.setName(nsr.getName()+"-"+vnffgr.getId());
        sfc_dict_test.setId(vnffgr.getId());

        // }

        sfc_dict_test.setChain(chain);
        sfc_dict_test.setInfraDriver("ODL");

        sfc_dict_test.setStatus("create");
        sfc_dict_test.setTenantId(NC.getTenantID());
        sfc_test.setSfcDict(sfc_dict_test);

        SFC.CreateSFC(sfc_test, vnfdicts);
        String instance_id=SFC.CreateSFP(sfc_test, vnfdicts);
        System.out.println(" ADD NSR ID as key to SFC DB:  " + nsr.getId() + " at time " + new Date().getTime());
        System.out.println(" ADD to it Instance  ID:  " + instance_id + " at time " + new Date().getTime());


        sfcc_dict.setStatus("create");
        sfcc_dict.setTenantId(NC.getTenantID());
        sfcc_dict.setInfraDriver("netvirtsfc");
        sfcc_dict.setId("sfcc-"+vnffgr.getId());
        sfcc_dict.setChain(sfc_dict_test.getId());
        AclMatchCriteria acl=new AclMatchCriteria();

    /*    if(vnffgr==null) {

            sfcc_dict.setName("sfc-classifier-"+nsr.getName());
            acl.setDestPort(80);
            acl.setSrcPort(0);
            acl.setProtocol(6);
        }else{*/

        sfcc_dict.setName("sfc-classifier-"+nsr.getName()+"-"+vnffgr.getId());
        for(NetworkForwardingPath nsp:vnffgr.getNetwork_forwarding_path()) {

            acl.setDestPort(nsp.getPolicy().getMatchingCriteria().getDestinationPort());
            acl.setSrcPort(nsp.getPolicy().getMatchingCriteria().getSourcePort());
            acl.setProtocol(nsp.getPolicy().getMatchingCriteria().getProtocol());
           // break;
        }
        //    }


        List<AclMatchCriteria> list_acl=new ArrayList<AclMatchCriteria>();
        list_acl.add(acl);
        sfcc_dict.setAclMatchCriteria(list_acl);
        String SFCC_name=classifier_test2.Create_SFC_Classifer(sfcc_dict,instance_id);
     /*   if(vnffgr==null) {

            sfcc_db.add(nsr.getId(), instance_id, sfcc_dict.getName());
            System.out.println(" GET  Instance  ID:  " + sfcc_db.getRspID(nsr.getId()) + " at time " + new Date().getTime());

        }else{ */
        sfcc_db.add(vnffgr.getId(), instance_id, sfcc_dict.getName(),sfc_dict_test.getSymmetrical(),vnfdicts,sfc_dict_test,sfcc_dict);
        System.out.println(" GET  Instance  ID:  " + sfcc_db.getRspID(vnffgr.getId()) + " at time " + new Date().getTime());


        //}

        if (SFCC_name!=null && instance_id!=null){
            System.out.println(" Chain Created successfully, instance id= " +instance_id);

            return true;
        }
        else {
            System.out.println(" Chain Not Created at time " + new Date().getTime());

            return false;
        }
    }

    public boolean Delete(String nsrID){
        System.out.println("delete NSR ID:  " + nsrID + " at time " + new Date().getTime());
        String rsp_id=sfcc_db.getRspID(nsrID);

        String sffc_name=sfcc_db.getSfccName(nsrID);
        System.out.println("instance id to be deleted:  " + sffc_name );


        ResponseEntity<String> result= classifier_test2.Delete_SFC_Classifier(sffc_name);
        System.out.println("Delete SFC Classifier :  " + result.getStatusCode().is2xxSuccessful() );
        ResponseEntity<String> sfc_result=SFC.DeleteSFC(rsp_id,sfcc_db.isSymmSFC(nsrID));
        System.out.println("Delete SFC   :  " + sfc_result.getStatusCode().is2xxSuccessful() );

        if(result!=null && sfc_result!=null) {


            if (result.getStatusCode().is2xxSuccessful() && sfc_result.getStatusCode().is2xxSuccessful()) {
                sfcc_db.remove(nsrID);
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
}