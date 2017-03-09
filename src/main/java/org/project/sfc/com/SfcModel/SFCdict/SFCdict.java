
package org.project.sfc.com.SfcModel.SFCdict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import javax.persistence.Entity;

//@Entity
@Generated("org.jsonschema2pojo")
public class SFCdict {

  @SerializedName("sfc_dict")
  @Expose
  private SfcDict sfcDict;

  /**
   *
   * @return The sfcDict
   */
  public SfcDict getSfcDict() {
    return sfcDict;
  }

  /**
   *
   * @param sfcDict The sfc_dict
   */
  public void setSfcDict(SfcDict sfcDict) {
    this.sfcDict = sfcDict;
  }
}
