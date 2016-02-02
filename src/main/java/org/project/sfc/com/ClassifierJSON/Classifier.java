
package org.project.sfc.com.ClassifierJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Classifier {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("acl")
    @Expose
    private String acl;
    @SerializedName("sffs")
    @Expose
    private Sffs sffs;
    @SerializedName("bridges")
    @Expose
    private Bridges bridges;

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
     *     The acl
     */
    public String getAcl() {
        return acl;
    }

    /**
     * 
     * @param acl
     *     The acl
     */
    public void setAcl(String acl) {
        this.acl = acl;
    }

    /**
     * 
     * @return
     *     The sffs
     */
    public Sffs getSffs() {
        return sffs;
    }

    /**
     * 
     * @param sffs
     *     The sffs
     */
    public void setSffs(Sffs sffs) {
        this.sffs = sffs;
    }

    /**
     * 
     * @return
     *     The bridges
     */
    public Bridges getBridges() {
        return bridges;
    }

    /**
     * 
     * @param bridges
     *     The bridges
     */
    public void setBridges(Bridges bridges) {
        this.bridges = bridges;
    }

}
