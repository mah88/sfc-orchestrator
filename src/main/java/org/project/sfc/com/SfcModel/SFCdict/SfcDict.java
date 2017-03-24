
package org.project.sfc.com.SfcModel.SFCdict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.openbaton.catalogue.util.IdGenerator;

import javax.annotation.Generated;
import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
@Entity
public class SfcDict implements Serializable {

  @SerializedName("status")
  @Expose
  @Enumerated(EnumType.STRING)
  private Status status;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("chain")
  @Expose
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> chain = new ArrayList<String>();

  @SerializedName("tenant_id")
  @Expose
  private String tenantId;

  @SerializedName("instance_id")
  @Expose
  private String instanceId;

  @SerializedName("infra_driver")
  @Expose
  private String infraDriver;


  @SerializedName("symmetrical")
  @Expose
  private Boolean symmetrical;

  @SerializedName("id")
  @Expose
  @Id
  private String id;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("paths")
  @Expose
  @OneToMany(
    cascade = {CascadeType.ALL},
    fetch = FetchType.EAGER
  )
  private List<SFPdict> paths;

  /**
   *
   * @return The status
   */
  public Status getStatus() {
    return status;
  }

  /**
   *
   * @param status The status
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   *
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  /**
   *
   * @param description The description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   *
   * @return The chain
   */
  public List<String> getChain() {
    return chain;
  }

  /**
   *
   * @param chain The chain
   */
  public void setChain(List<String> chain) {
    this.chain = chain;
  }

  /**
   *
   * @return The tenantId
   */
  public String getTenantId() {
    return tenantId;
  }

  /**
   *
   * @param tenantId The tenant_id
   */
  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  /**
   *
   * @return The instanceId
   */
  public String getInstanceId() {
    return instanceId;
  }

  /**
   *
   * @param instanceId The instance_id
   */
  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  /**
   *
   * @return The infraDriver
   */
  public String getInfraDriver() {
    return infraDriver;
  }

  /**
   *
   * @param infraDriver The infra_driver
   */
  public void setInfraDriver(String infraDriver) {
    this.infraDriver = infraDriver;
  }



  /**
   *
   * @return The symmetrical
   */
  public Boolean getSymmetrical() {
    return symmetrical;
  }

  /**
   *
   * @param symmetrical The symmetrical
   */
  public void setSymmetrical(Boolean symmetrical) {
    this.symmetrical = symmetrical;
  }

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
   * @return The name
   */
  public List<SFPdict> getPaths() {
    return paths;
  }

  /**
   *
   * @param name The name
   */
  public void setPaths(List<SFPdict> paths_) {
    this.paths = paths_;
  }

  @Override
  public String toString() {
    return "SfcDict{"
        + "id='"
        + id
        + '\''
        + ", name="
        + name
        + ", description="
        + description
        + ", infraDriver="
        + infraDriver
        + ", symmetrical="
        + symmetrical
        + ", CHAIN='"
        + chain
        + '\''
        + ", Paths='"
        + paths
        + '\''
        + ", TenantId='"
        + tenantId
        + '\''
        + ", status='"
        + status
        + '\''
        + '}';
  }
}
