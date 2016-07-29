package org.project.sfc.com.openbaton_nfvo.configurations;

/**
 * Created by mah on 3/11/16.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@ConfigurationProperties(prefix = "nfvo")
public class NfvoConfiguration {

    private String baseURL;
    private String basePort;
    private String username;
    private String password;
    private boolean security;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getBasePort() {
        return basePort;
    }

    public void setBasePort(String basePort) {
        this.basePort = basePort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    @PostConstruct
    private void init(){

        logger.debug("OBBASEURL IS " + baseURL + " OBPORT IS " + basePort + " USERNAME " + username + " PASSWORD " + password);

    }
}