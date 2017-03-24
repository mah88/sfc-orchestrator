package org.project.sfc.com.SfcModel.SFCdict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.openbaton.catalogue.util.IdGenerator;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Created by mah on 2/8/16.
 */
@Entity
public class VNFdict implements Serializable {

  @SerializedName("id")
  @Expose
  @Id
  private String id;

  @SerializedName("neutron port id")
  private String neutronPortId;

  @SerializedName("ip")
  private String ip;

  @SerializedName("type")
  private String type;

  @SerializedName("name")
  private String name;

  @SerializedName("load")
  private double trafficLoad;

  @SerializedName("host node")
  private String HostNode;

  @SerializedName("connected sff")
  private String ConnectedSFF;

  @SerializedName("status")
  @Expose
  @Enumerated(EnumType.STRING)
  private Status status;
  /**
   *
   * @return The id
   */
  public String getId() {
    return id;
  }

  public void setId(String ID) {
    this.id = ID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNeutronPortId() {
    return neutronPortId;
  }

  public void setNeutronPortId(String ID) {
    this.neutronPortId = ID;
  }

  public String getIP() {
    return ip;
  }

  public void setIP(String ip) {
    this.ip = ip;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getHostNode() {
    return HostNode;
  }

  public void setHostNode(String Host) {
    this.HostNode = Host;
  }

  public String getConnectedSFF() {
    return ConnectedSFF;
  }

  public void setConnectedSFF(String SFF) {
    this.ConnectedSFF = SFF;
  }

  public double getTrafficLoad() {
    return trafficLoad;
  }

  public void setTrafficLoad(double trafficLoad) {
    this.trafficLoad = trafficLoad;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "VNFDict{"
        + "id='"
        + id
        + '\''
        + ", status='"
        + status
        + '\''
        + ", name="
        + name
        + ", neutronPortId="
        + neutronPortId
        + ", ip="
        + ip
        + ", type="
        + type
        + ", trafficLoad='"
        + trafficLoad
        + ", ConnectedSFF='"
        + ConnectedSFF
        + '\''
        + '}';
  }
}
