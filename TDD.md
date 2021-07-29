# TDD 
Test-driven Development 로 '테스트 주도 개발'을 뜻한다. 반복 테스트를 이용한 소프트웨어 방법론으로, 작은 단위의 테스트 케이스를 작성하고 이를 통과하는 코드르 추가하는 단계를 반복하여 구현한다. 코드의 유지 보수 및 운영 환경에서의 에러를 미리 방지하기 위해서 단위별로 검증과정을 거친다. 

## JUnit
Java 기반의 단위 테스트를 위한 프레임워크이다. Annotation 기반으로 테스트를 지원하고, Assert 를 통하여 (예상값, 실제값) 검증을 수행한다. 

- 사용을 위해선 dependencies 가 추가되어 있어야 한다.
```java
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.7.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.7.0'
}

test {
    useJUnitPlatform()
}
```

- @Test 를 붙여 단위테스트를 만든다.
```java
@Test
public void testHello(){
    System.out.println("hello");
}
```
- Assertion.assertEquals(예상값, 계산식)
```java
@Test
public void dollarTest() {
    MarketApi marketApi = new MarketApi();
    DollarCalculator dollarCalculator = new DollarCalculator(marketApi);
    dollarCalculator.init();

    Calculator calculator = new Calculator(dollarCalculator);

    Assertions.assertEquals(22000, calculator.sum(10, 10));   // 예상값과 계산식의 결과값이 같으면 테스트를 통과한다.
    Assertions.assertEquals(0, calculator.minus(10, 10));     // 예상값과 계산식의 결과값이 다르면 테스트를 실패하고 값을 알려준다.
}
```

### 모킹(Mocking) 
Mock Object(모의 객체) 란 주로 객체 지향 프로그래밍으로 개발한 프로그램을 테스트할 경우 테스트를 수행할 모듈과 연결되는 외부의 다른 서비스나 모듈들을 실제 사용하는 모듈을 사용하지 않고 실제의 모듈을 흉내내는 가짜 모듈을 작성하여 테스트의 효용성을 높이는 데 사용하는 객체이다. 사용자 인터페이서(UI)나 데이터베이스 테스트 등과 같이 자동화된 테스트를 수행하기 어려운 때 널리 사용된다. 모의 객체를 이용하면 상당 부분의 테스트를 사용자의 개입 없이 자동화할 수 있다.

- dependencies 를 추가해야 한다.
```java
dependencies{
    testImplementation group: 'org.mockito', name: 'mockito-core', version: '3.11.2'
    testImplementation group: 'org.mockito', name: 'mockito-junit-jupiter', version: '3.10.0'
}
```
- MarketApi 를 실제로 불러오는 과정을 생략하고 예상값(1100) 으로 테스트 실행
```java
@ExtendWith(MockitoExtension.class)
public class DollarCalculatorTest {

    @Mock
    public MarketApi marketApi;

    @BeforeEach   // 테스트가 실행되기 이전에
    public void init(){
        Mockito.lenient().when(marketApi.connect()).thenReturn(1100);
        // marketApi.connect() 가 호출될 때 1100 을 리턴한다.
    }
    
    @Test
    // 실제 테스트에서는 Mock 객체(marketApi) 를 사용하여 테스트를 진행한다.
}
```


### Spring 테스트 
spring 에서 테스트 할 때에는 컴포넌트들이 Bean 으로 관리된다는 차이점이 있다.

- 통합테스트 - @SpringBootTest 를 붙여주면 모든 bean 이 다 등록되어 사용할 수 있게 된다.
```java
@SpringBootTest
public class DollarCalculatorTest{
    
    @MockBean   // bean 으로 관리되기 때문에 MockBean
    private MarketApi marketApi;
    
    @Autowired
    private Calculator calculator;
    
    ...
}
```


- 단위테스트 - @WebMvcTest() 하고 필요한 것들만 Import 해서 사용 
```java
@WebMvcTest(CalculatorApiController.class)  // 테스트 할 컨트롤러
@AutoConfigureWebMvc
@Import({Calculator.class, DollarCalculator.class})
public class CalculatorApiControllerTest {

	@MockBean
	public MarketApi marketApi;

	@Autowired
	private MockMvc mockMvc;	// mvc 를 모킹으로 테스트하겠다.

	@BeforeEach             
	public void init(){
		Mockito.when(marketApi.connect()).thenReturn(1100);
	}
	
	@Test
	public void sumTest() throws Exception{
		// http://localhose:8080/api/sum 을 테스트할거야

		mockMvc.perform(
			MockMvcRequestBuilders.get(“http://localhost:8080/api/sum”) // post, put, delete 메서드 넣어서 테스트 가능
				.queryParam(“x”, “10”)
				.queryParam(“y”, “10”)
		).andExpect(				// status 의 기대값 isOk
			MockMvcResultMatchers.status().isOk()
		).andExpect(				// content 기대값 22000
			MockMvcResultMatchers.content().string(“22000”)
		).andDo(MockMvcResultHandlers.print());		// 테스트 결과 출력
	}
}
```

## Jacoco 
Java 코드의 테스트 커버리지를 체크하는 라이브러리로 결과를 html, xml, csv 로 확인 가능하다. (테스트 해야 하는 항목을 다 테스트 했는지를 체크해준다.) 

- build.gradle 에 플러그인 추가
```java
plugins {
    ...
    id 'jacoco'
}
```


- gradle 의 verification 아래 test 를 클릭하여 실행
- 결과 : build/reports/jacoco/.../index.html 파일을 열어 확인
