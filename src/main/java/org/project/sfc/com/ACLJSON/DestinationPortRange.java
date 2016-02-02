
package org.project.sfc.com.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class DestinationPortRange {

    @SerializedName("lower-port")
    @Expose
    private String lowerPort;
    @SerializedName("upper-port")
    @Expose
    private String upperPort;

    /**
     * 
     * @return
     *     The lowerPort
     */
    public String getLowerPort() {
        return lowerPort;
    }

    /**
     * 
     * @param lowerPort
     *     The lower-port
     */
    public void setLowerPort(String lowerPort) {
        this.lowerPort = lowerPort;
    }

    /**
     * 
     * @return
     *     The upperPort
     */
    public String getUpperPort() {
        return upperPort;
    }

    /**
     * 
     * @param upperPort
     *     The upper-port
     */
    public void setUpperPort(String upperPort) {
        this.upperPort = upperPort;
    }

}
