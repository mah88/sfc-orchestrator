package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPs;

/**
 * Created by mah on 4/26/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class RSPs {

  @SerializedName("rendered-service-paths")
  @Expose
  private RenderedServicePaths renderedServicePaths;

  /**
   *
   * @return The renderedServicePaths
   */
  public RenderedServicePaths getRenderedServicePaths() {
    return renderedServicePaths;
  }

  /**
   *
   * @param renderedServicePaths The rendered-service-paths
   */
  public void setRenderedServicePaths(RenderedServicePaths renderedServicePaths) {
    this.renderedServicePaths = renderedServicePaths;
  }
}
