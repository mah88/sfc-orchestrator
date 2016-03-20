package OpenBaton.Messages;

/**
 * Created by mah on 3/1/16.
 */

import OpenBaton.Configuration.Flavor;

/**
 * Created by maa on 28.09.15.
 */
public class SFcreateAppRequest {

    private String gitURL;
    private String appName;
    private String projectName;  //Replacing
    private Flavor flavor;
    private int replicasNumber;
    private boolean cloudRepository;
    private int scaleInOut;

    public SFcreateAppRequest() {
    }

    public String getGitURL() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
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



    public Flavor getFlavor() {
        return flavor;
    }

    public void setFlavor(Flavor flavor) {
        this.flavor = flavor;
    }

    public int getReplicasNumber() {
        return replicasNumber;
    }

    public void setReplicasNumber(int replicasNumber) {
        this.replicasNumber = replicasNumber;
    }



    public boolean isCloudRepository() {
        return cloudRepository;
    }

    public void setCloudRepository(boolean cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public int getScaleInOut() {
        return scaleInOut;
    }

    public void setScaleInOut(int scaleInOut) {
        this.scaleInOut = scaleInOut;
    }

    @Override
    public String toString() {
        return "NubomediaCreateAppRequest{" +
                "gitURL='" + gitURL + '\'' +
                ", appName='" + appName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", flavor=" + flavor +
                ", replicasNumber=" + replicasNumber +
                ", scaleInOut=" + scaleInOut +
                '}';
    }
}