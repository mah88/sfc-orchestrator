
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFFJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionForwarderOvsOvsOptions {

  @SerializedName("remote-ip")
  @Expose
  private String remoteIp;

  @SerializedName("nsi")
  @Expose
  private String nsi;

  @SerializedName("nshc3")
  @Expose
  private String nshc3;

  @SerializedName("nshc2")
  @Expose
  private String nshc2;

  @SerializedName("nshc1")
  @Expose
  private String nshc1;

  @SerializedName("nshc4")
  @Expose
  private String nshc4;

  @SerializedName("key")
  @Expose
  private String key;

  @SerializedName("dst-port")
  @Expose
  private String dstPort;

  @SerializedName("nsp")
  @Expose
  private String nsp;

  @SerializedName("exts")
  @Expose
  private String exts;

  /**
   *
   * @return The remoteIp
   */
  public String getRemoteIp() {
    return remoteIp;
  }

  /**
   *
   * @param remoteIp The remote-ip
   */
  public void setRemoteIp(String remoteIp) {
    this.remoteIp = remoteIp;
  }

  /**
   *
   * @return The dstPort
   */
  public String getDstPort() {
    return dstPort;
  }

  /**
   *
   * @param dstPort The dst-port
   */
  public void setDstPort(String dstPort) {
    this.dstPort = dstPort;
  }

  /**
   *
   * @return The key
   */
  public String getKey() {
    return key;
  }

  /**
   *
   * @param key The key
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   *
   * @return The nsp
   */
  public String getNsp() {
    return nsp;
  }

  /**
   *
   * @param nsp The nsp
   */
  public void setNsp(String nsp) {
    this.nsp = nsp;
  }

  /**
   *
   * @return The nsi
   */
  public String getNsi() {
    return nsi;
  }

  /**
   *
   * @param nsi The nsi
   */
  public void setNsi(String nsi) {
    this.nsi = nsi;
  }

  /**
   *
   * @return The nshc1
   */
  public String getNshc1() {
    return nshc1;
  }

  /**
   *
   * @param nshc1 The nshc1
   */
  public void setNshc1(String nshc1) {
    this.nshc1 = nshc1;
  }

  /**
   *
   * @return The nshc2
   */
  public String getNshc2() {
    return nshc2;
  }

  /**
   *
   * @param nshc2 The nshc2
   */
  public void setNshc2(String nshc2) {
    this.nshc2 = nshc2;
  }

  /**
   *
   * @return The nshc3
   */
  public String getNshc3() {
    return nshc3;
  }

  /**
   *
   * @param nshc3 The nshc3
   */
  public void setNshc3(String nshc3) {
    this.nshc3 = nshc3;
  }

  /**
   *
   * @return The nshc4
   */
  public String getNshc4() {
    return nshc4;
  }

  /**
   *
   * @param nshc4 The nshc4
   */
  public void setNshc4(String nshc4) {
    this.nshc4 = nshc4;
  }

  /**
   *
   * @return The exts
   */
  public String getEXTS() {
    return exts;
  }

  /**
   *
   * @param exts The exts
   */
  public void setExts(String exts) {
    this.exts = exts;
  }
}
