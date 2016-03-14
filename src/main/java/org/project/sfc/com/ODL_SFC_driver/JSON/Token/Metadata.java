
package org.project.sfc.com.ODL_SFC_driver.JSON.Token;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Metadata {

    @SerializedName("is_admin")
    @Expose
    private Integer isAdmin;
    @SerializedName("roles")
    @Expose
    private List<String> roles = new ArrayList<String>();

    /**
     * 
     * @return
     *     The isAdmin
     */
    public Integer getIsAdmin() {
        return isAdmin;
    }

    /**
     * 
     * @param isAdmin
     *     The is_admin
     */
    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * 
     * @return
     *     The roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * 
     * @param roles
     *     The roles
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
