package pers.hdh.c_json.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import pers.hdh.c_json.entity.User;
import pers.hdh.c_json.entity.User2;

import java.io.IOException;

/**
 * UserSerializer class<br/>
 * 指定一个实现类来自定义序列化，需要继承JsonSerializer抽象类
 * @author hdonghong
 * @date 2018/06/18
 */
public class UserSerializer extends JsonSerializer<User2> {
    @Override
    public void serialize(User2 user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("username", user.getName());
        jsonGenerator.writeEndObject();
    }
}
