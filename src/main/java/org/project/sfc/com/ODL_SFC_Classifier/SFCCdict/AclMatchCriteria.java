package org.project.sfc.com.ODL_SFC_Classifier.SFCCdict;

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

}
