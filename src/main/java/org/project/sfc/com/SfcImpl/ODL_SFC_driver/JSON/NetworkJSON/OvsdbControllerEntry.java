
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OvsdbControllerEntry {

    @SerializedName("is-connected")
    @Expose
    private Boolean isConnected;
    @SerializedName("controller-uuid")
    @Expose
    private String controllerUuid;
    @SerializedName("target")
    @Expose
    private String target;

    /**
     * 
     * @return
     *     The isConnected
     */
    public Boolean getIsConnected() {
        return isConnected;
    }

    /**
     * 
     * @param isConnected
     *     The is-connected
     */
    public void setIsConnected(Boolean isConnected) {
        this.isConnected = isConnected;
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
