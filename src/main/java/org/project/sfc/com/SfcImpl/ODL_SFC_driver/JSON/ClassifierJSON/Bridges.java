
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ClassifierJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Bridges {

    @SerializedName("bridge")
    @Expose
    private List<Bridge> bridge = new ArrayList<Bridge>();

    /**
     * 
     * @return
     *     The bridge
     */
    public List<Bridge> getBridge() {
        return bridge;
    }

    /**
     * 
     * @param bridge
     *     The bridge
     */
    public void setBridge(List<Bridge> bridge) {
        this.bridge = bridge;
    }

}
