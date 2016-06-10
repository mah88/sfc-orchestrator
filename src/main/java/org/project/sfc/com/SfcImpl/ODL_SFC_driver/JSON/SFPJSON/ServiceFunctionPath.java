
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.SFPJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class ServiceFunctionPath {
    @SerializedName("symmetric")
    @Expose
    private Boolean symmetric;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("service-chain-name")
    @Expose
    private String serviceChainName;
    @SerializedName("starting-index")
    @Expose
    private Integer startingIndex;
    @SerializedName("service-path-hop")
    @Expose
    private List<ServicePathHop> servicePathHop=new ArrayList<ServicePathHop>();
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
     *     The serviceChainName
     */
    public String getServiceChainName() {
        return serviceChainName;
    }

    /**
     * 
     * @param serviceChainName
     *     The service-chain-name
     */
    public void setServiceChainName(String serviceChainName) {
        this.serviceChainName = serviceChainName;
    }

    /**
     *
     * @return
     *     The startingIndex
     */
    public Integer getStartingIndex() {
        return startingIndex;
    }

    /**
     *
     * @param startingIndex
     *     The starting-index
     */
    public void setStartingIndex(Integer startingIndex) {
        this.startingIndex = startingIndex;
    }

    /**
     * 
     * @return
     *     The symmetric
     */
    public Boolean getSymmetric() {
        return symmetric;
    }

    /**
     * 
     * @param symmetric
     *     The symmetric
     */
    public void setSymmetric(Boolean symmetric) {
        this.symmetric = symmetric;
    }

    /**
     *
     * @return
     *     The symmetric
     */
    public List<ServicePathHop> getServicePathHop() {
        return servicePathHop;
    }

    /**
     *
     * @param symmetric
     *     The symmetric
     */
    public void setServicePathHop(List<ServicePathHop>  servicepathhop) {
        this.servicePathHop = servicepathhop;
    }

}
