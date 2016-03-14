
package org.project.sfc.com.ODL_SFC_driver.JSON.ACLJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class AccessLists {

    @SerializedName("acl")
    @Expose
    private List<Acl> acl = new ArrayList<Acl>();

    /**
     * 
     * @return
     *     The acl
     */
    public List<Acl> getAcl() {
        return acl;
    }

    /**
     * 
     * @param acl
     *     The acl
     */
    public void setAcl(List<Acl> acl) {
        this.acl = acl;
    }

}
