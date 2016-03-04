
package org.project.sfc.com.NeutronPorts;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class DnsAssignment {

    @SerializedName("hostname")
    @Expose
    private String hostname;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;
    @SerializedName("fqdn")
    @Expose
    private String fqdn;

    /**
     * 
     * @return
     *     The hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * 
     * @param hostname
     *     The hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * 
     * @return
     *     The ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 
     * @param ipAddress
     *     The ip_address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * 
     * @return
     *     The fqdn
     */
    public String getFqdn() {
        return fqdn;
    }

    /**
     * 
     * @param fqdn
     *     The fqdn
     */
    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

}
