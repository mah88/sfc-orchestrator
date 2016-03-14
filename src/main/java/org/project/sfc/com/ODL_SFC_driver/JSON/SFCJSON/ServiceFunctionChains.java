
package org.project.sfc.com.ODL_SFC_driver.JSON.SFCJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionChains {

    @SerializedName("service-function-chain")
    @Expose
    private List<ServiceFunctionChain> serviceFunctionChain = new ArrayList<ServiceFunctionChain>();

    /**
     * 
     * @return
     *     The serviceFunctionChain
     */
    public List<ServiceFunctionChain> getServiceFunctionChain() {
        return serviceFunctionChain;
    }

    /**
     * 
     * @param serviceFunctionChain
     *     The service-function-chain
     */
    public void setServiceFunctionChain(List<ServiceFunctionChain> serviceFunctionChain) {
        this.serviceFunctionChain = serviceFunctionChain;
    }

}
