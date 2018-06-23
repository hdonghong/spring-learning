package pers.hdh.e_configure.config;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import pers.hdh.e_configure.config.bean.MyBean1;
import pers.hdh.e_configure.config.bean.MyBean2;
import pers.hdh.e_configure.config.condition.EncryptCondition;

/**
 * MyConfig class<br/>
 * Spring Boot的java配置核心是使用@Configuration，并且联合在此类上多个用@Bean注解的方法
 *
 * @author hdonghong
 * @date 2018/06/23
 */
@ConditionalOnBean(value = {MyBean2.class})// Bean条件装配（存在Bean才装配；相反的是@ConditionalOnMissingBean）
@ConditionalOnClass(value = {MyBean2.class})// Class条件装配（存在Class才装配；相反的是@ConditionalOnMissingClass）
@ConditionalOnProperty(name = "author.name", havingValue = "hdonghong", matchIfMissing = false) // Enviroment装配（取yaml文件中的配置）
@ConditionalOnExpression // 当表达式为true时生效，支持SpEL表达式
@ConditionalOnJava(range = ConditionalOnJava.Range.EQUAL_OR_NEWER, value = JavaVersion.EIGHT)// 当存在指定Java版本
@Conditional(EncryptCondition.class)
@Configuration
public class MyConfig {

    @Bean("myBean1")
    public MyBean1 getMyBean1(MyBean2 myBean2) {
        return new MyBean1(myBean2);
    }

}
