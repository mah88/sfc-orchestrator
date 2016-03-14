
package org.project.sfc.com.ODL_SFC_driver.JSON.SFCJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SFCJSON {

    @SerializedName("service-function-chains")
    @Expose
    private ServiceFunctionChains serviceFunctionChains;

    /**
     * 
     * @return
     *     The serviceFunctionChains
     */
    public ServiceFunctionChains getServiceFunctionChains() {
        return serviceFunctionChains;
    }

    /**
     * 
     * @param serviceFunctionChains
     *     The service-function-chains
     */
    public void setServiceFunctionChains(ServiceFunctionChains serviceFunctionChains) {
        this.serviceFunctionChains = serviceFunctionChains;
    }

}
