package org.project.sfc.com.SfcModel.SFCdict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mah on 6/6/16.
 */
public class SFPdict {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parent chain id")
    @Expose
    private String ParentChainID;
    @SerializedName("paths")
    @Expose
    private List<VNFdict> path_sfs;


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
     *     The parentchainid
     */
    public String getParentId() {
        return ParentChainID;
    }

    /**
     *
     * @param Parent Chain ID
     *     The parentchainid
     */
    public void setParentChainId(String id) {
        this.ParentChainID = id;
    }

    /**
     *
     * @return
     *     The path
     */
    public List<VNFdict> getPath_SFs() {
        return path_sfs;
    }

    /**
     *
     * @param path_sfs
     *     The path
     */
    public void setPath_SFs(List<VNFdict> path_sfs) {
        this.path_sfs = path_sfs;
    }

}
