
package org.project.sfc.com.SFFJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionForwarder {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("service-node")
    @Expose
    private String serviceNode;
    @SerializedName("service-function-forwarder-ovs:ovs-bridge")
    @Expose
    private ServiceFunctionForwarderOvsOvsBridge serviceFunctionForwarderOvsOvsBridge;
    @SerializedName("service-function-dictionary")
    @Expose
    private List<ServiceFunctionDictionary> serviceFunctionDictionary = new ArrayList<ServiceFunctionDictionary>();
    @SerializedName("sff-data-plane-locator")
    @Expose
    private List<SffDataPlaneLocator> sffDataPlaneLocator = new ArrayList<SffDataPlaneLocator>();

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
     *     The serviceNode
     */
    public String getServiceNode() {
        return serviceNode;
    }

    /**
     * 
     * @param serviceNode
     *     The service-node
     */
    public void setServiceNode(String serviceNode) {
        this.serviceNode = serviceNode;
    }

    /**
     * 
     * @return
     *     The serviceFunctionForwarderOvsOvsBridge
     */
    public ServiceFunctionForwarderOvsOvsBridge getServiceFunctionForwarderOvsOvsBridge() {
        return serviceFunctionForwarderOvsOvsBridge;
    }

    /**
     * 
     * @param serviceFunctionForwarderOvsOvsBridge
     *     The service-function-forwarder-ovs:ovs-bridge
     */
    public void setServiceFunctionForwarderOvsOvsBridge(ServiceFunctionForwarderOvsOvsBridge serviceFunctionForwarderOvsOvsBridge) {
        this.serviceFunctionForwarderOvsOvsBridge = serviceFunctionForwarderOvsOvsBridge;
    }

    /**
     * 
     * @return
     *     The serviceFunctionDictionary
     */
    public List<ServiceFunctionDictionary> getServiceFunctionDictionary() {
        return serviceFunctionDictionary;
    }

    /**
     * 
     * @param serviceFunctionDictionary
     *     The service-function-dictionary
     */
    public void setServiceFunctionDictionary(List<ServiceFunctionDictionary> serviceFunctionDictionary) {
        this.serviceFunctionDictionary = serviceFunctionDictionary;
    }

    /**
     * 
     * @return
     *     The sffDataPlaneLocator
     */
    public List<SffDataPlaneLocator> getSffDataPlaneLocator() {
        return sffDataPlaneLocator;
    }

    /**
     * 
     * @param sffDataPlaneLocator
     *     The sff-data-plane-locator
     */
    public void setSffDataPlaneLocator(List<SffDataPlaneLocator> sffDataPlaneLocator) {
        this.sffDataPlaneLocator = sffDataPlaneLocator;
    }

}
