package com.example.interceptor.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {     // 아무나 접근할 수 있는 오픈 API 형태

    @GetMapping("/hello")
    public String hello(){
        return "public hello";
    }
}
