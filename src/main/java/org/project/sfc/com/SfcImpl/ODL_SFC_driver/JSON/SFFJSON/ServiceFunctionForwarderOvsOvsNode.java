package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFFJSON;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by mah on 4/21/16.
 */
public class ServiceFunctionForwarderOvsOvsNode {

    @SerializedName("node-id")
    @Expose
    private String nodeId;

    /**
     *
     * @return
     *     The node-id
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     *
     * @param nodeID
     *     The node-id
     */
    public void setNodeId(String nodeID) {
        this.nodeId = nodeID;
    }

}
