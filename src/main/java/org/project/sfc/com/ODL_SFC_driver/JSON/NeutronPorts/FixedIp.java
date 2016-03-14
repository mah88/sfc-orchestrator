
package org.project.sfc.com.ODL_SFC_driver.JSON.NeutronPorts;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FixedIp {

    @SerializedName("subnet_id")
    @Expose
    private String subnetId;
    @SerializedName("ip_address")
    @Expose
    private String ipAddress;

    /**
     * 
     * @return
     *     The subnetId
     */
    public String getSubnetId() {
        return subnetId;
    }

    /**
     * 
     * @param subnetId
     *     The subnet_id
     */
    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    /**
     * 
     * @return
     *     The ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 
     * @param ipAddress
     *     The ip_address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
