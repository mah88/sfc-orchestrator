package org.project.sfc.com.SFCdb.catalogue;

/**
 * Created by mah on 3/3/16.
 */

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.util.IdGenerator;
import org.project.sfc.com.ODL_SFC_driver.JSON.SFPJSON.ServiceFunctionPath;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


/**
 * Created by mah on 3/3/16.
 */
@Entity
public class ServiceFunctionPathRecord implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String instance_id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(cascade = {/*CascadeType.PERSIST, CascadeType.MERGE*/ CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<VirtualNetworkFunctionRecord> vnfr;

    private boolean symmetrical;

    private String chain_name;

    private ServiceFunctionPath path;


    public ServiceFunctionPathRecord () {

    }
    @PrePersist
    public void ensureId(){
        id= IdGenerator.createUUID();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getInstanceID() {
        return instance_id;
    }

    public void setInstanceID(String id) {
        this.instance_id = id;
    }
    public boolean isSymmetrical() {
        return symmetrical;
    }

    public void setSymmetrical(boolean symm) {
        this.symmetrical = symm;
    }
    public String getSFC_name() {
        return chain_name;
    }

    public void setSFC_name(String sfc_name) {
        this.chain_name = sfc_name;
    }

    public ServiceFunctionPath getSFP() {
        return path;
    }

    public void setSFP(ServiceFunctionPath sfp) {
        this.path= sfp;
    }
    public Set<VirtualNetworkFunctionRecord> getVnfr() {
        return vnfr;
    }

    public void setVnfr(Set<VirtualNetworkFunctionRecord> vnfr) {
        this.vnfr = vnfr;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "ServiceFunctionPathRecord{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", description=" + description +
                ", instance id=" + instance_id +
                ", symmetrical=" + symmetrical +
                ", Path='" + path + '\'' +
                ", SFC NAME='" + chain_name + '\'' +
                ", vnfr=" + vnfr +
                ", status='" + status + '\'' +
                '}';
    }
}


