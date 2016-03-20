package OpenBaton;

/**
 * Created by mah on 2/25/16.
 */
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.nfvo.Action;

import java.io.Serializable;

public class OpenBatonEvent implements Serializable{

    private Action action;
    private NetworkServiceRecord payload;

    public OpenBatonEvent() {
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public NetworkServiceRecord getPayload() {
        return payload;
    }

    public void setPayload(NetworkServiceRecord payload) {
        this.payload = payload;
    }
}
