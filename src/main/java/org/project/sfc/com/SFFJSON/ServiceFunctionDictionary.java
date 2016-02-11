
package org.project.sfc.com.SFFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionDictionary {


    @SerializedName("sff-sf-data-plane-locator")
    @Expose
    private SffSfDataPlaneLocator sffSfDataPlaneLocator;
    @SerializedName("name")
    @Expose
    private String name;
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
     *     The sffSfDataPlaneLocator
     */
    public SffSfDataPlaneLocator getSffSfDataPlaneLocator() {
        return sffSfDataPlaneLocator;
    }

    /**
     * 
     * @param sffSfDataPlaneLocator
     *     The sff-sf-data-plane-locator
     */
    public void setSffSfDataPlaneLocator(SffSfDataPlaneLocator sffSfDataPlaneLocator) {
        this.sffSfDataPlaneLocator = sffSfDataPlaneLocator;
    }

}
