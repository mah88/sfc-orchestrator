package org.project.sfc.com;

import org.project.sfc.com.SfcHandler.SFC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
/**
 * Created by mah on 1/27/16.
 */
@SpringBootApplication
@EnableJpaRepositories("org.project.sfc.com.SfcRepository")
@EntityScan(
    basePackages = {
        "org.project.sfc.com.SfcModel.SFCdict",
        "org.project.sfc.com.SfcModel.SFCCdict"

    }
)
@ComponentScan(basePackages = "org.project.sfc.com")
@EnableScheduling
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
