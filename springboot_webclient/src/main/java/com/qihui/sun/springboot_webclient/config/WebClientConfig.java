package com.qihui.sun.springboot_webclient.config;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
public class WebClientConfig {
    private WebClientConfig(){}
    private static volatile WebClient WEBCLIENT;
    private static String baseUrl;
    private static String userName;
    private static String password;

    @Value("${remote.password}")
    public void setPassword(String password) {
        WebClientConfig.password = password;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUserName() {
        return userName;
    }

    @Value("${remote.userName}")
    public void setUserName(String userName) {
        WebClientConfig.userName = userName;
    }
    @Value("${remote.baseUrl}")
    public void setBaseUrl(String baseUrl) {
        WebClientConfig.baseUrl = baseUrl;
    }

    public static WebClient getInstance() {
        if (WEBCLIENT == null) {
            synchronized (WebClientConfig.class) {
                if (WEBCLIENT == null) {
                    WEBCLIENT = WebClient.builder()
                            .baseUrl(baseUrl)
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE,"charset=UTF-8")
                            .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                                    .secure(t -> t.sslContext(
                                            SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                                    ))))
                            .build();
                }
            }
        }
        return WebClientConfig.WEBCLIENT;
    }
}
