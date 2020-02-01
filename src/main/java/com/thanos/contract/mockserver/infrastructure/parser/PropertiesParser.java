package com.thanos.contract.mockserver.infrastructure.parser;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class PropertiesParser {

    private static final String PROP_FILE_NAME = "application.properties";
    private static Properties prop;
    private InputStream inputStream;

    public static Optional<String> getPropValues(String key) {
        return Optional.ofNullable(prop.getProperty(key));
    }

    public void init() throws IOException {
        prop = new Properties();
        inputStream = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + PROP_FILE_NAME + "' not found in the classpath");
        }
    }

    public static Boolean isPlatformMode() {
        return new Boolean(prop.getProperty("platformMode"));
    }

    public static String getContractServiceBaseHttpPath() {
        return prop.getProperty("contractService.baseHttpPath");
    }
}
