package org.project.sfc.com.openbaton_nfvo.configurations;

/**
 * Created by mah on 3/11/16.
 */

import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.stereotype.Service;

        import javax.annotation.PostConstruct;

/**
 * Created by maa on 01.02.16.
 */
@Service
@ConfigurationProperties(prefix = "sfcorchestrator")
public class SfcOrchestratorConfiguration {

    private String baseUrl;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @PostConstruct
    private void init(){

        logger.debug("SFCO BASEURL IS " + baseUrl);

    }
}