package pers.hdh.b_mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MvcConfigure class<br/>
 * WebMvcConfigure用来全局定制化Spring Boot的MVC特性。
 * @author hdonghong
 * @date 2018/06/17
 */
@Configuration
public class MvcConfigure implements WebMvcConfigurer {

    /** 拦截器 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenAuthorizationInterceptor())
                .addPathPatterns("/admin/**");
    }

    /** 跨域访问配置 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 这里匹配了所有的URL，允许所有的外域发起跨域请求，允许外域发起请求POST/GET HTTP Method，允许跨域请求包含任意的头信息。
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 格式化
     * Spring默认没有配置如何将字符串转为日期类型。
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    /** URI到视图的映射，不必要为了一个URL指定一个Controller方法 */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 将/index重定向到/index.html
        registry.addRedirectViewController("/index", "/index.html");
    }
}
