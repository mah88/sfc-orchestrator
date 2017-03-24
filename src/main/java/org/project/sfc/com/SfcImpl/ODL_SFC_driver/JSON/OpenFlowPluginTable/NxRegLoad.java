
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NxRegLoad {

  @SerializedName("value")
  @Expose
  private String value;

  @SerializedName("dst")
  @Expose
  private Dst dst;

  @SerializedName("nx-reg-move")
  @Expose
  private NxRegMove nxRegMove;

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

  /**
   *
   * @return The dst
   */
  public Dst getDst() {
    return dst;
  }

  /**
   *
   * @param dst The dst
   */
  public void setDst(Dst dst) {
    this.dst = dst;
  }

  /**
   *
   * @return The nxRegMove
   */
  public NxRegMove getNxRegMove() {
    return nxRegMove;
  }

  /**
   *
   * @param nxRegMove The nx-reg-move
   */
  public void setNxRegMove(NxRegMove nxRegMove) {
    this.nxRegMove = nxRegMove;
  }
}
