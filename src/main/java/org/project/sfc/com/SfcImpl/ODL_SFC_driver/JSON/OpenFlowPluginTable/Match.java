
package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.OpenFlowPluginTable;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Match {

    @SerializedName("in-port")
    @Expose
    private String inPort;
    @SerializedName("openflowplugin-extension-general:extension-list")
    @Expose
    private List<ExtensionList> extensionList=new ArrayList<>();
    @SerializedName("tcp-source-port")
    @Expose
    private String tcpSourcePort;
    @SerializedName("tcp-destination-port")
    @Expose
    private String tcpDestinationPort;
    @SerializedName("udp-source-port")
    @Expose
    private String udpSourcePort;
    @SerializedName("udp-destination-port")
    @Expose
    private String udpDestinationPort;
    @SerializedName("ethernet-match")
    @Expose
    private EthernetMatch ethernetMatch;
    @SerializedName("ip-match")
    @Expose
    private IpMatch ipMatch;
    @SerializedName("ipv4-destination")
    @Expose
    private String ipv4Destination;
    @SerializedName("ipv4-source")
    @Expose
    private String ipv4Source;

    /**
     * 
     * @return
     *     The inPort
     */
    public String getInPort() {
        return inPort;
    }

    /**
     * 
     * @param inPort
     *     The in-port
     */
    public void setInPort(String inPort) {
        this.inPort = inPort;
    }

    /**
     * 
     * @return
     *     The extensionList
     */
    public List<ExtensionList> getExtensionList() {
        return extensionList;
    }

    /**
     * 
     * @param extensionList
     *     The extension-list
     */
    public void setExtensionList(List<ExtensionList> extensionList) {
        this.extensionList = extensionList;
    }

    /**
     * 
     * @return
     *     The tcpSourcePort
     */
    public String getTcpSourcePort() {
        return tcpSourcePort;
    }

    /**
     * 
     * @param tcpSourcePort
     *     The tcp-source-port
     */
    public void setTcpSourcePort(String tcpSourcePort) {
        this.tcpSourcePort = tcpSourcePort;
    }

    /**
     * 
     * @return
     *     The tcpDestinationPort
     */
    public String getTcpDestinationPort() {
        return tcpDestinationPort;
    }

    /**
     * 
     * @param tcpDestinationPort
     *     The tcp-destination-port
     */
    public void setTcpDestinationPort(String tcpDestinationPort) {
        this.tcpDestinationPort = tcpDestinationPort;
    }



    public String getUdpSourcePort() {
        return udpSourcePort;
    }

    /**
     *
     * @param udpSourcePort
     *     The udp-source-port
     */
    public void setUdpSourcePort(String udpSourcePort) {
        this.udpSourcePort = udpSourcePort;
    }

    /**
     *
     * @return
     *     The udpDestinationPort
     */
    public String getUdpDestinationPort() {
        return udpDestinationPort;
    }

    /**
     *
     * @param udpDestinationPort
     *     The udp-destination-port
     */
    public void setUdpDestinationPort(String udpDestinationPort) {
        this.udpDestinationPort = udpDestinationPort;
    }

    /**
     * 
     * @return
     *     The ethernetMatch
     */
    public EthernetMatch getEthernetMatch() {
        return ethernetMatch;
    }

    /**
     * 
     * @param ethernetMatch
     *     The ethernet-match
     */
    public void setEthernetMatch(EthernetMatch ethernetMatch) {
        this.ethernetMatch = ethernetMatch;
    }

    /**
     * 
     * @return
     *     The ipMatch
     */
    public IpMatch getIpMatch() {
        return ipMatch;
    }

    /**
     * 
     * @param ipMatch
     *     The ip-match
     */
    public void setIpMatch(IpMatch ipMatch) {
        this.ipMatch = ipMatch;
    }

    /**
     * 
     * @return
     *     The ipv4Destination
     */
    public String getIpv4Destination() {
        return ipv4Destination;
    }

    /**
     * 
     * @param ipv4Destination
     *     The ipv4-destination
     */
    public void setIpv4Destination(String ipv4Destination) {
        this.ipv4Destination = ipv4Destination;
    }

    /**
     * 
     * @return
     *     The ipv4Source
     */
    public String getIpv4Source() {
        return ipv4Source;
    }

    /**
     * 
     * @param ipv4Source
     *     The ipv4-source
     */
    public void setIpv4Source(String ipv4Source) {
        this.ipv4Source = ipv4Source;
    }

}
