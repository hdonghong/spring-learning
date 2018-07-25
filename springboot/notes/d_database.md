# SpringBoot2学习笔记（四）JPA与Mybatis术



> 访问数据库的方式一般来说有两种，一种以Java Entity为中心，将实体和实体关系对应到数据库的表和表关系，例如Hibernate框架(Spring Data JPA由此实现)；另一种以原生SQL为中心，更加灵活便捷，例如Mybatis。
>  本篇要讲数据源配置，接着重点介绍下Spring Data JPA技术，最后讲下Spring Boot集成Mybatis。

# 一、配置Spring Data JPA

pom文件引入Spring Data JPA的依赖，选用mysql驱动包和druid作为数据源
```
<!-- jpa -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- mysql驱动 -->
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
</dependency>

<!-- Druid数据源，是第三方数据源。SpringBoot2默认HikariCP数据源，区别1版本用的Tomcat -->
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.1.6</version>
</dependency>
```

接着配置文件进行数据源的配置，这里官方推荐使用yml后缀文件替代properties后缀文件。示例配置如下：
```
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=false
    username: root
    password: toor
    driver-class-name: com.mysql.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
  jpa:
    show-sql: true
    open-in-view: true
```
**这里注意，spring.jpa.open-in-view设置为true是为了解决懒加载时加载完以后Session关闭导致的No Session异常。**
**同时还要加入一个过滤器，设置为Bean，代码如下**
```
@Bean
public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
	return new OpenEntityManagerInViewFilter();
}
```

此外，也可以通过Java代码来配置数据源。示例代码如下：
```
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
```

# 二、准备数据库与实体类
数据库
```
## 部门表
CREATE TABLE `department` (
  `department_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`department_id`)
);

## 用户表
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `department_id` int(11) DEFAULT NULL,
  `gmt_create` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  KEY `fk_department_id` (`department_id`),
  CONSTRAINT `fk_department_id` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE SET NULL ON UPDATE NO ACTION
);
```
实体类
```
// 部门实体
@Entity
public class Department implements Serializable {

    private static final long serialVersionUID = -8263045269071570448L;

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    /**
     * 一对多，一方默认是LAZY
     * mappedBy这里以声明Many端的对象(User实体)的department属性提供了对应的映射关系
     * yml文件需要保持session的开启状态，spring.jpa.open-in-view=true
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> users = new HashSet<>();
	
	// 省略getter/setter
}

// 用户实体
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 5608542878548547190L;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    /** 用户与部门 多对一，多方默认是EAGER */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

	// getter/setter
}
```
JPA注解简介：

| JPA注解         | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| @Entity         | 标记为实体类                                                 |
| @Table          | 对应表名，不写默认为类名，首字母小写                         |
| @Id             | 声明一个属性映射到主键的字段                                 |
| @GeneratedValue | 设定主键生成策略，自增可用AUTO或IDENTITY                     |
| @Column         | 表明属性对应到数据库的一个字段。                             |
| @ManyToOne      | 多对一，默认fetch = FetchType.EAGER                          |
| @JoinColumn     | 与@ManyToOne搭配，指明外键字段                               |
| @OneToMany      | 一对多，mappedBy这里以声明Many端的对象(User实体)的属性(department)提供了对应的映射关系 |

# 三、Repository

Repository是Spring Data的核心概念。提供有如下接口：

 - CrudRepository：提供基本的增删该查，批量操作接口。
 - PagingAndSortingRepository：集成CrudRepository，提供附加的分页查询功能。
 - JpaRepository：专用于JPA，是重点。

```
public interface UserRepository extends JpaRepository<User, Integer>{
	// 不需要我们编写实现
}
```

### 1.JpaRepository
这些接口不需要我们编写实现，Spring Boot底层采用Hibernate实现。现在主要来看JpaRepository的部分用法。

Jpa提供了很多方法，例如：`findAll()` ：返回一个集合，当元素为空时，返回空集合。示例代码如下：
```
// 查询所有用户
List<User> userList = userRepository.findAll();
```

如果想做分页查询可通过 `findAll(Pageable pageable)` ，利用Pageable进行分页查询。构造Pageable可使用其实现类PageRequest，Sort用来指定排序方式。示例代码如下：
```
// 分页查询第一页用户，限制每页10条记录，按创建时间降序排序
int page = 0, size = 10;
PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("gmtCreate")));
Page<User> userPage = userRepository.findAll(pageRequest);
```

如果即想做分页查询又需要条件查询，可以再实现接口 `JpaSpecificationExecutor<T>` ，使用 `Specification` 设置条件。示例代码如下：
```
// 分页查询第一页用户，限制每页10条记录，按创建时间降序排序，同时要求用户的id大于2
PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("gmtCreate")));
Specification<User> specification = (root, query, builder) -> {
    /*
        root：实体对象引用，这里是user
        query：规则查询对象
        builder：规则构建对象
     */
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(builder.greaterThan(root.get("id").as(Integer.class), 2));
    return builder.and(predicates.toArray(new Predicate[predicates.size()]));
};
userPage = userRepository.findAll(specification, pageRequest);
```

### 2.基于方法名字查询
Spring Data 通过查询的方法名和参数名来自动构造一个JPA OQL查询。也就是说，我们可直接在自己的Repository中按照Spring Data 的规则定义方法，有Spring Data帮我们实现。
示例代码如下：
```
/**
 * 根据用户名模糊查询
 * @param name 需要包含%或者?
 * @return userList
 */
