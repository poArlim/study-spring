## Validation

프로그램의 오류를 방지하기 위해서 미리 검증하는 과정으로 매우 중요한 부분이다. 단순하게는 아래와 같은 코드들을 말한다.


```java
public void run(String account, Spring pw, int age){
    if(account == null || pw == null){
        return 
    }
    if(age == 0) {
        return
    }
    // 정상 Logic
}
```

이처럼 처리하게 될 경우 아래와 같은 불편함이 있을 수 있다. 

- 검증해야 할 값이 많은 경우 코드의 길이가 길어진다.
- Service Logic 과의 분리가 필요하다.
- 흩어져 있는 경우 어디에서 검증을 하는지 알기 어려우며, 재사용의 한계가 있다.
- 검증 Logic 이 변경되는 경우 참조하는 클래스에서 Logic 의 변경이 필요해질 수 있다.


Spring 에서는 여러 편의를 위해 검증 과정을 아래의 여러 Annotation 기반으로 제공한다. 
> gradle dependecies 추가 : implementation("org.springframework.boot:spring-boot-starter-validation")

||||
|---|---|---|
|@Size|문자 길이 측정|Int Type 불가|
|@NotNull|null 불가||
|@NotEmpty|null, "" 불가||
|@NotBlank|null, "", " " 불가||
|@Past|과거 날짜||
|@PastOrPresent|오늘이거나 과거 날짜||
|@Future|미래 날짜||
|@FutureOrPresent|오늘이거나 미래 날짜||
|@Pattern|정규식 적용||
|@Max|최대값||
|@Min|최소값||
|@AssertTrue/False|별도 Logic 적용||
|@Valid|해당 object validation 실행||


#### 사용

- 형식에 맞는지 검증 후 맞지 않으면 에러를 뱉음 
```java
@Email
private String email;   // email 형식으로 들어와야 함 
```

- 사용하는 데에서 @Valid 를 붙여주어야 함
```java
public ResponseEntity user(@Valid @requestBody User user){
    // 매개변수로 들어오는 requestBody 에 대해서 validation 을 수행함.
}
```

- 정규식 사용 가능
```java
@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
private String phoneNumber;
// 모든 validation annotation 은 message 로 에러시 뱉을 메시지 설정이 가능함.
```

- 검증 오류 시 예외를 던지는 게 아니라 결과를 받아서 직접 처리해줄 수 있다.
```java
public ResponseEntity user(@Valid @requestBody User use, BindingResult bindingResult){
    //BindingResult 를 넣어주면 Valid 결과가 예외가 터지는 게 아니라 bindingResult 로 들어온다.
    if (bindingResult.hasErrors()){
        bindingResult.getAllErrors().forEach(objectError -> {
            FieldError filed = (FieldError) objectError;
            String message = objectError.getDefaultMessage();
            System.out.println(field.getField() + message);
    })}
    
}
```

- validation 을 직접 만들 수 있다. 
```java
@Size(min = 6, max = 6)
private String reqYearMonth;  // yyyymm

@AssertTrue   // return 이 true 가 되면 정상 false 면 비정상
public boolean isReqYearMonthValidation(){    
    try {
        LocalDate localDate = LocalDate.parse(getReqYearMonth()+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
    } catch (Exception e){
        return false;
    }
    return true;
}
```
- AssertTrue 를 이용한 방법은 재사용이 힘들다.
- 아래와 같이 만들면 재사용이 가능하다.

> YearMonth 어노테이션 파일
```java
@Constraint(validateBy = {YearMonthValidator.class})    // 어느 클래스를 가지고 검사를 할 건지 넘겨준다.
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CNOSTRUCTOR, PARAMETER, TYPE_USE }) // 검사할 타겟
@Retention(RUNTIME) 
public @interface YearMonth {
    String message() default "yyyyMM 형식에 맞지 않습니다.";
    ...
}
```
> 활용하는 부분 YearMonthValidator 클래스
```java
public class YearMonthValidator implements ConstraintValidator<YearMonth, String>{  // <사용할 annotation, 들어갈 값>
    private String pattern;
    
    @Override
    public void initialize(YearMonth constraintAnnotation){
        this.pattern = constraintAnnotation.pattern();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        // yyyyMM
        try{
            LocalDate localDate = LocalDate.parse(value+"01", this.pattern("yyyyMMdd"));
        } catch (Exception e){
            return false;
        }
        
        return true;
    }
}
```


