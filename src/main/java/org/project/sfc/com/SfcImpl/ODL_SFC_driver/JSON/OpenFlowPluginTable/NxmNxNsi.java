
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NxmNxNsi {

    @SerializedName("nsi")
    @Expose
    private String nsi;

    /**
     * 
     * @return
     *     The nsi
     */
    public String getNsi() {
        return nsi;
    }

    /**
     * 
     * @param nsi
     *     The nsi
     */
    public void setNsi(String nsi) {
        this.nsi = nsi;
    }

}
