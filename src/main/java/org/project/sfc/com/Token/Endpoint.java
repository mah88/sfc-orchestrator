
package org.project.sfc.com.Token;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Endpoint {

    @SerializedName("adminURL")
    @Expose
    private String adminURL;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("internalURL")
    @Expose
    private String internalURL;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("publicURL")
    @Expose
    private String publicURL;

    /**
     * 
     * @return
     *     The adminURL
     */
    public String getAdminURL() {
        return adminURL;
    }

    /**
     * 
     * @param adminURL
     *     The adminURL
     */
    public void setAdminURL(String adminURL) {
        this.adminURL = adminURL;
    }

    /**
     * 
     * @return
     *     The region
     */
    public String getRegion() {
        return region;
    }

    /**
     * 
     * @param region
     *     The region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 
     * @return
     *     The internalURL
     */
    public String getInternalURL() {
        return internalURL;
    }

    /**
     * 
     * @param internalURL
     *     The internalURL
     */
    public void setInternalURL(String internalURL) {
        this.internalURL = internalURL;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The publicURL
     */
    public String getPublicURL() {
        return publicURL;
    }

    /**
     * 
     * @param publicURL
     *     The publicURL
     */
    public void setPublicURL(String publicURL) {
        this.publicURL = publicURL;
    }

}
