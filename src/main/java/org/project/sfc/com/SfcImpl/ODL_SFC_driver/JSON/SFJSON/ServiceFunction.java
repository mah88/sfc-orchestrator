
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceFunction {
    @SerializedName("sf-data-plane-locator")
    @Expose
    private List<SfDataPlaneLocator> sfDataPlaneLocator = new ArrayList<SfDataPlaneLocator>();
    @SerializedName("nsh-aware")
    @Expose
    private String nshAware;
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ip-mgmt-address")
    @Expose
    private String ipMgmtAddress;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The ipMgmtAddress
     */
    public String getIpMgmtAddress() {
        return ipMgmtAddress;
    }

    /**
     * 
     * @param ipMgmtAddress
     *     The ip-mgmt-address
     */
    public void setIpMgmtAddress(String ipMgmtAddress) {
        this.ipMgmtAddress = ipMgmtAddress;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The nshAware
     */
    public String getNshAware() {
        return nshAware;
    }

    /**
     * 
     * @param nshAware
     *     The nsh-aware
     */
    public void setNshAware(String nshAware) {
        this.nshAware = nshAware;
    }

    /**
     * 
     * @return
     *     The sfDataPlaneLocator
     */
    public List<SfDataPlaneLocator> getSfDataPlaneLocator() {
        return sfDataPlaneLocator;
    }

    /**
     * 
     * @param sfDataPlaneLocator
     *     The sf-data-plane-locator
     */
    public void setSfDataPlaneLocator(List<SfDataPlaneLocator> sfDataPlaneLocator) {
        this.sfDataPlaneLocator = sfDataPlaneLocator;
    }

}
