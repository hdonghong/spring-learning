# SpringBoot2学习笔记（二）MVC框架

> 这次将谈谈SpringBoot中MVC框架的一些特性，包括uri到方法的映射、方法参数、验证框架、WebMvcConfigure、集成Thymeleaf视图、通过错误处理以及业务层事务处理等内容。

# 一、uri到方法的映射
首先来看一个示例：
```
@RestController
public class HelloController {
    @GetMapping(
            value = "/hello",
            consumes = "application/json",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            params = "myParam=myValue",
            headers = "myHeader=myValue")
    public String hello() {
        return "ok";
    }
}
```
上面@RestController注解和GetMapping注解在上文已经阐述过，这里重复一次。
`@RestController` ：SpringBoot支持Rest服务的直接，可理解为@Controller + @ResponseBody的效果。
`@GetMapping` ：SpringBoot简化SpringMVC的RequestMapping(method=RequestMethod.GET)，此外还有 PostMapping 、PutMapping 、DeleteMapping 、PatchMapping 。
这里的重点是介绍RequestMapping中的一些属性。
`consumes` ：对应HTTP头的Content-Type媒体类型。
`produces` ：对应HTTP头的Accept字段。
`params` 和 `headers` ：params属性和headers属性类似，要求请求参数或请求头中包含指定参数或者包含指定参数和指定值或者不包含指定参数。

由此，以上注解声明的含义是：
要求请求方式为Get，uri为/hello，Content-Type为application/json，Accept为application/json;charset=UTF-8，请求参数包含myParam且对应值为myValue，请求头包含myHeader且对应值为myValue。满足要求才能执行hello()方法。

# 二、方法参数

这部分主要还是属于SpringMVC的知识，不过这里也简单介绍下Controller方法能接收的参数。这里列举常用的一些参数。

| 参数                                     | 说明                                                         |
| :--------------------------------------- | :----------------------------------------------------------- |
| @PathVariable                            | 可以将URL中的值映射到方法参数中                              |
| Model & ModelAndView & Map & ModelMap    | 类似的MVC模型                                                |
| JavaBean                                 | 将HTTP参数映射到JavaBean对象                                 |
| MultipartFile                            | 接收上传文件，多个文件可用数组，max-file-size默认1mb         |
| @ModelAttibute                           | 通常作用在Controller的某个方法上，将方法结果作为Model的属性再调用对应的Controller处理方法 |
| HttpServletRequest & HttpServletResponse |                                                              |
| .....                                    |                                                              |

下面我们使用 `@PathVariable` 和 `@ModelAttribute` 来演示一个示例：
```
// 表示：访问/user/{id}请求时，会先调用findUserById方法添加Model属性，然后才执行controller的方法

@GetMapping("/user/{id}")
public String user(Model model) {
    System.out.println(model.containsAttribute("user"));
    return "ok";
}
@ModelAttribute
public void findUserById(@PathVariable("id") Long id, Model model) {
    model.addAttribute("user", id);
}
```

# 三、验证框架

Spring Boot支持JSR-303、Bean验证框架，默认实现用的事Hibernate validator。使用@Valid或Validated注解对参数对象进行校验，校验结果放在BindingResult对象中。

以下是JSR-303框架常用的一些注解

 - 空检查
  @Null，验证对象是否为空；
  @NotNull，验证对象不为空；
  @NotBlank，验证字符串不为空或者不是字符串，比如""和" "都会验证失败；
  @NotEmpty，验证对象不为空，或者集合不为空；

 - 长度检查
   @Size(min = , max = )，验证对象长度，可支持字符串、集合；
   @Length，字符串大小；

 - 数值检测
  @Min，验证数字是否大于等于指定值；
  @Max，验证数字是否小于等于指定值；
  @Digits，验证数字是否符合指定格式，如@Digits(integer = 9, fraction = 2)；
  @Range，验证数字是否在指定的范围内，如@Range(min = 233, max = 666)；

 - 其它
   @Email，验证是否为邮件格式；
   @Pattern，验证String对象是否符合正则表达式的规则；

**注意：除了空检查，其它检查在对象为空时不做校验。**

现在我们来看一个示例，定义一个员工表单实体：

```
public class EmployeeForm {
    @NotNull
    private String id;

    /** 验证对象长度，可支持字符串、集合，null时不校验 */
    @Size(min = 11, max = 11, message = "需要11位手机号码")
    private String phoneNumber;

    /** null时不校验 */
    @Range(min = 3000, max = 8000, message = "你工资范围是3000~8000")
    private Integer wage;

    /** 验证邮件格式，null时不校验 */
    @Email(message = "注意邮件格式")
    private String email;
}
```

Controller方法使用时加 `@Validated` 注解并声明 `BingdingResult` 即可。
```
@PostMapping("/add")
public void validateAdd(@Validated EmployeeForm employeeForm, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(e -> {
            FieldError error = (FieldError) e;
            System.out.println(error.getField() + ": " + e.getDefaultMessage());
        });
    }
}
```

