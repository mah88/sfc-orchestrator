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
public class DestPort {

  @SerializedName("destination-port-range")
  @Expose
  private List<String> destinationPortRange = new ArrayList<String>();

  /**
   *
   * @return The destinationPortRange
   */
  public List<String> getDestinationPortRange() {
    return destinationPortRange;
  }

  /**
   *
   * @param destinationPortRange The destination-port-range
   */
  public void setDestinationPortRange(List<String> destinationPortRange) {
    this.destinationPortRange = destinationPortRange;
  }
}
