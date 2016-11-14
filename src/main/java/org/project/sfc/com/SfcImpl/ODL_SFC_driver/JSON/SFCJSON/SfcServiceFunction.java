
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFCJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SfcServiceFunction {
  @SerializedName("type")
  @Expose
  private String type;

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

  /**
   *
   * @return The type
   */
  public String getType() {
    return type;
  }

  /**
   *
   * @param type The type
   */
  public void setType(String type) {
    this.type = type;
  }
}
