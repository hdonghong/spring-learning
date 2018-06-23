package pers.hdh.e_configure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * ConfigurationPropertiesConfig class<br/>
 *
 * @author hdonghong
 * @date 2018/06/23
 */
@Data
@PropertySource("classpath:myproperties.properties")
@ConfigurationProperties("user")
@Configuration
public class ConfigurationPropertiesConfig {

    private String name;

    private Integer age;

    private Boolean man;

    private String lifeHobby;

    private Double height;
}
