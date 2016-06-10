
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SffSfDataPlaneLocator {

    @SerializedName("sff-dpl-name")
    @Expose
    private String sffDplName;
    @SerializedName("sf-dpl-name")
    @Expose
    private String sfDplName;

    /**
     * 
     * @return
     *     The sffDplName
     */
    public String getSffDplName() {
        return sffDplName;
    }

    /**
     * 
     * @param sffDplName
     *     The sff-dpl-name
     */
    public void setSffDplName(String sffDplName) {
        this.sffDplName = sffDplName;
    }

    /**
     * 
     * @return
     *     The sfDplName
     */
    public String getSfDplName() {
        return sfDplName;
    }

    /**
     * 
     * @param sfDplName
     *     The sf-dpl-name
     */
    public void setSfDplName(String sfDplName) {
        this.sfDplName = sfDplName;
    }

}
