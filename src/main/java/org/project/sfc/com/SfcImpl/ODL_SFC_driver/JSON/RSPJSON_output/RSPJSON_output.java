package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPJSON_output;

/**
 * Created by mah on 1/24/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RSPJSON_output {

  @SerializedName("output")
  @Expose
  private Output output;

  public Output getOutput() {
    return output;
  }

  public void setOutput(Output output) {
    this.output = output;
  }

}