
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class NetworkJSON {

    @SerializedName("network-topology")
    @Expose
    private NetworkTopology networkTopology;

    /**
     * 
     * @return
     *     The networkTopology
     */
    public NetworkTopology getNetworkTopology() {
        return networkTopology;
    }

    /**
     * 
     * @param networkTopology
     *     The network-topology
     */
    public void setNetworkTopology(NetworkTopology networkTopology) {
        this.networkTopology = networkTopology;
    }

}
