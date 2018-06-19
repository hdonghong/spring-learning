package pers.hdh.d_database;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @author hdonghong
 * @date 2018/06/19
 */
@MapperScan(basePackages = {"pers.hdh.d_database.mapper"})
@SpringBootApplication
public class DDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DDatabaseApplication.class, args);
	}

//  Method threw 'org.hibernate.LazyInitializationException' exception. Cannot evaluate pers.hdh.d_database.dataobject.User.toString()
	@Bean
	public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
		return new OpenEntityManagerInViewFilter();
	}
}