List<User> findByNameLike(String name);
```

下面附上Spring Data支持的关键字：（[官方地址](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation)）
![这里写图片描述](https://img-blog.csdn.net/20180619214711807?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hvbmhvbmcxMDI0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


### 3.@Query查询

使用 `@Query` 注解标记在Repository方法上可以使用JPQL进行查询，示例代码如下：
```
@Query(value = "select u from User u where u.id = ?1")
User getOneByJpql(Integer id);
```

也可以使用原生SQL查询，不过要把设置 `nativeQuery = true`，示例代码如下：
```
@Query(value = "select * from user where user_id =:id", nativeQuery = true)
User getOneBySql(@Param("id") Integer id);
```
**注意：Idea中使用JPA原生sql查询时需要配置好Database的数据源，指定数据库方言，指定Schema。**
	否则可能遇到异常报错如：
	SQL dialect is not configured
	This inspection performs unresolved SQL references check

现在重新来看上面分页查询加条件查询，利用@Query是如何轻松的解决的。示例代码如下：
```
@Query(value ="select u from User u where u.id > :id")
Page<User> queryUsers(@Param("id") Integer id, Pageable pageable);

// 方法中调用查询
Page<User> userPage = 
		userRepository.queryUsers(2, PageRequest.of(0, 10, Sort.by(Sort.Order.desc("gmtCreate"))));
```

@Query允许SQL更新、删除语句，但必须搭配 `@Modifying` 使用。示例代码如下：
```
@Modifying
@Query(value = "update User u set u.name = ?1 where u.id = ?2")
int update(String name, Integer id);
```

### 4.EntityManager
这个还不熟悉，留着以后补充。

# 四、集成Mybatis

pom文件引入mybatis支持：
```
<dependency>
	<groupId>org.mybatis.spring.boot</groupId>
	<artifactId>mybatis-spring-boot-starter</artifactId>
	<version>1.2.2</version>
</dependency>
```

配置文件指定mapper文件所在处，例如我的：
```
mybatis:
  mapper-locations: classpath:mapper/*Mapper.xml
```

启动类指定mapper java文件所在包，例如我的：
```
@MapperScan(basePackages = {"pers.hdh.d_database.mapper"})
@SpringBootApplication
public class DDatabaseApplication {
	//...
}
```

然后在指定包下创建Mapper Java文件，mapper文件夹下创建xml文件即可。

# 五、最后
本次所有示例的代码：[传送门](https://github.com/hdonghong/spring-learning/tree/master/springboot/d_database)

博客地址：https://blog.csdn.net/honhong1024/article/details/80739713