package org.project.sfc.com.ODL_SFC_driver.ODL_SFC_Classifier.MatchTranslation;

/**
 * Created by mah on 2/8/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class MatchTranslation {

    @SerializedName("source_ip_prefix")
    @Expose
    private String sourceIpPrefix;
    @SerializedName("dest_ip_prefix")
    @Expose
    private String destIpPrefix;
    @SerializedName("source_port")
    @Expose
    private SourcePort sourcePort;
    @SerializedName("dest_port")
    @Expose
    private DestPort destPort;

    /**
     *
     * @return
     * The sourceIpPrefix
     */
    public String getSourceIpPrefix() {
        return sourceIpPrefix;
    }

    /**
     *
     * @param sourceIpPrefix
     * The source_ip_prefix
     */
    public void setSourceIpPrefix(String sourceIpPrefix) {
        this.sourceIpPrefix = sourceIpPrefix;
    }

    /**
     *
     * @return
     * The destIpPrefix
     */
    public String getDestIpPrefix() {
        return destIpPrefix;
    }

    /**
     *
     * @param destIpPrefix
     * The dest_ip_prefix
     */
    public void setDestIpPrefix(String destIpPrefix) {
        this.destIpPrefix = destIpPrefix;
    }

    /**
     *
     * @return
     * The sourcePort
     */
    public SourcePort getSourcePort() {
        return sourcePort;
    }

    /**
     *
     * @param sourcePort
     * The source_port
     */
    public void setSourcePort(SourcePort sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     *
     * @return
     * The destPort
     */
    public DestPort getDestPort() {
        return destPort;
    }

    /**
     *
     * @param destPort
     * The dest_port
     */
    public void setDestPort(DestPort destPort) {
        this.destPort = destPort;
    }

}
