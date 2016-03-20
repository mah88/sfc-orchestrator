package OpenBaton.SFCdb.repository;

/**
 * Created by mah on 3/4/16.
 */
import OpenBaton.SFCdb.catalogue.RenderedServicePathRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mah on 3/4/16.
 */

public interface RenderedServicePathRecordRepository extends CrudRepository<RenderedServicePathRecord, String>, ServiceFunctionPathRecordRepositoryCustom {
    RenderedServicePathRecord findFirstById(String id);

}
