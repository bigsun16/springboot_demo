package com.qihui.sun.springboot_webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootWebclientApplicationTests {

    @Value("${aa.bb}")
    private String text;

    @Test
    public void test(){
        System.out.println(text);
    }
}
