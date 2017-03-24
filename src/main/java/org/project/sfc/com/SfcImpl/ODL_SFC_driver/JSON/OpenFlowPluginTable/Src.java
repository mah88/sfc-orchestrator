
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Src {

  @SerializedName("start")
  @Expose
  private String start;

  @SerializedName("end")
  @Expose
  private String end;

  /**
   *
   * @return The start
   */
  public String getStart() {
    return start;
  }

  /**
   *
   * @param start The start
   */
  public void setStart(String start) {
    this.start = start;
  }

  /**
   *
   * @return The end
   */
  public String getEnd() {
    return end;
  }

  /**
   *
   * @param end The end
   */
  public void setEnd(String end) {
    this.end = end;
  }
}
