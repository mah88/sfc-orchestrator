
package org.project.sfc.com.ODL_SFC_driver.JSON.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class DestinationPortRange {

    @SerializedName("lower-port")
    @Expose
    private Integer lowerPort;
    @SerializedName("upper-port")
    @Expose
    private Integer upperPort;

    /**
     * 
     * @return
     *     The lowerPort
     */
    public Integer getLowerPort() {
        return lowerPort;
    }

    /**
     * 
     * @param lowerPort
     *     The lower-port
     */
    public void setLowerPort(Integer lowerPort) {
        this.lowerPort = lowerPort;
    }

    /**
     * 
     * @return
     *     The upperPort
     */
    public Integer getUpperPort() {
        return upperPort;
    }

    /**
     * 
     * @param upperPort
     *     The upper-port
     */
    public void setUpperPort(Integer upperPort) {
        this.upperPort = upperPort;
    }

}
