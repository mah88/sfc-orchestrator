
package org.project.sfc.com.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Matches {

    @SerializedName("protocol")
    @Expose
    private Integer protocol;
    @SerializedName("dest-port")
    @Expose
    private Integer destPort;

    @SerializedName("destination-port-range")
    @Expose
    private DestinationPortRange destinationPortRange;

    /**
     * 
     * @return
     *     The dest-port
     */
    public Integer getDestPort() {
        return destPort;
    }

    /**
     * 
     * @param dest-port
     *     The protocol
     */
    public void setDestPort(Integer port) {
        this.destPort= port;
    }


    /**
     *
     * @return
     *     The protocol
     */
    public Integer getProtocol() {
        return protocol;
    }

    /**
     *
     * @param protocol
     *     The protocol
     */
    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    /**
     * 
     * @return
     *     The destinationPortRange
     */
    public DestinationPortRange getDestinationPortRange() {
        return destinationPortRange;
    }

    /**
     * 
     * @param destinationPortRange
     *     The destination-port-range
     */
    public void setDestinationPortRange(DestinationPortRange destinationPortRange) {
        this.destinationPortRange = destinationPortRange;
    }

}
