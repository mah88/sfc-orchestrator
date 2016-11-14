package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPs;

/**
 * Created by mah on 4/26/16.
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class RenderedServicePath {

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("service-chain-name")
  @Expose
  private String serviceChainName;

  @SerializedName("transport-type")
  @Expose
  private String transportType;

  @SerializedName("rendered-service-path-hop")
  @Expose
  private List<RenderedServicePathHop> renderedServicePathHop =
      new ArrayList<RenderedServicePathHop>();

  @SerializedName("parent-service-function-path")
  @Expose
  private String parentServiceFunctionPath;

  @SerializedName("path-id")
  @Expose
  private Integer pathId;

  @SerializedName("starting-index")
  @Expose
  private Integer startingIndex;

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

  /**
   *
   * @return The serviceChainName
   */
  public String getServiceChainName() {
    return serviceChainName;
  }

  /**
   *
   * @param serviceChainName The service-chain-name
   */
  public void setServiceChainName(String serviceChainName) {
    this.serviceChainName = serviceChainName;
  }

  /**
   *
   * @return The transportType
   */
  public String getTransportType() {
    return transportType;
  }

  /**
   *
   * @param transportType The transport-type
   */
  public void setTransportType(String transportType) {
    this.transportType = transportType;
  }

  /**
   *
   * @return The renderedServicePathHop
   */
  public List<RenderedServicePathHop> getRenderedServicePathHop() {
    return renderedServicePathHop;
  }

  /**
   *
   * @param renderedServicePathHop The rendered-service-path-hop
   */
  public void setRenderedServicePathHop(List<RenderedServicePathHop> renderedServicePathHop) {
    this.renderedServicePathHop = renderedServicePathHop;
  }

  /**
   *
   * @return The parentServiceFunctionPath
   */
  public String getParentServiceFunctionPath() {
    return parentServiceFunctionPath;
  }

  /**
   *
   * @param parentServiceFunctionPath The parent-service-function-path
   */
  public void setParentServiceFunctionPath(String parentServiceFunctionPath) {
    this.parentServiceFunctionPath = parentServiceFunctionPath;
  }

  /**
   *
   * @return The pathId
   */
  public Integer getPathId() {
    return pathId;
  }

  /**
   *
   * @param pathId The path-id
   */
  public void setPathId(Integer pathId) {
    this.pathId = pathId;
  }

  /**
   *
   * @return The startingIndex
   */
  public Integer getStartingIndex() {
    return startingIndex;
  }

  /**
   *
   * @param startingIndex The starting-index
   */
  public void setStartingIndex(Integer startingIndex) {
    this.startingIndex = startingIndex;
  }
}
