package com.epsi.epsistore.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")  // Set the base URL
                .defaultCookie("cookieKey", "cookieValue")  // Add default cookie
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)  // Set default header
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8081"))  // Add default URI variable
                .build();
    }

}
