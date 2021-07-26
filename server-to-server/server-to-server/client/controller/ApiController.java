package com.example.client.controller;

import com.example.client.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
public class ApiController {

    public final RestTemplateService restTemplateService;

    public ApiController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @GetMapping("/hello")
    public String getHello(){
        return restTemplateService.hello();
        // controller 로 요청이 들어오면 restTemplateService 를 통해 서버를 호출해서 응답을 받아서 response 를 내린다.

        //return restTemplateService.post();
        //return restTemplateService.genericExchange();
    }
}
