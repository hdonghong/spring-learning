package pers.hdh.c_json.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import pers.hdh.c_json.entity.User2;

import java.io.IOException;

/**
 * UserDeserializer class<br/>
 * 用户自定义反序列化，需要继承JsonDeserializer抽象类
 * @author hdonghong
 * @date 2018/06/18
 */
public class UserDeserializer extends JsonDeserializer<User2> {
    @Override
    public User2 deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String name = node.get("username").asText();
        User2 user2 = new User2();
        user2.setName(name);
        return user2;
    }
}
