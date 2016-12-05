
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NxSetNsp {

    @SerializedName("nsp")
    @Expose
    private String nsp;

    /**
     * 
     * @return
     *     The nsp
     */
    public String getNsp() {
        return nsp;
    }

    /**
     * 
     * @param nsp
     *     The nsp
     */
    public void setNsp(String nsp) {
        this.nsp = nsp;
    }

}
