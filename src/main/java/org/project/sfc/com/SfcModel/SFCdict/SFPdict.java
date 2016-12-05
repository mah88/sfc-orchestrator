package org.project.sfc.com.SfcModel.SFCdict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mah on 6/6/16.
 */
public class SFPdict {
  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("parent chain id")
  @Expose
  private String ParentChainID;

  @SerializedName("paths")
  @Expose
  private HashMap<Integer, VNFdict> path_sfs;


  @SerializedName("path traffic Load")
  @Expose
  private double PathtrafficLoad;

  @SerializedName("old-traffic-load")
  @Expose
  private double oldTrafficLoad=0;

  @SerializedName("QoS")
  @Expose
  private int QoS;
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
  public  HashMap<Integer, VNFdict> getPath_SFs() {
    return path_sfs;
  }

  /**
   *
   * @param path_sfs The path
   */
  public void setPath_SFs(HashMap<Integer, VNFdict> path_sfs) {
    this.path_sfs = path_sfs;
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
}
