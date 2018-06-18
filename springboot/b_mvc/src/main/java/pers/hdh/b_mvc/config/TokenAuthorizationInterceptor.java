package pers.hdh.b_mvc.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TokenAuthorizationInterceptor class<br/>
 * Token的验证拦截器
 * @author hdonghong
 * @date 2018/06/17
 */
public class TokenAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Token-Authorization");
        return token != null;
    }
}
