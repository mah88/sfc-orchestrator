package OpenBaton.SFCdb.repository;

/**
 * Created by mah on 3/4/16.
 */
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.springframework.data.repository.CrudRepository;


public interface VNFRRepository extends CrudRepository<VirtualNetworkFunctionRecord, String> {
    VirtualNetworkFunctionRecord findFirstById(String id);
}
