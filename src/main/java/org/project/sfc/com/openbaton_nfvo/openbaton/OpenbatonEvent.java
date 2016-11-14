package org.project.sfc.com.openbaton_nfvo.openbaton;

/**
 * Created by mah on 3/14/16.
 */
import com.google.gson.JsonObject;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;

/**
 * Created by maa on 27.11.15.
 */
public class OpenbatonEvent {

  private Action action;
  private JsonObject payload;

  public OpenbatonEvent() {}

  public Action getAction() {
    return action;
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public JsonObject getPayload() {
    return payload;
  }

  public void setPayload(JsonObject payload) {
    this.payload = payload;
  }
}
