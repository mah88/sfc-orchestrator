
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FlowStatistics {

    @SerializedName("byte-count")
    @Expose
    private String byteCount;
    @SerializedName("packet-count")
    @Expose
    private String packetCount;
    @SerializedName("duration")
    @Expose
    private Duration duration;

    /**
     * 
     * @return
     *     The byteCount
     */
    public String getByteCount() {
        return byteCount;
    }

    /**
     * 
     * @param byteCount
     *     The byte-count
     */
    public void setByteCount(String byteCount) {
        this.byteCount = byteCount;
    }

    /**
     * 
     * @return
     *     The packetCount
     */
    public String getPacketCount() {
        return packetCount;
    }

    /**
     * 
     * @param packetCount
     *     The packet-count
     */
    public void setPacketCount(String packetCount) {
        this.packetCount = packetCount;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The duration
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

}
