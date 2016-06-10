
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NeutronPorts;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class BindingVifDetails {

    @SerializedName("port_filter")
    @Expose
    private Boolean portFilter;

    /**
     * 
     * @return
     *     The portFilter
     */
    public Boolean getPortFilter() {
        return portFilter;
    }

    /**
     * 
     * @param portFilter
     *     The port_filter
     */
    public void setPortFilter(Boolean portFilter) {
        this.portFilter = portFilter;
    }

}
