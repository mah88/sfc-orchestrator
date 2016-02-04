
package org.project.sfc.com.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OvsdbConnectionInfo {

    @SerializedName("local-ip")
    @Expose
    private String localIp;
    @SerializedName("remote-ip")
    @Expose
    private String remoteIp;
    @SerializedName("local-port")
    @Expose
    private Integer localPort;
    @SerializedName("remote-port")
    @Expose
    private Integer remotePort;

    /**
     * 
     * @return
     *     The localIp
     */
    public String getLocalIp() {
        return localIp;
    }

    /**
     * 
     * @param localIp
     *     The local-ip
     */
    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    /**
     * 
     * @return
     *     The remoteIp
     */
    public String getRemoteIp() {
        return remoteIp;
    }

    /**
     * 
     * @param remoteIp
     *     The remote-ip
     */
    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    /**
     * 
     * @return
     *     The localPort
     */
    public Integer getLocalPort() {
        return localPort;
    }

    /**
     * 
     * @param localPort
     *     The local-port
     */
    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    /**
     * 
     * @return
     *     The remotePort
     */
    public Integer getRemotePort() {
        return remotePort;
    }

    /**
     * 
     * @param remotePort
     *     The remote-port
     */
    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

}
