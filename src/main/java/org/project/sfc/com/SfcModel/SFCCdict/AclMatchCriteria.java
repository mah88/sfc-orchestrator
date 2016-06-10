package org.project.sfc.com.SfcModel.SFCCdict;

/**
 * Created by mah on 2/8/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class AclMatchCriteria {
    @SerializedName("source-ipv4-network")
    @Expose
    private String sourceIpv4Network;
    @SerializedName("destination-ipv4-network")
    @Expose
    private String destIpv4Network;

    @SerializedName("destination-mac-address")
    @Expose
    private String destMacAddress;
    @SerializedName("source-mac-address")
    @Expose
    private String srcMacAddress;

    @SerializedName("protocol")
    @Expose
    private Integer protocol;
    @SerializedName("dest_port")
    @Expose
    private Integer destPort;
    @SerializedName("src_port")
    @Expose
    private Integer srcPort;
/*
add later
    @SerializedName("source_ip_prefix")
    @Expose
    private String srcIp;

    @SerializedName("dest_ip_prefix")
    @Expose
    private String dstIp;
    /**
     *
     * @return
     * The protocol
     */
    public Integer getProtocol() {
        return protocol;
    }

    /**
     *
     * @param protocol
     * The protocol
     */
    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    /**
     *
     * @return
     * The destPort
     */
    public Integer getDestPort() {
        return destPort;
    }

    /**
     *
     * @param destPort
     * The dest_port
     */
    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    /**
     *
     * @return
     * The destPort
     */
    public Integer getSrcPort() {
        return srcPort;
    }

    /**
     *
     * @param destPort
     * The dest_port
     */
    public void setSrcPort(Integer srcPort) {
        this.srcPort = srcPort;
    }



    /**
     *
     * @return
     * The sourceIpPrefix
     */
    public String getSourceIpv4() {
        return sourceIpv4Network;
    }

    /**
     *
     * @param sourceIpPrefix
     * The source_ip_prefix
     */
    public void setSourceIpv4(String sourceIpPrefix) {
        this.sourceIpv4Network = sourceIpPrefix;
    }

    /**
     *
     * @return
     * The destIpPrefix
     */
    public String getDestIpv4() {
        return destIpv4Network;
    }

    /**
     *
     * @param destIpPrefix
     * The dest_ip_prefix
     */
    public void setDestIpv4(String destIpPrefix) {
        this.destIpv4Network = destIpPrefix;
    }

    /**
     *
     * @return
     * The sourceMAC address
     */
    public String getSourceMAC() {
        return srcMacAddress;
    }

    /**
     *
     * @param sourceMACaddress
     * The source MAC
     */
    public void setSourceMAC(String sourceMAC) {
        this.srcMacAddress = sourceMAC;
    }

    /**
     *
     * @return
     * The destMac address
     */
    public String getDestMAC() {
        return destMacAddress;
    }

    /**
     *
     * @param dest Mac
     * The dest Mac address
     */
    public void setDestMAC(String destMAC) {
        this.destMacAddress = destMAC;
    }
}
