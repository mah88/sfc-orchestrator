package org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ACLJSON.*;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ClassifierJSON.Classifier;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ClassifierJSON.ClassifierJSON;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ClassifierJSON.Classifiers;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.MatchTranslation.MatchTranslation;
import org.project.sfc.com.SfcInterfaces.SFC;
import org.project.sfc.com.SfcInterfaces.SFCclassifier;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
//import org.project.sfc.com.openbaton_nfvo.configurations.SfcConfiguration;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by mah on 2/5/16.
 */
@Service
@Scope(value = "prototype")
public class SFC_Classifier extends SFCclassifier {

  private Properties properties;
  public String ODL_ip ;
  public String ODL_port ;
  public String ODL_username ;
  public String ODL_password ;

  public String Config_acl_url = "restconf/config/ietf-access-control-list:access-lists/acl/{0}";
  public String Config_netvirtsfc_url =
      "restconf/config/netvirt-sfc-classifier:classifiers/classifier/{0}";
  public MatchTranslation matching_translate = new MatchTranslation();

  public int sff_counter = 1;

  private static Logger logger = LoggerFactory.getLogger(SFC_Classifier.class);

  private void init()  {


    this.Config_acl_url = Config_acl_url;

    this.Config_netvirtsfc_url = Config_netvirtsfc_url;

    this.sff_counter = sff_counter;
    matching_translate.setDestIpv4("source-ipv4-network");
    matching_translate.setSourceIpv4("destination-ipv4-network");
    List<String> l = new ArrayList<String>();
    l.add("lower-port");
    l.add("upper-port");
    matching_translate.getDestPort().setDestinationPortRange(l);
    matching_translate.getSourcePort().setSourcePortRange(l);

    this.matching_translate = matching_translate;
  }

  public SFC_Classifier() throws IOException{
    this.properties = ConfigReader.readProperties();

    this.ODL_ip = properties.getProperty("sfc.ip");
    this.ODL_port =  properties.getProperty("sfc.port");
    this.ODL_username = properties.getProperty("sfc.username");
    this.ODL_password = properties.getProperty("sfc.password");

  }
  public String getType() {

    return "netvirtsfc";
  }

  public String getName() {

    return "netvirtsfc";
  }

  public String getDescription() {

    return "netvirtsfc";
  }

