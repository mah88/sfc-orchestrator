
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetworkJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class TerminationPoint {

  @SerializedName("ovsdb:ofport")
  @Expose
  private Integer ovsdbOfport;

  @SerializedName("ovsdb:name")
  @Expose
  private String ovsdbName;

  @SerializedName("tp-id")
  @Expose
  private String tpId;

  @SerializedName("ovsdb:interface-uuid")
  @Expose
  private String ovsdbInterfaceUuid;

  @SerializedName("ovsdb:interface-type")
  @Expose
  private String ovsdbInterfaceType;

  @SerializedName("ovsdb:port-uuid")
  @Expose
  private String ovsdbPortUuid;

  @SerializedName("ovsdb:interface-external-ids")
  @Expose
  private List<OvsdbInterfaceExternalId> OvsdbInterfaceExternalIds =
      new ArrayList<OvsdbInterfaceExternalId>();

  public List<OvsdbInterfaceExternalId> getOvsdbInterfaceExternalIds() {
    return OvsdbInterfaceExternalIds;
  }

  /**
   *
   * @param ovsdbPortUuid The ovsdb:port-uuid
   */
  public void setOvsdbInterfaceExternalIds(
      List<OvsdbInterfaceExternalId> OvsdbInterfaceExternalIds) {
    this.OvsdbInterfaceExternalIds = OvsdbInterfaceExternalIds;
  }
  /**
   *
   * @return The ovsdbOfport
   */
  public Integer getOvsdbOfport() {
    return ovsdbOfport;
  }

  /**
   *
   * @param ovsdbOfport The ovsdb:ofport
   */
  public void setOvsdbOfport(Integer ovsdbOfport) {
    this.ovsdbOfport = ovsdbOfport;
  }

  /**
   *
   * @return The ovsdbName
   */
  public String getOvsdbName() {
    return ovsdbName;
  }

  /**
   *
   * @param ovsdbName The ovsdb:name
   */
  public void setOvsdbName(String ovsdbName) {
    this.ovsdbName = ovsdbName;
  }

  /**
   *
   * @return The tpId
   */
  public String getTpId() {
    return tpId;
  }

  /**
   *
   * @param tpId The tp-id
   */
  public void setTpId(String tpId) {
    this.tpId = tpId;
  }

  /**
   *
   * @return The ovsdbInterfaceUuid
   */
  public String getOvsdbInterfaceUuid() {
    return ovsdbInterfaceUuid;
  }

  /**
   *
   * @param ovsdbInterfaceUuid The ovsdb:interface-uuid
   */
  public void setOvsdbInterfaceUuid(String ovsdbInterfaceUuid) {
    this.ovsdbInterfaceUuid = ovsdbInterfaceUuid;
  }

  /**
   *
   * @return The ovsdbInterfaceType
   */
  public String getOvsdbInterfaceType() {
    return ovsdbInterfaceType;
  }

  /**
   *
   * @param ovsdbInterfaceType The ovsdb:interface-type
   */
  public void setOvsdbInterfaceType(String ovsdbInterfaceType) {
    this.ovsdbInterfaceType = ovsdbInterfaceType;
  }

  /**
   *
   * @return The ovsdbPortUuid
   */
  public String getOvsdbPortUuid() {
    return ovsdbPortUuid;
  }

  /**
   *
   * @param ovsdbPortUuid The ovsdb:port-uuid
   */
  public void setOvsdbPortUuid(String ovsdbPortUuid) {
    this.ovsdbPortUuid = ovsdbPortUuid;
  }
}
