package pers.hdh.c_json.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hdh.c_json.entity.User;
import pers.hdh.c_json.entity.User2;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HelloController class<br/>
 *
 * @author hdonghong
 * @date 2018/06/18
 */
@RestController
public class HelloController {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取当前时间
     * 检验自定义的ObjectMapper是否生效
     * 结果：
     * 使用默认的：{"now":"2018-06-18T08:50:03.980+0000"}
     * 使用自定义的：{"now":"2018-06-18 16:48:57"}
     * @return Map
     */
    @GetMapping("/now")
    public Map<String, Object> now() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("now", new Date());
        return map;
    }

    /**
     * Jackson树遍历，将传入的json字符串读入JsonNode对象中。
     * 这种方式适合没有POJO对应的JSON。
     * 输入：{"name":"hdonghong", "id": 1}
     * 输出：{"name": "hdonghong","id": 1}
     * @param jsonStr json字符串，不是json对象
     * @return jsonnode对象
     * @throws IOException
     */
    @PostMapping("/readtree")
    public JsonNode readtree(String jsonStr) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(jsonStr);
        String name = jsonNode.get("name").asText();
        Long id = jsonNode.get("id").asLong();
        System.out.println("name = " + name + ", id = " + id);
        return jsonNode;
    }

    /**
     * 对象绑定，将传入的JSON字符串反序列化成一个对应的POJO对象
     * @param jsonStr json字符串，不是json对象
     * @return 对应的对象
     * @throws IOException
     */
    @PostMapping("/databind")
    public User databind(String jsonStr) throws IOException {
        User user = objectMapper.readValue(jsonStr, User.class);
        return user;
    }

    /**
     * 将POJO序列化成json字符串
     * @return json字符串
     * @throws JsonProcessingException
     */
    @GetMapping("/serialize")
    public String serialize() throws JsonProcessingException {
        User user = new User();
        user.setId(7L);
        user.setName("007");

        return objectMapper.writeValueAsString(user);
    }

    /**
     * 树模型和数据绑定都是基于流式操作完成的，底层通过JsonParser类解析JSON字符串，形成JsonToken流
     * @param jsonStr JSON字符串
     * @return 解析结果
     * @throws IOException
     */
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

    /**
     * 测试自定义的序列化与反序列化
     * @param jsonStr
     * @return
     * @throws IOException
     */
    @PostMapping("/myserializer")
    public User2 myserializer(String jsonStr) throws IOException {
        User2 user2 = objectMapper.readValue(jsonStr, User2.class);
        System.out.println(objectMapper.writeValueAsString(user2));
        return user2;
    }
}
