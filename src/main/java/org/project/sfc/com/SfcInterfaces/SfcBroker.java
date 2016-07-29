package org.project.sfc.com.SfcInterfaces;

/**
 * Created by mah on 6/10/16.
 */
public interface SfcBroker {
     void addClient(SFCinterfaces sfc, String type);

    SFCinterfaces getClient(String type);

    SFC getSFC(String type);

    void addSfcClassifierClient(SfcClassifierInter classifier, String type);

    SfcClassifierInter getSfcClassifierClient(String type);

    SFCclassifier getSfcClassifier(String type);

}
