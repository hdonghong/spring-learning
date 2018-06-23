package pers.hdh.e_configure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * ValueConfig class<br/>
 *
 * @author hdonghong
 * @date 2018/06/23
 */
@Data
@Component
@PropertySource("classpath:myproperties.properties")
public class ValueConfig {

    @Value("${user.username}")
    private String name;

    @Value("${user.age}")
    private Integer age;

    @Value("${user.man}")
    private Boolean man;

    @Value("${user.life-hobby}")
    private String lifeHobby;

    /** 支持SpEL表达式。例如这里，如果属性不存在则设为180.1 */
    @Value("${user.height:180.1}")
    private Double height;

}
