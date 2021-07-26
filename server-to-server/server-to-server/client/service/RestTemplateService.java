package com.example.client.service;

import com.example.client.dto.Req;
import com.example.client.dto.UserRequest;
import com.example.client.dto.UserResponse;
import org.apache.catalina.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class RestTemplateService {

    // http://localhost/api/server/hello 를 호출해서
    // response 를 받아올거임

    public String hello(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/hello")
                // path 에는 다른 여러 옵션을 붙여 보낼 수 있음
                .queryParam("name", "steve")
                .queryParam("age", 10)
                // 이렇게
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        // uri 에 요청을 시키고 결과를 스트링으로 받을거다.
        // getForObject 에 get 은 가져오는 get 이 아니라 get 메서드를 말함.
        // getForObject 가 실행되는 순간이 클라이언트에서 서버로 http 로 붙는 순간.
        // 그냥 String 으로 response 가 오니까 getForObject 로 받았고 제대로 여러 정보를 받으려면

        ResponseEntity<String> result1 = restTemplate.getForEntity(uri, String.class);
        // ResponseEntity<Type> 에, getForEntity 로 받아주면 된다.

        System.out.println(result1.getStatusCode());
        System.out.println(result1.getBody());
        // 이런 정보들까지 다 들어있다.

        ResponseEntity<UserResponse> result2 = restTemplate.getForEntity(uri, UserResponse.class);
        // json 형태로 받을 수 있다. 이러면 이제 return result2.getBody(); 를 해야하니까 클래스의 타입, 컨트롤러의 getHello() 메서드 타입도 바꿔줘야 함.

        return result;
    }

    public UserResponse post(){

        // http://localhost:9090/api/server/user/{userId}/name/{userName}

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve")    // 순서대로 pathVariable 에 매칭됨. userId, userName 에 매칭.
                .toUri();
        System.out.println(uri);

        // post 이기 때문에 http body 가 있어야 한다.
        // object 로 보내면 -> object mapper 가 -> json 으로 바꿔서 -> rest template 에서 -> http body 에 json 으로 넣어줄거다.
        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);
        // 내가 보낼 데이터 json 이 아니라 object 로 만들어서 보내면
        // object mapper 가 알아서 json 으로 만들어서 보내줄거임.
        // 이 object(=request) 를 보낼거다.

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(uri, req, UserResponse.class);
        // uri 에다가 req body 를 만들어서 보내고 response 형태는 UserResponse.class 임.
        // post 방식
        // 나는 응답을 뭘로 받을지만 지정하면 됨. ResponseEntity<UserResponse> 로 받을거다.

        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());

        return response.getBody();
    }

    public UserResponse exchange(){ // header 넣어서 보내기
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve")
                .toUri();
        System.out.println(uri);

        UserRequest req = new UserRequest();
        req.setName("steve");
        req.setAge(10);

        // 요청을 보낼 때 requestEntity 를 만들어 보낼거임
        RequestEntity<UserRequest> requestEntity = RequestEntity
                .post(uri)  // post 방식으로 uri 를 보낼거다.
                .contentType(MediaType.APPLICATION_JSON)
                .header("x_authorization", "abcd")
                .header("custom-header", "fffff")
                .body(req);
        // header 와 body 에 넣을 값들을 지정하여 만든다.

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserResponse> response = restTemplate.exchange(requestEntity, UserResponse.class);
        // requestEntity 를 보내고 UserResponse.class 타입으로 받음

        return response.getBody();
    }

    public Req<UserResponse> genericExchange(){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9090")
                .path("/api/server/user/{userId}/name/{userName}")
                .encode()
                .build()
                .expand(100, "steve")
                .toUri();
        System.out.println(uri);

        // Req 는 Req 타입으로 헤더에 가변하는 바디 붙어서 갈거임
        UserRequest userRequest = new UserRequest();
        userRequest.setName("steve");
        userRequest.setAge(10);

        Req<UserRequest> req = new Req<UserRequest>();   // request의 body 의 제네릭타입에 UserRequest 타입으로 보낼거임
        req.setHeader(
                new Req.Header()
        );

        req.setBody(
                userRequest
                // 바디는 위에서 만든 userRequest
        );

        // response 받을 타입도 Req 가 감싸고 있는 UserRequest 로 바꿔준다.
        RequestEntity<Req<UserRequest>> requestEntity = RequestEntity
                .post(uri)  // post 방식으로 uri 를 보낼거다.
                .contentType(MediaType.APPLICATION_JSON)
                .header("x_authorization", "abcd")
                .header("custom-header", "fffff")
                .body(req);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Req<UserResponse>> response =
                restTemplate.exchange(requestEntity, new ParameterizedTypeReference<Req<UserResponse>>() {});
        // requestEntity 를 보내고 response 는 Req<UserResponse> 타입으로 받을건데 제네릭일때는 .class 를 못써서 이렇게 해줘야 함.

        return response.getBody();
    }
}
