
package org.project.sfc.com.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OvsdbManagedNodeEntry {

    @SerializedName("bridge-ref")
    @Expose
    private String bridgeRef;

    /**
     * 
     * @return
     *     The bridgeRef
     */
    public String getBridgeRef() {
        return bridgeRef;
    }

    /**
     * 
     * @param bridgeRef
     *     The bridge-ref
     */
    public void setBridgeRef(String bridgeRef) {
        this.bridgeRef = bridgeRef;
    }

}
