
package org.project.sfc.com.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OvsdbManagerEntry {

    @SerializedName("number_of_connections")
    @Expose
    private Integer numberOfConnections;
    @SerializedName("connected")
    @Expose
    private Boolean connected;
    @SerializedName("target")
    @Expose
    private String target;

    /**
     * 
     * @return
     *     The numberOfConnections
     */
    public Integer getNumberOfConnections() {
        return numberOfConnections;
    }

    /**
     * 
     * @param numberOfConnections
     *     The number_of_connections
     */
    public void setNumberOfConnections(Integer numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
    }

    /**
     * 
     * @return
     *     The connected
     */
    public Boolean getConnected() {
        return connected;
    }

    /**
     * 
     * @param connected
     *     The connected
     */
    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    /**
     * 
     * @return
     *     The target
     */
    public String getTarget() {
        return target;
    }

    /**
     * 
     * @param target
     *     The target
     */
    public void setTarget(String target) {
        this.target = target;
    }

}
