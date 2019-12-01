package com.oauth2.client.service;

import com.oauth2.client.service.model.TokenModel;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Oauth2Service {
    private static final String CLIENT_ID = "testClientId";
    private static final String SECRET = "testSecret";
    private static final String REDIRECT_URI = "http://127.0.0.1:8080/client/callback";
    private RestTemplateBuilder restTemplateBuilder;

    private String token;

    @Autowired
    public Oauth2Service(RestTemplateBuilder restTemplateBuilder) {

        this.restTemplateBuilder = restTemplateBuilder;
    }

    public TokenModel getTokenPasswordFlow(String userName, String password) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        String template = "http://127.0.0.1:8082/auth/oauth/token?scope=write&client_id=%s"
                + "&username=%s&password=%s&grant_type=password";
        String url = String.format(template, CLIENT_ID, userName, password);

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<TokenModel> token = restTemplate.exchange(url, HttpMethod.POST, entity,
                TokenModel.class);

        log.info("token is: " + token.getBody());

        return token.getBody();
    }

    public TokenModel getTokenCodeFlow(String code) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        String template =
                "http://127.0.0.1:8082/auth/oauth/token?client_id=%s&grant_type"
                        + "=authorization_code&code=%s&redirect_uri=%s";
        String url = String.format(template, CLIENT_ID, code, REDIRECT_URI);

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<TokenModel> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                TokenModel.class);

        return response.getBody();
    }

    public void logoutAuth() {
        RestTemplate restTemplate = restTemplateBuilder.build();

        String template =
                "http://127.0.0.1:8082/auth/oauth/logout?client_id=testClientId&returnTo=http://127.0.0.1/client";

        restTemplate.exchange(template, HttpMethod.POST, null, Void.class);
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String auth = CLIENT_ID + ":" + SECRET;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
