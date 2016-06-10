
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.Token;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Token {

    @SerializedName("access")
    @Expose
    private Access access;

    /**
     * 
     * @return
     *     The access
     */
    public Access getAccess() {
        return access;
    }

    /**
     * 
     * @param access
     *     The access
     */
    public void setAccess(Access access) {
        this.access = access;
    }

}
