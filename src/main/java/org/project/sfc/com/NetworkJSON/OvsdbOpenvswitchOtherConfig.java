
package org.project.sfc.com.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OvsdbOpenvswitchOtherConfig {

    @SerializedName("other-config-key")
    @Expose
    private String otherConfigKey;
    @SerializedName("other-config-value")
    @Expose
    private String otherConfigValue;

    /**
     * 
     * @return
     *     The otherConfigKey
     */
    public String getOtherConfigKey() {
        return otherConfigKey;
    }

    /**
     * 
     * @param otherConfigKey
     *     The other-config-key
     */
    public void setOtherConfigKey(String otherConfigKey) {
        this.otherConfigKey = otherConfigKey;
    }

    /**
     * 
     * @return
     *     The otherConfigValue
     */
    public String getOtherConfigValue() {
        return otherConfigValue;
    }

    /**
     * 
     * @param otherConfigValue
     *     The other-config-value
     */
    public void setOtherConfigValue(String otherConfigValue) {
        this.otherConfigValue = otherConfigValue;
    }

}
