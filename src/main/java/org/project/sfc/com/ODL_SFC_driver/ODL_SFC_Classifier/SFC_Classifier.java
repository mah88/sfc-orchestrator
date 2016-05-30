package org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.project.sfc.com.ODL_SFC_driver.JSON.ACLJSON.*;
import org.project.sfc.com.ODL_SFC_driver.JSON.ClassifierJSON.Classifier;
import org.project.sfc.com.ODL_SFC_driver.JSON.ClassifierJSON.ClassifierJSON;
import org.project.sfc.com.ODL_SFC_driver.JSON.ClassifierJSON.Classifiers;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.MatchTranslation.MatchTranslation;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict.SFCCdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mah on 2/5/16.
 */
public class SFC_Classifier {

   // public String ODL_ip="192.168.0.138";
    public String ODL_ip="192.168.145.120";

    public String ODL_port="8080";
    public String ODL_username="admin";
    public String ODL_password="admin";
    public String Config_acl_url="restconf/config/ietf-access-control-list:access-lists/acl/{0}";
    public String Config_netvirtsfc_url="restconf/config/netvirt-sfc-classifier:classifiers/classifier/{0}";
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
        matching_translate.setDestIpv4("source-ipv4-network");
        matching_translate.setSourceIpv4("destination-ipv4-network");
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

    public ResponseEntity<String> sendRest_Classifier(ClassifierJSON datax, String rest_type, String url){

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
        Classifiers result=new  Classifiers();

        ResponseEntity<String> request=null;
        if (rest_type=="PUT"){
            Classifiers data=datax.getClassifiers();

            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data, Classifiers.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of Classifier has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(), Classifiers.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result, Classifiers.class));

            }
        }else if (rest_type=="DELETE"){
            System.out.println("Deleting of SFC CLASSIFIER ");

            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
            request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Deleting Classifier produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(), Classifiers.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else{
            logger.debug("The REST Request is not exist");
        }

        return request;

    }
    public ResponseEntity<String> sendRest_ACL(ACLJSON datax, String rest_type, String url){

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
        AccessLists result=new AccessLists();
        ResponseEntity<String> request=null;
        if (rest_type=="PUT"){
            AccessLists data=datax.getAccessLists();

            HttpEntity <String> putEntity=new HttpEntity<String>(mapper.toJson(data,AccessLists.class),headers);
            request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
            logger.debug("Setting of ACL has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),AccessLists.class);
                logger.debug("RESULT IS " + request.getStatusCode() + " with body " + mapper.toJson(result,AccessLists.class));

            }
        }else if (rest_type=="DELETE"){
            System.out.println("Deleting of ACL ");
            HttpEntity <String> delEntity=new HttpEntity<String>(headers);
            request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
            logger.debug("Deleting of ACL has produced http status:" + request.getStatusCode() + " with body: " + request.getBody());

            if(!request.getStatusCode().is2xxSuccessful()){
                result=null;
            }
            else {
                result = mapper.fromJson(request.getBody(),AccessLists.class);
                logger.debug("RESULT IS " + request.getStatusCode() );

            }
        }else{
            logger.debug("The REST Request is not exist");
        }

        return request;

    }

    public ClassifierJSON build_classifier_json(String sfcc_name){
        ClassifierJSON sfcc_json=new ClassifierJSON();
        sfcc_json.setClassifiers(new Classifiers());
        sfcc_json.getClassifiers().setClassifier(new ArrayList<Classifier>());
        sfcc_json.getClassifiers().getClassifier().add(new Classifier());
        sfcc_json.getClassifiers().getClassifier().get(0).setName(sfcc_name);
        sfcc_json.getClassifiers().getClassifier().get(0).setAcl(sfcc_name);
        return sfcc_json;

    }

    public ACLJSON build_acl_json(SFCCdict sfcc_dict, String rsp_id){
        ACLJSON acl_json=new ACLJSON();
        acl_json.setAccessLists(new AccessLists());
        acl_json.getAccessLists().setAcl(new ArrayList<Acl>());
        acl_json.getAccessLists().getAcl().add(new Acl());
        acl_json.getAccessLists().getAcl().get(0).setAclName(sfcc_dict.getName());

        AccessListEntries match_dict=new AccessListEntries();
     //  acl_json.getAccessLists().getAcl().get(0).setAccessListEntries(new AccessListEntries());


        //need to be fixed later

        for(AclMatchCriteria value:sfcc_dict.getAclMatchCriteria()){
            int i =0;

            if(value!=null){
                match_dict.setAce(new ArrayList<Ace>());
                match_dict.getAce().add(new Ace());
                match_dict.getAce().get(i).setMatches(new Matches());
                if(value.getDestPort()!=null){
                    DestinationPortRange dpr=new DestinationPortRange();


                        dpr.setLowerPort(value.getDestPort());
                        dpr.setUpperPort(value.getDestPort());


                    match_dict.getAce().get(i).getMatches().setDestinationPortRange(dpr);


                }
                if(value.getSrcPort()!=null){
                    SourcePortRange spr=new SourcePortRange();


                    spr.setLowerPort(value.getSrcPort());
                    spr.setUpperPort(value.getSrcPort());


                    match_dict.getAce().get(i).getMatches().setSourcePortRange(spr);


                }
                match_dict.getAce().get(i).getMatches().setProtocol(value.getProtocol());
                match_dict.getAce().get(i).getMatches().setDestIpv4(value.getDestIpv4());
                match_dict.getAce().get(i).getMatches().setSourceIpv4(value.getSourceIpv4());


            }


          i++;
        }

       // match_dict.setAce(new ArrayList<Ace>());
       // match_dict.getAce().add(new Ace());
        match_dict.getAce().get(0).setActions(new Actions());
        match_dict.getAce().get(0).setRuleName(sfcc_dict.getName());
        match_dict.getAce().get(0).getActions().setNetvirtSfcAclSfcName(rsp_id);




        acl_json.getAccessLists().getAcl().get(0).setAccessListEntries(match_dict);
        return acl_json;

    }

    public  String Create_SFC_Classifer(SFCCdict sfcc_dict, String Chain_instance_id){
        ACLJSON acl_json=build_acl_json(sfcc_dict,Chain_instance_id);
        String sfcc_name=sfcc_dict.getName();


        ResponseEntity<String> acl_result=this.sendRest_ACL(acl_json,"PUT", MessageFormat.format(this.Config_acl_url,sfcc_name));

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

    public ResponseEntity<String> Delete_SFC_Classifier(String classifier_name){
        System.out.println("$$$$ delete Acl - SFC CLASSIFIER $$$$$$");

        ResponseEntity<String> sfcc_result=this.sendRest_Classifier(null,"DELETE", MessageFormat.format(this.Config_netvirtsfc_url,classifier_name));
        if(!sfcc_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to delete NetVirt Classifier");
        }else{
            System.out.println("Success to delete SFC CLassifier ");
        }
        ResponseEntity<String> acl_result=this.sendRest_ACL(null,"DELETE", MessageFormat.format(this.Config_acl_url,classifier_name));
        if(!acl_result.getStatusCode().is2xxSuccessful()){
            logger.error("Unable to delete Acl ");
        }else{
            System.out.println("Success to delete Acl ");
        }

        return acl_result;
    }



}
