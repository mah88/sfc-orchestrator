
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ACLJSON {

    @SerializedName("access-lists")
    @Expose
    private AccessLists accessLists;

    /**
     * 
     * @return
     *     The accessLists
     */
    public AccessLists getAccessLists() {
        return accessLists;
    }

    /**
     * 
     * @param accessLists
     *     The access-lists
     */
    public void setAccessLists(AccessLists accessLists) {
        this.accessLists = accessLists;
    }

}
