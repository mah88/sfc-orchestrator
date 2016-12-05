
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class GoToTable {

    @SerializedName("table_id")
    @Expose
    private String tableId;

    /**
     * 
     * @return
     *     The tableId
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * 
     * @param tableId
     *     The table_id
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

}
