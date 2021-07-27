# Server to Server 연동 

back-end 에서 다른 server 와의 연동은 필수이다. RestTemplate 을 사용하여 다른 server 와의 연동하는 방법을 공부해보자. 



### 파일설명
- client/ : client 역할을 하는 프로젝트 8080포트에서 수행
  - ApiController : http 메서드가 8080포트로 들어오면 restTemplateService 를 통해 mapping 된 메서드 실행
  - RestTemplateService : ApiController 에서 호출. UriComponentBuilder 를 통해 header, path parameter, query parameter 등을 추가하여 uri 를 만들고 post 메서드의 경우 body 를 만들어 request 를 만들어 서버로 전송하고 서버에서 돌아온 response 를 받아서 반환해준다.
  - UserRequest/UserResponse : request, response 를 위한 클래스
  - Req : header가 정해져있고 body 에 여러 자료가 들어올 수 있는경우 제네릭 타입으로 선언하여 다루기 위한 클래스. RestTemplateService.java 의 genericExchange() 메서드에 구현되어 있다.


- server/ : server 역할을 하는 프로젝트 9090포트에서 수행
  - ServerApiController : client 에서 보낸 request 를 받아서 그대로 user 정보를 다시 response 로 돌려준다.


## NAVER API 연결하기 

우선 API key 를 발급받아야 한다.

```java
@GetMapping("/naver")
public String naver(){
    String query = "맛집";  // 맛집리스트를 찾아보자

    URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com") // 네이버 open api 의
            .path("/v1/search/local.json")  // 지도검색을 이용하고
            .queryParam("query", query) // 맛집을 검색어로 던진다.
            .queryParam("display", 10)
            .queryParam("start", 1)
            .queryParam("sort", "random")
            .encode(Charset.forName("UTF-8")) 
            .build()
            .toUri();

    RestTemplate restTemplate = new RestTemplate();

    RequestEntity<Void> req = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", "")  // 발급받은 API key 의 client-id
            .header("X-Naver-Client-Secret", "")  // 발급받은 API key 의 client-secret
            .build();

    ResponseEntity<String> result = restTemplate.exchange(req, String.class); // 맛집리스트가 response 로 String 으로 들어온다.

    return result.getBody();
}
```
