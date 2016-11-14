package org.project.sfc.com.DynamicPathCreation;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.NetworkForwardingPath;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFForwardingGraphRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.project.sfc.com.SfcHandler.SFC;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.NeutronClient;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by mah on 11/14/16.
 */
public class LoadBalancedPathSelection {
  NeutronClient NC;
  SFC sfcc_db = org.project.sfc.com.SfcHandler.SFC.getInstance();

  public LoadBalancedPathSelection() throws IOException {
    NC = new NeutronClient();
  }

  public void ReadjustVNFsAllocation(VirtualNetworkFunctionRecord vnfr) {

   HashMap<String,Double> PrevVNFTrafficLoad=new HashMap<String, Double>();

    HashMap<String, SFC.SFC_Data> All_SFCs = sfcc_db.getAllSFCs();
    HashMap<String, SFC.SFC_Data> Involved_SFCs =new HashMap<String, SFC.SFC_Data>();

    if (All_SFCs != null) {
      System.out.println("[ ALL SFCs number ] =  " + All_SFCs.size());
    }
    //Get Involved SFCs for this VNF
    Iterator it = All_SFCs.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry SFCdata_counter = (Map.Entry) it.next();
      HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
      Iterator vnfs_count = VNFs.entrySet().iterator();
      while (vnfs_count.hasNext()) {
        Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
        if (VNFs.get(VNFcounter.getKey()).getType().equals(vnfr.getType())){
          Involved_SFCs.put(All_SFCs.get(SFCdata_counter.getKey()).getRspID(),All_SFCs.get(SFCdata_counter.getKey()));
        }

      }
    }


    int total_size_VNF_instances=0;

    for (VirtualDeploymentUnit vdu_x : vnfr.getVdu()) {
      for (VNFCInstance vnfc_instance : vdu_x.getVnfc_instance()) {
        total_size_VNF_instances++;
      }
    }

    HashMap<String, SFC.SFC_Data> SelectedSFCs =new HashMap<String, SFC.SFC_Data>();


    for(int x=0;x<total_size_VNF_instances;x++) {

      if(Involved_SFCs.size()>0) {
        double MaxLoad = 0;
        String RSPID = "";

        Iterator c = Involved_SFCs.entrySet().iterator();
        while (c.hasNext()) {
          Map.Entry SFCKey = (Map.Entry) c.next();

          if (Involved_SFCs.get(SFCKey.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getPathTrafficLoad() >
              MaxLoad) {
            SelectedSFCs.put(Involved_SFCs.get(SFCKey.getKey()).getRspID(), Involved_SFCs.get(SFCKey.getKey()));
            MaxLoad = Involved_SFCs.get(SFCKey.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getPathTrafficLoad();
            RSPID = SFCKey.getKey().toString();
          }
        }
        Involved_SFCs.remove(RSPID);
        System.out.println("[ Remove from the Involved SFCs ]  The RSP ID is  " + RSPID);
      }
    }

    // Get which SFC should be selected
    Iterator counter = SelectedSFCs.entrySet().iterator();
    int size_selected_sfcs=SelectedSFCs.size();
    for(int i=0;i<size_selected_sfcs;i++) {

      double Load = 0;
      int recentQoS = 0;
      String RSPID="";
      String SelectedVNFinstance="";
      SFC.SFC_Data selectedChain = null;

      while (counter.hasNext()) {
        Map.Entry involved_SFC_data_counter = (Map.Entry) counter.next();


        if (SelectedSFCs.get(involved_SFC_data_counter.getKey())
                        .getSFCdictInfo()
                        .getSfcDict()
                        .getPaths()
                        .get(0)
                        .getQoS() > recentQoS) {
          Load = SelectedSFCs.get(involved_SFC_data_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getPathTrafficLoad();
          selectedChain = SelectedSFCs.get(involved_SFC_data_counter.getKey());
          recentQoS =
              SelectedSFCs.get(involved_SFC_data_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getQoS();
          RSPID=involved_SFC_data_counter.getKey().toString();

        } else if (SelectedSFCs.get(involved_SFC_data_counter.getKey())
                               .getSFCdictInfo()
                               .getSfcDict()
                               .getPaths()
                               .get(0)
                               .getQoS() == recentQoS) {
          if (SelectedSFCs.get(involved_SFC_data_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0)
                          .getPathTrafficLoad() > Load) {
            selectedChain = SelectedSFCs.get(involved_SFC_data_counter.getKey());
            Load = SelectedSFCs.get(involved_SFC_data_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get
                (0).getPathTrafficLoad();
            recentQoS =
                SelectedSFCs.get(involved_SFC_data_counter.getKey()).getSFCdictInfo().getSfcDict().getPaths().get(0).getQoS();
            RSPID=involved_SFC_data_counter.getKey().toString();


          }
        }

        double MinLoad=Double.POSITIVE_INFINITY;

        //Allocate the selected SFC to lowest Load of the VNF instances
        Iterator vnf_TL_counter = PrevVNFTrafficLoad.entrySet().iterator();
        while (vnf_TL_counter.hasNext()) {
          Map.Entry VNF_key = (Map.Entry) vnf_TL_counter.next();
          if(PrevVNFTrafficLoad.get(VNF_key)<MinLoad){
            MinLoad=PrevVNFTrafficLoad.get(VNF_key);
            SelectedVNFinstance=VNF_key.toString();
          }

        }
      }

      SelectedSFCs.remove(RSPID);

      Iterator it_new = All_SFCs.entrySet().iterator();
      VNFdict SelectedVNFdict=null;
      while (it_new.hasNext()) {
        Map.Entry SFCdata_counter = (Map.Entry) it_new.next();
        HashMap<Integer, VNFdict> VNFs = All_SFCs.get(SFCdata_counter.getKey()).getChainSFs();
        Iterator vnfs_count = VNFs.entrySet().iterator();
        while (vnfs_count.hasNext()) {
          Map.Entry VNFcounter = (Map.Entry) vnfs_count.next();
          if (VNFs.get(VNFcounter.getKey()).getName().equals(SelectedVNFinstance)){
            SelectedVNFdict=VNFs.get(VNFcounter.getKey());
            break;

          }

        }
      }

      CreateChain(selectedChain,SelectedVNFdict);
    }



  }

public void CreateChain(SFC.SFC_Data Chain_Data, VNFdict VNF_instance){

}

}
