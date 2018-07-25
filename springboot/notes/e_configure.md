# SpringBoot2学习笔记（五）项目配置与部署


> 关于SpringBoot2配置，包括服务器配置、日志配置、配置信息读取以及自动装配。部署方面分jar包部署、war包部署以及多环境部署。

 **配置文件建议使用Spring官方推荐的yaml文件**

# 一、服务器配置

Spring Boot的Web服务器是tomcat，如果需要换端口有以下三种方式：

- 配置文件中写入： `server.port = port_number` 
- 启动时传入命令行参数： `java -jar demo.jar --server.port=port_number`
- 启动时传入虚拟机系统属性： `java -jar -Dserver.port=port_number demo.jar`

使用其它web服务器如 `jetty` 、`undertow` 时需要排除tomcat依赖：
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions>
		<exclusion>
			<artifactId>org.springframework.boot</artifactId>
			<groupId>spring-boot-starter-tomcat</groupId>
		</exclusion>
	</exclusions>
</dependency>
```

### 启动信息配置
启动时Spring Boot默认的欢迎信息可以修改，在 `classpath` (resources/)下加入banner文件即可，如：banner.txt、banner.jpg。

# 二、日志信息

日志在springboot的配置文件logging节点下，参考配置示例：
```
# Spring Boot使用LogBack作为日志实现，apache Commons Logging作为日志接口
logging:
# 日志级别，设置到包/类
  level:
    pers.hdh.e_configure: warn
  file: my.log
  pattern:
    console: '%date{HH:mm:ss} %level %logger{20}.%M %L :%m%n'
    file: '%date{ISO8601} %level [%thread] %logger{20}.%M %L :%m%n'
