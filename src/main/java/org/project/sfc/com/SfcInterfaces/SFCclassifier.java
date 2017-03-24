package org.project.sfc.com.SfcInterfaces;

import org.project.sfc.com.SfcDriver.SfcDriverCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by mah on 6/10/16.
 */
public abstract class SFCclassifier implements SfcClassifierInter {
  protected Logger log = LoggerFactory.getLogger(this.getClass());
  protected SfcDriverCaller client;

  public SFCclassifier() {}

  public SFCclassifier(String type) throws IOException {

    client = new SfcDriverCaller(type);
  }

  public SfcDriverCaller getClient() {
    return client;
  }

  public void setClient(SfcDriverCaller client) {
    this.client = client;
  }
}
