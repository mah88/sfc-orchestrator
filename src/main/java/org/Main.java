package org.project.sfc.com;
import org.project.sfc.com.ODL_SFC.Opendaylight;
import org.project.sfc.com.ODL_SFC.VNFdict;
import org.project.sfc.com.SFCdict.SFCdict;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mah on 1/27/16.
 */
@SpringBootApplication
@EnableJpaRepositories
@EntityScan
@ComponentScan
@EnableScheduling
public class Main {

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);











    }
}
