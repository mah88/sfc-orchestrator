
package org.project.sfc.com.ODL_SFC_driver.JSON.NeutronPorts;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NeutronPorts {

    @SerializedName("ports")
    @Expose
    private List<Port> ports = new ArrayList<Port>();

    /**
     * 
     * @return
     *     The ports
     */
    public List<Port> getPorts() {
        return ports;
    }

    /**
     * 
     * @param ports
     *     The ports
     */
    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

}
