package com.qihui.sun.springboot_webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

//@ImportResource(locations = {"classpath:beans.xml"})
@SpringBootApplication
@EnableOpenApi
public class SpringbootWebclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebclientApplication.class, args);
    }

}
