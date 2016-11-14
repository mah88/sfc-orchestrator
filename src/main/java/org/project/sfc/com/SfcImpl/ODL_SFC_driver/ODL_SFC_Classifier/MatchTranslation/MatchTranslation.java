package org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.MatchTranslation;

/**
 * Created by mah on 2/8/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class MatchTranslation {

  @SerializedName("source-ipv4-network")
  @Expose
  private String sourceIpv4Network;

  @SerializedName("destination-ipv4-network")
  @Expose
  private String destIpv4Network;

  @SerializedName("source_port")
  @Expose
  private SourcePort sourcePort;

  @SerializedName("dest_port")
  @Expose
  private DestPort destPort;

  /**
   *
   * @return The sourceIpPrefix
   */
  public String getSourceIpv4() {
    return sourceIpv4Network;
  }

  /**
   *
   * @param sourceIpPrefix The source_ip_prefix
   */
  public void setSourceIpv4(String sourceIpPrefix) {
    this.sourceIpv4Network = sourceIpPrefix;
  }

  /**
   *
   * @return The destIpPrefix
   */
  public String getDestIpv4() {
    return destIpv4Network;
  }

  /**
   *
   * @param destIpPrefix The dest_ip_prefix
   */
  public void setDestIpv4(String destIpPrefix) {
    this.destIpv4Network = destIpPrefix;
  }

  /**
   *
   * @return The sourcePort
   */
  public SourcePort getSourcePort() {
    return sourcePort;
  }

  /**
   *
   * @param sourcePort The source_port
   */
  public void setSourcePort(SourcePort sourcePort) {
    this.sourcePort = sourcePort;
  }

  /**
   *
   * @return The destPort
   */
  public DestPort getDestPort() {
    return destPort;
  }

  /**
   *
   * @param destPort The dest_port
   */
  public void setDestPort(DestPort destPort) {
    this.destPort = destPort;
  }
}
