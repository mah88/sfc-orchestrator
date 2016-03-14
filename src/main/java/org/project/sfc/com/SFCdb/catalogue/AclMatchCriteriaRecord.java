package org.project.sfc.com.SFCdb.catalogue;

/**
 * Created by mah on 3/3/16.
 */

        import javax.persistence.*;
        import java.io.Serializable;

/**
 * Created by mah on 3/3/16.
 */
@Entity
public class AclMatchCriteriaRecord implements Serializable {

    private String sfccid;

    private String srcMac;

    private String dstMac;
    private String src_ip_prefix;
    private String dst_ip_prefix;

    private int src_port;
    private int dst_port;
    private int protocol;

    private String EthType;


    public AclMatchCriteriaRecord() {

    }

    @PrePersist
    public void ensureId(SfcClassifierRecord sfccr) {
        sfccid = sfccr.getId();

    }


    public String getId() {
        return sfccid;
    }

    public void setId(String id) {
        this.sfccid = id;
    }

    public String getsrcMac() {
        return srcMac;
    }

    public void setsrcMac(String mac) {
        this.srcMac = mac;
    }

    public String getdstMac() {
        return dstMac;
    }

    public void setdstMac(String mac) {
        this.dstMac = mac;
    }

    public String getSrc_ip() {
        return src_ip_prefix;
    }

    public void setSrc_ip(String ip) {
        this.src_ip_prefix = ip;
    }

    public String getDst_ip() {
        return dst_ip_prefix;
    }

    public void setDst_ip(String ip) {
        this.dst_ip_prefix = ip;
    }

    public int getSrc_port() {
        return src_port;
    }

    public void setSrc_port(int port) {
        this.src_port = port;
    }

    public int getDst_port() {
        return dst_port;
    }

    public void setDst_port(int port) {
        this.dst_port = port;
    }

    public String getEth() {
        return EthType;
    }

    public void setEth(String eth) {
        this.EthType = eth;
    }


    @Override
    public String toString() {
        return "SFCclassifierRecord{" +
                "SFC Classifier id='" + sfccid + '\'' +

                ", Src MAC='" + srcMac + '\'' +
                ", Dst MAC ='" + dstMac + '\'' +

                ", Src IP Prefix ='" + src_ip_prefix + '\'' +
                ", Dst IP Prefix ='" + dst_ip_prefix + '\'' +
                ", Src Port ='" + src_port + '\'' +
                ", Dst Port ='" + dst_port + '\'' +
                ", Eth Type='" + EthType + '\'' +
                '}';


    }

}
