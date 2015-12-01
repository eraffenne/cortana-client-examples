package com.keedio.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class Translator extends CortanaService {

    private static final Logger log = LoggerFactory.getLogger(Translator.class);
    private static final String ENDPOINT = "http://api.microsofttranslator.com/V2/Http.svc/Translate";

    public Translator(Map<String, String> params) {
        super(params);
    }

    @Override
    public String request(String text) throws IOException {

        String authToken = "Bearer " + token;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ENDPOINT)
                .queryParam("appId", authToken)
                .queryParam("text", text)
                .queryParam("to", "en")
                .queryParam("contentType", "text/plain");

        RestTemplate restTemplate = new RestTemplate();
        URI query = builder.build().encode().toUri();
        log.info("uri es: " + query);

        return restTemplate.getForObject(query, String.class);
    }
}
