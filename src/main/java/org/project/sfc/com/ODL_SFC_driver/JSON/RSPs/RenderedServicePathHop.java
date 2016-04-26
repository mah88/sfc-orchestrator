package org.project.sfc.com.ODL_SFC_driver.JSON.RSPs;

/**
 * Created by mah on 4/26/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class RenderedServicePathHop {

    @SerializedName("hop-number")
    @Expose
    private Integer hopNumber;
    @SerializedName("service-function-forwarder")
    @Expose
    private String serviceFunctionForwarder;
    @SerializedName("service-function-name")
    @Expose
    private String serviceFunctionName;
    @SerializedName("service-function-forwarder-locator")
    @Expose
    private String serviceFunctionForwarderLocator;
    @SerializedName("service-index")
    @Expose
    private Integer serviceIndex;

    /**
     *
     * @return
     * The hopNumber
     */
    public Integer getHopNumber() {
        return hopNumber;
    }

    /**
     *
     * @param hopNumber
     * The hop-number
     */
    public void setHopNumber(Integer hopNumber) {
        this.hopNumber = hopNumber;
    }

    /**
     *
     * @return
     * The serviceFunctionForwarder
     */
    public String getServiceFunctionForwarder() {
        return serviceFunctionForwarder;
    }

    /**
     *
     * @param serviceFunctionForwarder
     * The service-function-forwarder
     */
    public void setServiceFunctionForwarder(String serviceFunctionForwarder) {
        this.serviceFunctionForwarder = serviceFunctionForwarder;
    }

    /**
     *
     * @return
     * The serviceFunctionName
     */
    public String getServiceFunctionName() {
        return serviceFunctionName;
    }

    /**
     *
     * @param serviceFunctionName
     * The service-function-name
     */
    public void setServiceFunctionName(String serviceFunctionName) {
        this.serviceFunctionName = serviceFunctionName;
    }

    /**
     *
     * @return
     * The serviceFunctionForwarderLocator
     */
    public String getServiceFunctionForwarderLocator() {
        return serviceFunctionForwarderLocator;
    }

    /**
     *
     * @param serviceFunctionForwarderLocator
     * The service-function-forwarder-locator
     */
    public void setServiceFunctionForwarderLocator(String serviceFunctionForwarderLocator) {
        this.serviceFunctionForwarderLocator = serviceFunctionForwarderLocator;
    }

    /**
     *
     * @return
     * The serviceIndex
     */
    public Integer getServiceIndex() {
        return serviceIndex;
    }

    /**
     *
     * @param serviceIndex
     * The service-index
     */
    public void setServiceIndex(Integer serviceIndex) {
        this.serviceIndex = serviceIndex;
    }

}
