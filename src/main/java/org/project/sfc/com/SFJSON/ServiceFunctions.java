
package org.project.sfc.com.SFJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctions {

    @SerializedName("service-function")
    @Expose
    private List<ServiceFunction> serviceFunction = new ArrayList<ServiceFunction>();

    /**
     * 
     * @return
     *     The serviceFunction
     */
    public List<ServiceFunction> getServiceFunction() {
        return serviceFunction;
    }

    /**
     * 
     * @param serviceFunction
     *     The service-function
     */
    public void setServiceFunction(List<ServiceFunction> serviceFunction) {
        this.serviceFunction = serviceFunction;
    }

}
