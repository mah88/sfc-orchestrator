package org.project.sfc.com.ODL_SFC;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.project.sfc.com.SFCJSON.SFCJSON;
import org.project.sfc.com.SFCJSON.ServiceFunctionChain;
import org.project.sfc.com.SFCJSON.ServiceFunctionChains;
import org.project.sfc.com.SFFJSON.SFFJSON;
import org.project.sfc.com.SFFJSON.ServiceFunctionForwarders;
import org.project.sfc.com.SFJSON.SFJSON;
import org.project.sfc.com.SFJSON.ServiceFunction;
import org.project.sfc.com.SFJSON.ServiceFunctions;
import org.project.sfc.com.SFJSON.SfDataPlaneLocator;
import org.project.sfc.com.SFPJSON.SFPJSON;
import org.project.sfc.com.SFPJSON.ServiceFunctionPath;
import org.project.sfc.com.SFPJSON.ServiceFunctionPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.sfc.com.RSPJSON.RSPJSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

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

    public  ResponseEntity<String> sendRest_SF(SFJSON data,String rest_type,String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_ip;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
             //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        SFJSON result=new SFJSON();
        ResponseEntity<String> request=null;
        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<>(mapper.toJson(data,SFJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),SFJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFJSON.class));

            }
        } else if (rest_type=="PUT"){
            HttpEntity <String> putEntity=new HttpEntity<>(mapper.toJson(data,SFJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),SFJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFJSON.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<>(headers);
             request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),SFJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<>(headers);
             request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),SFJSON.class);
                logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

    return request;

    }

    public ResponseEntity<String> sendRest_SFF(SFFJSON data,String rest_type,String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_ip;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        SFFJSON result=new SFFJSON();
        ResponseEntity<String> request=null;
        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<>(mapper.toJson(data,SFFJSON.class),headers);
             request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SFF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFFJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFFJSON.class));

            }
        } else if (rest_type=="PUT"){
            HttpEntity <String> putEntity=new HttpEntity<>(mapper.toJson(data,SFJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SFF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFFJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFFJSON.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<>(headers);
           request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SFF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFFJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<>(headers);
             request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFFJSON.class);
                logger.debug("Setting of SFF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

        return request;

    }

    public ResponseEntity<String> sendRest_SFP(SFPJSON data, String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_ip;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        SFPJSON result=new SFPJSON();
        ResponseEntity<String> request=null;

        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<>(mapper.toJson(data,SFPJSON.class),headers);
             request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFPJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFPJSON.class));

            }
        } else if (rest_type=="PUT"){
            HttpEntity <String> putEntity=new HttpEntity<>(mapper.toJson(data,SFPJSON.class),headers);
           request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFPJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFPJSON.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<>(headers);
             request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFPJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<>(headers);
             request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFPJSON.class);
                logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

        return request;

    }
    public ResponseEntity<String> sendRest_RSP(RSPJSON data, String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_ip;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        RSPJSON result=new RSPJSON();
        ResponseEntity<String> request=null;

        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<>(mapper.toJson(data,RSPJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of RSP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),RSPJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,RSPJSON.class));

            }
        } else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<>(headers);
            request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of RSP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),RSPJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }

        return request;

    }
    public ResponseEntity<String> sendRest_SFC(SFCJSON data, String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_ip;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        SFCJSON result=new SFCJSON();
        ResponseEntity<String> request=null;
        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<>(mapper.toJson(data,SFCJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFCJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFCJSON.class));

            }
        } else if (rest_type=="PUT"){
            HttpEntity <String> putEntity=new HttpEntity<>(mapper.toJson(data,SFCJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SFC has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFCJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,SFCJSON.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<>(headers);
             request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SFC has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFCJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<>(headers);
             request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),SFCJSON.class);
                logger.debug("Setting of SFC has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

        return request;

    }

     /*
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
    */
    //we have to know how should look like the NetworkTopologyList
    public String getNetworkTopologyList(){
        String url = "restconf/operational/network-topology:network-topology/";
        String network = this.sendRest(null, "GET", url);
        return network;
    }
    //ODL SFF Stuff (Get, Create, Update, Delete)
    public  ResponseEntity<String> getODLsff(){
        String url = "restconf/config/service-function-forwarder:service-function-forwarders/";
        ResponseEntity<String> sff_response = this.sendRest_SFF(null, "GET", url);
        return sff_response;
    }

    public  ResponseEntity<String> createODLsff(SFFJSON sffJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sffJSON, SFFJSON.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionForwarders>>() {}.getType();
        Map<String, ServiceFunctionForwarders> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sff_name = map.get("service-function-forwarders").getServiceFunctionForwarder().get(0).getName();
        ResponseEntity<String> sff_result = this.sendRest_SFF(sffJSON, "PUT", MessageFormat.format(this.Config_SFF_URL,sff_name));
        return sff_result;
    }

    public  ResponseEntity<String> updateODLsff(SFFJSON sffJSON){

        ResponseEntity<String> sff_result=this.sendRest_SFF(sffJSON,"PUT",this.Config_SFF_URL);
        return sff_result;
    }

    public  ResponseEntity<String> deleteODLsff(SFFJSON sffJSON){
        ResponseEntity<String> sff_result=this.sendRest_SFF(sffJSON,"DELETE",this.Config_SFF_URL);
        return sff_result;
    }
    //ODL SFs Stuff (Create, Update, Delete)
    public  ResponseEntity<String> createODLsf(SFJSON sfJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfJSON, SFJSON.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctions>>() {}.getType();
        Map<String, ServiceFunctions> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sf_name = map.get("service-functions").getServiceFunction().get(0).getName();
        ResponseEntity<String> sf_result = this.sendRest_SF(sfJSON, "PUT", MessageFormat.format(this.Config_SF_URL,sf_name));
        return sf_result;
    }
    public  ResponseEntity<String> updateODLsf(SFJSON sfJSON){

        ResponseEntity<String> sf_result=this.sendRest_SF(sfJSON,"PUT",this.Config_SF_URL);
        return sf_result;
    }

    public  ResponseEntity<String> deleteODLsf(SFJSON sfJSON){
        ResponseEntity<String> sf_result=this.sendRest_SF(sfJSON,"DELETE",this.Config_SF_URL);
        return sf_result;
    }
    //ODL SFC stuff (Create, Update, Delete)
    public  ResponseEntity<String> createODLsfc(SFCJSON sfcJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfcJSON, SFCJSON.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionChains>>() {}.getType();
        Map<String, ServiceFunctionChains> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sfc_name = map.get("service-function-chains").getServiceFunctionChain().get(0).getName();
        ResponseEntity<String> sfc_result = this.sendRest_SFC(sfcJSON, "PUT", MessageFormat.format(this.Config_SFC_URL,sfc_name));
        return sfc_result;
    }
    public  ResponseEntity<String> updateODLsfc(SFCJSON sfcJSON){

        ResponseEntity<String> sfc_result=this.sendRest_SFC(sfcJSON,"PUT",this.Config_SFC_URL);
        return sfc_result;
    }

    public  ResponseEntity<String> deleteODLsfc(SFCJSON sfcJSON){
        ResponseEntity<String> sfc_result=this.sendRest_SFC(sfcJSON,"DELETE",this.Config_SFC_URL);
        return sfc_result;
    }
    //ODL SFP stuff (Create, Update, Delete)
    public  ResponseEntity<String> createODLsfp(SFPJSON sfpJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfpJSON, SFPJSON.class);
        Response respuesta = gson.fromJson(json,Response.class);
        Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionPaths>>() {}.getType();
        Map<String, ServiceFunctionPaths> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        String sfp_name = map.get("service-function-paths").getServiceFunctionPath().get(0).getName();
        ResponseEntity<String> sfp_result = this.sendRest_SFP(sfpJSON, "PUT", MessageFormat.format(this.Config_SFP_URL,sfp_name));
        return sfp_result;
    }
    public  ResponseEntity<String> updateODLsfp(SFPJSON sfpJSON){

        ResponseEntity<String> sfp_result=this.sendRest_SFP(sfpJSON,"PUT",this.Config_SFC_URL);
        return sfp_result;
    }

    public  ResponseEntity<String> deleteODLsfp(SFPJSON sfpJSON){
        ResponseEntity<String> sfp_result=this.sendRest_SFP(sfpJSON,"DELETE",this.Config_SFP_URL);
        return sfp_result;
    }


    //ODL RSP stuff (Create, Delete) 
    public ResponseEntity<String> createODLrsp(RSPJSON rspJSON){
        String url = "restconf/operations/rendered-service-path:create-rendered-path";
        ResponseEntity<String> rsp_result = this.sendRest_RSP(rspJSON, "POST", url);
        return rsp_result;
    }
    public ResponseEntity<String> deleteODLrsp(RSPJSON rspJSON){
        String url = "restconf/operations/rendered-service-path:delete-rendered-path/";
        ResponseEntity<String> rsp_result = this.sendRest_RSP(rspJSON, "DELETE", url);
        return rsp_result;
    }

    public String CreateSFC(SFCdict sfc_dict, HashMap<Integer,VNFdict> vnf_dict){
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
         //   FullSFjson.setServiceFunctions(sfs_json);
            sf_net_map.put(SF_ID,vnf_dict.get(sf_i).getNeutronPortId());
        }
        // need to be adjusted
        HashMap<String,BridgeMapping> ovs_mapping=Locate_ovs_to_sf(sf_net_map);
        logger.debug("OVS MAP: "+ovs_mapping.toString());
        for(int br_map_counter=0;br_map_counter<ovs_mapping.size();br_map_counter++){
            for(int sf_id_counter=0;sf_id_counter<ovs_mapping.get(br_map_counter).sfs.size();sf_id_counter++){
                sfs_json.getServiceFunction().get(sf_id_counter).getSfDataPlaneLocator().get(0).setServiceFunctionForwarder(ovs_mapping.get(br_map_counter).getSFFname());
                sfs_json.getServiceFunction().get(sf_id_counter).getSfDataPlaneLocator().get(0).setName(ovs_mapping.get(br_map_counter).getSfs().get(sf_id_counter).getTap_port());
             //   FullSFjson.setServiceFunctions(sfs_json);
                logger.debug("SF updated with SFF:"+ ovs_mapping.get(br_map_counter).getSFFname());

            }
        }

        for(int sf_j=0;sf_j<sfs_json.getServiceFunction().size();sf_j++){
            FullSFjson.getServiceFunctions().getServiceFunction().add(sf_j,sfs_json.getServiceFunction().get(sf_j));
            ResponseEntity<String> sf_result=createODLsf(FullSFjson);
            if(!sf_result.getStatusCode().is2xxSuccessful()){
                logger.error("Unable to create ODL SF "+ FullSFjson.toString());
            }
        }


        //building SFF need to be added // TODO: 2/4/2016  

         //create SFC
        SFCJSON sfc_json=create_sfc_json(sfc_dict,vnf_dict);
        ResponseEntity<String> sfc_result=createODLsfc(sfc_json);
        if (!sfc_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to create ODL SFC");
        }

        //create SFP
        SFPJSON sfp_json=create_sfp_json(sfc_dict);
        ResponseEntity<String> sfp_result=createODLsfp(sfp_json);
        if (!sfp_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to create ODL SFP");
        }

        RSPJSON rsp_json=create_rsp_json(sfp_json);
        ResponseEntity<String> rsp_result=createODLrsp(rsp_json);
        if (!rsp_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to create ODL RSP");
        }

        String RSP_created=rsp_result.getBody();
        return RSP_created;



    }

    //create RSP JSON
    public static RSPJSON create_rsp_json(SFPJSON sfp_json){
        RSPJSON rsp_json=new RSPJSON();
        rsp_json.getInput().setName(sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName());
        rsp_json.getInput().setParentServiceFunctionPath(sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName());
        rsp_json.getInput().setSymmetric(sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getSymmetric());
        return rsp_json;
    }
    public static SFPJSON create_sfp_json(SFCdict sfc_dict){
        SFPJSON sfp_json=new SFPJSON();
        ServiceFunctionPath sfp=new ServiceFunctionPath();
        sfp.setName("Path-"+sfc_dict.getName());
        sfp.setServiceChainName(sfc_dict.getName());
        sfp.setSymmetric(sfc_dict.isSymmetrical());
        // need to change the 0 to the size of the current SFP --> need database creation
        sfp_json.getServiceFunctionPaths().getServiceFunctionPath().add(0,sfp);
        return sfp_json;

    }

    public static SFCJSON create_sfc_json(SFCdict sfc_dict, HashMap<Integer,VNFdict> vnf_dict){
        ServiceFunctionChain sfc=new ServiceFunctionChain();
        SFCJSON sfc_json=new SFCJSON();
       for (int sf=0;sf<sfc_dict.getChain().size();sf++){
           sfc.getSfcServiceFunction().get(sf).setName(vnf_dict.get(sf).getName());
           sfc.getSfcServiceFunction().get(sf).setType("service-function-type:"+vnf_dict.get(sf).getType());
           // need to change the 0 to the size of the current SFCs --> need database creation
           sfc_json.getServiceFunctionChains().getServiceFunctionChain().add(0,sfc);
       }
        sfc_json.getServiceFunctionChains().getServiceFunctionChain().get(0).setName(sfc_dict.getName());
        sfc_json.getServiceFunctionChains().getServiceFunctionChain().get(0).setSymmetric(sfc_dict.isSymmetrical());
        return sfc_json;

    }