```
当然，这样的配置对于稍微复杂点的日志需求显然是满足不了的，最好还是在 `logback.xml`或 `logback-spring.xml` 下进行配置。（这里推荐一个日志插件 `Lombock` ，炒鸡方便）

# 三、配置信息读取

### Environment读取
Environment是Spring Boot最早初始化的一个类，可以读取application.properties，命令行输入参数，系统属性，操作系统环境变量等。示例代码如下：
```
@Configuration
public class EnvConfig {
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
```

### @Value读取
可以读取到Spring Boot配置文件中的信息，也可以读取到自定义的配置文件（但读取不到自定义yaml文件），同时支持SpEL表达式。

现在自定义一个 `myproperties.properties` 文件。
```
user.username = hdonghong
user.age = 32
user.man = true
user.life-hobby = programming
```

读取自定义配置文件时，使用 `@PropertySource` 注解指定文件位置。示例代码如下：
```
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
```

### @ConfigurationProperties读取

一组同样类型的配置信息可直接映射成一个类。示例代码如下：
```
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
```
**注意：使用@ConfigurationProperties时，需要引入spring-boot-configuration-processor，在idea中会提示Spring Boot Configuration Annotation Processor not found in classpath**

# 四、自动装配

Spring Boot的java配置核心是使用 `@Configuration`，并且联合在此类上多个用 `@Bean` 注解的方法。
例如，创建两个类 `MyBean1`、`MyBean2` ， `MyBean1`的创建依赖于 `MyBean2`。示例代码如下：
```
@Configuration
public class MyConfig {
    @Bean("myBean1")
    public MyBean1 getMyBean1(MyBean2 myBean2) {
        return new MyBean1(myBean2);
    }
}

/**********************即可在其中类中使用*********************/
@Autowired private　MyBean1 bean1;
```

是否装配可通过条件进行限定：

| 注解                     | 说明                                                         | 示例                                                         |
| ------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| @ConditionalOnBean       | Bean条件装配（存在Bean才装配；与之相反是@ConditionalOnMissingBean） | @ConditionalOnBean(value = {MyBean2.class})                  |
| @ConditionalOnClass      | Class条件装配（存在Class才装配；与之相反是@ConditionalOnMissingClass） | @ConditionalOnClass(value = {MyBean2.class})                 |
| @ConditionalOnProperty   | Enviroment装配（取yaml文件中的配置）。name指获取的值；havingValue指预期值，不指定时name值若为false会不装配；matchIfMissing默认false，指不符合预期值时是否装配。 | @ConditionalOnProperty(name = "author.name", havingValue = "hdonghong", matchIfMissing = false) |
| @ConditionalOnExpression | 当表达式为true时生效，支持SpEL表达式                         |                                                              |
| @ConditionalOnJava       | 当存在指定Java版本                                           | @ConditionalOnJava(range = ConditionalOnJava.Range.EQUAL_OR_NEWER, value = JavaVersion.EIGHT) |
| @Conditional             | 自定义装配条件                                               |                                                              |

现在来看下示例代码：
```
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
```
其中 `EncryptCondition.class` 是自定义的装配条件类，需要实现 `Condition` 接口并实现它的 `matches` 方法。具体代码可到下方获取我的源码查看，这里就不贴出来了。

# 五、项目部署

### jar包方式

Spring Boot默认是Jar包方式运行。
maven打包命令为：`mvn clean package -Dmaven.test.skip=true` 
jar运行方式为：`java -jar jar包名` （命令行进入jar包目录下）
linux中后台启动使用nohup命令：`nohup java -jar jar包名 > nohup.out 2>&1 &`

### war包方式
需要将pom的packing改成 `war`，并将嵌入的tomcat依赖改成 `provided` ，最后修改启动类，使其继承SpringBootServletInitializer类，重载configure方法：
```
// pom.xml中
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-tomcat</artifactId>
	<scope>provided</scope>
</dependency>

// 启动类
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
```

### 多环境部署

对于不同环境，通常采用不同配置，需要在resource下创建 `application-{profile}.yml` 的配置文件。

例如：
application-dev.yml：开发环境
application-prod.yml：生产环境

当线下变成代码时，在application.yml中指定为开发环境，上线项目时传入 `-Dspring.profiles.active=prod` 指定为生产环境即可。
示例：
```
// application.yml中
spring:
  profiles:
    active: dev

// 上线项目时
nohup java -jar -Dspring.profiles.active=prod 项目jar包 > nohup.out 2>&1 &
```

#### @Profile注解
@Profile注解可以结合@Configuration和@Component使用，以决定不同环境下那种配置生效。
示例代码如下：
```
@Configuration
public class DataSourceConf {
	@Bean(name = "dataSource")
	@Profile("dev")
	public DataSource testDatasource(Environment env) {
		HikariDataSource  dev = getDataSource(env);	
		test.setMaximumPoolSize(10);
		return dev;
	}
	@Bean(name = "dataSource")
	@Profile("prod")
	public DataSource prodSource(Environment env) {
		HikariDataSource  prod = getDataSource(env);
		prod.setMaximumPoolSize(100);
		return prod;
		
	}
	
	private HikariDataSource getDataSource(Environment env){
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(env.getProperty("spring.datasource.url"));
		ds.setUsername(env.getProperty("spring.datasource.username"));
		ds.setPassword(env.getProperty("spring.datasource.password"));
		ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		return ds;
	}
}
```
这里献上我部署项目用的shell：
```
#! /bin/sh

# 首先判断程序是否已经运行，运行则停止，重启
PID=$(ps -fu `whoami` |grep 项目的jar包|grep -v grep|awk '{print $2}')
if [ -z "$PID" ];then
  echo "not running"
else
  kill -9 "$PID"
fi

nohup java -jar -Dserver.port=8080 -Dspring.profiles.active=prod demo.jar > nohup.out 2>&1 &
echo "starting..."
for i in {1..4}
do
  sleep 5
  if [ ! -f "nohuo.out" ];then
    tail -n 1 ./nohup.out
  else
    echo '不存在nohup.out'
    break
  fi
done
```

# 六、最后
本次所有示例的代码：[传送门](https://github.com/hdonghong/spring-learning/tree/master/springboot/e_configure)

博客地址：https://blog.csdn.net/honhong1024/article/details/80785652