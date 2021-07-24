package com.example.interceptor.interceptor;

import com.example.interceptor.annotation.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 필터딴에서 req, res 를 ContentCachingRequestWrapper, .. 로 변경해서 chain.doFilter() 에 넣어주었다면 인터셉터에서 형변환이 가능하다.
        // (ContentCachingRequestWrapper)request;
        String url = request.getRequestURI();

        URI uri = UriComponentsBuilder.fromUriString(request.getRequestURI())
                .query(request.getQueryString())
                .build()
                .toUri();


        log.info("request url : {}", url);
        boolean hasAnnotation = checkAnnotation(handler, Auth.class);
        log.info("has annotation : {}", hasAnnotation);

        // 나의 서버는 모두 public으로 동작을 하는데
        // 단 Auth 권한을 가진 요청에 대해서는 세션, 쿠키,
        if(hasAnnotation){
            // 권한체크
            String query = uri.getQuery();
            if(query.equals("name=steve")){
                return true;
            }

            throw new AuthException();      // 권한이 없으면 AuthException 을 터티리고 이거는 handler 에서 관리
       }


        return true;       // return false 이면 동작하지 않는다.
    }

    private boolean checkAnnotation(Object handler, Class clazz){
        // resource javascript, html 등 리소스에 대한 요청일 때는 무조건 통과시켜준다.
        if( handler instanceof ResourceHttpRequestHandler){
            return true;        // true 는 무조건 통과
        }

        // annotation check
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(null != handlerMethod.getMethodAnnotation(clazz) || null != handlerMethod.getBeanType().getAnnotation(clazz)){
            return true;        // Auth 어노테이션이 달려있는 경우. not null 인경우
        }

        return false;       // 나머지 경우에는 모두 False

    }
}