  public ResponseEntity<String> sendRest_Classifier(
      ClassifierJSON datax, String rest_type, String url) {

    String Full_URL = "http://" + ODL_ip + ":" + ODL_port + "/" + url;
    String plainCreds = ODL_username + ":" + ODL_password;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    headers.setContentType(MediaType.APPLICATION_JSON);

    headers.add("Authorization", "Basic " + base64Creds);
    Gson mapper = new Gson();
    Classifiers result = new Classifiers();

    ResponseEntity<String> request = null;
    if (rest_type == "PUT") {
      Classifiers data = datax.getClassifiers();

      HttpEntity<String> putEntity =
          new HttpEntity<String>(mapper.toJson(data, Classifiers.class), headers);
      request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
      logger.debug(
          "Setting of Classifier has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), Classifiers.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, Classifiers.class));
      }
    } else if (rest_type == "DELETE") {
      System.out.println("Deleting of Test_SFC CLASSIFIER ");

      HttpEntity<String> delEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
      logger.debug(
          "Deleting Classifier produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), Classifiers.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    } else {
      logger.debug("The REST Request is not exist");
    }

    return request;
  }

  public ResponseEntity<String> sendRest_ACL(ACLJSON datax, String rest_type, String url) {

    String Full_URL = "http://" + ODL_ip + ":" + ODL_port + "/" + url;
    String plainCreds = ODL_username + ":" + ODL_password;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    headers.setContentType(MediaType.APPLICATION_JSON);

    headers.add("Authorization", "Basic " + base64Creds);
    Gson mapper = new Gson();
    AccessLists result = new AccessLists();
    ResponseEntity<String> request = null;
    if (rest_type == "PUT") {
      AccessLists data = datax.getAccessLists();

      HttpEntity<String> putEntity =
          new HttpEntity<String>(mapper.toJson(data, AccessLists.class), headers);
      request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
      logger.debug(
          "Setting of ACL has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), AccessLists.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, AccessLists.class));
      }
    } else if (rest_type == "DELETE") {
      System.out.println("Deleting of ACL ");
      HttpEntity<String> delEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
      logger.debug(
          "Deleting of ACL has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), AccessLists.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    } else {
      logger.debug("The REST Request is not exist");
    }

    return request;
  }

  public ClassifierJSON build_classifier_json(String sfcc_name) {
    ClassifierJSON sfcc_json = new ClassifierJSON();
    sfcc_json.setClassifiers(new Classifiers());
    sfcc_json.getClassifiers().setClassifier(new ArrayList<Classifier>());
    sfcc_json.getClassifiers().getClassifier().add(new Classifier());
    sfcc_json.getClassifiers().getClassifier().get(0).setName(sfcc_name);
    sfcc_json.getClassifiers().getClassifier().get(0).setAcl(sfcc_name);
    return sfcc_json;
  }

  public ACLJSON build_acl_json(SFCCdict sfcc_dict, String rsp_id) {
    ACLJSON acl_json = new ACLJSON();
    acl_json.setAccessLists(new AccessLists());
    acl_json.getAccessLists().setAcl(new ArrayList<Acl>());
    acl_json.getAccessLists().getAcl().add(new Acl());
    acl_json.getAccessLists().getAcl().get(0).setAclName(sfcc_dict.getName());

    AccessListEntries match_dict = new AccessListEntries();

    for (AclMatchCriteria value : sfcc_dict.getAclMatchCriteria()) {
      int i = 0;

      if (value != null) {
        match_dict.setAce(new ArrayList<Ace>());
        match_dict.getAce().add(new Ace());
        match_dict.getAce().get(i).setMatches(new Matches());
        if (value.getDestPort() != null) {
          DestinationPortRange dpr = new DestinationPortRange();

          dpr.setLowerPort(value.getDestPort());
          dpr.setUpperPort(value.getDestPort());

          match_dict.getAce().get(i).getMatches().setDestinationPortRange(dpr);
        }
        if (value.getSrcPort() != null) {
          SourcePortRange spr = new SourcePortRange();

          spr.setLowerPort(value.getSrcPort());
          spr.setUpperPort(value.getSrcPort());

          match_dict.getAce().get(i).getMatches().setSourcePortRange(spr);
        }
        match_dict.getAce().get(i).getMatches().setProtocol(value.getProtocol());
        match_dict.getAce().get(i).getMatches().setDestIpv4(value.getDestIpv4());
        match_dict.getAce().get(i).getMatches().setSourceIpv4(value.getSourceIpv4());
        match_dict.getAce().get(i).getMatches().setDestMAC(value.getDestMAC());
        match_dict.getAce().get(i).getMatches().setSourceMAC(value.getSourceMAC());
      }

      i++;
    }

    match_dict.getAce().get(0).setActions(new Actions());
    match_dict.getAce().get(0).setRuleName(sfcc_dict.getName());
    match_dict.getAce().get(0).getActions().setNetvirtSfcAclSfcName(rsp_id);

    acl_json.getAccessLists().getAcl().get(0).setAccessListEntries(match_dict);
    return acl_json;
  }

  @Override
  public String Create_SFC_Classifer(SFCCdict sfcc_dict, String Chain_instance_id) {
    ACLJSON acl_json = build_acl_json(sfcc_dict, Chain_instance_id);
    String sfcc_name = sfcc_dict.getName();

    ResponseEntity<String> acl_result =
        this.sendRest_ACL(acl_json, "PUT", MessageFormat.format(this.Config_acl_url, sfcc_name));

    if (!acl_result.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to create NetVirt ACL");
    }
    ClassifierJSON sfcc_json = build_classifier_json(sfcc_name);
    ResponseEntity<String> sfcc_result =
        this.sendRest_Classifier(
            sfcc_json, "PUT", MessageFormat.format(this.Config_netvirtsfc_url, sfcc_name));
    if (!sfcc_result.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to create NetVirt Classifier");
    }
    //FIXME right now there is no check in netvirtsfc to ensure classifier was created with id
    return sfcc_name;
  }

  @Override
  public ResponseEntity<String> Delete_SFC_Classifier(String classifier_name) {
    System.out.println("$$$$ delete Acl - Test_SFC CLASSIFIER $$$$$$");

    ResponseEntity<String> sfcc_result =
        this.sendRest_Classifier(
            null, "DELETE", MessageFormat.format(this.Config_netvirtsfc_url, classifier_name));
    if (!sfcc_result.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to delete NetVirt Classifier");
    } else {
      System.out.println("Success to delete Test_SFC CLassifier ");
    }
    ResponseEntity<String> acl_result =
        this.sendRest_ACL(
            null, "DELETE", MessageFormat.format(this.Config_acl_url, classifier_name));
    if (!acl_result.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to delete Acl ");
    } else {
      System.out.println("Success to delete Acl ");
    }

    return acl_result;
  }
}
