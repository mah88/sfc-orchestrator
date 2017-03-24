package org.project.sfc.com.SfcModel.SFCdict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.openbaton.catalogue.util.IdGenerator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.persistence.*;

/**
 * Created by mah on 6/6/16.
 */
@Entity
public class SFPdict implements Serializable {

  @SerializedName("id")
  @Expose
  @Id
  private String id;

  @SerializedName("parent chain id")
  @Expose
  private String ParentChainID;

  @SerializedName("path sfs")
  @Expose
 /* @OneToMany(
    cascade = {CascadeType.ALL},
    fetch = FetchType.EAGER
  )*/
  private HashMap<Integer, VNFdict> path_sfs;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("path traffic Load")
  @Expose
  private double PathtrafficLoad;

  @SerializedName("old-traffic-load")
  @Expose
  private double oldTrafficLoad = 0;

  @SerializedName("QoS")
  @Expose
  private int QoS;

  @SerializedName("rsp instance id")
  @Expose
  private String rsp_instance_id;
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

  public String getInstanceId() {
    return rsp_instance_id;
  }

  public void setInstanceID(String rspID) {
    this.rsp_instance_id = rspID;
  }
  /**
   *
   * @return The parentchainid
   */
  public String getParentId() {
    return ParentChainID;
  }

  /**
   *
   * @param Parent Chain ID The parentchainid
   */
  public void setParentChainId(String id) {
    this.ParentChainID = id;
  }

  /**
   *
   * @return The path
   */
  public HashMap<Integer, VNFdict> getPath_SFs() {
    return path_sfs;
  }

  /**
   *
   * @param path_sfs The path
   */
  public void setPath_SFs(HashMap<Integer, VNFdict> path_sfs) {
    this.path_sfs = path_sfs;
  }
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

  public double getPathTrafficLoad() {
    return PathtrafficLoad;
  }

  public void setOldTrafficLoad(double load) {
    this.oldTrafficLoad = load;
  }

  public double getOldTrafficLoad() {
    return oldTrafficLoad;
  }

  public void setPathTrafficLoad(double load) {
    this.PathtrafficLoad = load;
  }

  public int getQoS() {
    return QoS;
  }

  public void setQoS(int QoS) {
    this.QoS = QoS;
  }

  @Override
  public String toString() {
    return "SFPdict{"
        + "id='"
        + id
        + '\''
        + ", ParentChainID="
        + ParentChainID
        + ", path sfs="
        + path_sfs
        + ", instance id="
        + rsp_instance_id
        + ", name="
        + name
        + ", Path traffic Load='"
        + PathtrafficLoad
        + '\''
        + ", old Traffic Load='"
        + oldTrafficLoad
        + '\''
        + ", QoS="
        + QoS
        + '\''
        + '}';
  }
}
