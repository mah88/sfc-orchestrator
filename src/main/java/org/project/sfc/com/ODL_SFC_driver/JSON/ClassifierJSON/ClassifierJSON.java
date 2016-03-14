
package org.project.sfc.com.ODL_SFC_driver.JSON.ClassifierJSON;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ClassifierJSON {

    @SerializedName("classifiers")
    @Expose
    private Classifiers classifiers;

    /**
     * 
     * @return
     *     The classifiers
     */
    public Classifiers getClassifiers() {
        return classifiers;
    }

    /**
     * 
     * @param classifiers
     *     The classifiers
     */
    public void setClassifiers(Classifiers classifiers) {
        this.classifiers = classifiers;
    }

}
