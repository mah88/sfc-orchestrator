
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OvsdbBridgeOtherConfig {

    @SerializedName("bridge-other-config-key")
    @Expose
    private String bridgeOtherConfigKey;
    @SerializedName("bridge-other-config-value")
    @Expose
    private String bridgeOtherConfigValue;

    /**
     * 
     * @return
     *     The bridgeOtherConfigKey
     */
    public String getBridgeOtherConfigKey() {
        return bridgeOtherConfigKey;
    }

    /**
     * 
     * @param bridgeOtherConfigKey
     *     The bridge-other-config-key
     */
    public void setBridgeOtherConfigKey(String bridgeOtherConfigKey) {
        this.bridgeOtherConfigKey = bridgeOtherConfigKey;
    }

    /**
     * 
     * @return
     *     The bridgeOtherConfigValue
     */
    public String getBridgeOtherConfigValue() {
        return bridgeOtherConfigValue;
    }

    /**
     * 
     * @param bridgeOtherConfigValue
     *     The bridge-other-config-value
     */
    public void setBridgeOtherConfigValue(String bridgeOtherConfigValue) {
        this.bridgeOtherConfigValue = bridgeOtherConfigValue;
    }

}
