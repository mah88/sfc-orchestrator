package org.project.sfc.com.ODL_SFC_driver.ODL_SFC;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.project.sfc.com.ODL_SFC_driver.JSON.NetvirtProvidersConfigJSON.NetvirtProvidersConfig;
import org.project.sfc.com.ODL_SFC_driver.JSON.NetvirtProvidersConfigJSON.NetvirtProvidersConfig_;
import org.project.sfc.com.ODL_SFC_driver.JSON.NetworkJSON.NetworkJSON;
import org.project.sfc.com.ODL_SFC_driver.JSON.NetworkJSON.NetworkTopology;
import org.project.sfc.com.ODL_SFC_driver.JSON.RSPJSON.Input;
import org.project.sfc.com.ODL_SFC_driver.JSON.RSPs.RenderedServicePaths;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCJSON.SFCJSON;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCJSON.ServiceFunctionChain;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCJSON.ServiceFunctionChains;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCJSON.SfcServiceFunction;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFFJSON.*;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFJSON.*;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFPJSON.SFPJSON;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFPJSON.ServiceFunctionPath;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFPJSON.ServiceFunctionPaths;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFCdict.SFCdict;
import org.project.sfc.com.ODL_SFC_driver.JSON.SfcOfRendererConfigJSON.SfcOfRendererConfig;
import org.project.sfc.com.ODL_SFC_driver.JSON.SfcOfRendererConfigJSON.SfcOfRendererConfigJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.sfc.com.ODL_SFC_driver.JSON.RSPJSON.RSPJSON;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mah on 1/27/16.
 */

public class Opendaylight {

    public String ODL_ip="192.168.0.138";
    public String ODL_port="8080";
    public String ODL_username="admin";
    public String ODL_password="admin";
    public String Config_SF_URL="restconf/config/service-function:service-functions/service-function/{0}/";
    public String Config_SFF_URL="restconf/config/service-function-forwarder:service-function-forwarders/service-function-forwarder/{0}/";
    public String Config_SFC_URL="restconf/config/service-function-chain:service-function-chains/service-function-chain/{0}/";
    public String Config_SFP_URL="restconf/config/service-function-path:service-function-paths/service-function-path/{0}";
    public int sff_counter=1;
    public String Config_sfc_of_render_URL="restconf/config/sfc-of-renderer:sfc-of-renderer-config";
    public String Config_netvirt_URL="restconf/config/netvirt-providers-config:netvirt-providers-config";


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

