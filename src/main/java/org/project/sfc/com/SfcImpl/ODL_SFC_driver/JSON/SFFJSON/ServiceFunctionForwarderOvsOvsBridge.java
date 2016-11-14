
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionForwarderOvsOvsBridge {

  @SerializedName("bridge-name")
  @Expose
  private String bridgeName;

  /**
   *
   * @return The bridgeName
   */
  public String getBridgeName() {
    return bridgeName;
  }

  /**
   *
   * @param bridgeName The bridge-name
   */
  public void setBridgeName(String bridgeName) {
    this.bridgeName = bridgeName;
  }
}
