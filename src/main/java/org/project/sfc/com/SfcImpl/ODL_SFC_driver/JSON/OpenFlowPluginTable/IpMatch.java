
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class IpMatch {

    @SerializedName("ip-protocol")
    @Expose
    private String ipProtocol;

    /**
     * 
     * @return
     *     The ipProtocol
     */
    public String getIpProtocol() {
        return ipProtocol;
    }

    /**
     * 
     * @param ipProtocol
     *     The ip-protocol
     */
    public void setIpProtocol(String ipProtocol) {
        this.ipProtocol = ipProtocol;
    }

}
