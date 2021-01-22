package com.qihui.sun.springboot_webclient.utils;

import com.qihui.sun.springboot_webclient.config.WebClientConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class WebClientRequestUtil {
    public static Object request(String uri, Map<String, Object> queryParams, HttpMethod httpMethod, String cookie) {
        WebClient.RequestBodyUriSpec requestBodyUriSpec = WebClientConfig.getInstance().method(httpMethod);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = null;
        switch (httpMethod) {
            case GET:
            case DELETE:
                Function<UriBuilder, URI> function = uriBuilder -> {
                    UriBuilder builder = uriBuilder.path(uri);
                    if (queryParams != null) {
                        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                            builder = builder.queryParam(entry.getKey(), entry.getValue());
                        }
                    }
                    return builder.build();
                };
                requestHeadersSpec = requestBodyUriSpec
                        .uri(function)
                        .header(HttpHeaders.COOKIE, cookie);
                break;
            case POST:
            case PUT:
                String jsonData = JacksonUtils.map2Json(queryParams);
                requestHeadersSpec = requestBodyUriSpec.uri(uri)
                        .header(HttpHeaders.COOKIE, cookie)
                        .body(Mono.just(jsonData), String.class);
                break;
        }
        assert requestHeadersSpec != null;
        return requestHeadersSpec
                .retrieve()
                .bodyToMono(Object.class).block();
    }

    public static String login() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("username", WebClientConfig.getUserName());
        params.add("password", WebClientConfig.getPassword());
        Mono<ResponseEntity<String>> responseEntityMono = WebClientConfig.getInstance().post()
                .uri("/login")
                .body(Mono.just(params), MultiValueMap.class)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .toEntity(String.class);
        HttpHeaders headers = Objects.requireNonNull(responseEntityMono.block()).getHeaders();
        return Objects.requireNonNull(headers.get(HttpHeaders.SET_COOKIE)).get(0).split(";")[0];
    }

    public static void logout(String cookie) {
        request("/logout", null, HttpMethod.GET, cookie);
    }
}
