
package org.project.sfc.com.NetworkJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ConnectionInfo {

    @SerializedName("remote-port")
    @Expose
    private String remotePort;
    @SerializedName("local-ip")
    @Expose
    private String localIp;
    @SerializedName("remote-ip")
    @Expose
    private String remoteIp;
    @SerializedName("local-port")
    @Expose
    private String localPort;

    /**
     * 
     * @return
     *     The remotePort
     */
    public String getRemotePort() {
        return remotePort;
    }

    /**
     * 
     * @param remotePort
     *     The remote-port
     */
    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

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
    public String getLocalPort() {
        return localPort;
    }

    /**
     * 
     * @param localPort
     *     The local-port
     */
    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }

}
