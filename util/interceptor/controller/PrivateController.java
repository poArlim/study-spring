package com.example.interceptor.controller;

import com.example.interceptor.annotation.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private")
@Auth           // 이 다음 메서드 혹은 컨트롤러에 이 AUTH 어노테이션이 붙어있으세션을 검사해서 있을때만 통과시킨다.
public class PrivateController {        // 내부 사용자 혹은 세션 인증된 사용자만 넘긴다.

    @GetMapping("/hello")
    public String hello(){
        return "private hello";

    }
}
