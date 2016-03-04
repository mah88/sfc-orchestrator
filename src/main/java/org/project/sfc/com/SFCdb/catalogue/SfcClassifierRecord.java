package org.project.sfc.com.SFCdb.catalogue;

/**
 * Created by mah on 3/3/16.
 */




        import org.openbaton.catalogue.util.IdGenerator;
        import org.project.sfc.com.RSPJSON.RSPJSON;
        import org.project.sfc.com.SFCJSON.ServiceFunctionChain;
        import org.project.sfc.com.SFPJSON.ServiceFunctionPath;

        import javax.persistence.*;
        import java.io.Serializable;



/**
 * Created by mah on 3/3/16.
 */
@Entity
public class SfcClassifierRecord implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String instance_id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private AclMatchCriteriaRecord ACL;

    private String RSP_ID;


    public SfcClassifierRecord () {

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

    public AclMatchCriteriaRecord getACL() {
        return ACL;
    }

    public void setACL(AclMatchCriteriaRecord acl_) {
        this.ACL = acl_;
    }
    public String getRSPID() {
        return RSP_ID;
    }

    public void setRSPID(String rsp_id) {
        this.RSP_ID= rsp_id;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "SFCclassifierRecord{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", description=" + description +
                ", instance id=" + instance_id +
                ", ACL MATCH Criteria=" + ACL +
                ", RSP ID ='" + RSP_ID + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}



