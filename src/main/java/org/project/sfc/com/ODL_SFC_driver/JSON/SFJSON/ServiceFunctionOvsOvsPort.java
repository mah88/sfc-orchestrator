package org.project.sfc.com.ODL_SFC_driver.JSON.SFJSON;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by mah on 4/21/16.
 */
public class ServiceFunctionOvsOvsPort {
    @SerializedName("port-id")
    @Expose
    private String portId;

    /**
     *
     * @return
     *     The port-id
     */
    public String getPortID() {
        return portId;
    }

    /**
     *
     * @param portID
     *     The portID
     */
    public void setPortID(String portID) {
        this.portId = portID;
    }
}
