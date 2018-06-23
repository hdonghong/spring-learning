package pers.hdh.e_configure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hdonghong
 * @date 2018/06/23
 */
@SpringBootApplication
@RestController
public class EConfigureApplication {

	public static void main(String[] args) {
		SpringApplication.run(EConfigureApplication.class, args);
	}

}

// 如果使用war方式打包，需要显示的引入tomcat依赖并将作用域设置为provided。
// 同时还要修改启动类，使其继承SpringBootServletInitializer类，重载configure方法
/*
@SpringBootApplication
public class EConfigureApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(EConfigureApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(EConfigureApplication.class, args);
	}

}
*/