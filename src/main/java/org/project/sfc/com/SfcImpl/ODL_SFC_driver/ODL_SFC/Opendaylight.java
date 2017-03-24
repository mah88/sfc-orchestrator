
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON.NetworkJSON;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON.NetworkTopology;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable.Match;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable.Table;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable.Table_;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPJSON.Input;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPs.RenderedServicePaths;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFCJSON.SFCJSON;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFCJSON.ServiceFunctionChain;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFCJSON.ServiceFunctionChains;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFCJSON.SfcServiceFunction;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFFJSON.*;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFJSON.*;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFPJSON.SFPJSON;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFPJSON.ServiceFunctionPath;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFPJSON.ServiceFunctionPaths;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.Token.Token;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.project.sfc.com.SfcInterfaces.SFC;
import org.project.sfc.com.SfcModel.SFCCdict.AclMatchCriteria;
import org.project.sfc.com.SfcModel.SFCCdict.SFCCdict;
import org.project.sfc.com.SfcModel.SFCdict.SFCdict;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFPJSON.ServicePathHop;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SfcOfRendererConfigJSON.SfcOfRendererConfig;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SfcOfRendererConfigJSON.SfcOfRendererConfigJSON;
import org.project.sfc.com.SfcModel.SFCdict.VNFdict;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPJSON.RSPJSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by mah on 1/27/16.
 */
@Service
@Scope(value = "prototype")
public class Opendaylight extends SFC {

  private Properties properties;
  public String ODL_ip;
  public String ODL_port;
  public String ODL_username;
  public String ODL_password;

  public String Config_SF_URL =
      "restconf/config/service-function:service-functions/service-function/{0}/";
  public String Config_SFF_URL =
      "restconf/config/service-function-forwarder:service-function-forwarders/service-function-forwarder/{0}/";
  public String Config_SFC_URL =
      "restconf/config/service-function-chain:service-function-chains/service-function-chain/{0}/";
  public String Config_SFP_URL =
      "restconf/config/service-function-path:service-function-paths/service-function-path/{0}";
  //public int sff_counter = 1;
  public String Config_sfc_of_render_URL = "restconf/config/sfc-of-renderer:sfc-of-renderer-config";
  public String Config_netvirt_URL =
      "restconf/config/netvirt-providers-config:netvirt-providers-config";

  private static Logger logger = LoggerFactory.getLogger(Opendaylight.class);

  private void init() {

    this.Config_SF_URL = Config_SF_URL;
    this.Config_SFC_URL = Config_SFC_URL;
    this.Config_SFF_URL = Config_SFF_URL;
    this.Config_SFP_URL = Config_SFP_URL;

    // this.sff_counter = sff_counter;
  }

  public Opendaylight() throws IOException {
    this.properties = ConfigReader.readProperties();

    this.ODL_ip = properties.getProperty("sfc.ip");
    this.ODL_port = properties.getProperty("sfc.port");
    this.ODL_username = properties.getProperty("sfc.username");
    this.ODL_password = properties.getProperty("sfc.password");
  }

  public ResponseEntity<String> sendRest_SfcOFrender_conf(
      SfcOfRendererConfigJSON data, String rest_type, String url) {

    String Full_URL = "http://" + ODL_ip + ":" + ODL_port + "/" + url;
    String plainCreds = ODL_username + ":" + ODL_password;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    //   http.createBasicAuthenticationHttpHeaders(username, password);
    headers.add("Accept", "application/json");
    headers.setContentType(MediaType.APPLICATION_JSON);
    //  headers.add("content-type","application/json;charset=utf-8");
    headers.add("Authorization", "Basic " + base64Creds);
    Gson mapper = new Gson();
    SfcOfRendererConfigJSON result = new SfcOfRendererConfigJSON();
    ResponseEntity<String> request = null;

    if (rest_type == "PUT") {
      HttpEntity<String> putEntity =
          new HttpEntity<String>(mapper.toJson(data, SfcOfRendererConfigJSON.class), headers);
      System.out.println("Entry : >> " + putEntity);
      request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
      logger.debug(
          "configuration of netvirt status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), SfcOfRendererConfigJSON.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, SfcOfRendererConfigJSON.class));
      }
    } else if (rest_type == "GET") {
      HttpEntity<String> getEntity = new HttpEntity<String>(headers);
      System.out.println("Entry : >> " + getEntity);
      request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      logger.debug(
          "Getting of netvirt status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
        request = null;
      } else {
        result = mapper.fromJson(request.getBody(), SfcOfRendererConfigJSON.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, SfcOfRendererConfigJSON.class));
      }
    }

