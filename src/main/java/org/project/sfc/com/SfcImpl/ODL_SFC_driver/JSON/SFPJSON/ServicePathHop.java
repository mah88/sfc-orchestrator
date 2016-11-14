package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFPJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mah on 6/3/16.
 */
public class ServicePathHop {

  @SerializedName("service-function-name")
  @Expose
  private String serviceFunctionName;

  @SerializedName("hop-number")
  @Expose
  private int hopNumber;

  @SerializedName("service-index")
  @Expose
  private int serviceIndex;

  @SerializedName("service-function-forwarder")
  @Expose
  private String serviceFunctionForwarder;

  /**
   *
   * @return The SF name
   */
  public String getServiceFunctionName() {
    return serviceFunctionName;
  }

  /**
   *
   * @param SF name The SF name
   */
  public void setServiceFunctionName(String name) {
    this.serviceFunctionName = name;
  }

  /**
   *
   * @return The hop-number
   */
  public int getHopNumber() {
    return hopNumber;
  }

  /**
   *
   * @param Hop The Hop
   */
  public void setHopNumber(int HopNumber) {
    this.hopNumber = HopNumber;
  }

  /**
   *
   * @return The SFF
   */
  public String getServiceFunctionForwarder() {
    return serviceFunctionForwarder;
  }

  /**
   *
   * @param SFF The SFF
   */
  public void setServiceFunctionForwarder(String SFF) {
    this.serviceFunctionForwarder = SFF;
  }

  /**
   *
   * @return The hop-number
   */
  public int getServiceIndex() {
    return serviceIndex;
  }

  /**
   *
   * @param ServiceIndex The ServiceIndex
   */
  public void setServiceIndex(int ServiceIndex) {
    this.serviceIndex = ServiceIndex;
  }
}
