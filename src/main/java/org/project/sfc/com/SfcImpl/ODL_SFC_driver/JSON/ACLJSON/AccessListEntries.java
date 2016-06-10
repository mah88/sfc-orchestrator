
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ACLJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class AccessListEntries {

    @SerializedName("ace")
    @Expose
    private List<Ace> ace = new ArrayList<Ace>();

    /**
     * 
     * @return
     *     The ace
     */
    public List<Ace> getAce() {
        return ace;
    }

    /**
     * 
     * @param ace
     *     The ace
     */
    public void setAce(List<Ace> ace) {
        this.ace = ace;
    }

}
