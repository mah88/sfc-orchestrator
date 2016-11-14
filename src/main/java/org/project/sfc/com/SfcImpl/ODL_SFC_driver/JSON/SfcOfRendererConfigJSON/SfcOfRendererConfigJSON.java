package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SfcOfRendererConfigJSON;

/**
 * Created by mah on 4/21/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SfcOfRendererConfigJSON {

  @SerializedName("sfc-of-renderer-config")
  @Expose
  private SfcOfRendererConfig sfcOfRendererConfig;

  /**
   *
   * @return The sfcOfRendererConfig
   */
  public SfcOfRendererConfig getSfcOfRendererConfig() {
    return sfcOfRendererConfig;
  }

  /**
   *
   * @param sfcOfRendererConfig The sfc-of-renderer-config
   */
  public void setSfcOfRendererConfig(SfcOfRendererConfig sfcOfRendererConfig) {
    this.sfcOfRendererConfig = sfcOfRendererConfig;
  }
}
