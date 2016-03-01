package org.project.sfc.com.OpenBaton.persistence;

/**
 * Created by mah on 3/1/16.
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.project.sfc.com.OpenBaton.Messages.BuildingStatus;
import org.project.sfc.com.OpenBaton.Configuration.Flavor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by maa on 28.09.15.
 */
@Entity
public class Application {

    @Id
    private String appID;
    private String appName;
    private String projectName;
    private String route;
    private String nsrID;
    private String gitURL;
    private int replicasNumber;
    private Flavor flavor;
    private BuildingStatus status;
    @JsonIgnore private boolean resourceOK;

    public Application(String appID,Flavor flavor, String appName, String projectName, String route, String nsrID, String gitURL,int replicasNumber,boolean resourceOK) {
        this.appID = appID;
        this.flavor = flavor;
        this.appName = appName;
        this.projectName = projectName;
        this.route = route;
        this.nsrID = nsrID;
        this.gitURL = gitURL;




        this.replicasNumber = replicasNumber;
        this.status = BuildingStatus.CREATED;
        this.resourceOK = resourceOK;
    }

    public Application() {
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getNsrID() {
        return nsrID;
    }

    public void setNsrID(String nsrID) {
        this.nsrID = nsrID;
    }

    public String getGitURL() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
    }

    public int getReplicasNumber() {
        return replicasNumber;
    }

    public void setReplicasNumber(int replicasNumber) {
        this.replicasNumber = replicasNumber;
    }


    public Flavor getFlavor() {
        return flavor;
    }

    public void setFlavor(Flavor flavor) {
        this.flavor = flavor;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public BuildingStatus getStatus() {
        return status;
    }

    public void setStatus(BuildingStatus status) {
        this.status = status;
    }

    public boolean isResourceOK() {
        return resourceOK;
    }

    public void setResourceOK(boolean resourceOK) {
        this.resourceOK = resourceOK;
    }



    @Override
    public String toString(){
        return "Application with ID: " + appID  + "\n" +
                "Application name: " + appName + "\n" +
                "Project: " + projectName + "\n" +
                "Route: " + route + "\n" +
                "Git URL: " + gitURL;
    }
}
