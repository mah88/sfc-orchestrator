
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPJSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class RSPJSON {

    @SerializedName("input")
    @Expose
    private Input input;

    /**
     * 
     * @return
     *     The input
     */
    public Input getInput() {
        return input;
    }

    /**
     * 
     * @param input
     *     The input
     */
    public void setInput(Input input) {
        this.input = input;
    }

}
