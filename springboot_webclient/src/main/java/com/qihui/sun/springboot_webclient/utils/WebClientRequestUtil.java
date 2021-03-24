package com.qihui.sun.springboot_webclient.utils;

import com.qihui.sun.springboot_webclient.config.WebClientConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author qihuis
 */
public class WebClientRequestUtil {

    public static ParallelFlux<Map> concurrentRequestWithDelay(Function<String, Mono<Map>> function) {
        Set<String> tokens = new HashSet<>();
        long delaySeconds = 1L;
        return Flux.fromStream(tokens.stream())
                .delayElements(Duration.ofMillis(delaySeconds))
                .parallel()
                .runOn(Schedulers.elastic())
                .flatMap(function);
    }

    public static Object request(String uri, Map<String, Object> queryParams,Object bodyData, HttpMethod httpMethod, String cookie) {
        WebClient.RequestBodyUriSpec requestBodyUriSpec = WebClientConfig.getInstance().method(httpMethod);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = null;
        Function<UriBuilder, URI> uriStr = uriBuilder -> {
            UriBuilder builder = uriBuilder.path(uri);
            if (queryParams != null) {
                for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    builder = builder.queryParam(entry.getKey(), entry.getValue());
                }
            }
            return builder.build();
        };
        switch (httpMethod) {
            case GET:
            case DELETE:
                requestHeadersSpec = requestBodyUriSpec
                        .uri(uriStr)
                        .header(HttpHeaders.COOKIE, cookie);
                break;
            case POST:
            case PUT:
                Mono<String> monoValue = Mono.empty();
                try {
                    String jsonData = JacksonUtils.obj2Json(bodyData);
                    monoValue = Mono.just(jsonData);
                }catch (Exception e){
                    e.printStackTrace();
                }
                requestHeadersSpec = requestBodyUriSpec.uri(uriStr)
                        .header(HttpHeaders.COOKIE, cookie)
                        .body(monoValue, String.class);
                break;
            default:
                break;
        }
        assert requestHeadersSpec != null;
        return requestHeadersSpec
                .retrieve()
                .bodyToMono(Object.class).block();
    }
}
