package com.epsi.epsistore.services.Impls;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TestWebClient {

    @Autowired
    private WebClient webClient;

    public String getMessage() {
        return webClient.get()
                .uri("/api/prices/message")  // Note the relative URI due to base URL
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
