
package org.project.sfc.com.ODL_SFC_driver.JSON.ClassifierJSON;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Classifiers {

    @SerializedName("classifier")
    @Expose
    private List<Classifier> classifier = new ArrayList<Classifier>();

    /**
     * 
     * @return
     *     The classifier
     */
    public List<Classifier> getClassifier() {
        return classifier;
    }

    /**
     * 
     * @param classifier
     *     The classifier
     */
    public void setClassifier(List<Classifier> classifier) {
        this.classifier = classifier;
    }

}
