
package org.project.sfc.com.SFFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SFFJSON {

    @SerializedName("service-function-forwarders")
    @Expose
    private ServiceFunctionForwarders serviceFunctionForwarders;

    /**
     * 
     * @return
     *     The serviceFunctionForwarders
     */
    public ServiceFunctionForwarders getServiceFunctionForwarders() {
        return serviceFunctionForwarders;
    }

    /**
     * 
     * @param serviceFunctionForwarders
     *     The service-function-forwarders
     */
    public void setServiceFunctionForwarders(ServiceFunctionForwarders serviceFunctionForwarders) {
        this.serviceFunctionForwarders = serviceFunctionForwarders;
    }

}
