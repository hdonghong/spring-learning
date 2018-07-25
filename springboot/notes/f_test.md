# SpringBoot2学习笔记（六）单元测试

> 单元测试对提高代码质量，重构代码都有极大的作用！个人平时也比较注重单元测试的编写，以上是阅读《SpringBoot 2精髓》一书的个人总结。

# 一、单元测试

首先是介绍下JUnit的相关概念。

| 概念   | 说明                                                         |
| ------ | ------------------------------------------------------------ |
| Assert | 测试条件，当条件不成立时抛出异常。如：Assert.assertSame(message, Expected, Actual)判断Expected对象和Actual是否同一个对象(==)，不同则抛出异常提示message的信息。 |
| Suite  | Suite允许将多个测试类归成一组。在测试类（可空类）加注解@RunWith(Suite.class)和@SuiteClass({多个测试类})。 |
| Runner | Runner类用来允许测试。JUnit没有main()方法入口，其实在org.junit.runner包下有个JUnitCore.class，其中存在一个标准**main**方法，这就是JUnit的入口函数。 |


# 二、Dao层的单元测试

Dao层的测试需要准备一个空数据库，以及一些初始化的数据，使用 `@Sql` 注解来初始化。

首先在classpath下准备一个初始化数据的脚本文件 `user.sql` ，内容如下：
```
INSERT INTO `user` (`user_id`, `name`) VALUES (99, '哈哈');
```
下面来看示例的测试代码：
```
// @ActiveProfiles激活test作为profile，使用准备好的空数据库
@ActiveProfiles("test")
// @Transactional测试执行后回滚数据
@Transactional
// ‘/’开头表示从classpath根目录开始搜索，没有以此开头默认在测试类所在包下。也可使用classpath:、file:、http: 开头
@Sql({"/user.sql"})
// @Runwith是JUnit标准的一个注解，Spring的单元测试都用SpringRunner.class
@RunWith(SpringRunner.class)
// @SpringBootTest用于Spring Boot应用测试，默认根据报名逐级往上寻找应用启动类
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testGetOne() {
        User user = userDao.getOne(99);
        Assert.assertNotNull(user);
        Assert.assertEquals("哈哈", user.getName());
        Assert.assertSame("不一样呀", 99, user.getId());
    }
}
```

# 三、Service层的单元测试

Service层是处理业务逻辑的地方，通常比较复杂，编写单元测试代码前需要处理好以下三个问题：

+ 保证可重复测试。一个service方法可以多次测试，因此测试完毕后数据要能自动回滚。
+ 模拟未完成的Service。当前测试的Service依赖的其它Service未开发完毕时，要能模拟其它Service。
+ 准备测试数据。单元测试前要模拟好测试场景的数据。

如何解决呢？

+ 对于第一个问题，可以使用Spring提供的 `@Transactional` 注解进行事务回滚。
+ 对于第二个问题，使用Spring Boot集成的 `Mockito` 来模拟未完成的Service或者不能随便调用的Service。
+ 对于第三个问题，`@Sql` 注解再了解一下:)

下面来看一下使用 `Mockito` 解决第二个问题的示例。

假设现有 `UserService` 依赖于 `CreditSystemService`，但是CreditSystemService并没有实现类
```
@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

	// UserService中依赖了CreditSystemService，但是CreditSystemService没有实现类
    @Autowired
    private UserService userService;

	// 注解@MockBean 可以自动注入Spring管理的Service，这里creditSystemService由Mockito工具产生。
    @MockBean
    private CreditSystemService creditSystemService;

    @Test
    public void getCredit() throws Exception {
       // given方法模拟一个service方法调用返回
	   BDDMockito.given(creditSystemService.getUserCredit(BDDMockito.anyInt())).willReturn(100);

       Assert.assertEquals(100, userService.getCredit(userId));
    }
}
```


# 四、Web层的单元测试

Spring MVC Test 通过 `@WebMvcTest` 来完成MVC单元测试。示例代码如下：

```
@RunWith(SpringRunner.class)
// @WebMvcTest表示这是一个MVC测试，可传入多个待测试的Controller。
@WebMvcTest(UserController.class)
public class UserControllerTest {

    // MockMvc是Spring专门提供用于测试SpringMVC类
    @Autowired
    private MockMvc mockMvc;

    // @MockBean 用来模拟实现，因为在Spring MVC测试中并不会初始化@Service注解的类，需要自己模拟service实现。
    @MockBean
    private UserService userService;

    @Test
    public void getUser() throws Exception {
        int userId = 1;
        BDDMockito.given(userService.getCredit(userId)).willReturn(100);
        // perform，完成一次MVC调用，Spring MVC Test是servlet容器内部的模拟测试，不会发起真正的HTTP请求。
        // get，模拟一次GET请求。
        // andExpect，表示请求期望的返回结果
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId))
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(100)));
    }
}
```

MockMvc的核心方式是 `public ResultActions perform(RequestBuilder requestBuilder)` ，下面是一些模拟请求示例：

模拟GET请求：
```
mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId));
```

模拟Post请求：
```
mockMvc.perform(MockMvcRequestBuilders.post("uri", parameters));
```

模拟文件上传：
```
mockMvc.perform(MockMvcRequestBuilders.multipart("uri").file("fileName", "file".getBytes("UTF-8")));
```

模拟session和cookie：
```
mockMvc.perform(MockMvcRequestBuilders.get("uri").sessionAttr("name", "value"));
mockMvc.perform(MockMvcRequestBuilders.get("uri").cookie(new Cookie("name", "value")));
```

设置HTTP Header：
```
mockMvc.perform(MockMvcRequestBuilders
                        .get("uri", parameters)
                        .contentType("application/x-www-form-urlencoded")
                        .accept("application/json")
                        .header("", ""));
```

### Mockito的用法非常多，这里就不再展示了。

# 五、最后

本次所有示例的代码：[传送门](https://github.com/hdonghong/spring-learning/tree/master/springboot/f_test)

博客地址：https://blog.csdn.net/honhong1024/article/details/80790161