
package org.project.sfc.com.Token;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceCatalog {

    @SerializedName("endpoints")
    @Expose
    private List<Endpoint> endpoints = new ArrayList<Endpoint>();
    @SerializedName("endpoints_links")
    @Expose
    private List<Object> endpointsLinks = new ArrayList<Object>();
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * 
     * @return
     *     The endpoints
     */
    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    /**
     * 
     * @param endpoints
     *     The endpoints
     */
    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    /**
     * 
     * @return
     *     The endpointsLinks
     */
    public List<Object> getEndpointsLinks() {
        return endpointsLinks;
    }

    /**
     * 
     * @param endpointsLinks
     *     The endpoints_links
     */
    public void setEndpointsLinks(List<Object> endpointsLinks) {
        this.endpointsLinks = endpointsLinks;
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

}
