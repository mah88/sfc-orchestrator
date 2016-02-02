package org.project.sfc.com.ODL_SFC;
import java.text.MessageFormat;
import java.util.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.project.sfc.com.SFCJSON.ServiceFunctionChains;
import org.project.sfc.com.SFFJSON.ServiceFunctionForwarders;
import org.project.sfc.com.SFJSON.SFJSON;
import org.project.sfc.com.SFJSON.ServiceFunction;
import org.project.sfc.com.SFJSON.ServiceFunctions;
import org.project.sfc.com.SFJSON.SfDataPlaneLocator;
import org.project.sfc.com.SFPJSON.ServiceFunctionPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.System.*;
import org.yaml.snakeyaml.Yaml;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
/**
 * Created by mah on 1/27/16.
 */

public class Opendaylight {

    public String ODL_ip="127.0.0.1";
    public String ODL_port="8080";
    public String ODL_username="admin";
    public String ODL_password="admin";
    public String Config_SF_URL="restconf/config/service-function:service-functions/service-function/{}/";
    public String Config_SFF_URL="restconf/config/service-function-forwarder:service-function-forwarders/service-function-forwarder/{}/";
    public String Config_SFC_URL="restconf/config/service-function-chain:service-function-chains/service-function-chain/{}/ ";
    public String Config_SFP_URL="restconf/config/service-function-path:service-function-paths/service-function-path/{}";
    private static Logger logger = LoggerFactory.getLogger(Opendaylight.class);

