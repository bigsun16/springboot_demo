package com.qihui.sun.springboot_webclient.controller;

import com.qihui.sun.springboot_webclient.vo.RemoteBasicInfoProperties;
import com.qihui.sun.springboot_webclient.vo.StudentProperties;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

@RestController
public class HelloWorldController implements InitializingBean {
//    @Value("${name}")
//    private String name;
//    @Value("${age}")
//    private Integer age;
//    @Value("${content}")
//    private String content;

    private static WebClient WEBCLIENT;
    private static String COOKIE;
    private StudentProperties studentProperties;
    private RemoteBasicInfoProperties remoteBasicInfoProperties;
    @Autowired
    public void setStudentProperties(StudentProperties studentProperties) {
        this.studentProperties = studentProperties;
    }
    @Autowired
    public void setRemoteBasicInfoProperties(RemoteBasicInfoProperties remoteBasicInfoProperties) {
        this.remoteBasicInfoProperties = remoteBasicInfoProperties;
    }

    @RequestMapping("/hello")
    public String helloWorld() {
        return studentProperties.getName()+"::"+studentProperties.getAge();
    }

    @RequestMapping("/testWebClient2")
    public Map testWebClient2(){
        login();
        Mono<ResponseEntity<Map>> responseEntityMono = WEBCLIENT
                .get()
                .uri("/ssc-device-manager/rest/malfunctionDefinition/allData/malfunctionName")
                .header(HttpHeaders.COOKIE,COOKIE)
                .retrieve()
                .toEntity(Map.class);
        Map body = responseEntityMono.block().getBody();
        logout();
        return body;
    }

    @RequestMapping("/testWebClient3")
    public Map testWebClient3(){
        login();
        Mono<ResponseEntity<Map>> responseEntityMono = WEBCLIENT
                .get()
                .uri(uriBuilder -> uriBuilder.path("/ssc-device-manager/rest/device/dedicatedNet/pmCounters")
                        .queryParam("interval", 15)
                        .queryParam("friendlyName", "ONT:fx149:IACM:R1.S1.LT3.PON3.ONT12")
                        .build())
                .header(HttpHeaders.COOKIE,COOKIE)
                .retrieve()
                .toEntity(Map.class)
                .doOnError(exception->{
                    System.out.println("error1==================================");
                    logout();
                });
        Map body = responseEntityMono.doOnError(exception->{
            System.out.println("error2==================================");
        }).block().getBody();
        logout();
        return body;
    }

    private void login(){
        MultiValueMap<String,String> parms = new LinkedMultiValueMap<>();
        parms.add("j_username", remoteBasicInfoProperties.getUserName());
        parms.add("j_password", remoteBasicInfoProperties.getPassword());
        Mono<ResponseEntity<String>> responseEntityMono1 = WEBCLIENT.post()
                .uri("/j_security_check")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(Mono.just(parms),MultiValueMap.class)
                .retrieve()
                .toEntity(String.class);
        ResponseEntity<String> block1 = responseEntityMono1.block();
        COOKIE = block1.getHeaders().get(HttpHeaders.SET_COOKIE).get(0).split(";")[0];
        System.out.println("login============"+ COOKIE);
    }
    
    private void logout(){
        System.out.println("logout============"+ COOKIE);
        Mono<ResponseEntity<Map>> responseEntityMono = WEBCLIENT.get()
                .uri("/ssc-common/rest/auth/logout")
                .header(HttpHeaders.COOKIE, COOKIE)
                .retrieve()
                .toEntity(Map.class);
        ResponseEntity<Map> block = responseEntityMono.block();
    }
//oos_nms
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("init WebClient....................");
        HttpClient secure = HttpClient.create()
                .secure(t -> t.sslContext(
                        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                ));
        WEBCLIENT = WebClient.builder()
                .baseUrl(remoteBasicInfoProperties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(secure))
                .build();
    }
}