//// TODO: 2/4/2016  
    //param sfs_dict: dictionary of SFs by id to network id (neutron port id)
    //return: dictionary mapping sfs to bridge name
    public HashMap<String,BridgeMapping> Locate_ovs_to_sf(HashMap<Integer,Long> sfs_dict){
        String response=getNetworkTopologyList();
     //   if(response.getStatusCode!=200)
        String network = response;  //need to be modified

        if(network==null){
            return null;
        }

        BridgeMapping br_mapping=new BridgeMapping();

        //network map=network.getNetworkTopology.getTopology(); need to be adjusted
        for(int i=0;i<sfs_dict.size();i++){
          //  br_dict=Find_ovs_br(sfs_dict.get(i),network_map); //need to be adjusted

        }
    }

    public class BridgeMapping{
        private String br_name;
        private List<SF_dict> sfs=new ArrayList<>();
       // private SF_dict SF_id=new SF_dict();
        private String ovs_ip;
        private String sff_name;
        public String getBr_name() {
            return br_name;
        }
        public void setBr_name(String name) {
            this.br_name = name;
        }
      /*  public SF_dict getSFId(){
            return SF_id;
        }
        public void setSFId(SF_dict ID) {
            this.SF_id = ID;
        }
        */
        public List<SF_dict> getSfs() {
            return sfs;
        }
        public void setSfs(List<SF_dict> sfs) {
            this.sfs = sfs;
        }
        public String getOVSip() {
            return ovs_ip;
        }
        public void setOVSip(String ovs_ip) {
            this.ovs_ip = ovs_ip;
        }
        public String getSFFname() {
            return sff_name;
        }
        public void setSFFname(String sff_name) {
            this.sff_name= sff_name;
        }
    }
    public class SF_dict{
        private Integer sf_id;
        private String tap_port;
        public String getTap_port() {
            return tap_port;
        }
        public void setTap_port(String name) {
            this.tap_port = name;
        }
        public Integer getSFId() {
            return sf_id;
        }
        public void setSFId(Integer ID) {
            this.sf_id = ID;
        }
    }
    public class SFCdict{
        private Long id;
        private String name;
        private List<String> chain=new ArrayList<String>();
        private String symmetrical;

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
        public String isSymmetrical() {
            return symmetrical;
        }
        public void setSymmetrical(String symm) {
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
