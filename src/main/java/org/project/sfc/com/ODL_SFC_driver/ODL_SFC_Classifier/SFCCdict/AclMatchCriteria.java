package org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.SFCCdict;

/**
 * Created by mah on 2/8/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class AclMatchCriteria {

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


//add later
    /**
     *
     * @return
     * The source_ip_prefix
     */
/*    public String getSrcIP() {
        return srcIp;
    }

    /**
     *
     * @param srcIP
     * The ssource_ip_prefix
     */
/*    public void setSrcIp(String srcIP) {
        this.srcIp = srcIP;
    }

    /**
     *
     * @return
     * The source_ip_prefix
     */
/*    public String getDstIP() {
        return dstIp;
    }

    /**
     *
     * @param srcIP
     * The ssource_ip_prefix
     */
/*    public void setDstIp(String dstIP) {
        this.dstIp = dstIP;
    }
*/
}
