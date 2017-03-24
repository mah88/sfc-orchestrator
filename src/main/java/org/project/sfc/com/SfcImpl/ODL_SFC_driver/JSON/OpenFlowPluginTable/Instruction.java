
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Instruction {

  @SerializedName("order")
  @Expose
  private String order;

  @SerializedName("go-to-table")
  @Expose
  private GoToTable goToTable;

  @SerializedName("apply-actions")
  @Expose
  private ApplyActions applyActions;

  /**
   *
   * @return The order
   */
  public String getOrder() {
    return order;
  }

  /**
   *
   * @param order The order
   */
  public void setOrder(String order) {
    this.order = order;
  }

  /**
   *
   * @return The goToTable
   */
  public GoToTable getGoToTable() {
    return goToTable;
  }

  /**
   *
   * @param goToTable The go-to-table
   */
  public void setGoToTable(GoToTable goToTable) {
    this.goToTable = goToTable;
  }

  /**
   *
   * @return The applyActions
   */
  public ApplyActions getApplyActions() {
    return applyActions;
  }

  /**
   *
   * @param applyActions The apply-actions
   */
  public void setApplyActions(ApplyActions applyActions) {
    this.applyActions = applyActions;
  }
}
