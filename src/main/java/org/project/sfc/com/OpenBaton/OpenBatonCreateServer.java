package org.project.sfc.com.OpenBaton;

/**
 * Created by mah on 2/25/16.
 */
public class OpenBatonCreateServer {

    private String serviceFunctionID;
    private String eventAllocatedID;
    private String eventErrorID;
    private String nsdID;
    private String token;

    public OpenBatonCreateServer() {
    }

    public String getNsdID() {
        return nsdID;
    }

    public void setNsdID(String nsdID) {
        this.nsdID = nsdID;
    }

    public String getServiceFunctionID() {
        return serviceFunctionID;
    }

    public void setServiceFunctionID(String ID) {
        this.serviceFunctionID = ID;
    }

    public String getEventAllocatedID() {
        return eventAllocatedID;
    }

    public void setEventAllocatedID(String eventAllocatedID) {
        this.eventAllocatedID = eventAllocatedID;
    }

    public String getEventErrorID() {
        return eventErrorID;
    }

    public void setEventErrorID(String eventErrorID) {
        this.eventErrorID = eventErrorID;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "OpenbatonCreateServer{" +
                "ServiceFunctionID='" + serviceFunctionID + '\'' +
                ", eventAllocatedID='" + eventAllocatedID + '\'' +
                ", eventErrorID='" + eventErrorID + '\'' +
                ", nsdID='" + nsdID + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}