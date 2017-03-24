package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPJSON_output;

/**
 * Created by mah on 1/24/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Output {

  @SerializedName("name")
  @Expose
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
