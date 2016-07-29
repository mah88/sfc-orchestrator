
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ClassifierJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Sffs {

    @SerializedName("sff")
    @Expose
    private List<Sff> sff = new ArrayList<Sff>();

    /**
     * 
     * @return
     *     The sff
     */
    public List<Sff> getSff() {
        return sff;
    }

    /**
     * 
     * @param sff
     *     The sff
     */
    public void setSff(List<Sff> sff) {
        this.sff = sff;
    }

}
