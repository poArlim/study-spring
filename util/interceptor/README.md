## Interceptor

인터셉터란 필터와 매우 유사한 형태로 존재하지만, 차이점은 Spring Context 에 등록된다. 

AOP 와 유사한 기능을 제공할 수있으며, 주로 인증 단계를 처리하거나 Logging 을 처리하는 데 사용한다. 

필터, 인터셉터에서 인증단계, 로깅을 처리하면 Service business logic 과 분리시킬 수 있다. 

<img width="762" alt="스크린샷 2021-07-25 오전 1 17 54" src="https://user-images.githubusercontent.com/43959582/126874663-1a83b32e-093e-47f1-9f49-7d86548cafba.png">
