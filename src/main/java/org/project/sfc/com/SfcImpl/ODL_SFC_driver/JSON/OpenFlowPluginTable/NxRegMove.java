
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NxRegMove {

  @SerializedName("src")
  @Expose
  private Src src;

  @SerializedName("dst")
  @Expose
  private Dst_ dst;

  /**
   *
   * @return The src
   */
  public Src getSrc() {
    return src;
  }

  /**
   *
   * @param src The src
   */
  public void setSrc(Src src) {
    this.src = src;
  }

  /**
   *
   * @return The dst
   */
  public Dst_ getDst() {
    return dst;
  }

  /**
   *
   * @param dst The dst
   */
  public void setDst(Dst_ dst) {
    this.dst = dst;
  }
}
