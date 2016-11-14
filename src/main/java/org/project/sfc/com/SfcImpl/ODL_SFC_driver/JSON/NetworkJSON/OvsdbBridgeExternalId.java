
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class OvsdbBridgeExternalId {

  @SerializedName("bridge-external-id-value")
  @Expose
  private String bridgeExternalIdValue;

  @SerializedName("bridge-external-id-key")
  @Expose
  private String bridgeExternalIdKey;

  /**
   *
   * @return The bridgeExternalIdValue
   */
  public String getBridgeExternalIdValue() {
    return bridgeExternalIdValue;
  }

  /**
   *
   * @param bridgeExternalIdValue The bridge-external-id-value
   */
  public void setBridgeExternalIdValue(String bridgeExternalIdValue) {
    this.bridgeExternalIdValue = bridgeExternalIdValue;
  }

  /**
   *
   * @return The bridgeExternalIdKey
   */
  public String getBridgeExternalIdKey() {
    return bridgeExternalIdKey;
  }

  /**
   *
   * @param bridgeExternalIdKey The bridge-external-id-key
   */
  public void setBridgeExternalIdKey(String bridgeExternalIdKey) {
    this.bridgeExternalIdKey = bridgeExternalIdKey;
  }
}
