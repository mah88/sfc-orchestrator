
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.Token;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Token_ {

    @SerializedName("issued_at")
    @Expose
    private String issuedAt;
    @SerializedName("expires")
    @Expose
    private String expires;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("tenant")
    @Expose
    private Tenant tenant;
    @SerializedName("audit_ids")
    @Expose
    private List<String> auditIds = new ArrayList<String>();

    /**
     * 
     * @return
     *     The issuedAt
     */
    public String getIssuedAt() {
        return issuedAt;
    }

    /**
     * 
     * @param issuedAt
     *     The issued_at
     */
    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    /**
     * 
     * @return
     *     The expires
     */
    public String getExpires() {
        return expires;
    }

    /**
     * 
     * @param expires
     *     The expires
     */
    public void setExpires(String expires) {
        this.expires = expires;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The tenant
     */
    public Tenant getTenant() {
        return tenant;
    }

    /**
     * 
     * @param tenant
     *     The tenant
     */
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    /**
     * 
     * @return
     *     The auditIds
     */
    public List<String> getAuditIds() {
        return auditIds;
    }

    /**
     * 
     * @param auditIds
     *     The audit_ids
     */
    public void setAuditIds(List<String> auditIds) {
        this.auditIds = auditIds;
    }

}
