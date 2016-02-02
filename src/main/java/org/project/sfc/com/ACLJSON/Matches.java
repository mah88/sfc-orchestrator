
package org.project.sfc.com.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Matches {

    @SerializedName("protocol")
    @Expose
    private String protocol;
    @SerializedName("destination-port-range")
    @Expose
    private DestinationPortRange destinationPortRange;

    /**
     * 
     * @return
     *     The protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * 
     * @param protocol
     *     The protocol
     */
    public void setProtocol(String protocol) {
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
