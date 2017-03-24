
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Extension {

  @SerializedName("openflowplugin-extension-nicira-match:nxm-nx-reg")
  @Expose
  private NxmNxReg nxmNxReg;

  @SerializedName("openflowplugin-extension-nicira-match:nxm-nx-nsi")
  @Expose
  private NxmNxNsi nxmNxNsi;

  @SerializedName("openflowplugin-extension-nicira-match:nxm-nx-nsp")
  @Expose
  private NxmNxNsp nxmNxNsp;

  /**
   *
   * @return The nxmNxReg
   */
  public NxmNxReg getNxmNxReg() {
    return nxmNxReg;
  }

  /**
   *
   * @param nxmNxReg The nxm-nx-reg
   */
  public void setNxmNxReg(NxmNxReg nxmNxReg) {
    this.nxmNxReg = nxmNxReg;
  }

  /**
   *
   * @return The nxmNxNsi
   */
  public NxmNxNsi getNxmNxNsi() {
    return nxmNxNsi;
  }

  /**
   *
   * @param nxmNxNsi The nxm-nx-nsi
   */
  public void setNxmNxNsi(NxmNxNsi nxmNxNsi) {
    this.nxmNxNsi = nxmNxNsi;
  }

  /**
   *
   * @return The nxmNxNsp
   */
  public NxmNxNsp getNxmNxNsp() {
    return nxmNxNsp;
  }

  /**
   *
   * @param nxmNxNsp The nxm-nx-nsp
   */
  public void setNxmNxNsp(NxmNxNsp nxmNxNsp) {
    this.nxmNxNsp = nxmNxNsp;
  }
}
