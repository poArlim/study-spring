package com.example.interceptor.config;

import com.example.interceptor.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 인터셉터 등록해주기
@Configuration
@RequiredArgsConstructor        // final 로 생성된 객체들을 생성자에서 주입받을 수 있도록 해준다. @Autowired 로 받을수도 있지만 순환참조가 생길 수 있다.
public class MvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/private/*");   // authInterceptor 등록, private 으로 들어오는 것만 검사하겠다. public 은 검사하지 않음.
        // addPathPatterns("..,", ",,,",",..") 으로 여러개 넣을 수 있고, 몇 개만 제외시키고 싶으면 excludePathPatterns 사용하면 됨.
    }
}
