
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Duration {

    @SerializedName("second")
    @Expose
    private String second;
    @SerializedName("nanosecond")
    @Expose
    private String nanosecond;

    /**
     * 
     * @return
     *     The second
     */
    public String getSecond() {
        return second;
    }

    /**
     * 
     * @param second
     *     The second
     */
    public void setSecond(String second) {
        this.second = second;
    }

    /**
     * 
     * @return
     *     The nanosecond
     */
    public String getNanosecond() {
        return nanosecond;
    }

    /**
     * 
     * @param nanosecond
     *     The nanosecond
     */
    public void setNanosecond(String nanosecond) {
        this.nanosecond = nanosecond;
    }

}
