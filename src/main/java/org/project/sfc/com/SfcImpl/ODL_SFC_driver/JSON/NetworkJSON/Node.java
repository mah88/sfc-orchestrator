
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Node {

  @SerializedName("ovsdb:managed-by")
  @Expose
  private String ovsdbManagedBy;

  @SerializedName("ovsdb:bridge-uuid")
  @Expose
  private String ovsdbBridgeUuid;

  @SerializedName("ovsdb:bridge-name")
  @Expose
  private String ovsdbBridgeName;

  @SerializedName("ovsdb:datapath-id")
  @Expose
  private String ovsdbDatapathId;

  @SerializedName("ovsdb:bridge-external-ids")
  @Expose
  private List<OvsdbBridgeExternalId> ovsdbBridgeExternalIds =
      new ArrayList<OvsdbBridgeExternalId>();

  @SerializedName("ovsdb:datapath-type")
  @Expose
  private String ovsdbDatapathType;

  @SerializedName("node-id")
  @Expose
  private String nodeId;

  @SerializedName("termination-point")
  @Expose
  private List<TerminationPoint> terminationPoint = new ArrayList<TerminationPoint>();

  @SerializedName("ovsdb:fail-mode")
  @Expose
  private String ovsdbFailMode;

  @SerializedName("ovsdb:protocol-entry")
  @Expose
  private List<OvsdbProtocolEntry> ovsdbProtocolEntry = new ArrayList<OvsdbProtocolEntry>();

  @SerializedName("ovsdb:bridge-openflow-node-ref")
  @Expose
  private String ovsdbBridgeOpenflowNodeRef;

  @SerializedName("ovsdb:bridge-other-configs")
  @Expose
  private List<OvsdbBridgeOtherConfig> ovsdbBridgeOtherConfigs =
      new ArrayList<OvsdbBridgeOtherConfig>();

  @SerializedName("ovsdb:controller-entry")
  @Expose
  private List<OvsdbControllerEntry> ovsdbControllerEntry = new ArrayList<OvsdbControllerEntry>();

  @SerializedName("ovsdb:openvswitch-other-configs")
  @Expose
  private List<OvsdbOpenvswitchOtherConfig> ovsdbOpenvswitchOtherConfigs =
      new ArrayList<OvsdbOpenvswitchOtherConfig>();

  @SerializedName("ovsdb:managed-node-entry")
  @Expose
  private List<OvsdbManagedNodeEntry> ovsdbManagedNodeEntry =
      new ArrayList<OvsdbManagedNodeEntry>();

  @SerializedName("ovsdb:manager-entry")
  @Expose
  private List<OvsdbManagerEntry> ovsdbManagerEntry = new ArrayList<OvsdbManagerEntry>();

  @SerializedName("ovsdb:connection-info")
  @Expose
  private OvsdbConnectionInfo ovsdbConnectionInfo;

  /**
   *
   * @return The ovsdbManagedBy
   */
  public String getOvsdbManagedBy() {
    return ovsdbManagedBy;
  }

  /**
   *
   * @param ovsdbManagedBy The ovsdb:managed-by
   */
  public void setOvsdbManagedBy(String ovsdbManagedBy) {
    this.ovsdbManagedBy = ovsdbManagedBy;
  }

  /**
   *
   * @return The ovsdbBridgeUuid
   */
  public String getOvsdbBridgeUuid() {
    return ovsdbBridgeUuid;
  }

  /**
   *
   * @param ovsdbBridgeUuid The ovsdb:bridge-uuid
   */
  public void setOvsdbBridgeUuid(String ovsdbBridgeUuid) {
    this.ovsdbBridgeUuid = ovsdbBridgeUuid;
  }

  /**
   *
   * @return The ovsdbBridgeName
   */
  public String getOvsdbBridgeName() {
    return ovsdbBridgeName;
  }

  /**
   *
   * @param ovsdbBridgeName The ovsdb:bridge-name
   */
  public void setOvsdbBridgeName(String ovsdbBridgeName) {
    this.ovsdbBridgeName = ovsdbBridgeName;
  }

  /**
   *
   * @return The ovsdbDatapathId
   */
  public String getOvsdbDatapathId() {
    return ovsdbDatapathId;
  }

  /**
   *
   * @param ovsdbDatapathId The ovsdb:datapath-id
   */
  public void setOvsdbDatapathId(String ovsdbDatapathId) {
    this.ovsdbDatapathId = ovsdbDatapathId;
  }

  /**
   *
   * @return The ovsdbBridgeExternalIds
   */
  public List<OvsdbBridgeExternalId> getOvsdbBridgeExternalIds() {
    return ovsdbBridgeExternalIds;
  }

  /**
   *
   * @param ovsdbBridgeExternalIds The ovsdb:bridge-external-ids
   */
  public void setOvsdbBridgeExternalIds(List<OvsdbBridgeExternalId> ovsdbBridgeExternalIds) {
    this.ovsdbBridgeExternalIds = ovsdbBridgeExternalIds;
  }

  /**
   *
   * @return The ovsdbDatapathType
   */
  public String getOvsdbDatapathType() {
    return ovsdbDatapathType;
  }

  /**
   *
   * @param ovsdbDatapathType The ovsdb:datapath-type
   */
  public void setOvsdbDatapathType(String ovsdbDatapathType) {
    this.ovsdbDatapathType = ovsdbDatapathType;
  }

  /**
   *
   * @return The nodeId
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   *
   * @param nodeId The node-id
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   *
   * @return The terminationPoint
   */
  public List<TerminationPoint> getTerminationPoint() {
    return terminationPoint;
  }

  /**
   *
   * @param terminationPoint The termination-point
   */
  public void setTerminationPoint(List<TerminationPoint> terminationPoint) {
    this.terminationPoint = terminationPoint;
  }

  /**
   *
   * @return The ovsdbFailMode
   */
  public String getOvsdbFailMode() {
    return ovsdbFailMode;
  }

  /**
   *
   * @param ovsdbFailMode The ovsdb:fail-mode
   */
  public void setOvsdbFailMode(String ovsdbFailMode) {
    this.ovsdbFailMode = ovsdbFailMode;
  }

  /**
   *
   * @return The ovsdbProtocolEntry
   */
  public List<OvsdbProtocolEntry> getOvsdbProtocolEntry() {
    return ovsdbProtocolEntry;
  }

  /**
   *
   * @param ovsdbProtocolEntry The ovsdb:protocol-entry
   */
  public void setOvsdbProtocolEntry(List<OvsdbProtocolEntry> ovsdbProtocolEntry) {
    this.ovsdbProtocolEntry = ovsdbProtocolEntry;
  }

  /**
   *
   * @return The ovsdbBridgeOpenflowNodeRef
   */
  public String getOvsdbBridgeOpenflowNodeRef() {
    return ovsdbBridgeOpenflowNodeRef;
  }

  /**
   *
   * @param ovsdbBridgeOpenflowNodeRef The ovsdb:bridge-openflow-node-ref
   */
  public void setOvsdbBridgeOpenflowNodeRef(String ovsdbBridgeOpenflowNodeRef) {
    this.ovsdbBridgeOpenflowNodeRef = ovsdbBridgeOpenflowNodeRef;
  }

  /**
   *
   * @return The ovsdbBridgeOtherConfigs
   */
  public List<OvsdbBridgeOtherConfig> getOvsdbBridgeOtherConfigs() {
    return ovsdbBridgeOtherConfigs;
  }

  /**
   *
   * @param ovsdbBridgeOtherConfigs The ovsdb:bridge-other-configs
   */
  public void setOvsdbBridgeOtherConfigs(List<OvsdbBridgeOtherConfig> ovsdbBridgeOtherConfigs) {
    this.ovsdbBridgeOtherConfigs = ovsdbBridgeOtherConfigs;
  }

  /**
   *
   * @return The ovsdbControllerEntry
   */
  public List<OvsdbControllerEntry> getOvsdbControllerEntry() {
    return ovsdbControllerEntry;
  }

  /**
   *
   * @param ovsdbControllerEntry The ovsdb:controller-entry
   */
  public void setOvsdbControllerEntry(List<OvsdbControllerEntry> ovsdbControllerEntry) {
    this.ovsdbControllerEntry = ovsdbControllerEntry;
  }

  /**
   *
   * @return The ovsdbOpenvswitchOtherConfigs
   */
  public List<OvsdbOpenvswitchOtherConfig> getOvsdbOpenvswitchOtherConfigs() {
    return ovsdbOpenvswitchOtherConfigs;
  }

  /**
   *
   * @param ovsdbOpenvswitchOtherConfigs The ovsdb:openvswitch-other-configs
   */
  public void setOvsdbOpenvswitchOtherConfigs(
      List<OvsdbOpenvswitchOtherConfig> ovsdbOpenvswitchOtherConfigs) {
    this.ovsdbOpenvswitchOtherConfigs = ovsdbOpenvswitchOtherConfigs;
  }

  /**
   *
   * @return The ovsdbManagedNodeEntry
   */
  public List<OvsdbManagedNodeEntry> getOvsdbManagedNodeEntry() {
    return ovsdbManagedNodeEntry;
  }

  /**
   *
   * @param ovsdbManagedNodeEntry The ovsdb:managed-node-entry
   */
  public void setOvsdbManagedNodeEntry(List<OvsdbManagedNodeEntry> ovsdbManagedNodeEntry) {
    this.ovsdbManagedNodeEntry = ovsdbManagedNodeEntry;
  }

  /**
   *
   * @return The ovsdbManagerEntry
   */
  public List<OvsdbManagerEntry> getOvsdbManagerEntry() {
    return ovsdbManagerEntry;
  }

  /**
   *
   * @param ovsdbManagerEntry The ovsdb:manager-entry
   */
  public void setOvsdbManagerEntry(List<OvsdbManagerEntry> ovsdbManagerEntry) {
    this.ovsdbManagerEntry = ovsdbManagerEntry;
  }

  /**
   *
   * @return The ovsdbConnectionInfo
   */
  public OvsdbConnectionInfo getOvsdbConnectionInfo() {
    return ovsdbConnectionInfo;
  }

  /**
   *
   * @param ovsdbConnectionInfo The ovsdb:connection-info
   */
  public void setOvsdbConnectionInfo(OvsdbConnectionInfo ovsdbConnectionInfo) {
    this.ovsdbConnectionInfo = ovsdbConnectionInfo;
  }
}
