package org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

import org.project.sfc.com.SfcDriver.SfcDriverCaller;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NeutronPorts.NeutronPorts;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NeutronPorts.Port;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.Token.Token;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.Token.Token_;
//import org.project.sfc.com.openbaton_nfvo.configurations.SfcConfiguration;
import org.project.sfc.com.openbaton_nfvo.utils.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by mah on 3/3/16.
 */
@Service
@Scope(value = "prototype")
public class NeutronClient  {


  public String Openstack_ip;
  private Properties properties;

  public String Identity_port = "5000";
  public String Networking_port = "9696";

  public String Openstack_username ;
  public String Openstack_password ;
  public String Openstack_tenantname ;

  public String Config_token_URL = "/v2.0/tokens";
  public String Config_neutronport_URL = "/v2.0/ports";
  private static Logger logger = LoggerFactory.getLogger(Opendaylight.class);

  private void init()  throws IOException{


    this.Identity_port = Identity_port;
    this.Networking_port = Networking_port;



    this.Config_token_URL = Config_token_URL;
    this.Config_neutronport_URL = Config_neutronport_URL;
  }

public  NeutronClient() throws IOException{
    this.properties = ConfigReader.readProperties();
    this.Openstack_ip  = properties.getProperty("openstack.ip");
    this.Openstack_username = properties.getProperty("openstack.username");
    this.Openstack_password = properties.getProperty("openstack.password");
    this.Openstack_tenantname = properties.getProperty("openstack.tenantname");
  }

  public Token getToken(Tokenbody data) {

    String Full_URL = "http://" + Openstack_ip + ":" + Identity_port + Config_token_URL;
    String plainCreds = Openstack_username + ":" + Openstack_password;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Basic " + base64Creds);
    Gson mapper = new Gson();
    Token result = new Token();
    ResponseEntity<String> request = null;

    HttpEntity<String> postEntity =
        new HttpEntity<String>(mapper.toJson(data, Tokenbody.class), headers);
    try {
      request = template.exchange(Full_URL, HttpMethod.POST, postEntity, String.class);
    }    catch (final HttpClientErrorException e) {
      System.out.println(e.getStatusCode());
      System.out.println(e.getResponseBodyAsString());
    }
    logger.debug(
        "Get the X-Auth-Token has produced http status:"
            + request.getStatusCode()
            + " with body: "
            + request.getBody());

    if (!request.getStatusCode().is2xxSuccessful()) {
      result = null;
    } else {
      result = mapper.fromJson(request.getBody(), Token.class);
      logger.debug(
          "RESULT IS "
              + request.getStatusCode()
              + " with body "
              + mapper.toJson(result, Token.class));
    }

    return result;
  }

  public NeutronPorts getNeutronPorts() {

    logger.info("OPNESTACK IP: "+ this.Openstack_ip);
    String Full_URL =
        "http://" + Openstack_ip + ":" + Networking_port + Config_neutronport_URL;
    String plainCreds = Openstack_username + ":" + Openstack_password;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Basic " + base64Creds);
    Tokenbody data = new Tokenbody();
    Auth authentication = new Auth();
    authentication.setTenantName(Openstack_tenantname);
    PasswordCredentials passcr = new PasswordCredentials();
    passcr.setPassword(Openstack_password);
    passcr.setUsername(Openstack_username);
    authentication.setPasswordCredentials(passcr);
    data.setAuth(authentication);
    Token tokenX = getToken(data);
    headers.add("X-Auth-Token", getXauthToken(tokenX));
    Gson mapper = new Gson();
    NeutronPorts result = new NeutronPorts();
    ResponseEntity<String> request = null;

    HttpEntity<String> getEntity = new HttpEntity<String>(headers);
    request = template.exchange(Full_URL, HttpMethod.GET, getEntity, String.class);
    if (!request.getStatusCode().is2xxSuccessful()) {
      result = null;
    } else {
      result = mapper.fromJson(request.getBody(), NeutronPorts.class);
      logger.debug(
          "Getting the neutron port has produced http status:"
              + request.getStatusCode()
              + " with body: "
              + request.getBody());
    }

    return result;
  }

  public String getXauthToken(Token x) {
    String x_auth_token = x.getAccess().getToken().getId();
    return x_auth_token;
  }

  public String getNeutronPortID(String ip) {
    NeutronPorts NPorts = getNeutronPorts();
    String Neutron_id = "";
    List<Port> ports = NPorts.getPorts();

    //It is configured to private IPs
    for (int i = 0; i < ports.size(); i++) {

      if (ports.get(i).getFixedIps().get(0).getIpAddress().equals(ip)) {
        Neutron_id = ports.get(i).getId();
        break;
      }
    }

    return Neutron_id;
  }

  public String getTenantID() {
    Tokenbody data = new Tokenbody();
    Auth authentication = new Auth();
    authentication.setTenantName(Openstack_tenantname);
    PasswordCredentials passcr = new PasswordCredentials();
    passcr.setPassword(Openstack_password);
    passcr.setUsername(Openstack_username);
    authentication.setPasswordCredentials(passcr);
    data.setAuth(authentication);
    Token tokenX = getToken(data);
    String tenant_id = "";
    Token_ token_ = tokenX.getAccess().getToken();
    tenant_id = token_.getTenant().getId();
    return tenant_id;
  }

  @Generated("org.jsonschema2pojo")
  public class Auth {

    @SerializedName("tenantName")
    @Expose
    private String tenantName;

    @SerializedName("passwordCredentials")
    @Expose
    private PasswordCredentials passwordCredentials;

    /**
     *
     * @return The tenantName
     */
    public String getTenantName() {
      return tenantName;
    }

    /**
     *
     * @param tenantName The tenantName
     */
    public void setTenantName(String tenantName) {
      this.tenantName = tenantName;
    }

    /**
     *
     * @return The passwordCredentials
     */
    public PasswordCredentials getPasswordCredentials() {
      return passwordCredentials;
    }

    /**
     *
     * @param passwordCredentials The passwordCredentials
     */
    public void setPasswordCredentials(PasswordCredentials passwordCredentials) {
      this.passwordCredentials = passwordCredentials;
    }
  }

  @Generated("org.jsonschema2pojo")
  public class PasswordCredentials {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    /**
     *
     * @return The username
     */
    public String getUsername() {
      return username;
    }

    /**
     *
     * @param username The username
     */
    public void setUsername(String username) {
      this.username = username;
    }

    /**
     *
     * @return The password
     */
    public String getPassword() {
      return password;
    }

    /**
     *
     * @param password The password
     */
    public void setPassword(String password) {
      this.password = password;
    }
  }

  @Generated("org.jsonschema2pojo")
  public class Tokenbody {

    @SerializedName("auth")
    @Expose
    private Auth auth;

    /**
     *
     * @return The auth
     */
    public Auth getAuth() {
      return auth;
    }

    /**
     *
     * @param auth The auth
     */
    public void setAuth(Auth auth) {
      this.auth = auth;
    }
  }



}
