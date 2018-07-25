# SpringBoot2学习笔记（七）REST之Swagger

> REST，即（Resource） Representational Status Transfer，资源表现层状态转移。REST是一种WEB API的标准、规范，取代了笨重的SOAP。本文要介绍SpringBoot中的RestTemplate和集成Swagger。

# 一、RestTemplate
RestTemplate是Spring封装了HttpClient，并提供了更为强大的对REST URI的支持。

下面是示例代码：
```
RestTemplate restTemplate = restTemplateBuilder.build();
String url = "http://localhost:8080/uu/hello?name=hdonghong";
return restTemplate.getForObject(url, String.class);
```

# 二、集成Swagger

集成Swagger这里一开始不懂，直接进了官网，逛了大半个小时没发现和Spring集成相关的文档。/(ㄒoㄒ)/~~

最后才发现，原来Spring集成Swagger是使用 `springfox` o((>ω< ))o，这里附上文档，一步一步来基本没问题了：http://springfox.github.io/springfox/docs/current/ 。不过目前最新版本我没下载成功。。。退而选择了2.6版本。

下面是正式的配置过程：

pom文件引入依赖：
```
<!-- swagger spec api 实现 -->
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.6.1</version>
</dependency>
<!-- swagger ui  -->
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.6.1</version>
</dependency>
```

Java代码配置：
```
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 扫描controller包
                .apis(RequestHandlerSelectors.basePackage("pers.hdh.g_rest.controller"))
                // 设置路径筛选
                .paths(PathSelectors.any())
                .build();
    }

    /** 构建 api文档的详细信息函数,注意这里的注解引用的是哪个 */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("Spring Boot 2 测试使用 Swagger2 构建RESTful API")
                // 描述
                .description("测试 API 描述")
                // 创建人
                .contact(new Contact("hdonghong", "https://github.com/hdonghong",""))
                // 版本号
                .version("1.0")
                .build();
    }
}
```

这样就配置完毕了( •̀ ω •́ )y，访问 `http://域名/主项目/swagger-ui.html` 即可。

# 三、Swagger注解

- @Api()用于类，表示标识这个类是swagger的资源 
- @ApiOperation()用于方法，表示一个http请求的操作
- @ApiParam()用于方法，参数，字段说明，表示对参数的添加元数据（说明或是否必填等） 
- @ApiModel()用于类，表示对类进行说明，用于参数用实体类接收 
- @ApiModelProperty()用于方法，字段 表示对model属性的说明或者数据操作更改 
- @ApiIgnore()用于类，方法，方法参数 表示这个方法或者类被忽略
- @ApiImplicitParam() 用于方法，表示单独的请求参数
- @ApiImplicitParams() 用于方法，包含多个 @ApiImplicitParam 

下面是示例代码：
```
@Api(value = "不知道有什么用", description = "招呼模块")
@ApiModel(value = "apimodel-value")
@RequestMapping("/uu")
@RestController
public class HelloController {
    @ApiImplicitParam(name = "name", value = "被打招呼者的称谓", required = true, paramType = "query", dataType = "String")
    @ApiOperation(value = "找打招呼", notes = "就是输入你的名字，系统会给你打招呼")
    @GetMapping("/hello")
    public String hello(String name) {
        return "hello, " + name;
    }
}
```

效果图：

![这里写图片描述](https://img-blog.csdn.net/20180630110508382?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hvbmhvbmcxMDI0/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

# 四、最后

本次所有示例的代码：[传送门](https://github.com/hdonghong/spring-learning/tree/master/springboot/g_rest)

博客地址：https://blog.csdn.net/honhong1024/article/details/80865148