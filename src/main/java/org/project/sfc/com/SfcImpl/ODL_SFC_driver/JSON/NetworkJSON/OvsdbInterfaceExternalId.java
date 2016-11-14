package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by mah on 2/4/16.
 */
public class OvsdbInterfaceExternalId {

  @SerializedName("external-id-value")
  @Expose
  private String externalidvalue;

  @SerializedName("external-id-key")
  @Expose
  private String externalidkey;

  public String getExternalIdValue() {
    return externalidvalue;
  }

  public void setExternalIdValue(String externalidvalue) {
    this.externalidvalue = externalidvalue;
  }

  public String getExternalIdKey() {
    return externalidkey;
  }

  public void setExternalIdKey(String externalidkey) {
    this.externalidkey = externalidkey;
  }
}
