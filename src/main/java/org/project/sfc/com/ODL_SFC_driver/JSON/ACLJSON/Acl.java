
package org.project.sfc.com.ODL_SFC_driver.JSON.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Acl {

    @SerializedName("acl-name")
    @Expose
    private String aclName;
    @SerializedName("access-list-entries")
    @Expose
    private AccessListEntries accessListEntries;

    /**
     * 
     * @return
     *     The aclName
     */
    public String getAclName() {
        return aclName;
    }

    /**
     * 
     * @param aclName
     *     The acl-name
     */
    public void setAclName(String aclName) {
        this.aclName = aclName;
    }

    /**
     * 
     * @return
     *     The accessListEntries
     */
    public AccessListEntries getAccessListEntries() {
        return accessListEntries;
    }

    /**
     * 
     * @param accessListEntries
     *     The access-list-entries
     */
    public void setAccessListEntries(AccessListEntries accessListEntries) {
        this.accessListEntries = accessListEntries;
    }

}
