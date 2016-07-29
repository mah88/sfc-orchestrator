
package org.project.sfc.com.NetworkJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ControllerEntry {

    @SerializedName("target")
    @Expose
    private String target;
    @SerializedName("controller-uuid")
    @Expose
    private String controllerUuid;
    @SerializedName("is-connected")
    @Expose
    private String isConnected;

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
     *     The controllerUuid
     */
    public String getControllerUuid() {
        return controllerUuid;
    }

    /**
     * 
     * @param controllerUuid
     *     The controller-uuid
     */
    public void setControllerUuid(String controllerUuid) {
        this.controllerUuid = controllerUuid;
    }

    /**
     * 
     * @return
     *     The isConnected
     */
    public String getIsConnected() {
        return isConnected;
    }

    /**
     * 
     * @param isConnected
     *     The is-connected
     */
    public void setIsConnected(String isConnected) {
        this.isConnected = isConnected;
    }

}
