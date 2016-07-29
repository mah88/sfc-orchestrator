package org.project.sfc.com.openbaton_nfvo.utils;

/**
 * Created by mah on 3/14/16.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by maa on 27.11.15.
 */
public class ConfigReader {

    private static Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    public static Properties readProperties() throws IOException {
        Properties properties = new Properties();
        File prop = new File("/var/tmp/sfc-controller/src/main/resources/SFC.properties");

        if(!prop.exists()){
            logger.info("file not found using local one");
            properties.load(ConfigReader.class.getResourceAsStream("/SFC.properties"));
        }
        else{
            logger.info("file found using etc local");
            properties.load(new FileInputStream(prop));
        }
        logger.info("properties loaded " + properties);

        return properties;
    }

}
