package org.project.sfc.com.SfcImpl.ODL_SFC_driver.JSON.NetvirtProvidersConfigJSON;

/**
 * Created by mah on 4/21/16.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class NetvirtProvidersConfig {

    @SerializedName("netvirt-providers-config")
    @Expose
    private NetvirtProvidersConfig_ netvirtProvidersConfig;

    /**
     *
     * @return
     * The netvirtProvidersConfig
     */
    public NetvirtProvidersConfig_ getNetvirtProvidersConfig() {
        return netvirtProvidersConfig;
    }

    /**
     *
     * @param netvirtProvidersConfig
     * The netvirt-providers-config
     */
    public void setNetvirtProvidersConfig(NetvirtProvidersConfig_ netvirtProvidersConfig) {
        this.netvirtProvidersConfig = netvirtProvidersConfig;
    }

}
