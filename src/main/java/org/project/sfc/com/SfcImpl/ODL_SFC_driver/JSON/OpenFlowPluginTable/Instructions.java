
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Instructions {

    @SerializedName("instruction")
    @Expose
    private List<Instruction > instruction=new ArrayList<>();

    /**
     * 
     * @return
     *     The instruction
     */
    public List<Instruction> getInstruction() {
        return instruction;
    }

    /**
     * 
     * @param instruction
     *     The instruction
     */
    public void setInstruction(List<Instruction> instruction) {
        this.instruction = instruction;
    }

}
