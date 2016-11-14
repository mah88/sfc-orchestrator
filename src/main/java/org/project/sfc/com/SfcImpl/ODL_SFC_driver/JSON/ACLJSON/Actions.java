
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Actions {

  @SerializedName("netvirt-sfc-acl:rsp-name")
  @Expose
  private String netvirtSfcAclSfcName;

  /**
   *
   * @return The netvirtSfcAclSfcName
   */
  public String getNetvirtSfcAclSfcName() {
    return netvirtSfcAclSfcName;
  }

  /**
   *
   * @param netvirtSfcAclSfcName The netvirt-org-acl:org-name
   */
  public void setNetvirtSfcAclSfcName(String netvirtSfcAclSfcName) {
    this.netvirtSfcAclSfcName = netvirtSfcAclSfcName;
  }
}
