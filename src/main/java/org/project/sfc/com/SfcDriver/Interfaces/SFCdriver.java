package org.project.sfc.com.SfcDriver.Interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mah on 6/10/16.
 */
public abstract class SFCdriver {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  protected Properties properties;

  protected SFCdriver() {
    loadProperties();
  }

  public void loadProperties() {
    properties = new Properties();

    log.debug("Loading properties");
    try {
      InputStream resourceAsStream =
          this.getClass()
              .getResourceAsStream(
                  "/var/tmp/sfc-controller/src/main/resources/sfc_driver.properties");
      if (resourceAsStream != null) {
        properties.load(resourceAsStream);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    log.debug("Loaded properties: " + properties);
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }
}
