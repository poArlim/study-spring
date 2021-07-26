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


