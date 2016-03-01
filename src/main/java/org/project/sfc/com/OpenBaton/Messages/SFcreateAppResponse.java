package org.project.sfc.com.OpenBaton.Messages;

/**
 * Created by mah on 3/1/16.
 */


import org.project.sfc.com.OpenBaton.persistence.Application;

/**
 * Created by maa on 28.09.15.
 */
public class SFcreateAppResponse {

    private Application app;
    private int code;

    public SFcreateAppResponse() {
    }

    public Application getApp() {
        return app;
    }

    public void setApp(Application app) {
        this.app = app;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}