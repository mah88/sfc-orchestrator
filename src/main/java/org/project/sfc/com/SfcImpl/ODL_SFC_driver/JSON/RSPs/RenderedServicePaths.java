package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.RSPs;

/**
 * Created by mah on 4/26/16.
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class RenderedServicePaths {

    @SerializedName("rendered-service-path")
    @Expose
    private List<RenderedServicePath> renderedServicePath = new ArrayList<RenderedServicePath>();

    /**
     *
     * @return
     * The renderedServicePath
     */
    public List<RenderedServicePath> getRenderedServicePath() {
        return renderedServicePath;
    }

    /**
     *
     * @param renderedServicePath
     * The rendered-service-path
     */
    public void setRenderedServicePath(List<RenderedServicePath> renderedServicePath) {
        this.renderedServicePath = renderedServicePath;
    }

}
