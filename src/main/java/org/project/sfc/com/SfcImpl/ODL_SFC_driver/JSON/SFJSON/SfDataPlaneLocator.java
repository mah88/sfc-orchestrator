
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SfDataPlaneLocator {


    @SerializedName("port")
    @Expose
    private String port;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("transport")
    @Expose
    private String transport;
    @SerializedName("service-function-forwarder")
    @Expose
    private String serviceFunctionForwarder;

    @SerializedName("service-function-ovs:ovs-port")
    @Expose
    private ServiceFunctionOvsOvsPort serviceFunctionOvsOvsPort;

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
     *     The port
     */
    public String getPort() {
        return port;
    }

    /**
     * 
     * @param port
     *     The port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * 
     * @return
     *     The ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * 
     * @param ip
     *     The ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 
     * @return
     *     The transport
     */
    public String getTransport() {
        return transport;
    }

    /**
     * 
     * @param transport
     *     The transport
     */
    public void setTransport(String transport) {
        this.transport = transport;
    }

    /**
     * 
     * @return
     *     The serviceFunctionForwarder
     */
    public String getServiceFunctionForwarder() {
        return serviceFunctionForwarder;
    }

    /**
     * 
     * @param serviceFunctionForwarder
     *     The service-function-forwarder
     */
    public void setServiceFunctionForwarder(String serviceFunctionForwarder) {
        this.serviceFunctionForwarder = serviceFunctionForwarder;
    }

    /**
     *
     * @return
     *     The serviceFunctionOvsOvsPort
     */
    public ServiceFunctionOvsOvsPort getServiceFunctionOvsOvsPort() {
        return serviceFunctionOvsOvsPort;
    }

    /**
     *
     * @param servicefunctionOvsOvsPort
     *     The servicefunctionOvsOvsPort
     */
    public void setServiceFunctionOvsOvsPort(ServiceFunctionOvsOvsPort servicefunctionOvsOvsPort) {
        this.serviceFunctionOvsOvsPort = servicefunctionOvsOvsPort;
    }

}
