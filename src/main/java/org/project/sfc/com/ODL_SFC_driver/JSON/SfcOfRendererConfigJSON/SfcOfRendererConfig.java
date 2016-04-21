package org.project.sfc.com.ODL_SFC_driver.JSON.SfcOfRendererConfigJSON;

/**
 * Created by mah on 4/21/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SfcOfRendererConfig {

    @SerializedName("sfc-of-app-egress-table-offset")
    @Expose
    private Integer sfcOfAppEgressTableOffset;
    @SerializedName("sfc-of-table-offset")
    @Expose
    private Integer sfcOfTableOffset;

    /**
     *
     * @return
     * The sfcOfAppEgressTableOffset
     */
    public Integer getSfcOfAppEgressTableOffset() {
        return sfcOfAppEgressTableOffset;
    }

    /**
     *
     * @param sfcOfAppEgressTableOffset
     * The sfc-of-app-egress-table-offset
     */
    public void setSfcOfAppEgressTableOffset(Integer sfcOfAppEgressTableOffset) {
        this.sfcOfAppEgressTableOffset = sfcOfAppEgressTableOffset;
    }

    /**
     *
     * @return
     * The sfcOfTableOffset
     */
    public Integer getSfcOfTableOffset() {
        return sfcOfTableOffset;
    }

    /**
     *
     * @param sfcOfTableOffset
     * The sfc-of-table-offset
     */
    public void setSfcOfTableOffset(Integer sfcOfTableOffset) {
        this.sfcOfTableOffset = sfcOfTableOffset;
    }

}
