
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class NetworkTopology {

  @SerializedName("topology")
  @Expose
  private List<Topology> topology = new ArrayList<Topology>();

  /**
   *
   * @return The topology
   */
  public List<Topology> getTopology() {
    return topology;
  }

  /**
   *
   * @param topology The topology
   */
  public void setTopology(List<Topology> topology) {
    this.topology = topology;
  }
}
