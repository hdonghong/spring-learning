package pers.hdh.c_json.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.ToString;
import pers.hdh.c_json.config.UserDeserializer;
import pers.hdh.c_json.config.UserSerializer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User2 class<br/>
 *
 * @author hdonghong
 * @date 2018/06/18
 */
@Data
@ToString
// @JsonSerialize，指定使用自定义序列化
@JsonSerialize(using = UserSerializer.class)
// @JsonDeserialize，指定使用自定义反序列化
@JsonDeserialize(using = UserDeserializer.class)
public class User2 {

    private String name;

}
