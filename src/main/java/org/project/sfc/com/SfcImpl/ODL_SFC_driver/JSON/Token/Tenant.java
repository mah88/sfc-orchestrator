
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.Token;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Tenant {

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("enabled")
  @Expose
  private Boolean enabled;

  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("name")
  @Expose
  private String name;

  /**
   *
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  /**
   *
   * @param description The description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   *
   * @return The enabled
   */
  public Boolean getEnabled() {
    return enabled;
  }

  /**
   *
   * @param enabled The enabled
   */
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  /**
   *
   * @return The id
   */
  public String getId() {
    return id;
  }

  /**
   *
   * @param id The id
   */
  public void setId(String id) {
    this.id = id;
  }

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
