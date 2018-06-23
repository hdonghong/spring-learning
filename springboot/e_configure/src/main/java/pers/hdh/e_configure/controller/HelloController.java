package pers.hdh.e_configure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hdh.e_configure.config.ConfigurationPropertiesConfig;
import pers.hdh.e_configure.config.EnvConfig;
import pers.hdh.e_configure.config.MyConfig;
import pers.hdh.e_configure.config.ValueConfig;
import pers.hdh.e_configure.config.bean.MyBean1;

import java.util.HashMap;
import java.util.Map;

/**
 * HelloController class<br/>
 *
 * @author hdonghong
 * @date 2018/06/23
 */
@RestController
public class HelloController {

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private ValueConfig valueConfig;

    @Autowired
    private ConfigurationPropertiesConfig configurationPropertiesConfig;

    @Autowired
    private MyBean1 myBean1;

    @GetMapping("/getByEnvironment")
    public Map<String, Object> getByEnvironment() {

        Map<String, Object> map = new HashMap<>(16);
        map.put("server.port", envConfig.getServerPort());
        map.put("JAVA_HOME", envConfig.getJavahome());
        map.put("user.dir", envConfig.getUserdir());
        map.put("user.home", envConfig.getUserhome());

        return map;
    }

    @GetMapping("/getByValue")
    public Map<String, Object> getByValue() {

        Map<String, Object> map = new HashMap<>(16);
        map.put("user.name", valueConfig.getName());
        map.put("user.age", valueConfig.getAge());
        map.put("user.man", valueConfig.getMan());
        map.put("user.life-hobby", valueConfig.getLifeHobby());
        map.put("user.height", valueConfig.getHeight());

        return map;
    }

    @GetMapping("/getByConfigurationProperties")
    public Map<String, Object> getByConfigurationProperties() {

        Map<String, Object> map = new HashMap<>(16);
        map.put("ConfigurationProperties", "区别");
        map.put("user.name", configurationPropertiesConfig.getName());
        map.put("user.age", configurationPropertiesConfig.getAge());
        map.put("user.man", configurationPropertiesConfig.getMan());
        map.put("user.life-hobby", configurationPropertiesConfig.getLifeHobby());
        map.put("user.height", configurationPropertiesConfig.getHeight());

        return map;
    }

    @GetMapping("/myconfig")
    public void myconfig() {
        System.out.println(myBean1);
    }
}
