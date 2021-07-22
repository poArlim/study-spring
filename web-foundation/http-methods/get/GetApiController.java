package com.example.hello.controller;

import com.example.hello.dto.UserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController                 // 해당 Class 는 REST API 처리하는 Controller
@RequestMapping("/api/get")      // RequestMapping URI 를 지정해주는 annotation
public class ApiController {

    @GetMapping(path = "/hello")    // http://localhost:8080/api/get/hello
    public String hello(){
        return "Hello spring boot!";
    }

    @RequestMapping(path = "/hi", method = RequestMethod.GET)       // method 를 지정해주지 않으면 get/post/put/... 모든 method 에 동작
    public String hi(){
        return "Hi";
    }

    @GetMapping("path-variable/{name}")     // name 의 자리에 pathVariable 을 넣어줄 수 있다.
    public String pathVariable(@PathVariable String name){
    //public String pathVariable(@PathVariable(name = "name") String pathName){ // name 을 다른 곳에서 사용해야 하는 경우 이렇게 지정해줄 수 있음
        System.out.println("PathVariable : "+name);
        return name;
    }

    // query parameter
    // ? 로 쿼리 파라미터의 시작을 구분
    // & 로 여러 파라미터들끼리 구분
    // ?key1=value1&key2=value2&key3=value3

    // http://localhost:8080/api/get/query-param?user=steve&email=steve@gmail.com&age=30
    @GetMapping(path = "query-param")
    public String queryParam(@RequestParam Map<String, String> queryParam) {    // RequestParam 어노테이션으로 쿼리 파라미터 받음,
        // Map 으로 받으면 어떤 key 가 들어올 지 모르기 때문에 받아온 후 하나씩 지정해주어야 함
        StringBuilder sb = new StringBuilder();

        queryParam.entrySet().forEach( entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("\n");

            sb.append(entry.getKey()+" = "+entry.getValue()+"\n");
        });

        return sb.toString();
    }

    @GetMapping(path = "query-param2")
    public String queryParam2(
            // 어떤 파라미터를 받을 지 명시적으로 알려줄 수 있음
            @RequestParam String user,
            @RequestParam String email,
            @RequestParam int age
    ){
        System.out.println(user);
        System.out.println(email);
        System.out.println(age);

        return user+" "+email+" "+age;
    }

    @GetMapping(path = "query-param3")
    // 가장 많이 사용한다.
    public String queryParam3(UserRequest userRequest){     // 파라미터들을 미리 정의해둔 객체를 만들어 사용할 수 있음. 이 경우 RequestParam 어노테이션 필요 없음.
        System.out.println(userRequest.getUser());
        System.out.println(userRequest.getEmail());
        System.out.println(userRequest.getAge());

        return userRequest.toString();
    }
}

