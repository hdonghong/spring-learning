package pers.hdh.e_configure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * EnvConfig class<br/>
 * 通用的Environment类获取
 *
 * @author hdonghong
 * @date 2018/06/23
 */
@Configuration
public class EnvConfig {

    /** Environment是Spring Boot最早初始化的一个类，可以读取application.properties，命令行输入参数，系统属性，操作系统环境变量等 */
    @Autowired
    private Environment environment;

    /** 可获取yml配置文件中的属性 */
    public Integer getServerPort() {
        return environment.getProperty("server.port", Integer.class);
    }

    /** 可获取环境变量 */
    public String getJavahome() {
        return environment.getProperty("JAVA_HOME", String.class);
    }

    /** 可获取系统属性，如程序运行的目录 */
    public String getUserdir() {
        return environment.getProperty("user.dir");
    }

    /** 可获取系统属性，如执行程序的用户的home目录 */
    public String getUserhome() {
        return environment.getProperty("user.home");
    }
}
