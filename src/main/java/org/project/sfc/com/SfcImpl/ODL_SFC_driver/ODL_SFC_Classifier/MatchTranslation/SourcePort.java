package org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.MatchTranslation;

/**
 * Created by mah on 2/8/16.
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SourcePort {

    @SerializedName("source-port-range")
    @Expose
    private List<String> sourcePortRange = new ArrayList<String>();

    /**
     *
     * @return
     * The sourcePortRange
     */
    public List<String> getSourcePortRange() {
        return sourcePortRange;
    }

    /**
     *
     * @param sourcePortRange
     * The source-port-range
     */
    public void setSourcePortRange(List<String> sourcePortRange) {
        this.sourcePortRange = sourcePortRange;
    }

}