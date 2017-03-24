
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FlowTableStatistics {

  @SerializedName("packets-looked-up")
  @Expose
  private String packetsLookedUp;

  @SerializedName("active-flows")
  @Expose
  private String activeFlows;

  @SerializedName("packets-matched")
  @Expose
  private String packetsMatched;

  /**
   *
   * @return The packetsLookedUp
   */
  public String getPacketsLookedUp() {
    return packetsLookedUp;
  }

  /**
   *
   * @param packetsLookedUp The packets-looked-up
   */
  public void setPacketsLookedUp(String packetsLookedUp) {
    this.packetsLookedUp = packetsLookedUp;
  }

  /**
   *
   * @return The activeFlows
   */
  public String getActiveFlows() {
    return activeFlows;
  }

  /**
   *
   * @param activeFlows The active-flows
   */
  public void setActiveFlows(String activeFlows) {
    this.activeFlows = activeFlows;
  }

  /**
   *
   * @return The packetsMatched
   */
  public String getPacketsMatched() {
    return packetsMatched;
  }

  /**
   *
   * @param packetsMatched The packets-matched
   */
  public void setPacketsMatched(String packetsMatched) {
    this.packetsMatched = packetsMatched;
  }
}
