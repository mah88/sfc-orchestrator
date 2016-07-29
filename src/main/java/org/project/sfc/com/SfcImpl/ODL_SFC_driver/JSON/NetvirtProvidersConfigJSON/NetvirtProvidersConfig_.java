package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetvirtProvidersConfigJSON;

/**
 * Created by mah on 4/21/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NetvirtProvidersConfig_ {

    @SerializedName("table-offset")
    @Expose
    private Integer tableOffset;

    /**
     *
     * @return
     * The tableOffset
     */
    public Integer getTableOffset() {
        return tableOffset;
    }

    /**
     *
     * @param tableOffset
     * The table-offset
     */
    public void setTableOffset(Integer tableOffset) {
        this.tableOffset = tableOffset;
    }

}
