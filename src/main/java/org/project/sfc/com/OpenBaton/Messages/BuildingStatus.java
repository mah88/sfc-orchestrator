package org.project.sfc.com.OpenBaton.Messages;

/**
 * Created by mah on 2/25/16.
 */
public enum BuildingStatus {

    CREATED,
    INITIALIZING,
    INITIALISED,
    DUPLICATED,
    BUILDING,
    BUILD_OK, //internal state not visible from outside, used to redirect requests to deploymentManager
    DEPLOYNG,
    RUNNING,
    FAILED,
    PAAS_RESOURCE_MISSING

}
