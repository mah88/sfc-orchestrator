package OpenBaton.SFCdb.repository;

/**
 * Created by mah on 3/4/16.
 */
import OpenBaton.SFCdb.catalogue.ServiceFunctionChainRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mah on 3/4/16.
 */

public interface ServiceFunctionChainRecordRepository extends CrudRepository<ServiceFunctionChainRecord, String>, ServiceFunctionPathRecordRepositoryCustom {
    ServiceFunctionChainRecord findFirstById(String id);

}