      this.sff_counter=sff_counter;



    }

    public ResponseEntity<String> sendRest_netvirt_conf(NetvirtProvidersConfig data,String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
        //  headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        NetvirtProvidersConfig result=new NetvirtProvidersConfig();
        ResponseEntity<String> request = null;

        if (rest_type=="PUT") {
            HttpEntity<String> putEntity = new HttpEntity<String>(mapper.toJson(data, NetvirtProvidersConfig.class), headers);
            System.out.println("Entry : >> " + putEntity);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("configuration of netvirt status:" + request.getStatusCode() + " with body: " + request.getBody());

            if (!request.getStatusCode().is2xxSuccessful()) {
                result = null;
            } else {
                result = mapper.fromJson(request.getBody(), NetvirtProvidersConfig.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result, NetvirtProvidersConfig.class));

            }
        }
        else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<String>(headers);
            System.out.println("Entry : >> " + getEntity);
            request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            logger.debug("Getting of netvirt status:" + request.getStatusCode() + " with body: " + request.getBody());

            if (!request.getStatusCode().is2xxSuccessful()) {
                result = null;
            } else {
                result = mapper.fromJson(request.getBody(), NetvirtProvidersConfig.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result, NetvirtProvidersConfig.class));

            }
        }


        return request;

    }
    public  ResponseEntity<String> Configure_NETVIRT(){
        NetvirtProvidersConfig dataJSON=new NetvirtProvidersConfig();
        dataJSON.setNetvirtProvidersConfig(new NetvirtProvidersConfig_());
        dataJSON.getNetvirtProvidersConfig().setTableOffset(10);

        ResponseEntity<String>  netvirt_result=this.sendRest_netvirt_conf(dataJSON,"PUT",this.Config_netvirt_URL);
        return netvirt_result;
    }
    public  boolean Check_Configuration_NETVIRT(){
        NetvirtProvidersConfig dataJSON=new NetvirtProvidersConfig();
        Gson mapper=new Gson();
        boolean check_result=false;

        ResponseEntity<String>  netvirt_result=this.sendRest_netvirt_conf(null,"GET",this.Config_netvirt_URL);
        dataJSON= mapper.fromJson(netvirt_result.getBody(), NetvirtProvidersConfig.class);
        if (dataJSON.getNetvirtProvidersConfig().getTableOffset()==10){
            check_result=true;
        }
        return check_result;
    }
    public  ResponseEntity<String> Configure_SfcOfRenderer(){
        SfcOfRendererConfigJSON dataJSON=new SfcOfRendererConfigJSON();
        dataJSON.setSfcOfRendererConfig(new SfcOfRendererConfig());
        dataJSON.getSfcOfRendererConfig().setSfcOfAppEgressTableOffset(11);
        dataJSON.getSfcOfRendererConfig().setSfcOfTableOffset(150);
        ResponseEntity<String>  sfc_OF_result=this.sendRest_SfcOFrender_conf(dataJSON,"PUT",this.Config_sfc_of_render_URL);
        return sfc_OF_result;
    }

    public  boolean Check_Configuration_SfcOfRenderer(){
        SfcOfRendererConfigJSON dataJSON=new SfcOfRendererConfigJSON();
        Gson mapper=new Gson();
        boolean check_result=false;

        ResponseEntity<String>  sfc_OF_result=this.sendRest_SfcOFrender_conf(null,"GET",this.Config_sfc_of_render_URL);
        dataJSON= mapper.fromJson(sfc_OF_result.getBody(), SfcOfRendererConfigJSON.class);
        if (dataJSON.getSfcOfRendererConfig().getSfcOfAppEgressTableOffset()==11 && dataJSON.getSfcOfRendererConfig().getSfcOfTableOffset()==150){
            check_result=true;
        }
        return check_result;
    }

    public ResponseEntity<String> sendRest_SfcOFrender_conf(SfcOfRendererConfigJSON data, String rest_type,String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
        //  headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        SfcOfRendererConfigJSON result=new SfcOfRendererConfigJSON();
        ResponseEntity<String> request=null;

        if(rest_type=="PUT") {
            HttpEntity<String> putEntity = new HttpEntity<String>(mapper.toJson(data, SfcOfRendererConfigJSON.class), headers);
            System.out.println("Entry : >> " + putEntity);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("configuration of netvirt status:" + request.getStatusCode() + " with body: " + request.getBody());

            if (!request.getStatusCode().is2xxSuccessful()) {
                result = null;
            } else {
                result = mapper.fromJson(request.getBody(), SfcOfRendererConfigJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result, SfcOfRendererConfigJSON.class));

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<String>(headers);
            System.out.println("Entry : >> " + getEntity);
            request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            logger.debug("Getting of netvirt status:" + request.getStatusCode() + " with body: " + request.getBody());

            if (!request.getStatusCode().is2xxSuccessful()) {
                result = null;
            } else {
                result = mapper.fromJson(request.getBody(), SfcOfRendererConfigJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result, SfcOfRendererConfigJSON.class));

            }
        }


        return request;

    }



    public  ResponseEntity<String> sendRest_SF(SFJSON datax,String rest_type,String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
             //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);
      //  headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        ServiceFunctions result=new ServiceFunctions();
        ResponseEntity<String> request=null;
        ServiceFunctions data=null;
        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctions.class),headers);
            request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),ServiceFunctions.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctions.class));

            }
        } else if (rest_type=="PUT"){
            data=datax.getServiceFunctions();
            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctions.class),headers);
            System.out.println("Entry : >> "+putEntity);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),ServiceFunctions.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctions.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
             request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),ServiceFunctions.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<String>(headers);
             request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                 result = mapper.fromJson(request.getBody(),ServiceFunctions.class);
                logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

    return request;

    }

    public ResponseEntity<String> sendRest_SFF(SFFJSON datax,String rest_type,String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
       headers.add("Accept","application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //  headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        ServiceFunctionForwarders result=new ServiceFunctionForwarders();
        ResponseEntity<String> request=null;
        ServiceFunctionForwarders data=new ServiceFunctionForwarders();
        if(datax!=null){
        data=datax.getServiceFunctionForwarders();}

        if(rest_type=="POST" ) {
            HttpEntity <String> postEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctionForwarders.class),headers);
             request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SFF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionForwarders.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctionForwarders.class));

            }
        } else if (rest_type=="PUT"){
            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctionForwarders.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SFF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionForwarders.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctionForwarders.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
           request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SFF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionForwarders.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            try {
                HttpEntity<String> getEntity = new HttpEntity<String>(headers);
                request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            }catch(HttpClientErrorException ce) {
                result=null;
            }
        }

        return request;

    }

    public ResponseEntity<String> sendRest_SFP(SFPJSON datax, String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
       headers.add("Accept","application/json");
      ///  headers.add("content-type","application/json;charset=utf-8");
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        ServiceFunctionPaths  result=new ServiceFunctionPaths ();
        ResponseEntity<String> request=null;
        ServiceFunctionPaths data=null;
        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctionPaths .class),headers);
             request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionPaths .class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctionPaths .class));

            }
        } else if (rest_type=="PUT"){
            data=datax.getServiceFunctionPaths();
            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctionPaths .class),headers);
           request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionPaths.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctionPaths.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
             request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionPaths.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<String>(headers);
             request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionPaths.class);
                logger.debug("Setting of SFP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

        return request;

    }
    public ResponseEntity<String> sendRest_RSP(RSPJSON data, String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        RSPJSON result=new RSPJSON();
        ResponseEntity<String> request=null;

        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<String>(mapper.toJson(data,RSPJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of RSP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),RSPJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,RSPJSON.class));

            }
        } else if (rest_type=="DELETE") {
            HttpEntity<String> delEntity = new HttpEntity<String>(headers);
            request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of RSP has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if (!request.getStatusCode().is2xxSuccessful()) {
                result = null;
            } else {
                result = mapper.fromJson(request.getBody(), RSPJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode());

            }
        } else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<String>(headers);
            request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            logger.debug("Getting  of all RSPs has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

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
    public ResponseEntity<String> sendRest_SFC(SFCJSON datax, String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        ServiceFunctionChains result=new ServiceFunctionChains();
        ResponseEntity<String> request=null;
        ServiceFunctionChains data=null;
        if(rest_type=="POST") {
            HttpEntity <String> postEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctionChains.class),headers);
            request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionChains.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctionChains.class));

            }
        } else if (rest_type=="PUT"){
            data=datax.getServiceFunctionChains();
            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data,ServiceFunctionChains.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SFC has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionChains.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ServiceFunctionChains.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
             request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SFC has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionChains.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<String>(headers);
             request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ServiceFunctionChains.class);
                logger.debug("Setting of SFC has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

        return request;

    }
