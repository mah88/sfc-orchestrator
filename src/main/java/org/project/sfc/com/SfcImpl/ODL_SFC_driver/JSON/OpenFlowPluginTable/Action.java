
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Action {

    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("openflowplugin-extension-nicira-action:nx-set-nsp")
    @Expose
    private NxSetNsp nxSetNsp;
    @SerializedName("openflowplugin-extension-nicira-action:nx-set-nsi")
    @Expose
    private NxSetNsi nxSetNsi;
    @SerializedName("openflowplugin-extension-nicira-action:nx-resubmit")
    @Expose
    private NxResubmit nxResubmit;
    @SerializedName("openflowplugin-extension-nicira-action:nx-reg-load")
    @Expose
    private NxRegLoad nxRegLoad;

    /**
     * 
     * @return
     *     The order
     */
    public String getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The order
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 
     * @return
     *     The nxSetNsp
     */
    public NxSetNsp getNxSetNsp() {
        return nxSetNsp;
    }

    /**
     * 
     * @param nxSetNsp
     *     The nx-set-nsp
     */
    public void setNxSetNsp(NxSetNsp nxSetNsp) {
        this.nxSetNsp = nxSetNsp;
    }

    /**
     * 
     * @return
     *     The nxSetNsi
     */
    public NxSetNsi getNxSetNsi() {
        return nxSetNsi;
    }

    /**
     * 
     * @param nxSetNsi
     *     The nx-set-nsi
     */
    public void setNxSetNsi(NxSetNsi nxSetNsi) {
        this.nxSetNsi = nxSetNsi;
    }

    /**
     * 
     * @return
     *     The nxResubmit
     */
    public NxResubmit getNxResubmit() {
        return nxResubmit;
    }

    /**
     * 
     * @param nxResubmit
     *     The nx-resubmit
     */
    public void setNxResubmit(NxResubmit nxResubmit) {
        this.nxResubmit = nxResubmit;
    }

    /**
     * 
     * @return
     *     The nxRegLoad
     */
    public NxRegLoad getNxRegLoad() {
        return nxRegLoad;
    }

    /**
     * 
     * @param nxRegLoad
     *     The nx-reg-load
     */
    public void setNxRegLoad(NxRegLoad nxRegLoad) {
        this.nxRegLoad = nxRegLoad;
    }

}
