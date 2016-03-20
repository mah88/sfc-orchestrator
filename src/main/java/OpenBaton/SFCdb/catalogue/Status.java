package OpenBaton.SFCdb.catalogue;

/**
 * Created by mah on 3/3/16.
 */
public enum Status {


    ERROR(0),

    PENDING_CREATE (1),


    PENDING_UPDATE(2),

    ACTIVE (3),
    INACTIVE (4),
    DOWN (5),

    PENDING_DELETE(6),

    DEAD (7);


    private int value;

    Status(int value) {
        this.value = value;
    }
}