/// Send request for getting the Network TOPOLOGY
    public ResponseEntity<String> sendRest_NetworkTopology(NetworkJSON data, String rest_type, String url){

        String Full_URL="http://" + this.ODL_ip + ":" + this.ODL_port + "/" + url;
        String plainCreds = this.ODL_username+":"+this.ODL_password;
      //  byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCreds.getBytes(Charset.forName("US-ASCII")));
        String base64Creds = new String(base64CredsBytes);
        RestTemplate template=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        //   http.createBasicAuthenticationHttpHeaders(username, password);
        headers.add("Accept","application/json");
   //     headers.add("content-type","application/json;charset=utf-8");
        headers.add("Authorization","Basic " + base64Creds);
        Gson mapper=new Gson();
        NetworkJSON result=new NetworkJSON();
        ResponseEntity<String> request=null;
      if (rest_type=="GET"){
            HttpEntity <String> getEntity=new HttpEntity<String>(headers);
            request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),NetworkJSON.class);
                logger.debug("Setting of SFC has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            }
        }

        return request;

    }


    /// Get Network Topology
    public ResponseEntity<String> getNetworkTopologyList(){
        String url = "restconf/operational/network-topology:network-topology/";
        ResponseEntity<String> network = this.sendRest_NetworkTopology(null, "GET", url);
        return network;
    }
    //ODL SFF Stuff (Get, Create, Update, Delete)
    public  ResponseEntity<String> getODLsff(){
        String url = "restconf/config/service-function-forwarder:service-function-forwarders/";
        ResponseEntity<String> sff_response = this.sendRest_SFF(null, "GET", url);
        return sff_response;
    }
    public  ResponseEntity<String> getODLsfc(String sfc_name){
        ResponseEntity<String> sfc_response = this.sendRest_SFC(null, "GET",  MessageFormat.format(this.Config_SFC_URL,sfc_name));
        return sfc_response;
    }

    public  ResponseEntity<String> createODLsff(SFFJSON sffJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sffJSON, SFFJSON.class);
      //  Response respuesta = gson.fromJson(json,Response.class);
       // Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionForwarders>>() {}.getType();
        // Map<String, ServiceFunctionForwarders> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        //String sff_name = map.get("service-function-forwarders").getServiceFunctionForwarder().get(0).getName();
        String sff_name =sffJSON.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getName();
        ResponseEntity<String> sff_result = this.sendRest_SFF(sffJSON, "PUT", MessageFormat.format(this.Config_SFF_URL,sff_name));
        return sff_result;
    }
    //FIXME need to be formated with the name as Create ODL SFF

    public  ResponseEntity<String> updateODLsff(SFFJSON sffJSON, String sffName){

        ResponseEntity<String> sff_result=this.sendRest_SFF(sffJSON,"PUT",MessageFormat.format(this.Config_SFF_URL,sffName));
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
    //    Response respuesta = gson.fromJson(json,Response.class);
      //  Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctions>>() {}.getType();
      //  Map<String, ServiceFunctions> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
      //  System.out.println("<<< service-functions >> "+ json);
       String sf_name =sfJSON.getServiceFunctions().getServiceFunction().get(0).getName();
             //  map.get("service-functions").getServiceFunction().get(0).getName();
        ResponseEntity<String> sf_result = this.sendRest_SF(sfJSON, "PUT", MessageFormat.format(this.Config_SF_URL,sf_name));
        return sf_result;
    }
    //FIXME need to be formated with the name as Create ODL SF

    public  ResponseEntity<String> updateODLsf(SFJSON sfJSON){

        ResponseEntity<String> sf_result=this.sendRest_SF(sfJSON,"PUT",this.Config_SF_URL);
        return sf_result;
    }
    public  ResponseEntity<String> getODLsf(){
        String url = "restconf/config/service-function:service-functions";
        ResponseEntity<String> sf_result=this.sendRest_SF(null,"GET",url);
        return sf_result;
    }
    public  ResponseEntity<String> deleteODLsf(String sf_name){
        ResponseEntity<String> sf_result=this.sendRest_SF(null,"DELETE",MessageFormat.format(this.Config_SF_URL,sf_name));
        return sf_result;
    }
    //ODL SFC stuff (Create, Update, Delete)
    public  ResponseEntity<String> createODLsfc(SFCJSON sfcJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfcJSON, SFCJSON.class);
       // Response respuesta = gson.fromJson(json,Response.class);
        //Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionChains>>() {}.getType();
        //Map<String, ServiceFunctionChains> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
        //String sfc_name = map.get("service-function-chains").getServiceFunctionChain().get(0).getName();
        String sfc_name =sfcJSON.getServiceFunctionChains().getServiceFunctionChain().get(0).getName();
        ResponseEntity<String> sfc_result = this.sendRest_SFC(sfcJSON, "PUT", MessageFormat.format(this.Config_SFC_URL,sfc_name));
        return sfc_result;
    }
    //FIXME need to be formated with the name as Create ODL SFC

    public  ResponseEntity<String> updateODLsfc(SFCJSON sfcJSON){

        ResponseEntity<String> sfc_result=this.sendRest_SFC(sfcJSON,"PUT",this.Config_SFC_URL);
        return sfc_result;
    }

    public  ResponseEntity<String> deleteODLsfc(String sfc_name){
        ResponseEntity<String> sfc_result=this.sendRest_SFC(null,"DELETE",MessageFormat.format(this.Config_SFC_URL,sfc_name));
        return sfc_result;
    }
    //ODL SFP stuff (Create, Update, Delete)
    public  ResponseEntity<String> createODLsfp(SFPJSON sfpJSON){
        com.google.gson.Gson gson = new com.google.gson.Gson();

        String json = gson.toJson(sfpJSON, SFPJSON.class);
       // Response respuesta = gson.fromJson(json,Response.class);
       // Type mapOfMapsType = new TypeToken<Map<String, ServiceFunctionPaths>>() {}.getType();
      //  Map<String, ServiceFunctionPaths> map = gson.fromJson(respuesta.getFr(), mapOfMapsType);
      //  String sfp_name = map.get("service-function-paths").getServiceFunctionPath().get(0).getName();
        String sfp_name =sfpJSON.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName();
        ResponseEntity<String> sfp_result = this.sendRest_SFP(sfpJSON, "PUT", MessageFormat.format(this.Config_SFP_URL,sfp_name));
        return sfp_result;
    }
    //FIXME need to be formated with the name as Create ODL SFP
    public  ResponseEntity<String> updateODLsfp(SFPJSON sfpJSON){

        ResponseEntity<String> sfp_result=this.sendRest_SFP(sfpJSON,"PUT",this.Config_SFP_URL);
        return sfp_result;
    }

    public  ResponseEntity<String> deleteODLsfp(String sfp_name){
        ResponseEntity<String> sfp_result=this.sendRest_SFP(null,"DELETE",MessageFormat.format(this.Config_SFP_URL,sfp_name));
        return sfp_result;
    }


    //ODL RSP stuff (Create, Delete, GET)
    public ResponseEntity<String> createODLrsp(RSPJSON rspJSON){
        String url = "restconf/operations/rendered-service-path:create-rendered-path";
        ResponseEntity<String> rsp_result = this.sendRest_RSP(rspJSON, "POST", url);
        return rsp_result;
    }
    public ResponseEntity<String> deleteODLrsp(RSPJSON rspJSON){
        String url = "restconf/operations/rendered-service-path:delete-rendered-path/";
        ResponseEntity<String> rsp_result = this.sendRest_RSP(rspJSON, "POST", url);
        return rsp_result;
    }

    public ResponseEntity<String> getODLrsp(String rsp_name){
        String url = "restconf/operational/rendered-service-path:rendered-service-paths/rendered-service-path/{0}";
        ResponseEntity<String> rsp_result = this.sendRest_RSP(null, "GET", MessageFormat.format(url,rsp_name));
        return rsp_result;
    }

    public void CreateSFC(SFCdict sfc_dict,HashMap<Integer,VNFdict> vnf_dict) {
        //create SFC
        SFCJSON sfc_json=create_sfc_json(sfc_dict,vnf_dict);
        ResponseEntity<String> sfc_result=createODLsfc(sfc_json);
        if (!sfc_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to create ODL SFC");
        }

    }


        public String CreateSFP(SFCdict sfc_dict, HashMap<Integer,VNFdict> vnf_dict){
        String dp_loc="sf-data-plane-locator";
        ServiceFunctions sfs_json=new ServiceFunctions();
        HashMap<Integer,VNFdict> sf_net_map=new HashMap<Integer, VNFdict>();
        SFJSON FullSFjson=new SFJSON();
        Integer SF_ID;
        List<ServiceFunction> list_sfs=new ArrayList<ServiceFunction>();
        for(int sf_i=0;sf_i<sfc_dict.getSfcDict().getChain().size();sf_i++){

            ServiceFunction sf_json=new ServiceFunction();
            SF_ID=sf_i;
            sf_json.setName(vnf_dict.get(sf_i).getName());
            SfDataPlaneLocator dplocDict=new  SfDataPlaneLocator ();
            //dplocDict.setName("vxlan");
            dplocDict.setName(vnf_dict.get(sf_i).getName()+"-dpl");
            dplocDict.setIp(vnf_dict.get(sf_i).getIP());
            dplocDict.setPort("6633");
            dplocDict.setTransport("service-locator:vxlan-gpe");
            dplocDict.setServiceFunctionForwarder("dummy");
            sf_json.setNshAware("true");
            sf_json.setIpMgmtAddress(vnf_dict.get(sf_i).getIP());
            //sf_json.setType("service-function-type:"+vnf_dict.get(sf_i).getType());
            sf_json.setType(vnf_dict.get(sf_i).getType());
            List< SfDataPlaneLocator> list_dploc=new ArrayList<SfDataPlaneLocator>();
            list_dploc.add(dplocDict);
            sf_json.setSfDataPlaneLocator(list_dploc);
            list_sfs.add(SF_ID,sf_json);
            sfs_json.setServiceFunction(list_sfs);
         //   FullSFjson.setServiceFunctions(sfs_json);

            sf_net_map.put(SF_ID,vnf_dict.get(sf_i));
        }
        // need to be adjusted
      System.out.println("service-function-Net-Map:"+sf_net_map.get(0).getName());
        HashMap<String,BridgeMapping> ovs_mapping=Locate_ovs_to_sf(sf_net_map);
        logger.debug("OVS MAP: "+ovs_mapping.toString());
        Iterator br_map=ovs_mapping.entrySet().iterator();



        while(br_map.hasNext()){
            Map.Entry br_map_counter= (Map.Entry)br_map.next();;
            for(int sf_id_counter=0;sf_id_counter<ovs_mapping.get(br_map_counter.getKey()).sfs.size();sf_id_counter++){
                sfs_json.getServiceFunction().get(sf_id_counter).getSfDataPlaneLocator().get(0).setServiceFunctionForwarder(ovs_mapping.get(br_map_counter.getKey()).getSFFname());
               // sfs_json.getServiceFunction().get(sf_id_counter).getSfDataPlaneLocator().get(0).setName("SF updated with SFF:");
                sfs_json.getServiceFunction().get(sf_id_counter).getSfDataPlaneLocator().get(0).setServiceFunctionOvsOvsPort(new ServiceFunctionOvsOvsPort());
                sfs_json.getServiceFunction().get(sf_id_counter).getSfDataPlaneLocator().get(0).getServiceFunctionOvsOvsPort().setPortID(ovs_mapping.get(br_map_counter.getKey()).getSFdict().get(ovs_mapping.get(br_map_counter.getKey()).getSfs().get(sf_id_counter)).getTap_port());
             //   FullSFjson.setServiceFunctions(sfs_json);
                logger.debug("SF updated with SFF:"+ ovs_mapping.get(br_map_counter.getKey()).getSFFname());

            }

           // br_map.remove();
            System.out.println("OVS_MAPPING >> "+ovs_mapping);
        }

        for(int sf_j=0;sf_j<sfs_json.getServiceFunction().size();sf_j++){

            //check SF Exist in ODL ?
            ServiceFunctions service_functions=new ServiceFunctions();
            List<ServiceFunction> list_service_function=new ArrayList<ServiceFunction>();
            list_service_function.add(sfs_json.getServiceFunction().get(sf_j));
            service_functions.setServiceFunction(list_service_function);
            FullSFjson.setServiceFunctions(service_functions);
            ResponseEntity<String> sf_result=createODLsf(FullSFjson);
            logger.debug("create ODL SF with name >> "+ FullSFjson.getServiceFunctions().getServiceFunction().get(0).getName());
            if(!sf_result.getStatusCode().is2xxSuccessful()){
                logger.error("Unable to create ODL SF "+ FullSFjson.toString());
            }
        }
           SFJSON All_SFs_JSON=new SFJSON();
            All_SFs_JSON.setServiceFunctions(sfs_json);
        List<ServiceFunctionForwarder> sff_list= new ArrayList<ServiceFunctionForwarder>();
        //building SFF
        ServiceFunctionForwarder prev_sff_dict=find_existing_sff(ovs_mapping);
        logger.debug("PREVIOUS sff_dict >> "+ prev_sff_dict);

        if(prev_sff_dict!=null){
            logger.debug("Previous SFF is detected ");
            sff_list=create_sff_json(ovs_mapping,All_SFs_JSON,prev_sff_dict);

        }else {
             sff_list=create_sff_json(ovs_mapping,All_SFs_JSON,null);


        }


        for (int sff_index=0;sff_index<sff_list.size();sff_index++){
            SFFJSON sff_json=new SFFJSON();
            sff_json.setServiceFunctionForwarders(new ServiceFunctionForwarders());
            sff_json.getServiceFunctionForwarders().setServiceFunctionForwarder(new ArrayList<ServiceFunctionForwarder>());
            sff_json.getServiceFunctionForwarders().getServiceFunctionForwarder().add(sff_list.get(sff_index));
            Gson mapper=new Gson();
            logger.debug("json request formatted sff json: "+ sff_json.toString());
           ResponseEntity<String> sff_result= createODLsff(sff_json);
            if(!sff_result.getStatusCode().is2xxSuccessful()){
                logger.error("Unable to create SFFs");
            }
        }

/*
         //create SFC
        SFCJSON sfc_json=create_sfc_json(sfc_dict,vnf_dict);
        ResponseEntity<String> sfc_result=createODLsfc(sfc_json);
        if (!sfc_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to create ODL SFC");
        }
*/
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


       String RSP_created=rsp_json.getInput().getName();
        return RSP_created;



    }

    //create RSP JSON
    public static RSPJSON create_rsp_json(SFPJSON sfp_json){
        RSPJSON rsp_json=new RSPJSON();
        rsp_json.setInput(new Input());
        rsp_json.getInput().setName(sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName());
        rsp_json.getInput().setParentServiceFunctionPath(sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName());
        rsp_json.getInput().setSymmetric(sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getSymmetric());
        return rsp_json;
    }
    public static SFPJSON create_sfp_json(SFCdict sfc_dict){
        SFPJSON sfp_json=new SFPJSON();
        ServiceFunctionPath sfp=new ServiceFunctionPath();
        sfp.setName("Path-"+sfc_dict.getSfcDict().getName());
        sfp.setServiceChainName(sfc_dict.getSfcDict().getName());
        sfp.setSymmetric(sfc_dict.getSfcDict().getSymmetrical());
        sfp_json.setServiceFunctionPaths(new ServiceFunctionPaths());
        sfp_json.getServiceFunctionPaths().setServiceFunctionPath(new ArrayList<ServiceFunctionPath>());

        sfp_json.getServiceFunctionPaths().getServiceFunctionPath().add(sfp);
        return sfp_json;

    }

    public static SFCJSON create_sfc_json(SFCdict sfc_dict, HashMap<Integer,VNFdict> vnf_dict){
        ServiceFunctionChain sfc=new ServiceFunctionChain();

        SFCJSON sfc_json=new SFCJSON();
        sfc_json.setServiceFunctionChains(new ServiceFunctionChains());
        sfc_json.getServiceFunctionChains().setServiceFunctionChain(new ArrayList<ServiceFunctionChain>());


       for (int sf=0;sf<sfc_dict.getSfcDict().getChain().size();sf++){
         //  System.out.println("sfc size>>> "+sfc.getSfcServiceFunction().get(sf));
           SfcServiceFunction sfc_sf=new SfcServiceFunction();
           sfc.getSfcServiceFunction().add(sfc_sf);
           sfc.getSfcServiceFunction().get(sf).setName(sfc_dict.getSfcDict().getChain().get(sf));//vnf_dict.get(sf).getName());
         //  sfc.getSfcServiceFunction().get(sf).setType("service-function-type:"+vnf_dict.get(sf).getType());
           sfc.getSfcServiceFunction().get(sf).setType(vnf_dict.get(sf).getType());

           // need to change the 0 to the size of the current SFCs --> need database creation

           sfc_json.getServiceFunctionChains().getServiceFunctionChain().add(sfc);
       }
        sfc_json.getServiceFunctionChains().getServiceFunctionChain().get(0).setName(sfc_dict.getSfcDict().getName());
        sfc_json.getServiceFunctionChains().getServiceFunctionChain().get(0).setSymmetric(sfc_dict.getSfcDict().getSymmetrical());
        return sfc_json;

    }

    //param sfs_dict: dictionary of SFs by id to network id (neutron port id)
    //return: dictionary mapping sfs to bridge name
    public HashMap<String,BridgeMapping> Locate_ovs_to_sf(HashMap<Integer,VNFdict> sfs_dict){
        System.out.println("LOCATE OVS TO SF >>> " );

        ResponseEntity<String> response=getNetworkTopologyList();

        if (!response.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to get network topology");

        }
       String network_s=response.getBody();
        if(network_s==null){
            return null;
        }
        Gson mapper=new Gson();
        NetworkJSON network=new NetworkJSON();
        logger.debug("Network is "+network_s);
        network=mapper.fromJson(response.getBody(),NetworkJSON.class);

       HashMap<String, BridgeMapping> br_mapping=new HashMap<String,BridgeMapping>();
        NetworkTopology networkmap;
        networkmap=network.getNetworkTopology();

        Brdict br_dict;

            for (int i = 0; i < sfs_dict.size(); i++) {
                System.out.println("LOCATE OVS TO SF >>> " + i);

                br_dict = find_ovs_br(sfs_dict.get(i), networkmap);
                //   logger.debug("br_dict from find_ovs:" +br_dict.);
                System.out.println("BR NAME >>> " + br_dict.getBr_name());

                if (br_dict.getBr_name() != null) {
                    //set it to br_uuid
                    String br_uuid = br_dict.getBr_name();
                    if (br_mapping.get(br_uuid) != null) {
                        System.out.println("sfs_dict >>> " + sfs_dict.get(i).getName());
                     //   System.out.println("br mapping get SFs  >>> " + br_mapping.get(br_uuid).getSfs());
                        br_mapping.get(br_uuid).getSfs().add(sfs_dict.get(i).getName());
                        br_mapping.get(br_uuid).getSFdict().put(sfs_dict.get(i).getName(),new SF_dict());

                        System.out.println("br mapping get SFs  >>> " + br_mapping.get(br_uuid).getSfs());
                        System.out.println("br dict TAP Port  >>> " + br_dict.getTap_port());
                        System.out.println("NAME OF the Added SF to br mapping : >>" +br_mapping.get(br_uuid).getSFdict().get(sfs_dict.get(i).getName()));

                        br_mapping.get(br_uuid).getSFdict().get(sfs_dict.get(i).getName()).setTap_port(br_dict.getTap_port());


                    } else {

                        br_mapping.put(br_uuid,new BridgeMapping());

                        br_mapping.get(br_uuid).getSfs().add(sfs_dict.get(i).getName());
                        br_mapping.get(br_uuid).getSFdict().put(sfs_dict.get(i).getName(),new SF_dict());
                        br_mapping.get(br_uuid).setOVSip(br_dict.getOVSIp());
                        br_mapping.get(br_uuid).setBr_name(br_dict.getBr_name());

                        br_mapping.get(br_uuid).setNode_ID(br_dict.getNodeID());

                       // String sff_name = "sff" + sff_counter;
                       // br_mapping.get(br_uuid).setSFFname(sff_name);
                        System.out.println("brmapping_dict name >>> " + br_mapping.get(br_uuid).getSFdict().get(sfs_dict.get(i).getName()));

                        br_mapping.get(br_uuid).getSFdict().get(sfs_dict.get(i).getName()).setTap_port(br_dict.getTap_port());
                       // sff_counter++;

                        ServiceFunctionForwarder prev_SFF=find_existing_sff(br_mapping);


                        if(prev_SFF!=null && prev_SFF.getServiceFunctionForwarderOvsOvsBridge().getBridgeName().equals(br_mapping.get(br_uuid))){
                            br_mapping.get(br_uuid).setSFFname(prev_SFF.getName());
                            System.out.println("<<<< PREV SFF not Null >>> ");
                            System.out.println("PREV SFF Found >>> " + prev_SFF.getName());


                        } else {
                            br_mapping.get(br_uuid).setSFFname("SFF"+sff_counter );
                            sff_counter++;
                        }

                    }
                } else {
                    logger.debug("Could not find OVS bridge for " + sfs_dict.get(i).getName());
                }

            }


        return br_mapping;
    }

    public static class Brdict{
       private String ovs_ip;
       private String br_name;
        private String br_uuid;
       private String tap_port;
       private Integer ovs_port;
        private String node_id;

        public String getTap_port() {
            return tap_port;
        }
        public void setTap_port(String name) {
            this.tap_port = name;
        }

        public String getOVSIp() {
            return ovs_ip;
        }
        public void setOVSIp(String name) {
            this.ovs_ip = name;
        }

        public Integer getOVS_port() {
            return ovs_port;
        }
        public void setOVS_port(Integer port) {
            this.ovs_port = port;
        }

        public String getBr_name() {
            return br_name;
        }
        public void setBr_name(String name) {
            this.br_name = name;
        }
        public String getBr_uuid() {
            return br_uuid;
        }
        public void setBr_uuid(String uuid) {
            this.br_uuid = uuid;
        }
        public String getNodeID() {
            return node_id;
        }
        public void setNodeID(String nodeID) {
            this.node_id = nodeID;
        }
    }


    public ServiceFunctionForwarder find_existing_sff(HashMap<String, BridgeMapping> BridgeMapping){

        ResponseEntity<String> response=getODLsff();

        if(response!=null) {
            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.warn("Unable to get SFFs from ODL");
                return null;
            }
        }else{
            return null;
        }

         Gson mapper=new Gson();
           SFFJSON sff_json_response=mapper.fromJson(response.getBody(),SFFJSON.class);
        List<ServiceFunctionForwarder> odl_sff_list=sff_json_response.getServiceFunctionForwarders().getServiceFunctionForwarder();
        ServiceFunctionForwarder sff_br_dict=null;
        Iterator br=BridgeMapping.entrySet().iterator();


        while(br.hasNext()){
            Map.Entry Bridge_name = (Map.Entry)br.next();;
        //    System.out.println("br has NEXT");
            for(int sff=0;sff<odl_sff_list.size();sff++){

                //System.out.println("ODL SFF IP Mgmt Address>> "+odl_sff_list.get(sff).getIpMgmtAddress());
                System.out.println("Br Mapping OVS IP Address>>  "+ BridgeMapping.get(Bridge_name.getKey()).getOVSip());
               //changed to node id instead of IP MGMT address
                if (odl_sff_list.get(sff).getServiceFunctionForwarderOvsOvsNode().getNodeId().equals(BridgeMapping.get(Bridge_name.getKey()).getNode_ID())){
                   sff_br_dict=odl_sff_list.get(sff);
                   System.out.println("BridgeMapping>> "+BridgeMapping);


                   continue;
               }
            }
           // br.remove();
        }
        if (sff_br_dict!=null){
            return sff_br_dict;
        }else {
            logger.debug("SFF is null -- sff_br_dict");
            return null;
        }


    }
    public static List<ServiceFunctionForwarder> create_sff_json(HashMap<String, BridgeMapping> bridgemapping,SFJSON sfs_json,ServiceFunctionForwarder prev_sff_dict ){
        SffDataPlaneLocator  temp_sff_dp_loc=new SffDataPlaneLocator ();
        SffDataPlaneLocator sff_dp_loc=new SffDataPlaneLocator();
        DataPlaneLocator dp_loc=new DataPlaneLocator();
        dp_loc.setTransport("service-locator:vxlan-gpe");
        dp_loc.setIp("");
        dp_loc.setPort(null);
        sff_dp_loc.setName("");
        ServiceFunctionForwarderOvsOvsOptions sffopts=new ServiceFunctionForwarderOvsOvsOptions();
        sffopts.setDstPort("6633");
        sffopts.setKey("flow");
        sffopts.setNshc1("flow");
        sffopts.setNshc2("flow");
        sffopts.setNshc3("flow");
        sffopts.setNshc4("flow");
        sffopts.setNsi("flow");
        sffopts.setNsp("flow");
        sffopts.setRemoteIp("flow");



        sff_dp_loc.setServiceFunctionForwarderOvsOvsOptions(sffopts);
        sff_dp_loc.setDataPlaneLocator(dp_loc);
        Iterator br=bridgemapping.entrySet().iterator();
        ServiceFunctionDictionary sf_dict=new ServiceFunctionDictionary();
        ServiceFunctionDictionary temp_sf_dict=null;
        ServiceFunctionForwarderOvsOvsNode temp_sffNode=new ServiceFunctionForwarderOvsOvsNode();

        sf_dict.setName("");
        SffSfDataPlaneLocator sff_sf_dp_loc=null;
        SffSfDataPlaneLocator temp_sff_sf_dp_loc=new SffSfDataPlaneLocator();
        List<ServiceFunctionDictionary> sf_dicts_list=new ArrayList<ServiceFunctionDictionary>();
        List<ServiceFunctionForwarder> sff_list=new ArrayList<ServiceFunctionForwarder>();



        // build dict for each bridge

        while (br.hasNext()){

            Map.Entry Bridge_name = (Map.Entry)br.next();
            temp_sff_dp_loc=sff_dp_loc;
            temp_sff_dp_loc.setName("vxgpe");
            temp_sff_dp_loc.getDataPlaneLocator().setPort("6633");
            temp_sff_dp_loc.getDataPlaneLocator().setIp(bridgemapping.get(Bridge_name.getKey()).getOVSip());
            String bridge_name=Bridge_name.getKey().toString();
            temp_sffNode.setNodeId(bridgemapping.get(Bridge_name.getKey()).getNode_ID());
            logger.debug("bridge  Name >> "+bridgemapping.get(Bridge_name.getKey()).getBr_name());
      //    System.out.println("Śize of SFs in Br Mapping: "+bridgemapping.get(Bridge_name.getKey()).getSfs().size());

            for(int sf=0;sf<bridgemapping.get(Bridge_name.getKey()).getSfs().size();sf++){
               temp_sf_dict=new ServiceFunctionDictionary();
               // temp_sf_dict=sf_dict;
               int SF_counter=100;
           //     System.out.println("Size of SFs in JSON file: "+sfs_json.getServiceFunctions().getServiceFunction().size());

               //for loop to search in sfs_json for this SF in bridge mapping
               for(int sf_counter=0;sf_counter<sfs_json.getServiceFunctions().getServiceFunction().size();sf_counter++){


                  // System.out.println("SFs Json SF NAME: "+sfs_json.getServiceFunctions().getServiceFunction().get(sf_counter).getName());
                 //  System.out.println("BR MApping SF Name: "+bridgemapping.get(Bridge_name.getKey()).getSfs().get(sf));
                   if(sfs_json.getServiceFunctions().getServiceFunction().get(sf_counter).getName().equals(bridgemapping.get(Bridge_name.getKey()).getSfs().get(sf))){


                       SF_counter=sf_counter;
                       break;
                   }

               }
             //   System.out.println("SF counter ******: "+SF_counter);

                if(SF_counter==100){
                   logger.error("Can not find the SF in bridgemapping");
               }
               temp_sf_dict.setName(sfs_json.getServiceFunctions().getServiceFunction().get(SF_counter).getName());
              //  System.out.println("ADD SF NAME to SFF dict: "+sfs_json.getServiceFunctions().getServiceFunction().get(SF_counter).getName());
                sff_sf_dp_loc=new SffSfDataPlaneLocator();
                sff_sf_dp_loc.setSfDplName("");
                sff_sf_dp_loc.setSffDplName("");
                temp_sff_sf_dp_loc=sff_sf_dp_loc;
               temp_sff_sf_dp_loc.setSffDplName("vxgpe");
               temp_sff_sf_dp_loc.setSfDplName(sfs_json.getServiceFunctions().getServiceFunction().get(SF_counter).getSfDataPlaneLocator().get(0).getName());
               // System.out.println("ADD SFF-SF DP-Loc : "+sfs_json.getServiceFunctions().getServiceFunction().get(SF_counter).getSfDataPlaneLocator().get(0).getName());

               temp_sf_dict.setSffSfDataPlaneLocator(temp_sff_sf_dp_loc);
                System.out.println("SF DPL  : "+sfs_json.getServiceFunctions().getServiceFunction().get(SF_counter).getSfDataPlaneLocator().get(0).getName());

                sf_dicts_list.add(temp_sf_dict);

                System.out.println("SF_dicts List : "+sf_dicts_list.get(sf).getName());



           }
            for(int ix = 0; ix<sf_dicts_list.size(); ix++) {
                System.out.println("1) Dictionary SFs List " +ix+" : "+ sf_dicts_list.get(ix).getSffSfDataPlaneLocator().getSfDplName());
            }
            ServiceFunctionForwarder temp_sff=new ServiceFunctionForwarder();
            temp_sff.setServiceFunctionDictionary(new ArrayList<ServiceFunctionDictionary>());
            for(int ix = 0; ix<sf_dicts_list.size(); ix++) {
                System.out.println("2 ) Dictionary SFs List " +ix+" : "+ sf_dicts_list.get(ix).getName());
            }

            // if exists, use current sff, and only update sfs
            if(prev_sff_dict!=null && bridgemapping.get(Bridge_name.getKey()).getSFFname().equals(prev_sff_dict.getName())){

                temp_sff=prev_sff_dict;
                List<ServiceFunctionDictionary>  prev_sff_sf_list=temp_sff.getServiceFunctionDictionary();
                for(int new_sf=0;new_sf<sf_dicts_list.size();new_sf++){
                    boolean new_sf_update=false;
                    for(int index=0; index<prev_sff_sf_list.size();index++){
                        if(prev_sff_sf_list.get(index).getName().equals(sf_dicts_list.get(new_sf).getName())){
                            System.out.println("Prev SFF SF List :::: add sf name "+sf_dicts_list.get(new_sf).getName());

                            temp_sff.getServiceFunctionDictionary().add(index,sf_dicts_list.get(new_sf));
                            new_sf_update=true;
                            break;
                        }
                    }
                    if(new_sf_update==false){
                       System.out.println("NEW SF :::: add sf name "+sf_dicts_list.get(new_sf).getName());

                        temp_sff.getServiceFunctionDictionary().add(sf_dicts_list.get(new_sf));

                    }
                }

            }
            else{
                temp_sff.setName(bridgemapping.get(Bridge_name.getKey()).getSFFname());
                temp_sff.getSffDataPlaneLocator().add(temp_sff_dp_loc);
                temp_sff.setServiceFunctionForwarderOvsOvsNode(new ServiceFunctionForwarderOvsOvsNode());
                temp_sff.getServiceFunctionForwarderOvsOvsNode().setNodeId(temp_sffNode.getNodeId());

                temp_sff.setServiceFunctionDictionary(sf_dicts_list);
               // temp_sff.setIpMgmtAddress(bridgemapping.get(Bridge_name.getKey()).getOVSip());
                temp_sff.setServiceNode("");
                temp_sff.setServiceFunctionForwarderOvsOvsBridge(new ServiceFunctionForwarderOvsOvsBridge());
                temp_sff.getServiceFunctionForwarderOvsOvsBridge().setBridgeName(bridgemapping.get(Bridge_name.getKey()).getBr_name());



            }

            sff_list.add(temp_sff);





         //  br.remove();

        }

        logger.debug("SFF List output is "+ sff_list);

        return sff_list;





    }
    public static Brdict find_ovs_br(VNFdict sf_id, NetworkTopology network_map){

        Brdict bridge_dict=new Brdict();
        String Node_id="";
        for(int net=0;net<network_map.getTopology().size();net++){
            if(network_map.getTopology().get(net).getNode()!=null){
                System.out.println("NODE is here ***** > ");

                for(int node_entry=0;node_entry<network_map.getTopology().get(net).getNode().size();node_entry++){
                    if(network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint()!=null){
                        System.out.println("Termination Point is here ***** size > "+ network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().size());

                        for(int endpoint=0;endpoint<network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().size();endpoint++){
                            if(bridge_dict.getBr_name()!=null){
                                System.out.println("bridge dict is not null ***** > "+bridge_dict);


                                break;
                            }
                            else if(network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().get(endpoint).getOvsdbInterfaceExternalIds()!=null){

                              //  System.out.println("OVSDB external interface EXIST ***** > ");
                                for (int external_id=0;external_id<network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().get(endpoint).getOvsdbInterfaceExternalIds().size();external_id++){
                                 //   System.out.println("Network topology: external id > "+network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().get(endpoint).getOvsdbInterfaceExternalIds().get(external_id).getExternalIdValue());
                                 //   System.out.println("Neutron port id > "+ sf_id.getNeutronPortId());
                                    if(network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().get(endpoint).getOvsdbInterfaceExternalIds().get(external_id).getExternalIdValue()!=null){
                                        System.out.println("SF VNF- Neutron Port ID: "+sf_id.getNeutronPortId());
                                        System.out.println("Network Topology - Ovsdb Interface External Ids - External Id Value: "+network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().get(endpoint).getOvsdbInterfaceExternalIds().get(external_id).getExternalIdValue());

                                        if (network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().get(endpoint).getOvsdbInterfaceExternalIds().get(external_id).getExternalIdValue().equals(sf_id.getNeutronPortId())){

                                            System.out.println("Found");
                                            System.out.println("OVSDB Bridge Name: "+network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbBridgeName());
                                            System.out.println("OVSDB Bridge UUID: "+network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbBridgeUuid());
                                            bridge_dict.setBr_uuid(network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbBridgeUuid());
                                            bridge_dict.setBr_name(network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbBridgeName());
                                            String full_node_id=network_map.getTopology().get(net).getNode().get(node_entry).getNodeId();
                                            String remove_it="/bridge/"+bridge_dict.getBr_name();
                                            Node_id=full_node_id.replaceAll(remove_it,"");
                                            String Openflow_node_id=network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbBridgeOpenflowNodeRef();

                                            //added new
                                            bridge_dict.setNodeID(Openflow_node_id);

                                            bridge_dict.setTap_port(network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().get(endpoint).getOvsdbName());
                                            break;

                                        }else {
                                            System.out.println("NOT Found");

                                        }
                                    }
                                }

                            }

                        }
                    }
                }
                if(bridge_dict.getBr_name()!=null){
                    for(int node_entry=0;node_entry<network_map.getTopology().get(net).getNode().size();node_entry++){

                        if(network_map.getTopology().get(net).getNode().get(node_entry).getNodeId().equals(Node_id) && network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbConnectionInfo()!=null){
                            System.out.println("bridge dict name ***** > "+bridge_dict.getBr_name());

                            bridge_dict.setOVS_port(network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbConnectionInfo().getRemotePort());
                            bridge_dict.setOVSIp(network_map.getTopology().get(net).getNode().get(node_entry).getOvsdbConnectionInfo().getRemoteIp());
                            break;

                        }
                    }

                }
            }

        }

        if(bridge_dict.getBr_uuid()!=null&& bridge_dict.getBr_name()!=null && bridge_dict.getOVS_port()!=null && bridge_dict.getOVSIp()!=null && bridge_dict.getTap_port()!=null && bridge_dict.getNodeID()!=null)
        {
            logger.debug("bridge dictionary is  created successfully!!");

            return bridge_dict;
        }else
        {
            logger.error("bridge dictionary is not created successfully!!");
            return null;
        }





    }

    public class BridgeMapping{

        private List<String> sfs=new ArrayList<String>();
       // private SF_dict SF_id=new SF_dict();
        private String ovs_ip;
        private String sff_name;
        private HashMap<String, SF_dict> sf=new HashMap<String,SF_dict>();
        private String Br_name;
        private String Node_id;

        public HashMap<String, SF_dict>getSFdict() {
            return sf;
        }
        public void setSFdict(HashMap<String, SF_dict> sf) {
            this.sf = sf;
        }
        public List<String> getSfs() {
            return sfs;
        }
        public void setSfs(List<String> sfs) {
            this.sfs = sfs;
        }
        public String getOVSip() {
            return ovs_ip;
        }
        public void setBr_name(String name) {
            this.Br_name= name;
        }
        public String getBr_name() {
            return Br_name;
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
        public String getNode_ID() {
            return Node_id;
        }
        public void setNode_ID(String nodeID) {
            this.Node_id= nodeID;
        }
    }
    public class SF_dict{

        private String tap_port;
        public String getTap_port() {
            return tap_port;
        }
        public void setTap_port(String name) {
            this.tap_port = name;
        }

    }



public ResponseEntity<String> DeleteSFC(String instance_id,boolean isSymmetric){


//--------------- delete SFs and Update SFF

    Gson mapper=new Gson();
    System.out.println("SFC NAME "+ instance_id.substring(5));
    ResponseEntity<String> rsp_response= getODLrsp(instance_id);
    RenderedServicePaths rsp = mapper.fromJson(rsp_response.getBody(), RenderedServicePaths.class);
    Gson mapper_sff=new Gson();
    ResponseEntity<String> sff_response= getODLsff();
    SFFJSON sffs=mapper_sff.fromJson(sff_response.getBody(), SFFJSON.class);

    for (int y=0;y<rsp.getRenderedServicePath().get(0).getRenderedServicePathHop().size();y++){
        System.out.println("Deleted SF  NAME "+ rsp.getRenderedServicePath().get(0).getRenderedServicePathHop().get(y).getServiceFunctionName());
        for(int x=0;x<sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size();x++){
            System.out.println("SFF SIZE "+ sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size());

            for (int z=0;z<sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getServiceFunctionDictionary().size();z++) {
                System.out.println("SF Dictionary SIZE "+ sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getServiceFunctionDictionary().size());

                if (sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getServiceFunctionDictionary().get(z).getName().equals(rsp.getRenderedServicePath().get(0).getRenderedServicePathHop().get(y).getServiceFunctionName())) {
                    System.out.println("EQUAL >>>> "+ sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getServiceFunctionDictionary().get(z).getName());

                    sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getServiceFunctionDictionary().remove(z);
                }
            }
            updateODLsff(sffs,sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getName());

        }
        deleteODLsf(rsp.getRenderedServicePath().get(0).getRenderedServicePathHop().get(y).getServiceFunctionName());
    }

    System.out.println("SFF NAME "+ sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getName());
    System.out.println("SFF Data plane Locator NAME "+ sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getSffDataPlaneLocator().get(0).getName());
    System.out.println("SFF Bridge NAME "+ sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getServiceFunctionForwarderOvsOvsBridge().getBridgeName());
    System.out.println("SFF Node ID "+ sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getServiceFunctionForwarderOvsOvsNode().getNodeId());
    if (sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getServiceFunctionDictionary().size()!=0) {
        System.out.println("SF Dictionary SF- Name " + sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getServiceFunctionDictionary().get(0).getName());
    }else{
        System.out.println("SF Dictionary is empty " );
    }
    //-------------------------------

    List<String> instance_list=new ArrayList<String>();
    instance_list.add(0,instance_id);
    if(isSymmetric==true){
        String reverse_id = instance_id+"-Reverse";
        instance_list.add(1,reverse_id);

    }
    ResponseEntity<String> rsp_result=null;
    for (int ins=0;ins<instance_list.size();ins++){
        RSPJSON rsp_dict=new RSPJSON();
        Input x=new Input();
        x.setName(instance_list.get(ins));
       // x.setSymmetric(isSymmetric);


        rsp_dict.setInput(x);
        rsp_result=deleteODLrsp(rsp_dict);

        if(!rsp_result.getStatusCode().is2xxSuccessful()){

            logger.error("Unable to delete RSP ! ");
        }



    }




    deleteODLsfp(instance_id);


    deleteODLsfc(instance_id.substring(5));

    return rsp_result;
}



}
