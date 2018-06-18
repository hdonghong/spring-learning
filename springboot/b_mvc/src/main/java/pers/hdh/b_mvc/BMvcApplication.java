package pers.hdh.b_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author hdonghong
 * @date 2018/06/17
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BMvcApplication.class, args);
	}
}
