package com.keedio.services;

import com.keedio.domain.AdmAccessToken;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public abstract class CortanaService {

    private static final String DATAMARKET_ACCESS_URI = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";

    protected String deviceID;
    protected String appID;
    protected String appSecret;
    protected String scope;
    protected String token;
    public Boolean isTokenSet;

    public CortanaService(Map<String, String> params) {
        this.deviceID = params.get("deviceID");
        this.appID = params.get("appID");
        this.appSecret = params.get("appSecret");
        this.scope = params.get("scope");
        this.isTokenSet = false;
        try {
            authenticate();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    abstract public Object request(String arg) throws IOException;

    /**
     * Authentication helper method.
     *
     * @throws UnsupportedEncodingException
     */
    public void authenticate() throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();

        String body =
                String.format("grant_type=client_credentials&client_id=%s&client_secret=%s&scope=%s",
                        URLEncoder.encode(appID, "UTF-8"),
                        URLEncoder.encode(appSecret, "UTF-8"),
                        URLEncoder.encode(scope, "UTF-8"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<AdmAccessToken> result =
                restTemplate.exchange(DATAMARKET_ACCESS_URI, HttpMethod.POST, entity, AdmAccessToken.class);

        this.token = result.getBody().getAccess_token();
        this.isTokenSet = true;
    }

}