## Exception 처리
- ControllerAdvice : Global 예외 처리 및 특정 package / Controller 예외처리
- ExceptionHandler : 특정 Controller 의 예외처리 

> RestControllerAdvice -> 글로벌한 예외를 다 잡을 수 있다.
```java
@RestControllerAdvice
public class GlobalControllerAdvice{

	@ExceptionHandler(value = Exception.class)	// 전체 예외를 다 잡을거다
	public ResponseEntity exception(Exception e){
		System.out.println(e.getClass().getName());
		System.out.println(e.getLocalizedMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(“”);
	}

	// 이 에러는 따로 여기서 잡는다. 위에꺼는 통과
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}
```

> 특정 위치(패키지) 아래에서만 예외를 잡고 싶다면
```java
@RestControllerAdvice(basePackages = “com.example.exception.controller”)  // controller 패키지 아래에서만 동작
```

> 특정 Controller 의 예외처리
```java
// Controller 안에 따로 떼어서 넣으면 얘가 적용됨.
@ExceptionHandler(value = MethodArgumentNotValidException.class)
public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e){
  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
}
```

## Lombok 롬복

자바에서 사용하는 라이브러리로 get/set/기본생성자 등을 쉽게 생성해 주는 등 여러 기능을 제공한다. 

|||
|---|---|
|@Getter|getter|
|@Setter|setter|
|@Data|getter, setter, toString 등|
|@NoArgsConstructor|기본생성자|
|@AllArgsConstructor|모든 매개변수가 들어가는 생성자|
|@Slf4j|로그(log.info)|

```java
@Data                 // get, set, toString, ...
@NoArgsConstructor    // User()
@AllArgsConstructor   // User(String, int)
public class User{
    private String name;
    private int age;
}
```
```java
@Slf4j
@RestController
@RequestMapping("/api/user")
public class ApiController{
    @PostMapping("")
    public User user(@RequestBody User user){
        log.info("User : {}", user);
        return user;
    }
}
```


## Filter 필터 

Web Application 에서 관리되는 영역으로써 Spring Boot Framework 에서 Client로부터 오는 요청/응답에 대해서 최초/최종 단계의 위치에 존재하며, 이를 통해서 요청/응답의 정보를 변경하거나, Spring에 의해서 데이터가 변환되기 전의 순수한 Client의 요청/응답 값을 확인할 수 있다.

유일하게 ServletRequest, ServletResponse 의 객체를 변환할 수 있다. 주로 request/response 의 logging 용도로 활용하거나, 인증과 관련된 로직들을 필터에서 처리한다. 이러한 과정들을 선/후 처리함으로써, Service business logic 과 분리시킨다.

<img width="595" alt="스크린샷 2021-07-25 오전 1 07 08" src="https://user-images.githubusercontent.com/43959582/126874368-4488e0d4-b7b4-4406-b521-f6043403374c.png">

```java
@WebFilter(urlPatterns = "/api/user/*")   // 필터를 걸어줄 url 을 설정
public class GlobalFilter implements Filter {   // Filter 를 구현
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
        // 전처리
        
        chain.doFilter(request, response);
        
        // 후처리
    }
}
```

doFilter 를 통해 request, response, chain 이 들어온다. chain.doFilter(..) 앞에 전처리, 뒤에 후처리 코드를 넣어주면 필터를 적용할 수 있다.

- 작성한 필터를 적용할 때는 SpringBootApplicatoin 에 @ServletComponentScan 어노테이션을 걸어준다.












