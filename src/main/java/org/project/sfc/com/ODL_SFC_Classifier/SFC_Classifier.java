package org.project.sfc.com.ODL_SFC_Classifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mah on 2/5/16.
 */
public class SFC_Classifier {

    public String ODL_ip="127.0.0.1";
    public String ODL_port="8080";
    public String ODL_username="admin";
    public String ODL_password="admin";
    public String Config_acl_url="restconf/config/ietf-access-control-list:access-lists/acl/{}";
    public String Config_netvirtsfc_url="restconf/config/netvirt-sfc-classifier:classifiers/classifier/{}";

    public int sff_counter=1;

    private static Logger logger = LoggerFactory.getLogger(SFC_Classifier.class);

    private void init(){

        this.ODL_ip=ODL_ip;
        this.ODL_port=ODL_port;
        this.ODL_username=ODL_username;
        this.ODL_password=ODL_password;
        this.Config_acl_url=Config_acl_url;

        this.Config_netvirtsfc_url=Config_netvirtsfc_url;

        this.sff_counter=sff_counter;



    }
}
