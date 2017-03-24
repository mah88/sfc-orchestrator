
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NxResubmit {

  @SerializedName("in-port")
  @Expose
  private String inPort;

  @SerializedName("table")
  @Expose
  private String table;

  /**
   *
   * @return The inPort
   */
  public String getInPort() {
    return inPort;
  }

  /**
   *
   * @param inPort The in-port
   */
  public void setInPort(String inPort) {
    this.inPort = inPort;
  }

  /**
   *
   * @return The table
   */
  public String getTable() {
    return table;
  }

  /**
   *
   * @param table The table
   */
  public void setTable(String table) {
    this.table = table;
  }
}
