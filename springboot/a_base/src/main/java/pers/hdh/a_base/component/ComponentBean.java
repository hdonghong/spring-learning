package pers.hdh.a_base.component;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * ComponentBean class<br/>
 *
 * @author hdonghong
 * @date 2018/06/17
 */
@Component
public class ComponentBean {

    @PostConstruct
    public void init() {
        System.out.println("当Bean被容器初始化后，会调用@PostConstruct的注解方法");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("在容器被销毁之前，调用被@PreDestroy注解的方法");
    }
}
