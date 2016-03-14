
package org.project.sfc.com.ODL_SFC_driver.JSON.NeutronPorts;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Port {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("binding:host_id")
    @Expose
    private String bindingHostId;
    @SerializedName("allowed_address_pairs")
    @Expose
    private List<Object> allowedAddressPairs = new ArrayList<Object>();
    @SerializedName("extra_dhcp_opts")
    @Expose
    private List<Object> extraDhcpOpts = new ArrayList<Object>();
    @SerializedName("dns_assignment")
    @Expose
    private List<DnsAssignment> dnsAssignment = new ArrayList<DnsAssignment>();
    @SerializedName("device_owner")
    @Expose
    private String deviceOwner;
    @SerializedName("port_security_enabled")
    @Expose
    private Boolean portSecurityEnabled;
    @SerializedName("binding:profile")
    @Expose
    private BindingProfile bindingProfile;
    @SerializedName("fixed_ips")
    @Expose
    private List<FixedIp> fixedIps = new ArrayList<FixedIp>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("security_groups")
    @Expose
    private List<String> securityGroups = new ArrayList<String>();
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("admin_state_up")
    @Expose
    private Boolean adminStateUp;
    @SerializedName("network_id")
    @Expose
    private String networkId;
    @SerializedName("dns_name")
    @Expose
    private String dnsName;
    @SerializedName("binding:vif_details")
    @Expose
    private BindingVifDetails bindingVifDetails;
    @SerializedName("binding:vnic_type")
    @Expose
    private String bindingVnicType;
    @SerializedName("binding:vif_type")
    @Expose
    private String bindingVifType;
    @SerializedName("tenant_id")
    @Expose
    private String tenantId;
    @SerializedName("mac_address")
    @Expose
    private String macAddress;

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The bindingHostId
     */
    public String getBindingHostId() {
        return bindingHostId;
    }

    /**
     * 
     * @param bindingHostId
     *     The binding:host_id
     */
    public void setBindingHostId(String bindingHostId) {
        this.bindingHostId = bindingHostId;
    }

    /**
     * 
     * @return
     *     The allowedAddressPairs
     */
    public List<Object> getAllowedAddressPairs() {
        return allowedAddressPairs;
    }

    /**
     * 
     * @param allowedAddressPairs
     *     The allowed_address_pairs
     */
    public void setAllowedAddressPairs(List<Object> allowedAddressPairs) {
        this.allowedAddressPairs = allowedAddressPairs;
    }

    /**
     * 
     * @return
     *     The extraDhcpOpts
     */
    public List<Object> getExtraDhcpOpts() {
        return extraDhcpOpts;
    }

    /**
     * 
     * @param extraDhcpOpts
     *     The extra_dhcp_opts
     */
    public void setExtraDhcpOpts(List<Object> extraDhcpOpts) {
        this.extraDhcpOpts = extraDhcpOpts;
    }

    /**
     * 
     * @return
     *     The dnsAssignment
     */
    public List<DnsAssignment> getDnsAssignment() {
        return dnsAssignment;
    }

    /**
     * 
     * @param dnsAssignment
     *     The dns_assignment
     */
    public void setDnsAssignment(List<DnsAssignment> dnsAssignment) {
        this.dnsAssignment = dnsAssignment;
    }

    /**
     * 
     * @return
     *     The deviceOwner
     */
    public String getDeviceOwner() {
        return deviceOwner;
    }

    /**
     * 
     * @param deviceOwner
     *     The device_owner
     */
    public void setDeviceOwner(String deviceOwner) {
        this.deviceOwner = deviceOwner;
    }

    /**
     * 
     * @return
     *     The portSecurityEnabled
     */
    public Boolean getPortSecurityEnabled() {
        return portSecurityEnabled;
    }

    /**
     * 
     * @param portSecurityEnabled
     *     The port_security_enabled
     */
    public void setPortSecurityEnabled(Boolean portSecurityEnabled) {
        this.portSecurityEnabled = portSecurityEnabled;
    }

    /**
     * 
     * @return
     *     The bindingProfile
     */
    public BindingProfile getBindingProfile() {
        return bindingProfile;
    }

    /**
     * 
     * @param bindingProfile
     *     The binding:profile
     */
    public void setBindingProfile(BindingProfile bindingProfile) {
        this.bindingProfile = bindingProfile;
    }

    /**
     * 
     * @return
     *     The fixedIps
     */
    public List<FixedIp> getFixedIps() {
        return fixedIps;
    }

    /**
     * 
     * @param fixedIps
     *     The fixed_ips
     */
    public void setFixedIps(List<FixedIp> fixedIps) {
        this.fixedIps = fixedIps;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The securityGroups
     */
    public List<String> getSecurityGroups() {
        return securityGroups;
    }

    /**
     * 
     * @param securityGroups
     *     The security_groups
     */
    public void setSecurityGroups(List<String> securityGroups) {
        this.securityGroups = securityGroups;
    }

    /**
     * 
     * @return
     *     The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 
     * @param deviceId
     *     The device_id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The adminStateUp
     */
    public Boolean getAdminStateUp() {
        return adminStateUp;
    }

    /**
     * 
     * @param adminStateUp
     *     The admin_state_up
     */
    public void setAdminStateUp(Boolean adminStateUp) {
        this.adminStateUp = adminStateUp;
    }

    /**
     * 
     * @return
     *     The networkId
     */
    public String getNetworkId() {
        return networkId;
    }

    /**
     * 
     * @param networkId
     *     The network_id
     */
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    /**
     * 
     * @return
     *     The dnsName
     */
    public String getDnsName() {
        return dnsName;
    }

    /**
     * 
     * @param dnsName
     *     The dns_name
     */
    public void setDnsName(String dnsName) {
        this.dnsName = dnsName;
    }

    /**
     * 
     * @return
     *     The bindingVifDetails
     */
    public BindingVifDetails getBindingVifDetails() {
        return bindingVifDetails;
    }

    /**
     * 
     * @param bindingVifDetails
     *     The binding:vif_details
     */
    public void setBindingVifDetails(BindingVifDetails bindingVifDetails) {
        this.bindingVifDetails = bindingVifDetails;
    }

    /**
     * 
     * @return
     *     The bindingVnicType
     */
    public String getBindingVnicType() {
        return bindingVnicType;
    }

    /**
     * 
     * @param bindingVnicType
     *     The binding:vnic_type
     */
    public void setBindingVnicType(String bindingVnicType) {
        this.bindingVnicType = bindingVnicType;
    }

    /**
     * 
     * @return
     *     The bindingVifType
     */
    public String getBindingVifType() {
        return bindingVifType;
    }

    /**
     * 
     * @param bindingVifType
     *     The binding:vif_type
     */
    public void setBindingVifType(String bindingVifType) {
        this.bindingVifType = bindingVifType;
    }

    /**
     * 
     * @return
     *     The tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * 
     * @param tenantId
     *     The tenant_id
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 
     * @return
     *     The macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * 
     * @param macAddress
     *     The mac_address
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

}
