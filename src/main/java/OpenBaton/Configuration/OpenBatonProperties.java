package OpenBaton.Configuration;

/**
 * Created by mah on 3/1/16.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by maa on 22.01.16.
 */
@Service
@ConfigurationProperties(prefix="sfco")
public class OpenBatonProperties {

    private String internalURL;
    private String vnfmIP;
    private String vnfmPort;

    public OpenBatonProperties() {
    }

    public String getInternalURL() {
        return internalURL;
    }

    public void setInternalURL(String internalURL) {
        this.internalURL = internalURL;
    }

    public String getVnfmIP() {
        return vnfmIP;
    }

    public void setVnfmIP(String vnfmIP) {
        this.vnfmIP = vnfmIP;
    }

    public String getVnfmPort() {
        return vnfmPort;
    }

    public void setVnfmPort(String vnfmPort) {
        this.vnfmPort = vnfmPort;
    }
}