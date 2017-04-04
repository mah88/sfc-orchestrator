package org.project.sfc.com.SfcInterfaces;

import org.project.sfc.com.SfcDriver.SfcDriverCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.io.IOException;
/**
 * Created by mah on 6/10/16.
 */
@Service
@Scope("prototype")
public abstract class SFC implements SFCinterfaces {

  protected Logger log = LoggerFactory.getLogger(this.getClass());
  @Autowired protected SfcDriverCaller client;

  public SFC() {}

  public SFC(String type) throws IOException {

    //    client = new SfcDriverCaller(type);
  }

  public SfcDriverCaller getClient() {
    return client;
  }

  public void setClient(SfcDriverCaller client) {
    this.client = client;
  }
}
