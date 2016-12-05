
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ApplyActions {

    @SerializedName("action")
    @Expose
    private List<Action> action = new ArrayList<Action>();

    /**
     * 
     * @return
     *     The action
     */
    public List<Action> getAction() {
        return action;
    }

    /**
     * 
     * @param action
     *     The action
     */
    public void setAction(List<Action> action) {
        this.action = action;
    }

}
