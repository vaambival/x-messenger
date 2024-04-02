package ru.otus.xmessenger.web.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class UserClientImpl implements UserClient {

    @Value("${messenger.auth.basic-url}")
    private String basicUrl;
    @Value("${messenger.auth.path}")
    private String path;

    private final RestClient restClient;

    public UserClientImpl() {
        restClient = RestClient.create();
    }

    @Override
    public UUID getUser(String token) {
        return restClient
                .get()
                .uri(basicUrl + path)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .body(UUID.class);
    }
}
