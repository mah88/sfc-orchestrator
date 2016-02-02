
package org.project.sfc.com.SFFJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionForwarders {

    @SerializedName("service-function-forwarder")
    @Expose
    private List<ServiceFunctionForwarder> serviceFunctionForwarder = new ArrayList<ServiceFunctionForwarder>();

    /**
     * 
     * @return
     *     The serviceFunctionForwarder
     */
    public List<ServiceFunctionForwarder> getServiceFunctionForwarder() {
        return serviceFunctionForwarder;
    }

    /**
     * 
     * @param serviceFunctionForwarder
     *     The service-function-forwarder
     */
    public void setServiceFunctionForwarder(List<ServiceFunctionForwarder> serviceFunctionForwarder) {
        this.serviceFunctionForwarder = serviceFunctionForwarder;
    }

}
