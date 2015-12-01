package com.keedio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Example app showing the usage of Azure Speech-to-text REST API.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${impl}")
    private String impl;

    @Value("${scope}")
    private String scope;

    @Value("${appID}")
    private String appID;

    @Value("${appSecret}")
    private String appSecret;

    @Value("${deviceID}")
    private String deviceID;

    /**
     * Application entry point, pass the "auth" string as an argument to force authentication against MS server.
     * <p/>
     * Pass the path to the wav file to call the Microsoft conversion API.
     *
     * @param args  Application arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Map<String, String> params = getParams();

        Class<?> clazz = Class.forName(impl);
        Constructor constructor = clazz.getConstructor(Map.class);
        Method request = clazz.getDeclaredMethod("request", String.class);

        Object service = constructor.newInstance(params);
        Object response = request.invoke(service, args[0]);
        if (response != null)
            log.info(response.toString());
    }

    private Map<String,String> getParams() {
        Map<String, String> params = new HashMap<>();

        params.put("scope", scope);
        params.put("appID", appID);
        params.put("appSecret", appSecret);
        params.put("deviceID", deviceID);

        return params;
    }

}
