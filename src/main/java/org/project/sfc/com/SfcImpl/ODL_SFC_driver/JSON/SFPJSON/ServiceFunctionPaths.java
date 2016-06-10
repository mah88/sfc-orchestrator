
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFPJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionPaths {

    @SerializedName("service-function-path")
    @Expose
    private List<ServiceFunctionPath> serviceFunctionPath = new ArrayList<ServiceFunctionPath>();

    /**
     * 
     * @return
     *     The serviceFunctionPath
     */
    public List<ServiceFunctionPath> getServiceFunctionPath() {
        return serviceFunctionPath;
    }

    /**
     * 
     * @param serviceFunctionPath
     *     The service-function-path
     */
    public void setServiceFunctionPath(List<ServiceFunctionPath> serviceFunctionPath) {
        this.serviceFunctionPath = serviceFunctionPath;
    }

}
