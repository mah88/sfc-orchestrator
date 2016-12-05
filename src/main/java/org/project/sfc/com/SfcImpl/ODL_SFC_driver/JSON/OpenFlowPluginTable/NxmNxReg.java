
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NxmNxReg {

    @SerializedName("reg")
    @Expose
    private String reg;
    @SerializedName("value")
    @Expose
    private String value;

    /**
     * 
     * @return
     *     The reg
     */
    public String getReg() {
        return reg;
    }

    /**
     * 
     * @param reg
     *     The reg
     */
    public void setReg(String reg) {
        this.reg = reg;
    }

    /**
     * 
     * @return
     *     The value
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

}
