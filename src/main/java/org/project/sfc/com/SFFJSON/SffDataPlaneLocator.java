
package org.project.sfc.com.SFFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SffDataPlaneLocator {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("data-plane-locator")
    @Expose
    private DataPlaneLocator dataPlaneLocator;
    @SerializedName("service-function-forwarder-ovs:ovs-options")
    @Expose
    private ServiceFunctionForwarderOvsOvsOptions serviceFunctionForwarderOvsOvsOptions;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The dataPlaneLocator
     */
    public DataPlaneLocator getDataPlaneLocator() {
        return dataPlaneLocator;
    }

    /**
     * 
     * @param dataPlaneLocator
     *     The data-plane-locator
     */
    public void setDataPlaneLocator(DataPlaneLocator dataPlaneLocator) {
        this.dataPlaneLocator = dataPlaneLocator;
    }

    /**
     * 
     * @return
     *     The serviceFunctionForwarderOvsOvsOptions
     */
    public ServiceFunctionForwarderOvsOvsOptions getServiceFunctionForwarderOvsOvsOptions() {
        return serviceFunctionForwarderOvsOvsOptions;
    }

    /**
     * 
     * @param serviceFunctionForwarderOvsOvsOptions
     *     The service-function-forwarder-ovs:ovs-options
     */
    public void setServiceFunctionForwarderOvsOvsOptions(ServiceFunctionForwarderOvsOvsOptions serviceFunctionForwarderOvsOvsOptions) {
        this.serviceFunctionForwarderOvsOvsOptions = serviceFunctionForwarderOvsOvsOptions;
    }

}
