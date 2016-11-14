
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ClassifierJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Sff {

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
