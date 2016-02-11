
package org.project.sfc.com.SFCJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionChain {
    @SerializedName("symmetric")
    @Expose
    private Boolean symmetric;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("sfc-service-function")
    @Expose
    private List<SfcServiceFunction> sfcServiceFunction = new ArrayList<SfcServiceFunction>();

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

    /**
     * 
     * @return
     *     The sfcServiceFunction
     */
    public List<SfcServiceFunction> getSfcServiceFunction() {
        return sfcServiceFunction;
    }

    /**
     * 
     * @param sfcServiceFunction
     *     The org-service-function
     */
    public void setSfcServiceFunction(List<SfcServiceFunction> sfcServiceFunction) {
        this.sfcServiceFunction = sfcServiceFunction;
    }

}
