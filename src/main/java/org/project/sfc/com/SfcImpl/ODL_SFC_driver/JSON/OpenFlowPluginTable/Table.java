
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Table {

    @SerializedName("flow-node-inventory:table")
    @Expose
    private List<Table_> table=new ArrayList<>();

    /**
     * 
     * @return
     *     The table
     */
    public List<Table_> getTable() {
        return table;
    }

    /**
     * 
     * @param table
     *     The table
     */
    public void setTable(List<Table_> table) {
        this.table = table;
    }

}
