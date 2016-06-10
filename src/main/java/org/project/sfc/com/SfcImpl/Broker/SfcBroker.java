package org.project.sfc.com.SfcImpl.Broker;

import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC.Opendaylight;
import org.project.sfc.com.SfcImpl.ODL_SFC_driver.ODL_SFC_Classifier.SFC_Classifier;
import org.project.sfc.com.SfcInterfaces.SFC;
import org.project.sfc.com.SfcInterfaces.SFCclassifier;
import org.project.sfc.com.SfcInterfaces.SFCinterfaces;
import org.project.sfc.com.SfcInterfaces.SfcClassifierInter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by mah on 6/10/16.
 */
@Service
@Scope
@ConfigurationProperties
public class SfcBroker implements org.project.sfc.com.SfcInterfaces.SfcBroker {

    private HashMap<String, SFCinterfaces> sfcinterfaces;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private HashMap<String, SfcClassifierInter> sfcClassifierInterfaces;

    @PostConstruct
    private void init() {
        this.sfcinterfaces = new HashMap<String,SFCinterfaces>();
        this.sfcClassifierInterfaces = new HashMap<String,SfcClassifierInter>();

    }

    @Override
    public void addClient(SFCinterfaces sfc, String type) {
        log.info("Registered client of type: " + type);
        this.sfcinterfaces.put(type, sfc);
    }

    @Override
    public SFCinterfaces getClient(String type) {
        return this.sfcinterfaces.get(type);
    }

    @Override
    public SFC getSFC(String type)   {
        if (type=="opendaylight") {

                return new Opendaylight();
        }

        else {
                return new Opendaylight();
            }

    }

    @Override
    public void addSfcClassifierClient(SfcClassifierInter classifier, String type){
        log.info("Registered client of type: " + type);
        this.sfcClassifierInterfaces.put(type, classifier);
    }
    @Override
    public SfcClassifierInter getSfcClassifierClient(String type) {
        return this.sfcClassifierInterfaces.get(type);
    }

    @Override
    public  SFCclassifier getSfcClassifier(String type) {
        if (type=="opendaylight") {

            return new SFC_Classifier();
        }

        else {
            return new SFC_Classifier();
        }

    }
}
