
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ExtensionList {

  @SerializedName("extension-key")
  @Expose
  private String extensionKey;

  @SerializedName("extension")
  @Expose
  private Extension extension;

  /**
   *
   * @return The extensionKey
   */
  public String getExtensionKey() {
    return extensionKey;
  }

  /**
   *
   * @param extensionKey The extension-key
   */
  public void setExtensionKey(String extensionKey) {
    this.extensionKey = extensionKey;
  }

  /**
   *
   * @return The extension
   */
  public Extension getExtension() {
    return extension;
  }

  /**
   *
   * @param extension The extension
   */
  public void setExtension(Extension extension) {
    this.extension = extension;
  }
}
