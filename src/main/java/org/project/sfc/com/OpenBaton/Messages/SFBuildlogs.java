package org.project.sfc.com.OpenBaton.Messages;

/**
 * Created by mah on 3/1/16.
 */
public class SFBuildlogs {

    private String id;
    private String appName;
    private String projectName;
    private String log;

    public SFBuildlogs() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}