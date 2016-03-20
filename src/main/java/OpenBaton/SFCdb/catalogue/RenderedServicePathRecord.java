package OpenBaton.SFCdb.catalogue;

/**
 * Created by mah on 3/3/16.
 */



        import org.openbaton.catalogue.util.IdGenerator;
        import org.project.sfc.com.ODL_SFC_driver.JSON.RSPJSON.RSPJSON;

        import javax.persistence.*;
        import java.io.Serializable;


/**
 * Created by mah on 3/3/16.
 */
@Entity
public class RenderedServicePathRecord implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String instance_id;

    @Enumerated(EnumType.STRING)
    private Status status;


    private boolean symmetrical;


    private String parent_path_name;

    private RSPJSON rsp;


    public RenderedServicePathRecord () {

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
    public String getParentSFP_name() {
        return parent_path_name;
    }

    public void setParentSFP_name(String sfp_name) {
        this.parent_path_name = sfp_name;
    }
    public RSPJSON getRSP() {
        return rsp;
    }

    public void setSFP(RSPJSON path) {
        this.rsp= path;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "RenderedServicePathRecord{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", description=" + description +
                ", instance id=" + instance_id +
                ", symmetrical=" + symmetrical +
                ", Path Name='" + parent_path_name + '\'' +
                ", RSP ='" + rsp + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}