    return request;
  }

  public ResponseEntity<String> sendRest_SF(SFJSON datax, String rest_type, String url) {

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
    ServiceFunctions result = new ServiceFunctions();
    ResponseEntity<String> request = null;
    ServiceFunctions data = null;
    if (rest_type == "POST") {
      HttpEntity<String> postEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctions.class), headers);
      request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
      logger.debug(
          "Setting of SF has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctions.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctions.class));
      }
    } else if (rest_type == "PUT") {
      data = datax.getServiceFunctions();
      HttpEntity<String> putEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctions.class), headers);
      System.out.println("Entry : >> " + putEntity);
      request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
      logger.debug(
          "Setting of SF has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctions.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctions.class));
      }
    } else if (rest_type == "DELETE") {
      HttpEntity<String> delEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
      logger.debug(
          "Setting of SF has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctions.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    } else if (rest_type == "GET") {
      HttpEntity<String> getEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctions.class);
        logger.debug(
            "Setting of SF has produced http status:"
                + request.getStatusCode()
                + " with body: "
                + request.getBody());
      }
    }

    return request;
  }

  public ResponseEntity<String> sendRest_SFF(SFFJSON datax, String rest_type, String url) {

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
    ServiceFunctionForwarders result = new ServiceFunctionForwarders();
    ResponseEntity<String> request = null;
    ServiceFunctionForwarders data = new ServiceFunctionForwarders();
    if (datax != null) {
      data = datax.getServiceFunctionForwarders();
    }

    if (rest_type == "POST") {
      HttpEntity<String> postEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctionForwarders.class), headers);
      request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
      logger.debug(
          "Setting of SFF has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionForwarders.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctionForwarders.class));
      }
    } else if (rest_type == "PUT") {
      HttpEntity<String> putEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctionForwarders.class), headers);
      request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
      logger.debug(
          "Setting of SFF has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionForwarders.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctionForwarders.class));
      }
    } else if (rest_type == "DELETE") {
      HttpEntity<String> delEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
      logger.debug(
          "Setting of SFF has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionForwarders.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    } else if (rest_type == "GET") {
      try {
        HttpEntity<String> getEntity = new HttpEntity<String>(headers);
        request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      } catch (HttpClientErrorException ce) {
        result = null;
      }
    }

    return request;
  }

  public ResponseEntity<String> sendRest_SFP(SFPJSON datax, String rest_type, String url) {

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
    ServiceFunctionPaths result = new ServiceFunctionPaths();
    ResponseEntity<String> request = null;
    ServiceFunctionPaths data = null;
    if (rest_type == "POST") {
      HttpEntity<String> postEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctionPaths.class), headers);
      request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
      logger.debug(
          "Setting of SFP has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionPaths.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctionPaths.class));
      }
    } else if (rest_type == "PUT") {
      data = datax.getServiceFunctionPaths();
      HttpEntity<String> putEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctionPaths.class), headers);
      request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
      logger.debug(
          "Setting of SFP has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionPaths.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctionPaths.class));
      }
    } else if (rest_type == "DELETE") {
      HttpEntity<String> delEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
      logger.debug(
          "Setting of SFP has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionPaths.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    } else if (rest_type == "GET") {
      HttpEntity<String> getEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionPaths.class);
        logger.debug(
            "Setting of SFP has produced http status:"
                + request.getStatusCode()
                + " with body: "
                + request.getBody());
      }
    }

    return request;
  }

  public ResponseEntity<String> sendRest_RSP(RSPJSON data, String rest_type, String url) {

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
    RSPJSON result = new RSPJSON();
    ResponseEntity<String> request = null;

    if (rest_type == "POST") {
      HttpEntity<String> postEntity =
          new HttpEntity<String>(mapper.toJson(data, RSPJSON.class), headers);
      request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
      logger.debug(
          "Setting of RSP has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), RSPJSON.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, RSPJSON.class));
      }
    } else if (rest_type == "DELETE") {
      HttpEntity<String> delEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
      logger.debug(
          "Setting of RSP has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), RSPJSON.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    } else if (rest_type == "GET") {
      HttpEntity<String> getEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      logger.debug(
          "Getting  of all RSPs has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), RSPJSON.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    }

    return request;
  }

  public ResponseEntity<String> sendRest_SFC(SFCJSON datax, String rest_type, String url) {

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
    ServiceFunctionChains result = new ServiceFunctionChains();
    ResponseEntity<String> request = null;
    ServiceFunctionChains data = null;
    if (rest_type == "POST") {
      HttpEntity<String> postEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctionChains.class), headers);
      request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
      logger.debug(
          "Setting of SF has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionChains.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctionChains.class));
      }
    } else if (rest_type == "PUT") {
      data = datax.getServiceFunctionChains();
      HttpEntity<String> putEntity =
          new HttpEntity<String>(mapper.toJson(data, ServiceFunctionChains.class), headers);
      request = template.exchange(Full_URL, HttpMethod.PUT, putEntity, String.class);
      logger.debug(
          "Setting of SFC has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionChains.class);
        logger.debug(
            "RESULT IS "
                + request.getStatusCode()
                + " with body "
                + mapper.toJson(result, ServiceFunctionChains.class));
      }
    } else if (rest_type == "DELETE") {
      HttpEntity<String> delEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.DELETE, delEntity, String.class);
      logger.debug(
          "Setting of SFC has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());

      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionChains.class);
        logger.debug("RESULT IS " + request.getStatusCode());
      }
    } else if (rest_type == "GET") {
      HttpEntity<String> getEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), ServiceFunctionChains.class);
        logger.debug(
            "Setting of SFC has produced http status:"
                + request.getStatusCode()
                + " with body: "
                + request.getBody());
      }
    }

    return request;
  }

  /// Send request for getting the Network TOPOLOGY
  public ResponseEntity<String> sendRest_NetworkTopology(
      NetworkJSON data, String rest_type, String url) {

    String Full_URL = "http://" + ODL_ip + ":" + ODL_port + "/" + url;
    String plainCreds = ODL_username + ":" + ODL_password;
    byte[] base64CredsBytes = Base64.encodeBase64(plainCreds.getBytes(Charset.forName("US-ASCII")));
    String base64Creds = new String(base64CredsBytes);
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    headers.add("Authorization", "Basic " + base64Creds);
    Gson mapper = new Gson();
    NetworkJSON result = new NetworkJSON();
    ResponseEntity<String> request = null;
    if (rest_type == "GET") {
      HttpEntity<String> getEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), NetworkJSON.class);
        logger.debug(
            "Setting of Test_SFC has produced http status:"
                + request.getStatusCode()
                + " with body: "
                + request.getBody());
      }
    }

    return request;
  }
  /// Send request for getting the Network TOPOLOGY
  public ResponseEntity<String> sendRest_OpenFlowPlugin(Table data, String rest_type, String url) {

    String Full_URL = "http://" + ODL_ip + ":" + ODL_port + "/" + url;
    String plainCreds = ODL_username + ":" + ODL_password;
    byte[] base64CredsBytes = Base64.encodeBase64(plainCreds.getBytes(Charset.forName("US-ASCII")));
    String base64Creds = new String(base64CredsBytes);
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    headers.add("Authorization", "Basic " + base64Creds);
    Gson mapper = new Gson();
    Table result = new Table();
    ResponseEntity<String> request = null;
    if (rest_type == "GET") {
      HttpEntity<String> getEntity = new HttpEntity<String>(headers);
      request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
      if (!request.getStatusCode().is2xxSuccessful()) {
        result = null;
      } else {
        result = mapper.fromJson(request.getBody(), Table.class);
        logger.debug(
            "Setting of Test_SFC has produced http status:"
                + request.getStatusCode()
                + " with body: "
                + request.getBody());
      }
    }

    return request;
  }
  /// Get Network Topology
  public ResponseEntity<String> getNetworkTopologyList() {
    String url = "restconf/operational/network-topology:network-topology/";
    ResponseEntity<String> network = this.sendRest_NetworkTopology(null, "GET", url);
    return network;
  }

  /// Get Network Topology
  public ResponseEntity<String> getSfcClassifierTable(String openflow_id) {
    logger.info(" GET Test_SFC CLASSIFIER TABLE 11: for Openflow ID= " + openflow_id);
    String url =
        "restconf/operational/opendaylight-inventory:nodes/node/" + openflow_id + "/table/11/";
    ResponseEntity<String> table = this.sendRest_OpenFlowPlugin(null, "GET", url);
    return table;
  }
  //ODL SFF Stuff (Get, Create, Update, Delete)
  public ResponseEntity<String> getODLsff() {
    String url = "restconf/config/service-function-forwarder:service-function-forwarders/";
    ResponseEntity<String> sff_response = this.sendRest_SFF(null, "GET", url);
    return sff_response;
  }

  public ResponseEntity<String> getODLsfc(String sfc_name) {
    ResponseEntity<String> sfc_response =
        this.sendRest_SFC(null, "GET", MessageFormat.format(this.Config_SFC_URL, sfc_name));
    return sfc_response;
  }

  public ResponseEntity<String> createODLsff(SFFJSON sffJSON) {
    com.google.gson.Gson gson = new com.google.gson.Gson();

    String json = gson.toJson(sffJSON, SFFJSON.class);

    String sff_name =
        sffJSON.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getName();

    ResponseEntity<String> sff_result =
        this.sendRest_SFF(sffJSON, "PUT", MessageFormat.format(this.Config_SFF_URL, sff_name));
    return sff_result;
  }
  //FIXME need to be formated with the name as Create ODL SFF

  public ResponseEntity<String> updateODLsff(SFFJSON sffJSON, String sffName) {

    ResponseEntity<String> sff_result =
        this.sendRest_SFF(sffJSON, "PUT", MessageFormat.format(this.Config_SFF_URL, sffName));
    return sff_result;
  }

  public ResponseEntity<String> deleteODLsff(SFFJSON sffJSON) {
    ResponseEntity<String> sff_result = this.sendRest_SFF(sffJSON, "DELETE", this.Config_SFF_URL);
    return sff_result;
  }
  //ODL SFs Stuff (Create, Update, Delete)
  public ResponseEntity<String> createODLsf(SFJSON sfJSON) {
    com.google.gson.Gson gson = new com.google.gson.Gson();

    String json = gson.toJson(sfJSON, SFJSON.class);

    String sf_name = sfJSON.getServiceFunctions().getServiceFunction().get(0).getName();
    ResponseEntity<String> sf_result =
        this.sendRest_SF(sfJSON, "PUT", MessageFormat.format(this.Config_SF_URL, sf_name));
    return sf_result;
  }
  //FIXME need to be formated with the name as Create ODL SF

  public ResponseEntity<String> updateODLsf(SFJSON sfJSON) {

    ResponseEntity<String> sf_result = this.sendRest_SF(sfJSON, "PUT", this.Config_SF_URL);
    return sf_result;
  }

  public ResponseEntity<String> getODLsf() {
    String url = "restconf/config/service-function:service-functions";
    ResponseEntity<String> sf_result = this.sendRest_SF(null, "GET", url);
    return sf_result;
  }

  public ResponseEntity<String> deleteODLsf(String sf_name) {
    ResponseEntity<String> sf_result =
        this.sendRest_SF(null, "DELETE", MessageFormat.format(this.Config_SF_URL, sf_name));
    return sf_result;
  }
  //ODL Test_SFC stuff (Create, Update, Delete)
  public ResponseEntity<String> createODLsfc(SFCJSON sfcJSON) {
    com.google.gson.Gson gson = new com.google.gson.Gson();

    String json = gson.toJson(sfcJSON, SFCJSON.class);

    String sfc_name = sfcJSON.getServiceFunctionChains().getServiceFunctionChain().get(0).getName();
    ResponseEntity<String> sfc_result =
        this.sendRest_SFC(sfcJSON, "PUT", MessageFormat.format(this.Config_SFC_URL, sfc_name));
    return sfc_result;
  }
  //FIXME need to be formated with the name as Create ODL Test_SFC

  public ResponseEntity<String> updateODLsfc(SFCJSON sfcJSON) {

    ResponseEntity<String> sfc_result = this.sendRest_SFC(sfcJSON, "PUT", this.Config_SFC_URL);
    return sfc_result;
  }

  public ResponseEntity<String> deleteODLsfc(String sfc_name) {
    ResponseEntity<String> sfc_result =
        this.sendRest_SFC(null, "DELETE", MessageFormat.format(this.Config_SFC_URL, sfc_name));
    return sfc_result;
  }
  //ODL SFP stuff (Create, Update, Delete)
  public ResponseEntity<String> createODLsfp(SFPJSON sfpJSON) {
    com.google.gson.Gson gson = new com.google.gson.Gson();

    String json = gson.toJson(sfpJSON, SFPJSON.class);

    String sfp_name = sfpJSON.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName();
    ResponseEntity<String> sfp_result =
        this.sendRest_SFP(sfpJSON, "PUT", MessageFormat.format(this.Config_SFP_URL, sfp_name));
    return sfp_result;
  }
  //FIXME need to be formated with the name as Create ODL SFP
  public ResponseEntity<String> updateODLsfp(SFPJSON sfpJSON) {

    ResponseEntity<String> sfp_result = this.sendRest_SFP(sfpJSON, "PUT", this.Config_SFP_URL);
    return sfp_result;
  }

  public ResponseEntity<String> deleteODLsfp(String sfp_name) {
    ResponseEntity<String> sfp_result =
        this.sendRest_SFP(null, "DELETE", MessageFormat.format(this.Config_SFP_URL, sfp_name));
    return sfp_result;
  }

  //ODL RSP stuff (Create, Delete, GET)
  public ResponseEntity<String> createODLrsp(RSPJSON rspJSON) {
    String url = "restconf/operations/rendered-service-path:create-rendered-path";
    ResponseEntity<String> rsp_result = this.sendRest_RSP(rspJSON, "POST", url);
    return rsp_result;
  }

  public ResponseEntity<String> deleteODLrsp(RSPJSON rspJSON) {
    String url = "restconf/operations/rendered-service-path:delete-rendered-path/";
    ResponseEntity<String> rsp_result = this.sendRest_RSP(rspJSON, "POST", url);
    return rsp_result;
  }

  public ResponseEntity<String> getODLrsp(String rsp_name) {
    String url =
        "restconf/operational/rendered-service-path:rendered-service-paths/rendered-service-path/{0}";
    ResponseEntity<String> rsp_result =
        this.sendRest_RSP(null, "GET", MessageFormat.format(url, rsp_name));
    return rsp_result;
  }

  @Override
  public void CreateSFC(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict) {
    //create Test_SFC
    SFCJSON sfc_json = create_sfc_json(sfc_dict, vnf_dict);
    ResponseEntity<String> sfc_result = createODLsfc(sfc_json);
    if (sfc_result == null) {
      logger.error("Unable to create ODL Test_SFC");
    }
    /*
    if (!sfc_result.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to create ODL Test_SFC");
    }*/
  }

  @Override
  public void CreateSFs(HashMap<Integer, VNFdict> vnf_dict) throws IOException {

    String dp_loc = "sf-data-plane-locator";
    ServiceFunctions sfs_json = new ServiceFunctions();
    HashMap<Integer, VNFdict> sf_net_map = new HashMap<Integer, VNFdict>();
    SFJSON FullSFjson = new SFJSON();
    Integer SF_ID;
    List<ServiceFunction> list_sfs = new ArrayList<ServiceFunction>();

    for (int sf_i = 0; sf_i < vnf_dict.size(); sf_i++) {
      logger.info("[ODL Create SF-Name]:" + vnf_dict.get(sf_i).getName());

      ServiceFunction sf_json = new ServiceFunction();
      SF_ID = sf_i;
      sf_json.setName(vnf_dict.get(sf_i).getName());
      SfDataPlaneLocator dplocDict = new SfDataPlaneLocator();
      //dplocDict.setName("vxlan");
      dplocDict.setName(vnf_dict.get(sf_i).getName() + "-dpl");
      dplocDict.setIp(vnf_dict.get(sf_i).getIP());
      dplocDict.setPort("6633");
      dplocDict.setTransport("service-locator:vxlan-gpe");
      dplocDict.setServiceFunctionForwarder("dummy");
      sf_json.setNshAware("true");
      sf_json.setIpMgmtAddress(vnf_dict.get(sf_i).getIP());
      //sf_json.setType("service-function-type:"+vnf_dict.get(sf_i).getType());
      sf_json.setType(vnf_dict.get(sf_i).getType());
      List<SfDataPlaneLocator> list_dploc = new ArrayList<SfDataPlaneLocator>();
      list_dploc.add(dplocDict);
      sf_json.setSfDataPlaneLocator(list_dploc);
      list_sfs.add(SF_ID, sf_json);
      sfs_json.setServiceFunction(list_sfs);
      //   FullSFjson.setServiceFunctions(sfs_json);

      sf_net_map.put(SF_ID, vnf_dict.get(sf_i));
    }
    // need to be adjusted
    logger.debug("service-function-Net-Map:" + sf_net_map.get(0).getName());
    HashMap<String, BridgeMapping> ovs_mapping = Locate_ovs_to_sf(sf_net_map);
    logger.debug("OVS MAP: " + ovs_mapping.toString());
    Iterator br_map = ovs_mapping.entrySet().iterator();
    int counter = 0;

    while (br_map.hasNext()) {
      int position = Integer.MAX_VALUE;

      Map.Entry br_map_counter = (Map.Entry) br_map.next();
      for (int sf_id_counter = 0;
          sf_id_counter < ovs_mapping.get(br_map_counter.getKey()).sfs.size();
          sf_id_counter++) {
        logger.debug(
            " ---> SF NAME: " + ovs_mapping.get(br_map_counter.getKey()).sfs.get(sf_id_counter));

        logger.debug(
            "OVS Port:"
                + ovs_mapping
                    .get(br_map_counter.getKey())
                    .getSFdict()
                    .get(ovs_mapping.get(br_map_counter.getKey()).getSfs().get(sf_id_counter))
                    .getTap_port());

        for (int sf_counter = 0; sf_counter < vnf_dict.size(); sf_counter++) {
          if (vnf_dict
              .get(sf_counter)
              .getName()
              .equals(ovs_mapping.get(br_map_counter.getKey()).sfs.get(sf_id_counter))) {

            position = sf_counter;
            logger.debug(
                " --->Find the SF: NAME: "
                    + ovs_mapping.get(br_map_counter.getKey()).sfs.get(sf_id_counter)
                    + " Position: "
                    + position);

            logger.debug(
                "Found the Position of the Service Function "
                    + ovs_mapping.get(br_map_counter.getKey()).sfs.get(sf_id_counter));
          }
        }
        if (position == Integer.MAX_VALUE) {
          logger.error(
              " Could not find the position of the Service Function "
                  + ovs_mapping.get(br_map_counter.getKey()).sfs.get(sf_id_counter));
          break;
        }

        sfs_json
            .getServiceFunction()
            .get(position)
            .getSfDataPlaneLocator()
            .get(0)
            .setServiceFunctionForwarder(ovs_mapping.get(br_map_counter.getKey()).getSFFname());
        sfs_json
            .getServiceFunction()
            .get(position)
            .getSfDataPlaneLocator()
            .get(0)
            .setServiceFunctionOvsOvsPort(new ServiceFunctionOvsOvsPort());
        sfs_json
            .getServiceFunction()
            .get(position)
            .getSfDataPlaneLocator()
            .get(0)
            .getServiceFunctionOvsOvsPort()
            .setPortID(
                ovs_mapping
                    .get(br_map_counter.getKey())
                    .getSFdict()
                    .get(ovs_mapping.get(br_map_counter.getKey()).getSfs().get(sf_id_counter))
                    .getTap_port());
        logger.debug("SF updated with SFF:" + ovs_mapping.get(br_map_counter.getKey()).getSFFname());
      }
      logger.debug(
          " LATER ---> SF NAME: " + sfs_json.getServiceFunction().get(position).getName());

      logger.debug(
          " LATER ---> SFF NAME: "
              + sfs_json
                  .getServiceFunction()
                  .get(position)
                  .getSfDataPlaneLocator()
                  .get(0)
                  .getServiceFunctionForwarder());

      logger.debug("OVS_MAPPING >> " + ovs_mapping);
      counter++;
    }

    for (int sf_j = 0; sf_j < sfs_json.getServiceFunction().size(); sf_j++) {

      //check SF Exist in ODL ?
      ServiceFunctions service_functions = new ServiceFunctions();
      List<ServiceFunction> list_service_function = new ArrayList<ServiceFunction>();
      list_service_function.add(sfs_json.getServiceFunction().get(sf_j));
      service_functions.setServiceFunction(list_service_function);
      FullSFjson.setServiceFunctions(service_functions);
      ResponseEntity<String> sf_result = createODLsf(FullSFjson);
      logger.debug(
          "create ODL SF with name >> "
              + FullSFjson.getServiceFunctions().getServiceFunction().get(0).getName());
      if (!sf_result.getStatusCode().is2xxSuccessful()) {
        logger.error("Unable to create ODL SF " + FullSFjson.toString());
      }
    }

    SFJSON All_SFs_JSON = new SFJSON();
    All_SFs_JSON.setServiceFunctions(sfs_json);
    List<ServiceFunctionForwarder> sff_list = new ArrayList<ServiceFunctionForwarder>();
    //building SFF
    ServiceFunctionForwarders prev_sff_dict = find_existing_sff(ovs_mapping);
    logger.debug("PREVIOUS sff_dict >> " + prev_sff_dict);

    if (prev_sff_dict != null) {
      logger.debug("Previous SFF is detected ");
      sff_list = create_sff_json(ovs_mapping, All_SFs_JSON, prev_sff_dict);

    } else {
      sff_list = create_sff_json(ovs_mapping, All_SFs_JSON, null);
    }
    for (int i = 0; i < sff_list.size(); i++) {
      for (int x = 0; x < sff_list.get(i).getServiceFunctionDictionary().size(); x++) {
        logger.debug(
            i
                + ")"
                + x
                + ") SFF LIST includes SF: "
                + sff_list.get(i).getServiceFunctionDictionary().get(x).getName());
      }
    }

    for (int sff_index = 0; sff_index < sff_list.size(); sff_index++) {
      SFFJSON sff_json = new SFFJSON();
      sff_json.setServiceFunctionForwarders(new ServiceFunctionForwarders());
      sff_json
          .getServiceFunctionForwarders()
          .setServiceFunctionForwarder(new ArrayList<ServiceFunctionForwarder>());
      sff_json
          .getServiceFunctionForwarders()
          .getServiceFunctionForwarder()
          .add(sff_list.get(sff_index));
      Gson mapper = new Gson();
      for (int x = 0; x < sff_list.get(sff_index).getServiceFunctionDictionary().size(); x++) {

        logger.debug(
            "json request formatted sff json: SF NAME  "
                + sff_json
                    .getServiceFunctionForwarders()
                    .getServiceFunctionForwarder()
                    .get(0)
                    .getServiceFunctionDictionary()
                    .get(x)
                    .getName());
      }
      ResponseEntity<String> sff_result = createODLsff(sff_json);
      if (!sff_result.getStatusCode().is2xxSuccessful()) {
        logger.error("Unable to create ODL SFF ");
      }
    }
  }

  @Override
  public String CreateSFP(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict) {

    SFPJSON sfp_json = create_sfp_json(sfc_dict, vnf_dict);
    ResponseEntity<String> sfp_result = createODLsfp(sfp_json);
    if (!sfp_result.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to create ODL SFP ");
    }

    RSPJSON rsp_json = create_rsp_json(sfp_json);
    ResponseEntity<String> rsp_result = createODLrsp(rsp_json);
    if (!rsp_result.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to create ODL RSP ");
    }

    String RSP_created = rsp_json.getInput().getName();
    return RSP_created;
  }

  //create RSP JSON
  public static RSPJSON create_rsp_json(SFPJSON sfp_json) {
    RSPJSON rsp_json = new RSPJSON();
    rsp_json.setInput(new Input());
    logger.debug(
        "[Length of SFPs] SFP size >>> "
            + sfp_json.getServiceFunctionPaths().getServiceFunctionPath().size());

    logger.debug(
        "[creating RSP JSON] SFP NAME >>> "
            + sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName());

    rsp_json
        .getInput()
        .setName(sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName());
    rsp_json
        .getInput()
        .setParentServiceFunctionPath(
            sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getName());
    rsp_json
        .getInput()
        .setSymmetric(
            sfp_json.getServiceFunctionPaths().getServiceFunctionPath().get(0).getSymmetric());
    return rsp_json;
  }

  public static SFPJSON create_sfp_json(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict) {
    SFPJSON sfp_json = new SFPJSON();
    ServiceFunctionPath sfp = new ServiceFunctionPath();
    sfp.setName("Path-" + sfc_dict.getSfcDict().getName());
    sfp.setServiceChainName(sfc_dict.getSfcDict().getName());
    sfp.setSymmetric(sfc_dict.getSfcDict().getSymmetrical());
    sfp.setServicePathHop(new ArrayList<ServicePathHop>());

    for (int i = 0; i < vnf_dict.size(); i++) {
      sfp.getServicePathHop().add(i, new ServicePathHop());
      sfp.getServicePathHop().get(i).setHopNumber(i);
      sfp.getServicePathHop().get(i).setServiceFunctionName(vnf_dict.get(i).getName());
    }
    sfp_json.setServiceFunctionPaths(new ServiceFunctionPaths());
    sfp_json.getServiceFunctionPaths().setServiceFunctionPath(new ArrayList<ServiceFunctionPath>());

    sfp_json.getServiceFunctionPaths().getServiceFunctionPath().add(sfp);
    return sfp_json;
  }

  public static SFCJSON create_sfc_json(SFCdict sfc_dict, HashMap<Integer, VNFdict> vnf_dict) {
    ServiceFunctionChain sfc = new ServiceFunctionChain();

    SFCJSON sfc_json = new SFCJSON();
    sfc_json.setServiceFunctionChains(new ServiceFunctionChains());
    sfc_json
        .getServiceFunctionChains()
        .setServiceFunctionChain(new ArrayList<ServiceFunctionChain>());

    for (int sf = 0; sf < sfc_dict.getSfcDict().getChain().size(); sf++) {
      SfcServiceFunction sfc_sf = new SfcServiceFunction();
      sfc.getSfcServiceFunction().add(sfc_sf);
      sfc.getSfcServiceFunction()
          .get(sf)
          .setName(sfc_dict.getSfcDict().getChain().get(sf)); //vnf_dict.get(sf).getName());
      sfc.getSfcServiceFunction().get(sf).setType(vnf_dict.get(sf).getType());
      sfc_json.getServiceFunctionChains().getServiceFunctionChain().add(sfc);
    }
    sfc_json
        .getServiceFunctionChains()
        .getServiceFunctionChain()
        .get(0)
        .setName(sfc_dict.getSfcDict().getName());
    sfc_json
        .getServiceFunctionChains()
        .getServiceFunctionChain()
        .get(0)
        .setSymmetric(sfc_dict.getSfcDict().getSymmetrical());
    return sfc_json;
  }

  //param sfs_dict: dictionary of SFs by id to network id (neutron port id)
  //return: dictionary mapping sfs to bridge name
  public HashMap<String, BridgeMapping> Locate_ovs_to_sf(HashMap<Integer, VNFdict> sfs_dict)
      throws IOException {
    System.out.println("LOCATE OVS TO SF >>> ");

    ResponseEntity<String> response = getNetworkTopologyList();

    if (!response.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to get network topology");
    }
    String network_s = response.getBody();
    if (network_s == null) {
      return null;
    }
    Gson mapper = new Gson();
    NetworkJSON network = new NetworkJSON();
    logger.debug("Network is " + network_s);
    network = mapper.fromJson(response.getBody(), NetworkJSON.class);

    HashMap<String, BridgeMapping> br_mapping = new HashMap<String, BridgeMapping>();
    NetworkTopology networkmap;
    networkmap = network.getNetworkTopology();

    Brdict br_dict;

    for (int i = 0; i < sfs_dict.size(); i++) {
      logger.debug("LOCATE OVS TO SF >>> " + i);

      br_dict = find_ovs_br(sfs_dict.get(i), networkmap);
      logger.debug("BR NAME >>> " + br_dict.getBr_name());
      logger.debug("BR UUID >>> " + br_dict.getBr_uuid());

      if (br_dict.getBr_name() != null) {
        String br_uuid = br_dict.getBr_uuid();

        if (br_mapping.get(br_uuid) != null) {
          System.out.println("sfs_dict >>> " + sfs_dict.get(i).getName());
          br_mapping.get(br_uuid).getSfs().add(sfs_dict.get(i).getName());
          br_mapping.get(br_uuid).getSFdict().put(sfs_dict.get(i).getName(), new SF_dict());

          logger.debug("br mapping get SFs  >>> " + br_mapping.get(br_uuid).getSfs());
          logger.debug("br dict TAP Port  >>> " + br_dict.getTap_port());
          logger.debug(
              "NAME OF the Added SF to br mapping : >>"
                  + br_mapping.get(br_uuid).getSFdict().get(sfs_dict.get(i).getName()));
          logger.debug("br dict OVS IP    >>> " + br_dict.getOVSIp());

          br_mapping
              .get(br_uuid)
              .getSFdict()
              .get(sfs_dict.get(i).getName())
              .setTap_port(br_dict.getTap_port());

        } else {

          br_mapping.put(br_uuid, new BridgeMapping());

          br_mapping.get(br_uuid).getSfs().add(sfs_dict.get(i).getName());
          br_mapping.get(br_uuid).getSFdict().put(sfs_dict.get(i).getName(), new SF_dict());
          br_mapping.get(br_uuid).setOVSip(br_dict.getOVSIp());
          //change to use br uuid
          br_mapping.get(br_uuid).setBr_name(br_dict.getBr_name());

          br_mapping.get(br_uuid).setNode_ID(br_dict.getNodeID());

          logger.debug(
              "brmapping_dict name >>> "
                  + br_mapping.get(br_uuid).getSFdict().get(sfs_dict.get(i).getName()));
          System.out.println("br dict OVS IP    >>> " + br_dict.getOVSIp());

          br_mapping
              .get(br_uuid)
              .getSFdict()
              .get(sfs_dict.get(i).getName())
              .setTap_port(br_dict.getTap_port());

          ServiceFunctionForwarders prev_SFFs = find_existing_sff(br_mapping);
          logger.debug("SF Name >>> " + sfs_dict.get(i).getName());
          boolean SFFexist = false;

          if (prev_SFFs != null) {
            for (int sff_counter = 0;
                sff_counter < prev_SFFs.getServiceFunctionForwarder().size();
                sff_counter++) {

              if (prev_SFFs
                  .getServiceFunctionForwarder()
                  .get(sff_counter)
                  .getServiceFunctionForwarderOvsOvsNode()
                  .getNodeId()
                  .equals(br_mapping.get(br_uuid).getNode_ID())) {
                br_mapping
                    .get(br_uuid)
                    .setSFFname(prev_SFFs.getServiceFunctionForwarder().get(sff_counter).getName());
                logger.debug("<<<< PREV SFF not Null >>> ");
                logger.debug(
                    "PREV SFF Found >>> "
                        + prev_SFFs.getServiceFunctionForwarder().get(sff_counter).getName());
                SFFexist = true;
              }
            }
          }
          if (SFFexist == false) {
            br_mapping.get(br_uuid).setSFFname("SFF-" + br_mapping.get(br_uuid).getOVSip());
            logger.debug(
                "Set new SFF  with name  >>> " + "SFF-" + br_mapping.get(br_uuid).getOVSip());

            //  sff_counter++;
          }
        }
      } else {
        logger.debug("Could not find OVS bridge for " + sfs_dict.get(i).getName());
      }
    }

    return br_mapping;
  }

  public static class Brdict {
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

  public ServiceFunctionForwarders find_existing_sff(HashMap<String, BridgeMapping> BridgeMapping)
      throws IOException {

    ResponseEntity<String> response = getODLsff();

    if (response != null) {
      if (!response.getStatusCode().is2xxSuccessful()) {
        logger.warn("Unable to get SFFs from ODL");
        return null;
      }
    } else {
      return null;
    }

    Gson mapper = new Gson();
    SFFJSON sff_json_response = mapper.fromJson(response.getBody(), SFFJSON.class);
    List<ServiceFunctionForwarder> odl_sff_list =
        sff_json_response.getServiceFunctionForwarders().getServiceFunctionForwarder();
    ServiceFunctionForwarders SFFs = new ServiceFunctionForwarders();
    List<ServiceFunctionForwarder> sffs_list = new ArrayList<>();
    ServiceFunctionForwarder sff_br_dict = null;
    Iterator br = BridgeMapping.entrySet().iterator();

    while (br.hasNext()) {
      Map.Entry Bridge_name = (Map.Entry) br.next();

      for (int sff = 0; sff < odl_sff_list.size(); sff++) {

        logger.debug(
            "Br Mapping OVS IP Address>>  " + BridgeMapping.get(Bridge_name.getKey()).getOVSip());
        //changed to node id instead of IP MGMT address
        if (odl_sff_list
            .get(sff)
            .getServiceFunctionForwarderOvsOvsNode()
            .getNodeId()
            .equals(BridgeMapping.get(Bridge_name.getKey()).getNode_ID())) {
          sff_br_dict = odl_sff_list.get(sff);
          sffs_list.add(sff_br_dict);

          logger.debug("BridgeMapping>> " + BridgeMapping);

          continue;
        }
      }
    }
    if (sffs_list != null) {
      SFFs.setServiceFunctionForwarder(sffs_list);
      return SFFs;
    } else {
      logger.error("SFFs List is null");
      return null;
    }
  }

  public static List<ServiceFunctionForwarder> create_sff_json(
      HashMap<String, BridgeMapping> bridgemapping,
      SFJSON sfs_json,
      ServiceFunctionForwarders prev_sff_dict) {
    SffDataPlaneLocator sff_dp_loc = new SffDataPlaneLocator();

    sff_dp_loc.setName("");
    ServiceFunctionForwarderOvsOvsOptions sffopts = new ServiceFunctionForwarderOvsOvsOptions();
    sffopts.setDstPort("6633");
    sffopts.setKey("flow");
    sffopts.setNshc1("flow");
    sffopts.setNshc2("flow");
    sffopts.setNshc3("flow");
    sffopts.setNshc4("flow");
    sffopts.setNsi("flow");
    sffopts.setNsp("flow");
    sffopts.setRemoteIp("flow");
    sffopts.setExts("gpe");

    sff_dp_loc.setServiceFunctionForwarderOvsOvsOptions(sffopts);
    Iterator br = bridgemapping.entrySet().iterator();
    ServiceFunctionDictionary sf_dict = new ServiceFunctionDictionary();
    ServiceFunctionDictionary temp_sf_dict = null;
    ServiceFunctionForwarderOvsOvsNode temp_sffNode = new ServiceFunctionForwarderOvsOvsNode();

    sf_dict.setName("");
    SffSfDataPlaneLocator sff_sf_dp_loc = null;
    SffSfDataPlaneLocator temp_sff_sf_dp_loc = new SffSfDataPlaneLocator();
    List<ServiceFunctionForwarder> sff_list = new ArrayList<ServiceFunctionForwarder>();

    // build dict for each bridge
    int counter_x = 0;

    while (br.hasNext()) {
      List<ServiceFunctionDictionary> sf_dicts_list = new ArrayList<ServiceFunctionDictionary>();
      DataPlaneLocator dp_loc = new DataPlaneLocator();
      dp_loc.setTransport("service-locator:vxlan-gpe");
      dp_loc.setIp("");
      dp_loc.setPort(null);
      sff_dp_loc.setDataPlaneLocator(dp_loc);
      Map.Entry Bridge_name = (Map.Entry) br.next();
      SffDataPlaneLocator temp_sff_dp_loc = new SffDataPlaneLocator();

      temp_sff_dp_loc.setDataPlaneLocator(dp_loc);
      temp_sff_dp_loc.setServiceFunctionForwarderOvsOvsOptions(
          sff_dp_loc.getServiceFunctionForwarderOvsOvsOptions());
      temp_sff_dp_loc.setName("vxgpe");
      temp_sff_dp_loc.getDataPlaneLocator().setPort("6633");
      temp_sff_dp_loc
          .getDataPlaneLocator()
          .setIp(bridgemapping.get(Bridge_name.getKey()).getOVSip());
      logger.debug(" Bridge NAME: " + bridgemapping.get(Bridge_name.getKey()).getBr_name());
      logger.debug(" Bridge ID: " + bridgemapping.get(Bridge_name.getKey()).getNode_ID());
      logger.debug(" Bridge OVS IP: " + bridgemapping.get(Bridge_name.getKey()).getOVSip());

      logger.debug(
          " Bridge SFs size: " + bridgemapping.get(Bridge_name.getKey()).getSfs().size());
      logger.debug(
          " Bridge SF dict size: " + bridgemapping.get(Bridge_name.getKey()).getSFdict().size());

      String bridge_name = Bridge_name.getKey().toString();
      temp_sffNode.setNodeId(bridgemapping.get(Bridge_name.getKey()).getNode_ID());
      logger.debug("bridge  Name >> " + bridgemapping.get(Bridge_name.getKey()).getBr_name());

      for (int sf = 0; sf < bridgemapping.get(Bridge_name.getKey()).getSfs().size(); sf++) {
        temp_sf_dict = new ServiceFunctionDictionary();
        int SF_counter = Integer.MAX_VALUE;

        //for loop to search in sfs_json for this SF in bridge mapping
        for (int sf_counter = 0;
            sf_counter < sfs_json.getServiceFunctions().getServiceFunction().size();
            sf_counter++) {

          if (sfs_json
              .getServiceFunctions()
              .getServiceFunction()
              .get(sf_counter)
              .getName()
              .equals(bridgemapping.get(Bridge_name.getKey()).getSfs().get(sf))) {

            SF_counter = sf_counter;
            break;
          }
        }

        if (SF_counter == Integer.MAX_VALUE) {
          logger.error("Can not find the SF in bridgemapping");
        }
        temp_sf_dict.setName(
            sfs_json.getServiceFunctions().getServiceFunction().get(SF_counter).getName());
        sff_sf_dp_loc = new SffSfDataPlaneLocator();
        sff_sf_dp_loc.setSfDplName("");
        sff_sf_dp_loc.setSffDplName("");
        temp_sff_sf_dp_loc = sff_sf_dp_loc;
        temp_sff_sf_dp_loc.setSffDplName("vxgpe");
        temp_sff_sf_dp_loc.setSfDplName(
            sfs_json
                .getServiceFunctions()
                .getServiceFunction()
                .get(SF_counter)
                .getSfDataPlaneLocator()
                .get(0)
                .getName());

        temp_sf_dict.setSffSfDataPlaneLocator(temp_sff_sf_dp_loc);
        logger.debug(
            "SF DPL  : "
                + sfs_json
                    .getServiceFunctions()
                    .getServiceFunction()
                    .get(SF_counter)
                    .getSfDataPlaneLocator()
                    .get(0)
                    .getName());

        sf_dicts_list.add(temp_sf_dict);

        logger.debug("SF_dicts List : " + sf_dicts_list.get(sf).getName());
      }
      for (int ix = 0; ix < sf_dicts_list.size(); ix++) {
        logger.debug(
            "Dictionary SFs List "
                + ix
                + " : "
                + sf_dicts_list.get(ix).getSffSfDataPlaneLocator().getSfDplName());
      }
      ServiceFunctionForwarder temp_sff = new ServiceFunctionForwarder();
      temp_sff.setServiceFunctionDictionary(new ArrayList<ServiceFunctionDictionary>());

      boolean SFFexist = false;

      // if exists, use current sff, and only update sfs
      if (prev_sff_dict != null) {
        logger.debug(
            "***) Previous SFF size : " + prev_sff_dict.getServiceFunctionForwarder().size());
        for (int sff_counter = 0;
            sff_counter < prev_sff_dict.getServiceFunctionForwarder().size();
            sff_counter++) {
          logger.debug(
              "***) SFF NAME  : "
                  + prev_sff_dict.getServiceFunctionForwarder().get(sff_counter).getName());
          logger.debug(
              "***) bridge SFF NAME  : " + bridgemapping.get(Bridge_name.getKey()).getSFFname());
          if (bridgemapping
              .get(Bridge_name.getKey())
              .getSFFname()
              .equals(prev_sff_dict.getServiceFunctionForwarder().get(sff_counter).getName())) {
            logger.debug(
                "***) Previous SFF exists "
                    + prev_sff_dict.getServiceFunctionForwarder().get(sff_counter).getName());
            temp_sff = prev_sff_dict.getServiceFunctionForwarder().get(sff_counter);
            List<ServiceFunctionDictionary> prev_sff_sf_list =
                temp_sff.getServiceFunctionDictionary();
            for (int new_sf = 0; new_sf < sf_dicts_list.size(); new_sf++) {
              boolean new_sf_update = false;
              for (int index = 0; index < prev_sff_sf_list.size(); index++) {
                logger.debug(
                    "Prev SFF SF List :::: has SF:  " + prev_sff_sf_list.get(index).getName());

                if (prev_sff_sf_list
                    .get(index)
                    .getName()
                    .equals(sf_dicts_list.get(new_sf).getName())) {

                  new_sf_update = true;
                  break;
                }
              }
              if (new_sf_update == false) {
                logger.debug(
                    "NEW SF :::: add sf name " + sf_dicts_list.get(new_sf).getName());
                int position = temp_sff.getServiceFunctionDictionary().size();
                logger.debug("NEW SF :::: position :" + position);

                temp_sff.getServiceFunctionDictionary().add(sf_dicts_list.get(new_sf));
              }
            }
            SFFexist = true;
            break;
          }
        }
      }
      if (SFFexist == false) {
        logger.debug("SFF Exist ? " + SFFexist);
        logger.debug("Prev SFF dict is NULL ? " + prev_sff_dict);

        temp_sff.setName(bridgemapping.get(Bridge_name.getKey()).getSFFname());
        temp_sff.setSffDataPlaneLocator(new ArrayList<SffDataPlaneLocator>());
        temp_sff.getSffDataPlaneLocator().add(temp_sff_dp_loc);
        temp_sff.setServiceFunctionForwarderOvsOvsNode(new ServiceFunctionForwarderOvsOvsNode());
        temp_sff.getServiceFunctionForwarderOvsOvsNode().setNodeId(temp_sffNode.getNodeId());

        temp_sff.setServiceFunctionDictionary(sf_dicts_list);
        // temp_sff.setIpMgmtAddress(bridgemapping.get(Bridge_name.getKey()).getOVSip());
        temp_sff.setServiceNode("");
        temp_sff.setServiceFunctionForwarderOvsOvsBridge(
            new ServiceFunctionForwarderOvsOvsBridge());
        temp_sff
            .getServiceFunctionForwarderOvsOvsBridge()
            .setBridgeName(bridgemapping.get(Bridge_name.getKey()).getBr_name());
        logger.debug(
            counter_x
                + ") -Check one- SFF List IPs: "
                + temp_sff.getSffDataPlaneLocator().get(0).getDataPlaneLocator().getIp());
      }

      for (int y = 0; y < temp_sff.getServiceFunctionDictionary().size(); y++) {
        logger.debug(
            "new SFF list has SF:  "
                + temp_sff.getServiceFunctionDictionary().get(y).getName()
                + "  :: IP:  "
                + temp_sff.getSffDataPlaneLocator().get(0).getDataPlaneLocator().getIp());
      }
      sff_list.add(temp_sff);
      logger.debug(
          counter_x
              + ") -Check two- SFF List IP for SFF 1 "
              + sff_list.get(0).getSffDataPlaneLocator().get(0).getDataPlaneLocator().getIp()
              + " SFF NAME: "
              + sff_list.get(0).getName()
              + " :: "
              + sff_list.get(0).getSffDataPlaneLocator().get(0).getDataPlaneLocator().getPort());
      if (sff_list.size() > 1) {
        logger.debug(
            counter_x
                + ") -Check two- SFF List IP for SFF 2 "
                + sff_list.get(1).getSffDataPlaneLocator().get(0).getDataPlaneLocator().getIp()
                + " SFF NAME: "
                + sff_list.get(1).getName()
                + " :: "
                + sff_list.get(1).getSffDataPlaneLocator().get(0).getDataPlaneLocator().getPort());
      }
      counter_x++;
    }

    logger.info("SFF List output is " + sff_list);

    return sff_list;
  }

  public static Brdict find_ovs_br(VNFdict sf_id, NetworkTopology network_map) {

    Brdict bridge_dict = new Brdict();
    String Node_id = "";
    for (int net = 0; net < network_map.getTopology().size(); net++) {
      if (network_map.getTopology().get(net).getNode() != null) {
        //  System.out.println("NODE is here ***** > ");

        for (int node_entry = 0;
            node_entry < network_map.getTopology().get(net).getNode().size();
            node_entry++) {
          if (network_map.getTopology().get(net).getNode().get(node_entry).getTerminationPoint()
              != null) {
            /* System.out.println(
                 "Termination Point is here ***** size > "
                 + network_map
                     .getTopology()
                     .get(net)
                     .getNode()
                     .get(node_entry)
                     .getTerminationPoint()
                     .size());
            */
            for (int endpoint = 0;
                endpoint
                    < network_map
                        .getTopology()
                        .get(net)
                        .getNode()
                        .get(node_entry)
                        .getTerminationPoint()
                        .size();
                endpoint++) {
              if (bridge_dict.getBr_name() != null) {
                logger.debug("bridge dict is not null ***** > " + bridge_dict);

                break;
              } else if (network_map
                      .getTopology()
                      .get(net)
                      .getNode()
                      .get(node_entry)
                      .getTerminationPoint()
                      .get(endpoint)
                      .getOvsdbInterfaceExternalIds()
                  != null) {

                for (int external_id = 0;
                    external_id
                        < network_map
                            .getTopology()
                            .get(net)
                            .getNode()
                            .get(node_entry)
                            .getTerminationPoint()
                            .get(endpoint)
                            .getOvsdbInterfaceExternalIds()
                            .size();
                    external_id++) {
                  if (network_map
                          .getTopology()
                          .get(net)
                          .getNode()
                          .get(node_entry)
                          .getTerminationPoint()
                          .get(endpoint)
                          .getOvsdbInterfaceExternalIds()
                          .get(external_id)
                          .getExternalIdValue()
                      != null) {
                    /*  System.out.println("SF VNF- Neutron Port ID: " + sf_id.getNeutronPortId());
                    System.out.println(
                        "Network Topology - Ovsdb Interface External Ids - External Id Value: "
                        + network_map
                            .getTopology()
                            .get(net)
                            .getNode()
                            .get(node_entry)
                            .getTerminationPoint()
                            .get(endpoint)
                            .getOvsdbInterfaceExternalIds()
                            .get(external_id)
                            .getExternalIdValue());
                    */
                    if (network_map
                        .getTopology()
                        .get(net)
                        .getNode()
                        .get(node_entry)
                        .getTerminationPoint()
                        .get(endpoint)
                        .getOvsdbInterfaceExternalIds()
                        .get(external_id)
                        .getExternalIdValue()
                        .equals(sf_id.getNeutronPortId())) {
                      /*
                      System.out.println("Found");
                      System.out.println(
                          "OVSDB Bridge Name: "
                          + network_map
                              .getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getOvsdbBridgeName());
                      System.out.println(
                          "OVSDB Bridge UUID: "
                          + network_map
                              .getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getOvsdbBridgeUuid());*/
                      bridge_dict.setBr_uuid(
                          network_map
                              .getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getOvsdbBridgeUuid());
                      bridge_dict.setBr_name(
                          network_map
                              .getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getOvsdbBridgeName());

                      String full_node_id =
                          network_map.getTopology().get(net).getNode().get(node_entry).getNodeId();
                      String remove_it = "/bridge/" + bridge_dict.getBr_name();
                      Node_id = full_node_id.replaceAll(remove_it, "");
                      String Openflow_node_id =
                          network_map
                              .getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getOvsdbManagedBy(); //getOvsdbBridgeOpenflowNodeRef();
                      logger.debug("OF NODE ID ***** > " + Openflow_node_id);

                      //added new
                      bridge_dict.setNodeID(Openflow_node_id);

                      bridge_dict.setTap_port(
                          network_map
                              .getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getTerminationPoint()
                              .get(endpoint)
                              .getOvsdbName());
                      break;

                    } else {
                      //  System.out.println("NOT Found");
                    }
                  }
                }
              }
            }
          }
        }
        if (bridge_dict.getBr_name() != null) {
          for (int node_entry = 0;
              node_entry < network_map.getTopology().get(net).getNode().size();
              node_entry++) {

            if (network_map
                    .getTopology()
                    .get(net)
                    .getNode()
                    .get(node_entry)
                    .getNodeId()
                    .equals(Node_id)
                && network_map
                        .getTopology()
                        .get(net)
                        .getNode()
                        .get(node_entry)
                        .getOvsdbConnectionInfo()
                    != null) {
              // System.out.println("bridge dict name ***** > " + bridge_dict.getBr_name());

              bridge_dict.setOVS_port(
                  network_map
                      .getTopology()
                      .get(net)
                      .getNode()
                      .get(node_entry)
                      .getOvsdbConnectionInfo()
                      .getRemotePort());
              bridge_dict.setOVSIp(
                  network_map
                      .getTopology()
                      .get(net)
                      .getNode()
                      .get(node_entry)
                      .getOvsdbConnectionInfo()
                      .getRemoteIp());
              break;
            }
          }
        }
      }
    }

    if (bridge_dict.getBr_uuid() != null
        && bridge_dict.getBr_name() != null
        && bridge_dict.getOVS_port() != null
        && bridge_dict.getOVSIp() != null
        && bridge_dict.getTap_port() != null
        && bridge_dict.getNodeID() != null) {
      logger.info(
          "bridge dictionary is  created successfully!! NAME: "
              + bridge_dict.getBr_name()
              + "  UUID: "
              + bridge_dict.getBr_uuid());

      return bridge_dict;
    } else {
      logger.error("bridge dictionary is not created successfully!!");
      return null;
    }
  }

  public class BridgeMapping {

    private List<String> sfs = new ArrayList<String>();
    private String ovs_ip;
    private String sff_name;
    private HashMap<String, SF_dict> sf = new HashMap<String, SF_dict>();
    private String Br_name;
    private String Node_id;

    public HashMap<String, SF_dict> getSFdict() {
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
      this.Br_name = name;
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
      this.sff_name = sff_name;
    }

    public String getNode_ID() {
      return Node_id;
    }

    public void setNode_ID(String nodeID) {
      this.Node_id = nodeID;
    }
  }

  public class SF_dict {

    private String tap_port;

    public String getTap_port() {
      return tap_port;
    }

    public void setTap_port(String name) {
      this.tap_port = name;
    }
  }

  @Override
  public String GetBytesCount(SFCCdict SFCC_dict) {
    ResponseEntity<String> response = getNetworkTopologyList();

    if (!response.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to get network topology");
    }
    String network_s = response.getBody();
    if (network_s == null) {
      return null;
    }
    Gson mapper = new Gson();
    NetworkJSON network = new NetworkJSON();
    logger.debug("Network is " + network_s);
    network = mapper.fromJson(response.getBody(), NetworkJSON.class);
    String BytesCount = null;

    NetworkTopology networkmap;
    networkmap = network.getNetworkTopology();
    for (int net = 0; net < networkmap.getTopology().size(); net++) {
      if (networkmap.getTopology().get(net).getNode() != null) {
        logger.debug(
            "Counter: "
                + net
                + " -->  [OpenFlow Node ID]: "
                + networkmap.getTopology().get(net).getTopologyId());

        for (int i = 0; i < networkmap.getTopology().get(net).getNode().size(); i++) {

          if (networkmap.getTopology().get(net).getNode().get(i).getNodeId().contains("openflow")) {
            logger.debug(" OpenFlow NODE is Found");

            ResponseEntity<String> response2 =
                getSfcClassifierTable(
                    networkmap.getTopology().get(net).getNode().get(i).getNodeId());

            if (!response2.getStatusCode().is2xxSuccessful()) {
              logger.error("Unable to get OpenFlowPluginTable");
            }

            String table_s = response2.getBody();
            logger.debug(" OF plugin Table 11 is  " + table_s);

            Gson mapper2 = new Gson();
            Table tableJSON = new Table();
            tableJSON = mapper.fromJson(response2.getBody(), Table.class);
            Table_ table = tableJSON.getTable().get(0);
            if (table.getFlow() != null) {
              logger.debug(" Table has Flows");

              for (int x = 0; x < table.getFlow().size(); x++) {
                if (table.getFlow().get(x).getMatch() != null) {
                  logger.debug(" Match is not null");

                  Match matching_rules = table.getFlow().get(x).getMatch();
                  if (Check_matching(matching_rules, SFCC_dict.getAclMatchCriteria().get(0))) {
                    logger.debug("Check Matching is correct");

                    if (table.getFlow().get(x).getFlowStatistics().getByteCount() != null) {
                      BytesCount = table.getFlow().get(x).getFlowStatistics().getByteCount();
                      logger.debug(" Bytes Count is found =" + BytesCount);
                    }
                  }
                }
              }
            }
          } else {
            logger.error(" NO Openflow Node EXIST");
          }
        }
      }
    }
    return BytesCount;
  }

  public boolean Check_matching(Match matching_rules, AclMatchCriteria acl) {
    boolean matched = false;
    String aclProtocol = null;
    if (acl.getProtocol() != null) {
      logger.debug("ACL has Protocol");

      aclProtocol = acl.getProtocol().toString();
      logger.debug("euqla to = " + aclProtocol);
    }

    String Match_protocol = null;
    if (matching_rules.getIpMatch() != null) {
      logger.debug("ACL has IP");

      if (matching_rules.getIpMatch().getIpProtocol() != null) {

        Match_protocol = matching_rules.getIpMatch().getIpProtocol();
        logger.debug("euqla to = " + Match_protocol);
      }
    }

    String aclDstPort = null;
    if (acl.getDestPort() != null) {
      logger.debug("ACL has dest port");

      if (acl.getDestPort() != 0) {
        aclDstPort = acl.getDestPort().toString();
      }
    }
    String aclSrcPort = null;
    if (acl.getSrcPort() != null) {
      logger.debug("ACL has src port");

      if (acl.getSrcPort() != 0) {
        aclSrcPort = acl.getSrcPort().toString();
      }
    }

    if (CheckEquality(acl.getDestIpv4(), matching_rules.getIpv4Destination()) == true) {
      if (CheckEquality(acl.getSourceIpv4(), matching_rules.getIpv4Source()) == true) {
        if (CheckEquality(aclProtocol, Match_protocol) == true) {
          if (acl.getProtocol() == 6) {
            if (CheckEquality(aclDstPort, matching_rules.getTcpDestinationPort()) == true) {
              if (CheckEquality(aclSrcPort, matching_rules.getTcpSourcePort()) == true) {
                logger.debug("[ Matched ]");
                matched = true;
              }
            }
          } else if (acl.getProtocol() == 17) {
            if (CheckEquality(aclDstPort, matching_rules.getUdpDestinationPort()) == true) {
              if (CheckEquality(aclSrcPort, matching_rules.getUdpSourcePort()) == true) {
                logger.debug("[ Matched ]");

                matched = true;
              }
            }
          }
        }
      }
    }
    logger.debug("[ Matched or not ??  " + matched);

    return matched;
  }

  public boolean CheckEquality(String A, String B) {

    logger.debug(
        "Compare these two values: Value from ACL of opendaylight: ["
            + A
            + "] , Value from Matching Criteria of Test_SFC Classifier Descriptor: ["
            + B
            + "]");

    if (A == null && B == null) {
      return true;
    } else if (A == null && B != null) {

      return false;
    } else if (A != null && B == null) {

      return false;
    } else {
      if (A.equals(B)) {

        return true;
      } else {

        return false;
      }
    }
  }

  @Override
  public ResponseEntity<String> DeleteSFC(String instance_id, boolean isSymmetric) {

    List<String> instance_list = new ArrayList<String>();
    instance_list.add(0, instance_id);
    if (isSymmetric == true) {
      String reverse_id = instance_id + "-Reverse";
      instance_list.add(1, reverse_id);
    }
    ResponseEntity<String> rsp_result = null;
    for (int ins = 0; ins < instance_list.size(); ins++) {
      RSPJSON rsp_dict = new RSPJSON();
      Input x = new Input();
      x.setName(instance_list.get(ins));

      rsp_dict.setInput(x);
      rsp_result = deleteODLrsp(rsp_dict);

      if (!rsp_result.getStatusCode().is2xxSuccessful()) {

        logger.error("Unable to delete RSP ! ");
      }
    }

    deleteODLsfp(instance_id);

    deleteODLsfc(instance_id.substring(5));

    return rsp_result;
  }

  @Override
  public ResponseEntity<String> DeleteSFP(String instance_id, boolean isSymmetric) {

    List<String> instance_list = new ArrayList<String>();
    instance_list.add(0, instance_id);
    if (isSymmetric == true) {
      String reverse_id = instance_id + "-Reverse";
      instance_list.add(1, reverse_id);
    }
    ResponseEntity<String> rsp_result = null;
    for (int ins = 0; ins < instance_list.size(); ins++) {
      RSPJSON rsp_dict = new RSPJSON();
      Input x = new Input();
      x.setName(instance_list.get(ins));

      rsp_dict.setInput(x);
      rsp_result = deleteODLrsp(rsp_dict);

      if (!rsp_result.getStatusCode().is2xxSuccessful()) {

        logger.error("Unable to delete RSP ! ");
      }
    }

    deleteODLsfp(instance_id);
    return rsp_result;
  }

  public void DeleteSFs(String instance_id, boolean isSymmetric) throws IOException {
    //--------------- delete SFs and Update SFF

    // Need to check if the SFs involved in other RSPs !!!
    Gson mapper = new Gson();
    logger.info("SFC NAME " + instance_id.substring(5));
    ResponseEntity<String> rsp_response = getODLrsp(instance_id);
    String stringResponse1 = rsp_response.getBody();

    RenderedServicePaths rsp = mapper.fromJson(stringResponse1, RenderedServicePaths.class);
    Gson mapper_sff = new Gson();
    ResponseEntity<String> sff_response = getODLsff();

    SFFJSON sffs = mapper_sff.fromJson(sff_response.getBody(), SFFJSON.class);

    for (int y = 0;
        y < rsp.getRenderedServicePath().get(0).getRenderedServicePathHop().size();
        y++) {
      logger.info(
          "Deleted SF  NAME "
              + rsp.getRenderedServicePath()
                  .get(0)
                  .getRenderedServicePathHop()
                  .get(y)
                  .getServiceFunctionName());
      for (int x = 0;
          x < sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size();
          x++) {
        logger.debug(
            "SFF SIZE " + sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size());

        for (int z = 0;
            z
                < sffs.getServiceFunctionForwarders()
                    .getServiceFunctionForwarder()
                    .get(x)
                    .getServiceFunctionDictionary()
                    .size();
            z++) {
          System.out.println(
              "SF Dictionary SIZE "
                  + sffs.getServiceFunctionForwarders()
                      .getServiceFunctionForwarder()
                      .get(x)
                      .getServiceFunctionDictionary()
                      .size());

          if (sffs.getServiceFunctionForwarders()
              .getServiceFunctionForwarder()
              .get(x)
              .getServiceFunctionDictionary()
              .get(z)
              .getName()
              .equals(
                  rsp.getRenderedServicePath()
                      .get(0)
                      .getRenderedServicePathHop()
                      .get(y)
                      .getServiceFunctionName())) {
            logger.debug(
                "EQUAL >>>> "
                    + sffs.getServiceFunctionForwarders()
                        .getServiceFunctionForwarder()
                        .get(x)
                        .getServiceFunctionDictionary()
                        .get(z)
                        .getName());

            sffs.getServiceFunctionForwarders()
                .getServiceFunctionForwarder()
                .get(x)
                .getServiceFunctionDictionary()
                .remove(z);
          }
        }
        updateODLsff(
            sffs,
            sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getName());
      }
      deleteODLsf(
          rsp.getRenderedServicePath()
              .get(0)
              .getRenderedServicePathHop()
              .get(y)
              .getServiceFunctionName());
    }

    logger.debug(
        "SFF NAME "
            + sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getName());
    logger.debug(
        "SFF Data plane Locator NAME "
            + sffs.getServiceFunctionForwarders()
                .getServiceFunctionForwarder()
                .get(0)
                .getSffDataPlaneLocator()
                .get(0)
                .getName());
    logger.debug(
        "SFF Bridge NAME "
            + sffs.getServiceFunctionForwarders()
                .getServiceFunctionForwarder()
                .get(0)
                .getServiceFunctionForwarderOvsOvsBridge()
                .getBridgeName());
    logger.debug(
        "SFF Node ID "
            + sffs.getServiceFunctionForwarders()
                .getServiceFunctionForwarder()
                .get(0)
                .getServiceFunctionForwarderOvsOvsNode()
                .getNodeId());
    if (sffs.getServiceFunctionForwarders()
            .getServiceFunctionForwarder()
            .get(0)
            .getServiceFunctionDictionary()
            .size()
        != 0) {
      logger.debug(
          "SF Dictionary SF- Name "
              + sffs.getServiceFunctionForwarders()
                  .getServiceFunctionForwarder()
                  .get(0)
                  .getServiceFunctionDictionary()
                  .get(0)
                  .getName());
    } else {
      logger.warn("SF Dictionary is empty ");
    }
    //-------------------------------
  }

  public void DeleteSF(String SF_name) throws IOException {
    //--------------- delete SFs and Update SFF

    // Need to check if the SFs involved in other RSPs !!!

    Gson mapper_sff = new Gson();
    ResponseEntity<String> sff_response = getODLsff();

    SFFJSON sffs = mapper_sff.fromJson(sff_response.getBody(), SFFJSON.class);

    for (int x = 0;
        x < sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size();
        x++) {
      System.out.println(
          "SFF SIZE " + sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size());

      for (int z = 0;
          z
              < sffs.getServiceFunctionForwarders()
                  .getServiceFunctionForwarder()
                  .get(x)
                  .getServiceFunctionDictionary()
                  .size();
          z++) {
        System.out.println(
            "SF Dictionary SIZE "
                + sffs.getServiceFunctionForwarders()
                    .getServiceFunctionForwarder()
                    .get(x)
                    .getServiceFunctionDictionary()
                    .size());

        if (sffs.getServiceFunctionForwarders()
            .getServiceFunctionForwarder()
            .get(x)
            .getServiceFunctionDictionary()
            .get(z)
            .getName()
            .equals(SF_name)) {
          logger.debug(
              "EQUAL >>>> "
                  + sffs.getServiceFunctionForwarders()
                      .getServiceFunctionForwarder()
                      .get(x)
                      .getServiceFunctionDictionary()
                      .get(z)
                      .getName());

          sffs.getServiceFunctionForwarders()
              .getServiceFunctionForwarder()
              .get(x)
              .getServiceFunctionDictionary()
              .remove(z);
        }
      }
      updateODLsff(
          sffs, sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getName());
    }
    deleteODLsf(SF_name);

    logger.debug(
        "SFF NAME "
            + sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(0).getName());
    logger.debug(
        "SFF Data plane Locator NAME "
            + sffs.getServiceFunctionForwarders()
                .getServiceFunctionForwarder()
                .get(0)
                .getSffDataPlaneLocator()
                .get(0)
                .getName());
    logger.debug(
        "SFF Bridge NAME "
            + sffs.getServiceFunctionForwarders()
                .getServiceFunctionForwarder()
                .get(0)
                .getServiceFunctionForwarderOvsOvsBridge()
                .getBridgeName());
    logger.debug(
        "SFF Node ID "
            + sffs.getServiceFunctionForwarders()
                .getServiceFunctionForwarder()
                .get(0)
                .getServiceFunctionForwarderOvsOvsNode()
                .getNodeId());
    if (sffs.getServiceFunctionForwarders()
            .getServiceFunctionForwarder()
            .get(0)
            .getServiceFunctionDictionary()
            .size()
        != 0) {
      logger.debug(
          "SF Dictionary SF- Name "
              + sffs.getServiceFunctionForwarders()
                  .getServiceFunctionForwarder()
                  .get(0)
                  .getServiceFunctionDictionary()
                  .get(0)
                  .getName());
    } else {
      logger.warn("SF Dictionary is empty ");
    }
    //-------------------------------
  }

  @Override
  public String GetConnectedSFF(String SF_name) throws IOException {

    //--------------- delete SFs and Update SFF

    // Need to check if the SFs involved in other RSPs !!!
    String ConnectedSFF = null;
    Gson mapper_sff = new Gson();
    ResponseEntity<String> sff_response = getODLsff();

    SFFJSON sffs = mapper_sff.fromJson(sff_response.getBody(), SFFJSON.class);

    for (int x = 0;
        x < sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size();
        x++) {
      System.out.println(
          "SFF SIZE " + sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().size());

      for (int z = 0;
          z
              < sffs.getServiceFunctionForwarders()
                  .getServiceFunctionForwarder()
                  .get(x)
                  .getServiceFunctionDictionary()
                  .size();
          z++) {
        logger.debug(
            "SF Dictionary SIZE "
                + sffs.getServiceFunctionForwarders()
                    .getServiceFunctionForwarder()
                    .get(x)
                    .getServiceFunctionDictionary()
                    .size());

        if (sffs.getServiceFunctionForwarders()
            .getServiceFunctionForwarder()
            .get(x)
            .getServiceFunctionDictionary()
            .get(z)
            .getName()
            .equals(SF_name)) {
          logger.debug(
              "Connected SFF name >>>> "
                  + sffs.getServiceFunctionForwarders()
                      .getServiceFunctionForwarder()
                      .get(x)
                      .getName());
          ConnectedSFF =
              sffs.getServiceFunctionForwarders().getServiceFunctionForwarder().get(x).getName();
        }
      }
    }
    return ConnectedSFF;
  }

  //return the UUID of the OVS bridge
  @Override
  public String GetHostID(String SF_Neutron_port_id) {
    // System.out.println("LOCATE OVS TO SF >>> ");

    ResponseEntity<String> response = getNetworkTopologyList();

    if (!response.getStatusCode().is2xxSuccessful()) {
      logger.error("Unable to get network topology");
    }
    String network_s = response.getBody();
    if (network_s == null) {
      return null;
    }
    String HostNodeID = null;
    Gson mapper = new Gson();
    NetworkJSON network = new NetworkJSON();
    logger.debug("Network is " + network_s);
    network = mapper.fromJson(response.getBody(), NetworkJSON.class);

    HashMap<String, BridgeMapping> br_mapping = new HashMap<String, BridgeMapping>();
    NetworkTopology networkmap;
    networkmap = network.getNetworkTopology();

    for (int net = 0; net < networkmap.getTopology().size(); net++) {
      if (networkmap.getTopology().get(net).getNode() != null) {
        //  System.out.println("NODE is here ***** > ");

        for (int node_entry = 0;
            node_entry < networkmap.getTopology().get(net).getNode().size();
            node_entry++) {
          if (networkmap.getTopology().get(net).getNode().get(node_entry).getTerminationPoint()
              != null) {
            /* System.out.println("Termination Point is here ***** size > " +
            networkmap.getTopology().get(net).getNode().get(node_entry).getTerminationPoint().size
                ());
                */

            for (int endpoint = 0;
                endpoint
                    < networkmap
                        .getTopology()
                        .get(net)
                        .getNode()
                        .get(node_entry)
                        .getTerminationPoint()
                        .size();
                endpoint++) {
              if (networkmap
                      .getTopology()
                      .get(net)
                      .getNode()
                      .get(node_entry)
                      .getTerminationPoint()
                      .get(endpoint)
                      .getOvsdbInterfaceExternalIds()
                  != null) {

                for (int external_id = 0;
                    external_id
                        < networkmap
                            .getTopology()
                            .get(net)
                            .getNode()
                            .get(node_entry)
                            .getTerminationPoint()
                            .get(endpoint)
                            .getOvsdbInterfaceExternalIds()
                            .size();
                    external_id++) {
                  if (networkmap
                          .getTopology()
                          .get(net)
                          .getNode()
                          .get(node_entry)
                          .getTerminationPoint()
                          .get(endpoint)
                          .getOvsdbInterfaceExternalIds()
                          .get(external_id)
                          .getExternalIdValue()
                      != null) {
                    //System.out.println("SF VNF- Neutron Port ID: " + SF_Neutron_port_id);
                    /*   System.out.println("Network Topology - Ovsdb Interface External Ids - External Id Value: " +
                    networkmap.getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getTerminationPoint()
                              .get(endpoint)
                              .getOvsdbInterfaceExternalIds()
                              .get(external_id)
                              .getExternalIdValue());
                              */

                    if (networkmap
                        .getTopology()
                        .get(net)
                        .getNode()
                        .get(node_entry)
                        .getTerminationPoint()
                        .get(endpoint)
                        .getOvsdbInterfaceExternalIds()
                        .get(external_id)
                        .getExternalIdValue()
                        .equals(SF_Neutron_port_id)) {

                      logger.debug(
                          "OVSDB Bridge Name: "
                              + networkmap
                                  .getTopology()
                                  .get(net)
                                  .getNode()
                                  .get(node_entry)
                                  .getOvsdbBridgeName());
                      logger.debug(
                          "OVSDB Bridge UUID: "
                              + networkmap
                                  .getTopology()
                                  .get(net)
                                  .getNode()
                                  .get(node_entry)
                                  .getOvsdbBridgeUuid());
                      HostNodeID =
                          networkmap
                              .getTopology()
                              .get(net)
                              .getNode()
                              .get(node_entry)
                              .getOvsdbBridgeUuid();
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return HostNodeID;
  }
}
