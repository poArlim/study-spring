package com.example.post.controller;


import com.example.post.dto.PostRestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostApiController {

    @PostMapping("/post")           // DataBody 에 담아서 보냄.
    public void post(@RequestBody Map<String, Object> requestData) {    // Post 에서는 RequestBody 어노테이션.(Get 에서는 RequestParam 이었음)
        requestData.forEach((key, value) -> {
            System.out.println("key : " + key);
            System.out.println("key : " + value);
        });
    }

    @PostMapping("/post2")
    public void post(@RequestBody PostRestDto requestData) {
        System.out.println(requestData);
    }
}

