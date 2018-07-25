# SpringBoot2学习笔记（三）JSON技术



> Spring Boot内置了jackson来完成JSON的序列化与反序列化操作，Jackson支持三种层次的序列化与反序列化方式。分别是树遍历方式、DataBind方式以及底层的JsonParser方式。

# 一、ObjectMapper

当使用 `@ResponseBody` 注解时，我们可以自定义一个ObjectMapper来代替默认的，进行序列化。
示例代码：
```
@Configuration
public class JacksonConfigure {
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }
}
```
以上规定了日期的使出格式。不过这里我有个疑惑没解决，希望知道的朋友留言为我解答下，谢谢。
**SimpleDateFormat在Jdk8后被建议用DateTimeFormatter替代，但是objectMapper.setDateFormat()方法却要求传入一个DateFormat类型，我不得已而使用了SimpleDateFormat。想知道这里是否能使用DateTimeFormatter相关的方式替代SimpleDateFormat？**

# 二、Jackson树遍历

树遍历方式通常适合没有POJO对应的JSON字符串，代码如下：
```
@PostMapping("/readtree")
public JsonNode readtree(String jsonStr) throws IOException {
    JsonNode jsonNode = objectMapper.readTree(jsonStr);
    String name = jsonNode.get("name").asText();
    Long id = jsonNode.get("id").asLong();
    System.out.println("name = " + name + ", id = " + id);
    return jsonNode;
}
```
JsonNode支持的方法：

| 方法        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| asXxx       | 比如asTest、asInt、asLong等                                  |
| isArray     | 判断JsonNode是否为数组，如果是数组可通过get(i)进行遍历，size()获取长度 |
| get(String) | 获取当前结点的子节点                                         |


# 三、对象绑定

比起没有POJO对应json，使用Java对象与JSON数据互相绑定更为常用。现在创建一个POJO对象来与JSON对应。
```
public class User {
    private String name;
    private Long id;
    // 省略getter/setter
}
```
然后可以使用readValue进行反序列化：
```
@PostMapping("/databind")
public User databind(String jsonStr) throws IOException {
    User user = objectMapper.readValue(jsonStr, User.class);
    return user;
}
```
当然也可使用writeValueAsString进行对象的序列化：
```
User user = new User();
user.setId(7L);
user.setName("007");
String jsonStr = objectMapper.writeValueAsString(user);
```

# 四、JsonParser

上述树模型和数据绑定都是基于流式操作完成的，底层通过JsonParser类解析JSON字符串，形成JsonToken流。
```
@PostMapping("/parser")
public String parser(String jsonStr) throws IOException {
   JsonFactory factory = objectMapper.getFactory();
   String key = null;
   String value = null;
   JsonParser parser = factory.createParser(jsonStr);

   // 第一个Token {, START_OBJECT，忽略
   JsonToken token = parser.nextToken();

   // "name", FILED_NAME
   token = parser.nextToken();
   if (token == JsonToken.FIELD_NAME) {
       key = parser.getCurrentName();
   }

   // "hdonghong",VALUE_STRING
   token = parser.nextToken();
   if (token == JsonToken.VALUE_STRING) {
       value = parser.getValueAsString();
   }

   parser.close();
   return "\"" + key + "\":\"" + value + "\"";
}
```

# 五、Jackson注解

Jackson提供很多注解用来进行序列化和反序列化操作，以下是常用的几个注解：

| 注解          | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| @JsonProperty | 作用在属性上，指定序列化与反序列化时的字段名                 |
| @JsonIgnore       | 作用在属性上，序列化与反序列化时忽略此字段 |
| @JsonIgnoreProperties   | 作用在类上，忽略一组属性                                        |
| @JsonNaming | 作用在类或属性上，指定一个命名策略。如PropertyNamingStrategy.SnakeCaseStrategy.class可将驼峰命名转成下划线命名 |
| @JsonAnySetter | 作用在方法上，Jackson在反序列化过程中未找到对应属性则调用此方法，将未知属性存储在自定义map中 |
| @JsonAnyGetter | 作用在方法上，Jackson在序列化过程取出map中的键值对进行序列化 |
| @JsonFormat | 用于日期格式化，如：@JsonFormat(pattern = "yyyy-MM-dd HH/mm/ss") |
| @JsonSerialize | 可指定一个实现类来自定义序列化 |
| @JsonDeserialize | 可指定一个实现类来自定义反序列化 |
| @JsonView | 作用在类或属性上，用于定义一个序列化组。Controller方法使用@JsonView指定一个组名。 |

定义一个User类，示例代码：
```
@JsonIgnoreProperties(value = {"phone", "email"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {
    @JsonProperty("username")
    private String name;
    
    private Long id;

    @JsonIgnore
    private String password;

    private String phone;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH/mm/ss")
    private Date createDate;

    @JsonIgnore
    private Map<String, Object> unknownProperties = new HashMap<>(16);

    @JsonAnySetter
    public void setUnknown(String unknownProperty, Object value) {
        unknownProperties.put(unknownProperty, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getUnknown() {
        return unknownProperties;
    }
}
```

其中要注意，用来存储未知属性的map需要使用注解 `@JsonIgnore` 忽略它，否则会被序列化。下面来看示例：
```
不使用@JsonIgnore会直接序列化map：
{
	...
	"unknown_properties": {
		"unknown1": 15,
		"unknown2": "测试未知字段"
	}
	"unknown1": 15,
	"unknown2": "测试未知字段"
}
```

# 六、最后
本次所有示例的代码：[传送门](https://github.com/hdonghong/spring-learning/tree/master/springboot/c_json)

博客地址：https://blog.csdn.net/honhong1024/article/details/80727483