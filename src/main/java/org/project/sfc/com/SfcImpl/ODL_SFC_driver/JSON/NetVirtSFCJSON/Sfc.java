
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetVirtSFCJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Sfc {

  @SerializedName("name")
  @Expose
  private String name;

  /**
   *
   * @return The name
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @param name The name
   */
  public void setName(String name) {
    this.name = name;
  }
}
