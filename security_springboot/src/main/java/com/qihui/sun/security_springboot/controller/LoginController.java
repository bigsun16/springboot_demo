package com.qihui.sun.security_springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @RequestMapping(value = "/login-success",produces = {"text/plain;charset=utf-8"})
    public String loginSuccess(){
        return "登录成功";
    }

    @GetMapping(value = "/r/r1",
            produces = "text/plain;charset=utf-8")
    public String resource1(HttpSession session){
        return "访问资源1";
    }
    @GetMapping(value = "/r/r2",
            produces = "text/plain;charset=utf-8")
    public String resource2(HttpSession session){
        return "访问资源2";
    }
}
