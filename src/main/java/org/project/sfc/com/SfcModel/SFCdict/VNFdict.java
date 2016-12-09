package org.project.sfc.com.SfcModel.SFCdict;

/**
 * Created by mah on 2/8/16.
 */
public class VNFdict {

  private String neutronPortId;
  private String ip;
  private String type;
  private String name;
  private double trafficLoad;
  private String HostNode;
  private String ConnectedSFF;

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
}
