package org.project.sfc.com.OpenBaton;

/**
 * Created by mah on 2/25/16.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.nfvo.Location;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.project.sfc.com.OpenBaton.Configuration.VimProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;


@Configuration
@ComponentScan ("org.project.sfc.com")
public class OpenBatonConfiguration {

    @Autowired private VimProperties vimProperties;
    private static Logger logger = LoggerFactory.getLogger(OpenBatonConfiguration.class);

    @Bean
    public VimInstance getVimInstance(){
        VimInstance vim = new VimInstance();
        logger.debug("Creating vim");
        vim.setName("SFC-vim");
        vim.setAuthUrl(vimProperties.getAuthURL());
        vim.setKeyPair(vimProperties.getKeypair());
        vim.setPassword(vimProperties.getPassword());
        vim.setTenant(vimProperties.getTenant());
        vim.setUsername(vimProperties.getUsername());
        vim.setType(vimProperties.getType());
        Location location = new Location();
        location.setName(vimProperties.getLocationName());
        location.setLatitude(vimProperties.getLocationLatitude());
        location.setLongitude(vimProperties.getLocationLongitude());
        vim.setLocation(location);
        logger.debug("Sending VIM " + vim.toString());

        return vim;
    }

    @Bean
    public VirtualNetworkFunctionDescriptor getServiceFunction(){
        VirtualNetworkFunctionDescriptor vnfd = null;
        Gson mapper = new GsonBuilder().create();

        try{
            logger.debug("Reading Service Function Descriptor");
            FileReader vnfdFile = new FileReader("/var/tmp/sf_descriptors/sf-vnfd.json");
            vnfd = mapper.fromJson(vnfdFile,VirtualNetworkFunctionDescriptor.class);
            logger.debug("Service Function IS " + vnfd.toString());

        }
        catch (FileNotFoundException e){
            logger.debug("DO NOT REMOVE OR RENAME THE FILE /var/tmp/sf_descriptors/sf-vnfd.json!!!!\nexiting");
        }

        return vnfd;
    }

    @Bean
    public NetworkServiceDescriptor getNetworkServiceDescriptor(){
        logger.debug("Reading Network Service descriptor");
        NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();
        Gson mapper = new GsonBuilder().create();

        try{
            logger.debug("Trying to read the NS descriptor");
            FileReader nsdFile = new FileReader("/var/tmp/sf_descriptors/sf-nsd.json");
            nsd = mapper.fromJson(nsdFile,NetworkServiceDescriptor.class);
            logger.debug("NS DESCRIPTOR " + nsd.toString());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            logger.debug("DO NOT REMOVE OR RENAME THE FILE /var/tmp/sf_descriptors/sf-nsd.json!!!!\nexiting");
        }
        return nsd;
    }

}
