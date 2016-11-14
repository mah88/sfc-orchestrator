
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Topology {

  @SerializedName("node")
  @Expose
  private List<Node> node = new ArrayList<Node>();

  @SerializedName("topology-id")
  @Expose
  private String topologyId;

  /**
   *
   * @return The node
   */
  public List<Node> getNode() {
    return node;
  }

  /**
   *
   * @param node The node
   */
  public void setNode(List<Node> node) {
    this.node = node;
  }

  /**
   *
   * @return The topologyId
   */
  public String getTopologyId() {
    return topologyId;
  }

  /**
   *
   * @param topologyId The topology-id
   */
  public void setTopologyId(String topologyId) {
    this.topologyId = topologyId;
  }
}
