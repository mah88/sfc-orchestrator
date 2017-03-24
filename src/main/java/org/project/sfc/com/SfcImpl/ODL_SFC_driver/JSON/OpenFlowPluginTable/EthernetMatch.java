
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class EthernetMatch {

  @SerializedName("ethernet-type")
  @Expose
  private EthernetType ethernetType;

  /**
   *
   * @return The ethernetType
   */
  public EthernetType getEthernetType() {
    return ethernetType;
  }

  /**
   *
   * @param ethernetType The ethernet-type
   */
  public void setEthernetType(EthernetType ethernetType) {
    this.ethernetType = ethernetType;
  }
}
