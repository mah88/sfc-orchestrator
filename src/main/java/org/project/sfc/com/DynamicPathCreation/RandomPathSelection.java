package org.project.sfc.com.DynamicPathCreation;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC.VNFdict;

import java.util.*;

/**
 * Created by mah on 4/22/16.
 */
public class RandomPathSelection {


        NeutronClient NC=new NeutronClient();


        public VNFdict SelectVNF(VirtualNetworkFunctionRecord vnfr){
            List<String> VNF_instances=new ArrayList<String>();

            for(VirtualDeploymentUnit vdu_x: vnfr.getVdu()){
                for(VNFCInstance vnfc_instance:vdu_x.getVnfc_instance()){
                    VNF_instances.add(vnfc_instance.getHostname());
                }
            }

            VNFdict new_vnf=new VNFdict();

            Random randomizer = new Random();
            String VNF_instance_selected = VNF_instances.get(randomizer.nextInt(VNF_instances.size()));
            new_vnf.setName(VNF_instance_selected);
            new_vnf.setType(vnfr.getType());
            for(VirtualDeploymentUnit vdu_x: vnfr.getVdu()){
                for(VNFCInstance vnfc_instance:vdu_x.getVnfc_instance()){
                    if(vnfc_instance.getHostname()==VNF_instance_selected){
                        for ( Ip ip:vnfc_instance.getIps()) {
                            new_vnf.setIP(ip.getIp());
                            new_vnf.setNeutronPortId(NC.getNeutronPortID(ip.getIp()));
                            break;
                        }
                    }
                }
            }


            return new_vnf;
        }

        public HashMap<Integer, VNFdict>  CreatePath(Set<VirtualNetworkFunctionRecord> vnfrs, NetworkServiceRecord nsr){
            HashMap<Integer, VNFdict> vnfdicts = new HashMap<Integer, VNFdict>();
            List<VNFdict> vnf_test =new ArrayList<>();
            List<String> chain = new ArrayList<String>();


            int i=0;
            // for getting the VNF instance NAME
            String VNF_NAME;
            for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {


                VNFdict new_vnf=SelectVNF(vnfr);

                vnf_test.add(new_vnf);

                vnfdicts.put(i,vnf_test.get(i));

                i++;
            }


            return vnfdicts;

        }




}
