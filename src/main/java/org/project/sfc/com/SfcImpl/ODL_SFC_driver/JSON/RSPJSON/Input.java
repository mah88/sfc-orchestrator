
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Input {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent-service-function-path")
    @Expose
    private String parentServiceFunctionPath;
    @SerializedName("symmetric")
    @Expose
    private Boolean symmetric;

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
     *     The parentServiceFunctionPath
     */
    public String getParentServiceFunctionPath() {
        return parentServiceFunctionPath;
    }

    /**
     * 
     * @param parentServiceFunctionPath
     *     The parent-service-function-path
     */
    public void setParentServiceFunctionPath(String parentServiceFunctionPath) {
        this.parentServiceFunctionPath = parentServiceFunctionPath;
    }

    /**
     * 
     * @return
     *     The symmetric
     */
    public Boolean getSymmetric() {
        return symmetric;
    }

    /**
     * 
     * @param symmetric
     *     The symmetric
     */
    public void setSymmetric(Boolean symmetric) {
        this.symmetric = symmetric;
    }

}
