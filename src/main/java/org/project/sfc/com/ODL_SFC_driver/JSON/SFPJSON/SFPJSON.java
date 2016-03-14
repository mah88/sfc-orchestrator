
package org.project.sfc.com.ODL_SFC_driver.JSON.SFPJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SFPJSON {

    @SerializedName("service-function-paths")
    @Expose
    private ServiceFunctionPaths serviceFunctionPaths;

    /**
     * 
     * @return
     *     The serviceFunctionPaths
     */
    public ServiceFunctionPaths getServiceFunctionPaths() {
        return serviceFunctionPaths;
    }

    /**
     * 
     * @param serviceFunctionPaths
     *     The service-function-paths
     */
    public void setServiceFunctionPaths(ServiceFunctionPaths serviceFunctionPaths) {
        this.serviceFunctionPaths = serviceFunctionPaths;
    }

}
