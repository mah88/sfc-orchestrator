package org.project.sfc.com.OpenBaton.persistence;

/**
 * Created by mah on 3/1/16.
 */
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by maa on 21.10.15.
 */
public interface ApplicationRepository extends CrudRepository<Application, String> {

    List<Application> findByAppName(String appName);
    Application findFirstByAppID(String id);
}
