
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NxmNxNsp {

  @SerializedName("value")
  @Expose
  private String value;

  /**
   *
   * @return The value
   */
  public String getValue() {
    return value;
  }

  /**
   *
   * @param value The value
   */
  public void setValue(String value) {
    this.value = value;
  }
}
