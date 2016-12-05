
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Flow {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("instructions")
    @Expose
    private Instructions instructions;
    @SerializedName("hard-timeout")
    @Expose
    private String hardTimeout;
    @SerializedName("match")
    @Expose
    private Match match;
    @SerializedName("idle-timeout")
    @Expose
    private String idleTimeout;
    @SerializedName("opendaylight-flow-statistics:flow-statistics")
    @Expose
    private FlowStatistics flowStatistics;
    @SerializedName("table_id")
    @Expose
    private String tableId;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("cookie")
    @Expose
    private String cookie;

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
     *     The instructions
     */
    public Instructions getInstructions() {
        return instructions;
    }

    /**
     * 
     * @param instructions
     *     The instructions
     */
    public void setInstructions(Instructions instructions) {
        this.instructions = instructions;
    }

    /**
     * 
     * @return
     *     The hardTimeout
     */
    public String getHardTimeout() {
        return hardTimeout;
    }

    /**
     * 
     * @param hardTimeout
     *     The hard-timeout
     */
    public void setHardTimeout(String hardTimeout) {
        this.hardTimeout = hardTimeout;
    }

    /**
     * 
     * @return
     *     The match
     */
    public Match getMatch() {
        return match;
    }

    /**
     * 
     * @param match
     *     The match
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * 
     * @return
     *     The idleTimeout
     */
    public String getIdleTimeout() {
        return idleTimeout;
    }

    /**
     * 
     * @param idleTimeout
     *     The idle-timeout
     */
    public void setIdleTimeout(String idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    /**
     * 
     * @return
     *     The flowStatistics
     */
    public FlowStatistics getFlowStatistics() {
        return flowStatistics;
    }

    /**
     * 
     * @param flowStatistics
     *     The flow-statistics
     */
    public void setFlowStatistics(FlowStatistics flowStatistics) {
        this.flowStatistics = flowStatistics;
    }

    /**
     * 
     * @return
     *     The tableId
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * 
     * @param tableId
     *     The table_id
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     * 
     * @return
     *     The priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     The cookie
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * 
     * @param cookie
     *     The cookie
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

}
