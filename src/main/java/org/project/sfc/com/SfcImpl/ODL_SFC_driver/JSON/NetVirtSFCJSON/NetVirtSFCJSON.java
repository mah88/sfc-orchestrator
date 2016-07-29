
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetVirtSFCJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NetVirtSFCJSON {

    @SerializedName("org")
    @Expose
    private Sfc sfc;

    /**
     * 
     * @return
     *     The org
     */
    public Sfc getSfc() {
        return sfc;
    }

    /**
     * 
     * @param sfc
     *     The org
     */
    public void setSfc(Sfc sfc) {
        this.sfc = sfc;
    }

}
