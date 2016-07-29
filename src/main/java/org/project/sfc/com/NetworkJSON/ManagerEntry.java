
package org.project.sfc.com.NetworkJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ManagerEntry {

    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("connected")
    @Expose
    private String connected;
    @SerializedName("number_of_connections")
    @Expose
    private String numberOfConnections;

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

    /**
     * 
     * @return
     *     The connected
     */
    public String getConnected() {
        return connected;
    }

    /**
     * 
     * @param connected
     *     The connected
     */
    public void setConnected(String connected) {
        this.connected = connected;
    }

    /**
     * 
     * @return
     *     The numberOfConnections
     */
    public String getNumberOfConnections() {
        return numberOfConnections;
    }

    /**
     * 
     * @param numberOfConnections
     *     The number_of_connections
     */
    public void setNumberOfConnections(String numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
    }

}
