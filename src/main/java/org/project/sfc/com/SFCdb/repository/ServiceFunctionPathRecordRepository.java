package org.project.sfc.com.SFCdb.repository;

import org.project.sfc.com.SFCdb.catalogue.ServiceFunctionPathRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mah on 3/4/16.
 */

public interface ServiceFunctionPathRecordRepository extends CrudRepository<ServiceFunctionPathRecord , String>, ServiceFunctionPathRecordRepositoryCustom {
    ServiceFunctionPathRecord findFirstById(String id);

}