    private void init(){

       this.ODL_ip=ODL_ip;
        this.ODL_port=ODL_port;
        this.ODL_username=ODL_username;
        this.ODL_password=ODL_password;
        this.Config_SF_URL=Config_SF_URL;
        this.Config_SFC_URL=Config_SFC_URL;
        this.Config_SFF_URL=Config_SFF_URL;
        this.Config_SFP_URL=Config_SFP_URL;
        int sff_counter=1;



    }
    public String sendRest(Gson data,String rest_type,String url)
    {
        String reply="ERROR Occured in Send REST";


        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;


        try  {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(this.ODL_username,this.ODL_password));
            com.google.gson.Gson gson1 = new com.google.gson.Gson();
            String json = gson1.toJson(data, String.class);
            StringEntity params=new StringEntity(data.toString());
            if (rest_type=="GET"){

                HttpGet request=new HttpGet(Full_URL);
                request.addHeader("content-type", "application/json");

                HttpResponse result = httpclient.execute(request);

                String jsonx = EntityUtils.toString(result.getEntity(), "UTF-8");
                com.google.gson.Gson gson = new com.google.gson.Gson();
                Response respuesta = gson.fromJson(json,Response.class);

                System.out.println(respuesta.getExample());
                System.out.println(respuesta.getFr());
                reply=respuesta.getFr();
                logger.debug("rest call response: "+ reply);


            }else if (rest_type=="POST"){
                HttpPost request=new HttpPost(Full_URL);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse result = httpclient.execute(request);

                String jsonx = EntityUtils.toString(result.getEntity(), "UTF-8");
                com.google.gson.Gson gson = new com.google.gson.Gson();
                Response respuesta = gson.fromJson(json,Response.class);

                System.out.println(respuesta.getExample());
                System.out.println(respuesta.getFr());
                reply=respuesta.getFr();
                logger.debug("rest call response: "+ reply);

            }else if (rest_type=="PUT"){
                HttpPut request=new HttpPut(Full_URL);
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                HttpResponse result = httpclient.execute(request);

                String jsonx = EntityUtils.toString(result.getEntity(), "UTF-8");
                com.google.gson.Gson gson = new com.google.gson.Gson();
                Response respuesta = gson.fromJson(json,Response.class);

                System.out.println(respuesta.getExample());
                System.out.println(respuesta.getFr());
                reply=respuesta.getFr();
                logger.debug("rest call response: "+ reply);

            }else if (rest_type=="DELETE"){
                HttpDelete request=new HttpDelete(Full_URL);
                request.addHeader("content-type", "application/json");
                HttpResponse result = httpclient.execute(request);

                String jsonx = EntityUtils.toString(result.getEntity(), "UTF-8");
                com.google.gson.Gson gson = new com.google.gson.Gson();
                Response respuesta = gson.fromJson(json,Response.class);

                System.out.println(respuesta.getExample());
                System.out.println(respuesta.getFr());
                reply=respuesta.getFr();
                logger.debug("rest call response: "+ reply);

            }

        } catch (IOException ex) {
        }
        return reply;

    }
    public String getNetworkTopologyList(){
        String url = "restconf/operational/network-topology:network-topology/";
        String network = this.sendRest(null, "GET", url);
        return network;
    }
    //ODL SFF Stuff (Get, Create, Update, Delete)
    public String getODLsff(){
        String url = "restconf/config/service-function-forwarder:service-function-forwarders/";
        String sff_response = this.sendRest(null, "GET", url);
        return sff_response;
    }

    public String createODLsff(Gson sffJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sffJSON, String.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionForwarders>>() {}.getType();
        Map<String, ServiceFunctionForwarders> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sff_name = map.get("service-function-forwarders").getServiceFunctionForwarder().get(0).getName();
        String sff_result = this.sendRest(sffJSON, "PUT", MessageFormat.format(this.Config_SFF_URL,sff_name));
        return sff_result;
    }

    public String updateODLsff(Gson sffJSON){

        String sff_result=this.sendRest(sffJSON,"PUT",this.Config_SFF_URL);
        return sff_result;
    }

    public String deleteODLsff(Gson sffJSON){
        String sff_result=this.sendRest(sffJSON,"DELETE",this.Config_SFF_URL);
        return sff_result;
    }
    //ODL SFs Stuff (Create, Update, Delete)
    public String createODLsf(Gson sfJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfJSON, String.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctions>>() {}.getType();
        Map<String, ServiceFunctions> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sf_name = map.get("service-functions").getServiceFunction().get(0).getName();
        String sf_result = this.sendRest(sfJSON, "PUT", MessageFormat.format(this.Config_SF_URL,sf_name));
        return sf_result;
    }
    public String updateODLsf(Gson sfJSON){

        String sf_result=this.sendRest(sfJSON,"PUT",this.Config_SF_URL);
        return sf_result;
    }

    public String deleteODLsf(Gson sfJSON){
        String sf_result=this.sendRest(sfJSON,"DELETE",this.Config_SF_URL);
        return sf_result;
    }
    //ODL SFC stuff (Create, Update, Delete)
    public String createODLsfc(Gson sfcJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfcJSON, String.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionChains>>() {}.getType();
        Map<String, ServiceFunctionChains> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sfc_name = map.get("service-function-chains").getServiceFunctionChain().get(0).getName();
        String sfc_result = this.sendRest(sfcJSON, "PUT", MessageFormat.format(this.Config_SFC_URL,sfc_name));
        return sfc_result;
    }
    public String updateODLsfc(Gson sfcJSON){

        String sfc_result=this.sendRest(sfcJSON,"PUT",this.Config_SFC_URL);
        return sfc_result;
    }

    public String deleteODLsfc(Gson sfcJSON){
        String sfc_result=this.sendRest(sfcJSON,"DELETE",this.Config_SFC_URL);
        return sfc_result;
    }
    //ODL SFP stuff (Create, Update, Delete)
    public String createODLsfp(Gson sfpJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfpJSON, String.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionPaths>>() {}.getType();
        Map<String, ServiceFunctionPaths> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sfp_name = map.get("service-function-paths").getServiceFunctionPath().get(0).getName();
        String sfp_result = this.sendRest(sfpJSON, "PUT", MessageFormat.format(this.Config_SFP_URL,sfp_name));
        return sfp_result;
    }
    public String updateODLsfp(Gson sfpJSON){

        String sfp_result=this.sendRest(sfpJSON,"PUT",this.Config_SFC_URL);
        return sfp_result;
    }

    public String deleteODLsfp(Gson sfpJSON){
        String sfp_result=this.sendRest(sfpJSON,"DELETE",this.Config_SFP_URL);
        return sfp_result;
    }
    //ODL RSP stuff (Create, Delete)
    public String createODLrsp(Gson rspJSON){
        String url = "restconf/operations/rendered-service-path:create-rendered-path";
        String rsp_result = this.sendRest(rspJSON, "POST", url);
        return rsp_result;
    }
    public String deleteODLrsp(Gson rspJSON){
        String url = "restconf/operations/rendered-service-path:delete-rendered-path/";
        String rsp_result = this.sendRest(rspJSON, "DELETE", url);
        return rsp_result;
    }

    public Integer CreateSFC(SFCdict sfc_dict, Map<Integer,VNFdict> vnf_dict){
     Long SFC_id=sfc_dict.getId();
        String dp_loc="sf-data-plane-locator";
        ServiceFunctions sfs_json=new ServiceFunctions();
        HashMap<Integer,Long> sf_net_map=new HashMap<Integer, Long>();
        SFJSON FullSFjson=new SFJSON();
        Integer SF_ID;
        List<ServiceFunction> list_sfs=new ArrayList<>();
        for(int sf_i=0;sf_i<sfc_dict.getChain().size();sf_i++){
            ServiceFunction sf_json=new ServiceFunction();
            SF_ID=sf_i;
            sf_json.setName(vnf_dict.get(sf_i).getName());
            SfDataPlaneLocator dplocDict=new  SfDataPlaneLocator ();
            dplocDict.setName("vxlan");
            dplocDict.setIp(vnf_dict.get(sf_i).getIP());
            dplocDict.setPort("6633");
            dplocDict.setTransport("service-locator:vxlan-gpe");
            dplocDict.setServiceFunctionForwarder("dummy");
            sf_json.setNshAware("true");
            sf_json.setIpMgmtAddress(vnf_dict.get(sf_i).getIP());
            sf_json.setType("service-function-type:"+vnf_dict.get(sf_i).getType());
            List< SfDataPlaneLocator > list_dploc=new ArrayList<>();
            list_dploc.add(0,dplocDict);
            sf_json.setSfDataPlaneLocator(list_dploc);
            list_sfs.add(SF_ID,sf_json);
            sfs_json.setServiceFunction(list_sfs);
            FullSFjson.setServiceFunctions(sfs_json);
            sf_net_map.put(SF_ID,vnf_dict.get(sf_i).getNeutronPortId();
        }

        //ovs_mapping=locate_ovs_to_sf(set_net_map); need a function for locating






    }



    public class SFCdict{
        private Long id;
        private String name;
        private List<String> chain=new ArrayList<String>();
        private Boolean symmetrical;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Long getId() {
            return id;
        }
        public void setId(Long ID) {
            this.id = ID;
        }
        public List<String> getChain() {
            return chain;
        }
        public void setChain(List<String> chain) {
            this.chain = chain;
        }
        public Boolean isSymmetrical() {
            return symmetrical;
        }
        public void setSymmetrical(Boolean symm) {
            this.symmetrical = symm;
        }

    }

    public class VNFdict{
        private String ip;
        private String name;
        private String type;
        private Long neutronPortId;


        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Long getNeutronPortId() {
            return neutronPortId;
        }
        public void setNeutronPortId(Long ID) {
            this.neutronPortId = ID;
        }
        public String getIP() {
            return ip;
        }
        public void setIP(String ip) {
            this.ip = ip;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }


    }



    public class Response{

        private String example;
        private String fr;

        public String getExample() {
            return example;
        }
        public void setExample(String example) {
            this.example = example;
        }
        public String getFr() {
            return fr;
        }
        public void setFr(String fr) {
            this.fr = fr;
        }
    }
}
