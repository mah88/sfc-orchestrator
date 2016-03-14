
package org.project.sfc.com.ODL_SFC_driver.JSON.SFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SFJSON {

    @SerializedName("service-functions")
    @Expose
    private ServiceFunctions serviceFunctions;

    /**
     * 
     * @return
     *     The serviceFunctions
     */
    public ServiceFunctions getServiceFunctions() {
        return serviceFunctions;
    }

    /**
     * 
     * @param serviceFunctions
     *     The service-functions
     */
    public void setServiceFunctions(ServiceFunctions serviceFunctions) {
        this.serviceFunctions = serviceFunctions;
    }

}
