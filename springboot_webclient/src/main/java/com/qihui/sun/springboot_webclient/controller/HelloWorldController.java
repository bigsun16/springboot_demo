package com.qihui.sun.springboot_webclient.controller;

import com.qihui.sun.springboot_webclient.utils.WebClientRequestUtil;
import com.qihui.sun.springboot_webclient.vo.StudentProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloWorldController {
    //    @Value("${name}")
//    private String name;
//    @Value("${age}")
//    private Integer age;
//    @Value("${content}")
//    private String content;
    private StudentProperties studentProperties;

    @Autowired
    public void setStudentProperties(StudentProperties studentProperties) {
        this.studentProperties = studentProperties;
    }

    @GetMapping("/hello")
    @ApiOperation("test springboot hello world")
    public String helloWorld() {
        return studentProperties.getName() + "::" + studentProperties.getAge();
    }

    @GetMapping("/login")
    @ApiOperation("login")
    public Map testWebClient() {
        WebClientRequestUtil.login();
        return null;
    }


    @GetMapping("/logout")
    @ApiOperation("logout")
    public Map testWebClient2() {
        WebClientRequestUtil.logout("");
        return null;
    }

}
