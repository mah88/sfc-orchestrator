
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.ACLJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Ace {


    @SerializedName("matches")
    @Expose
    private Matches matches;
    @SerializedName("actions")
    @Expose
    private Actions actions;
    @SerializedName("rule-name")
    @Expose
    private String ruleName;
    /**
     * 
     * @return
     *     The ruleName
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * 
     * @param ruleName
     *     The rule-name
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * 
     * @return
     *     The matches
     */
    public Matches getMatches() {
        return matches;
    }

    /**
     * 
     * @param matches
     *     The matches
     */
    public void setMatches(Matches matches) {
        this.matches = matches;
    }

    /**
     * 
     * @return
     *     The actions
     */
    public Actions getActions() {
        return actions;
    }

    /**
     * 
     * @param actions
     *     The actions
     */
    public void setActions(Actions actions) {
        this.actions = actions;
    }

}
