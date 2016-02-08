package org.project.sfc.com.ODL_SFC_Classifier;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.project.sfc.com.ACLJSON.ACLJSON;
import org.project.sfc.com.ACLJSON.AccessListEntries;
import org.project.sfc.com.ACLJSON.Ace;
import org.project.sfc.com.ACLJSON.Matches;
import org.project.sfc.com.ClassifierJSON.ClassifierJSON;
import org.project.sfc.com.ODL_SFC_Classifier.MatchTranslation.MatchTranslation;
import org.project.sfc.com.ODL_SFC_Classifier.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.ODL_SFC_Classifier.SFCCdict.SFCCdict;
import org.project.sfc.com.SFJSON.SFJSON;
import org.project.sfc.com.SFPJSON.SFPJSON;
import org.project.sfc.com.SFPJSON.ServiceFunctionPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mah on 2/5/16.
 */
public class SFC_Classifier {

    public String ODL_ip="127.0.0.1";
    public String ODL_port="8080";
    public String ODL_username="admin";
    public String ODL_password="admin";
    public String Config_acl_url="restconf/config/ietf-access-control-list:access-lists/acl/{}";
    public String Config_netvirtsfc_url="restconf/config/netvirt-sfc-classifier:classifiers/classifier/{}";
    public MatchTranslation matching_translate=new MatchTranslation();

    // Gson mapper=new Gson();
    //BufferedReader br = new BufferedReader((new FileReader("/var/tmp/sfc-controller/src/main/java/org/project/sfc/com/ODL_SFC_Classifier/matching_translate.json")));

    public int sff_counter=1;

    private static Logger logger = LoggerFactory.getLogger(SFC_Classifier.class);

    private void init(){

        this.ODL_ip=ODL_ip;
        this.ODL_port=ODL_port;
        this.ODL_username=ODL_username;
        this.ODL_password=ODL_password;
        this.Config_acl_url=Config_acl_url;

        this.Config_netvirtsfc_url=Config_netvirtsfc_url;

        this.sff_counter=sff_counter;
        matching_translate.setDestIpPrefix("source-ipv4-network");
        matching_translate.setSourceIpPrefix("destination-ipv4-network");
        List<String> l= new ArrayList<String>();
        l.add("lower-port");
        l.add("upper-port");
        matching_translate.getDestPort().setDestinationPortRange(l);
        matching_translate.getSourcePort().setSourcePortRange(l);

        this.matching_translate=matching_translate;



    }

    public String getType(){

        return "netvirtsfc";

    }

    public String getName(){

        return "netvirtsfc";

    }

    public String getDescription(){

        return "netvirtsfc";

    }

    public ResponseEntity<String> sendRest_Classifier(ClassifierJSON data, String rest_type, String url){

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
        ClassifierJSON result=new ClassifierJSON();
        ResponseEntity<String> request=null;
        if (rest_type=="PUT"){
            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data,ClassifierJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ClassifierJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ClassifierJSON.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
            request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ClassifierJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else{
            logger.debug("The REST Request is not exist");
        }

        return request;

    }
    public ResponseEntity<String> sendRest_ACL(ACLJSON data, String rest_type, String url){

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
        ACLJSON result=new ACLJSON();
        ResponseEntity<String> request=null;
        if (rest_type=="PUT"){
            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data,ACLJSON.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ACLJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,ACLJSON.class));

            }
        }else if (rest_type=="DELETE"){
            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
            request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Setting of SF has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),ACLJSON.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else{
            logger.debug("The REST Request is not exist");
        }

        return request;

    }

    public ClassifierJSON build_classifier_json(String sfcc_name){
        ClassifierJSON sfcc_json=new ClassifierJSON();
        sfcc_json.getClassifiers().getClassifier().get(0).setName(sfcc_name);
        sfcc_json.getClassifiers().getClassifier().get(0).setAcl(sfcc_name);
        return sfcc_json;

    }

    public ACLJSON build_acl_json(SFCCdict sfcc_dict, String rsp_id){
        ACLJSON acl_json=new ACLJSON();
        acl_json.getAccessLists().getAcl().get(0).setAclName(sfcc_dict.getName());
        acl_json.getAccessLists().getAcl().get(0).getAccessListEntries().getAce().get(0).setRuleName(sfcc_dict.getName());
        acl_json.getAccessLists().getAcl().get(0).getAccessListEntries().getAce().get(0).getActions().setNetvirtSfcAclSfcName(rsp_id);
        AccessListEntries match_dict=new AccessListEntries();

        //need to be fixed later

        for(AclMatchCriteria key:sfcc_dict.getAclMatchCriteria()){
            int i =0;
            if(key!=null){
                match_dict.getAce().get(i).getMatches().setDestPort(key.getDestPort());
                match_dict.getAce().get(i).getMatches().setProtocol(key.getProtocol());
            }
            i++;


        }

        acl_json.getAccessLists().getAcl().get(0).setAccessListEntries(match_dict);
        return acl_json;

    }

    public  String Create_SFC_Classifer(SFCCdict sfcc_dict, String Chain_instance_id){
        ACLJSON acl_json=build_acl_json(sfcc_dict,Chain_instance_id);
        String sfcc_name=sfcc_dict.getName();


        ResponseEntity<String> acl_result=this.sendRest_ACL(acl_json,"put", MessageFormat.format(this.Config_acl_url,sfcc_name));

        if(!acl_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to create NetVirt ACL");
        }
        ClassifierJSON sfcc_json=build_classifier_json(sfcc_name);
        ResponseEntity<String> sfcc_result=this.sendRest_Classifier(sfcc_json,"PUT", MessageFormat.format(this.Config_netvirtsfc_url,sfcc_name));
        if(!sfcc_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to create NetVirt Classifier");
        }
        //FIXME right now there is no check in netvirtsfc to ensure classifier was created with id
        return sfcc_name;
    }

    public ResponseEntity<String> Delete_SFC_Classifier(String rsp_id){
        ResponseEntity<String> sfcc_result=this.sendRest_Classifier(null,"DELETE", MessageFormat.format(this.Config_netvirtsfc_url,rsp_id));
        if(!sfcc_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to delete NetVirt Classifier");
        }
        return sfcc_result;
    }



}
