
package org.project.sfc.com.SFFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class DataPlaneLocator {



    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("port")
    @Expose
    private String port;
    @SerializedName("transport")
    @Expose
    private String transport;
    /**
     * 
     * @return
     *     The transport
     */
    public String getTransport() {
        return transport;
    }

    /**
     * 
     * @param transport
     *     The transport
     */
    public void setTransport(String transport) {
        this.transport = transport;
    }

    /**
     * 
     * @return
     *     The port
     */
    public String getPort() {
        return port;
    }

    /**
     * 
     * @param port
     *     The port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * 
     * @return
     *     The ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 
     * @param ip
     *     The ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

}
