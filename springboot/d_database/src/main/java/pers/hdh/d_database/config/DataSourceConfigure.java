package pers.hdh.d_database.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * DataSourceConfigure class<br/>
 * java中配置数据源，以此为准。在配置文件中配置的优先级低于Java配置
 * @author hdonghong
 * @date 2018/06/19
 */
@Configuration
public class DataSourceConfigure {

    @Autowired
    private Environment env;

    private static final String JDBC_URL;
    private static final String DRIVER_CLASS;
    private static final String USERNAME;
    private static final String PASSWORD;


    static {
        JDBC_URL = "spring.datasource.url";
        DRIVER_CLASS = "com.mysql.jdbc.Driver";
        USERNAME = "spring.datasource.username";
        PASSWORD = "spring.datasource.password";
    }

    @Bean(name = "datasource")
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl(env.getProperty(JDBC_URL));
        dataSource.setDriverClassName(env.getProperty(DRIVER_CLASS));
        dataSource.setUsername(env.getProperty(USERNAME));
        dataSource.setPassword(env.getProperty(PASSWORD));
        return dataSource;
    }
}