现在有个问题，如果如果创建一个新员工的记录时，从页面传来的数据中肯定是不包含id，而上面 `EmployeeForm` 类已经规定了id不为空，这样的话如何处理呢，是否要再定义一个员工表单类不要求id非空？
也就是说，如何适应不同场景的业务要求？
JSR-303定义了校验组group的概念，可通过不同校验组对应不同场景。
继续完善示例：
```
/** 定义Update接口，更新时校验组 */
public interface Update {}

/** 定义Add接口，添加时校验组 */
public interface Add {}

/** 以下代码表示，当校验上下文为Add.class时，@Null生效；为Update.class时，@NotNull生效 */
@Null(groups = Add.class, message = "添加时不能已存在id！")
@NotNull(groups = Update.class, message = "更新时必须指定id")
private String id;
```
而刚刚的Controller方法可指定校验组：
```
public void validateAdd(@Validated(EmployeeForm.Add.class)EmployeeForm employeeForm, 
						BindingResult bindingResult) {
	...
}
```
**注意：指定校验组后，其它的字段也要需要指定校验组。**

此外，JSR-303也允许用户定义自己的校验器。这里我们定义一个员工工作时长的校验。
```
// 使用@Constraint注解来声明用什么类实现约束验证
@Constraint(validatedBy = {WorkOverTimeValidator.class})
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkOverTime {

    /** 用于创建错误信息 */
    String message() default "加班时间不能超过{max}小时";

    int max() default 5;

    /** 验证规则分组 */
    Class<?>[] groups() default {};

    /** 定义了验证的有效负荷 */
    Class<? extends Payload>[] payload() default {};
}
```
校验器的实现代码如下：
```
public class WorkOverTimeValidator implements ConstraintValidator<WorkOverTime, Integer> {

    private WorkOverTime work;

    private int max;

    @Override
    public void initialize(WorkOverTime work) {
        // 获取注解的定义
        this.work = work;
        max = work.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext ontext) {
        // 校验
        return value == null || value < max;
    }
}
```
这样以后，就可以直接在实体类中使用了：
```
/** 自定义校验 */
@WorkOverTime(max = 8, groups = {Add.class, Update.class})
private Integer workTime;
```

# 四、WebMvcConfigure

WebMvcConfigure是用来全局定制化Spring Boot的MVC特性。如设置拦截器、跨域访问配置、格式化、URI到视图的映射或者其它全局定制接口。
```
@Configuration
public class MvcConfigure implements WebMvcConfigurer {
	// TODO 
}
```

拦截器：
```
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new TokenAuthorizationInterceptor())
            .addPathPatterns("/admin/**");
}
```

跨域访问配置：
```
public void addCorsMappings(CorsRegistry registry) {
    // 匹配所有的URI，允许所有的外域发起跨域请求，允许外域发起请求POST/GET，允许跨域请求包含任意的头信息。
    registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("POST", "GET")
            .allowedHeaders("*")
            .allowCredentials(true);
}
```

日期格式化：
```
public void addFormatters(FormatterRegistry registry) {
    registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
}
```

URI到视图的映射，没必要为了一个URL特地指定一个Controller方法：
```
public void addViewControllers(ViewControllerRegistry registry) {
    // 将/index重定向到/index.html
    registry.addRedirectViewController("/index", "/index.html");
}
```

# 五、集成Thymeleaf
SpringBoot默认支持了Thymeleaf模板引擎，可在配置文件中加入如下配置（默认）：
```
spring:
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML5
    encoding: UTF-8
    cache: false
    servlet:
      content-type: text/html
```
同时需要pom.xml引入thymeleaf的start支持：
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

当使用Thymeleaf的html文件中只需在 `<html>` 标签中加入 `xmlns:th="http://www.w3.org/1999/xhtml"` 约束即可。

# 六、通用错误处理
Spring Boot中，Controller中抛出的异常都默认交给了 `/error` 处理，我们可以将 `/error` 映射到一个特定的Controller中来替代Spring Boot的默认实现处理，继承 `AbstractErrorController` 来统一处理系统的各种异常。
代码如下：
```
public class ErrorController extends AbstractErrorController {

    public ErrorController() {
        super(new DefaultErrorAttributes());
    }

    @RequestMapping("/error")
    public ModelAndView getErrorPath(HttpServletRequest request, HttpServletResponse response) {
		// TODO
        return null;
    }
    
	// Do Something
}
```

# 七、事务管理
首先pom文件引入jdbc的starter支持：
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```
在业务层使用 `@Transactional` 注解即可。

# 八、最后
本次所有示例的代码：[传送门](https://github.com/hdonghong/spring-learning/tree/master/springboot/b_mvc)

博客地址：https://blog.csdn.net/honhong1024/article/details/80723628