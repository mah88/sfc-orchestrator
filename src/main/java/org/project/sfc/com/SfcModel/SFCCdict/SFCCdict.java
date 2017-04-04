package org.project.sfc.com.SfcModel.SFCCdict;

/**
 * Created by mah on 2/8/16.
 */
import javax.annotation.Generated;
import javax.persistence.*;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ACLJSON.Acl;
import org.project.sfc.com.SfcModel.SFCdict.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Generated("org.jsonschema2pojo")
@Entity
public class SFCCdict implements Serializable {

  @SerializedName("status")
  @Expose
  @Enumerated(EnumType.STRING)
  private Status status;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("chain")
  @Expose
  private String chain;

  @SerializedName("tenant_id")
  @Expose
  private String tenantId;

  @SerializedName("instance_id")
  @Expose
  private String instanceId;

  //  @SerializedName("acl_match_criteria")
  //  @Expose
  @OneToMany(
    cascade = {CascadeType.ALL},
    fetch = FetchType.EAGER
  )
  private List<AclMatchCriteria> aclMatchCriteria = new ArrayList<AclMatchCriteria>();

  @SerializedName("infra_driver")
  @Expose
  private String infraDriver;

  @SerializedName("id")
  @Expose
  @Id
  private String id;

  @SerializedName("name")
  @Expose
  private String name;

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
  public String getChain() {
    return chain;
  }

  /**
   *
   * @param chain The chain
   */
  public void setChain(String chain) {
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
   * @return The aclMatchCriteria
   */
  public List<AclMatchCriteria> getAclMatchCriteria() {
    return aclMatchCriteria;
  }

  /**
   *
   * @param aclMatchCriteria The acl_match_criteria
   */
  public void setAclMatchCriteria(List<AclMatchCriteria> aclMatchCriteria) {
    this.aclMatchCriteria = aclMatchCriteria;
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

  @Override
  public String toString() {
    return "SFCCdict"
        + "id='"
        + id
        + '\''
        + ", name="
        + name
        + ", description="
        + description
        + ", Path instance id="
        + instanceId
        + ", tenant id="
        + tenantId
        + ", infra driver="
        + infraDriver
        + ", ACL MATCH Criteria="
        + aclMatchCriteria
        + ", chain ='"
        + chain
        + ", status='"
        + status
        + '\''
        + '}';
  }
}
