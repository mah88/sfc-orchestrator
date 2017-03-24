
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Table_ {

  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("flow-hash-id-map")
  @Expose
  private List<FlowHashIdMap> flowHashIdMap = new ArrayList<FlowHashIdMap>();

  @SerializedName("flow")
  @Expose
  private List<Flow> flow = new ArrayList<Flow>();

  @SerializedName("opendaylight-flow-table-statistics:flow-table-statistics")
  @Expose
  private FlowTableStatistics flowTableStatistics;

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
   * @return The flowHashIdMap
   */
  public List<FlowHashIdMap> getFlowHashIdMap() {
    return flowHashIdMap;
  }

  /**
   *
   * @param flowHashIdMap The flow-hash-id-map
   */
  public void setFlowHashIdMap(List<FlowHashIdMap> flowHashIdMap) {
    this.flowHashIdMap = flowHashIdMap;
  }

  /**
   *
   * @return The flow
   */
  public List<Flow> getFlow() {
    return flow;
  }

  /**
   *
   * @param flow The flow
   */
  public void setFlow(List<Flow> flow) {
    this.flow = flow;
  }

  /**
   *
   * @return The flowTableStatistics
   */
  public FlowTableStatistics getFlowTableStatistics() {
    return flowTableStatistics;
  }

  /**
   *
   * @param flowTableStatistics The flow-table-statistics
   */
  public void setFlowTableStatistics(FlowTableStatistics flowTableStatistics) {
    this.flowTableStatistics = flowTableStatistics;
  }
}
