package org.project.sfc.com.SFCdb.catalogue;


        import org.openbaton.catalogue.util.IdGenerator;
        import org.project.sfc.com.SFCJSON.ServiceFunctionChain;

        import javax.persistence.*;
        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Set;
/**
 * Created by mah on 3/3/16.
 */
@Entity
public class ServiceFunctionChainRecord implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String instance_id;

    @Enumerated(EnumType.STRING)
    private Status status;


    private boolean symmetrical;

    private List<String> chain=new ArrayList<>();

    public ServiceFunctionChainRecord () {

    }
    @PrePersist
    public void ensureId(){
        id=IdGenerator.createUUID();
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
    public List<String> getSFC() {
        return chain;
    }

    public void setSFC(List<String> sfc) {
        this.chain = sfc;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "ServiceFunctionChainRecord{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", description=" + description +
                ", instance id=" + instance_id +
                ", symmetrical=" + symmetrical +
                ", CHAIN='" + chain + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

