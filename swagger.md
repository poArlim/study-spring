# Swagger 
Swagger 란 개발한 REST API 를 편리하게 문서화 해주고, 이를 통해서 관리 및 제 3의 사용자가 편리하게 API 를 호출해보고 테스트할 수 있는 프로젝트이다. 서버의 API 를 외부, 내부 사용자에게 문서를 주는 것이 아니라 swagger UI 를 제공해서 직접 테스트해 볼 수 있게 한다. 

Spring Boot 에서는 간단하게 springfox-boot-starter 를 gradle dependencies 에 추가함으로 사용할 수 있다. 다만, 운영환경과 같은 외부에 노출되면 안 되는 곳에서 사용할 때는 주의해야 한다.

- 서버주소/swagger.ui/ 로 접속하여 확인할 수 있다. (ex. localhost:8080/swagger.ui/)


- Swagger Annotation 

| Annotation ||
| --- | --- |
|@Api|클래스를 스웨거의 리소스로 표시|
|@ApiOperation|특정 경로의 오퍼레이션 HTTP 메소드 설명|
|@ApiParam|오퍼레이션 파라미터에 메타 데이터 설명|
|@ApiResponse|오퍼레이션의 응답 지정|
|@ApiModelProperty|모델의 속성 데이터를 설명|
|@ApiImplicitParam|메소드 단위의 오퍼레이션 파라미터를 설명|
|@ApiImplicitParams||


```java
@Api(tags={"API 정보를 제공하는 Controller"})
@RestController
@RequestMapping("/api")
public class ApiController{
  
    @GetMapping("/plus/{x}")
    public int plus(    // x, y 값에 대한 설명, 기본값 등 여러 속성을 설명해줄 수 있음
            @ApiParam(value="x값", defaultValue="20") @PathVariable int x,
            @ApiParam(value="y값", defaultValue="5") @RequestParam int y
    ){
        return x+y;
    }
  
    @ApiImplicitParams ({   // 메소드 단위로 매개변수를 배열형태로 지정해서 설명해줄 수 있음
            @ApiImplicitParam(name = “x”, value = “x 값”, defualtValue = 20), 
            @ApiImplicitParam(name = “y”, value = “y 값”, defualtValue = 5)
    })
    @GetMapping(“/plus/{x}”)
    public int plus(@PathVariable int x, @RequestParam int y){
        return x+y;
    }
    
    @ApiOperation(value = "사용자의 이름과 나이를 리턴하는 메서드")
    @GetMapping("/user")
    public UserRes user(UserReq userReq){
        return new UserRes(userReq.getName(), userReq.getAge());
    }
}

public class UserReq {
	  @ApiModelProperty(value = “사용자의 이름”, example = “steve”, required = true)
    private String name;

    @ApiModelProperty(value = “사용자의 나이”, example = “10”, required = true)
    private int age;
}
```
