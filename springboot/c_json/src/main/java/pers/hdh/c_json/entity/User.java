package pers.hdh.c_json.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User class<br/>
 *
 * @author hdonghong
 * @date 2018/06/18
 */
@Data
@ToString
@JsonIgnoreProperties(value = {"phone", "email"})
//@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class) // @deprecated
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User {

    // @JsonProperty指定序列化与反序列化时的字段名
    @JsonProperty("username")
    private String name;

    private Long id;

    // @JsonIgnore序列化与反序列化时忽略此字段
    @JsonIgnore
    private String password;

    private String phone;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH/mm/ss")
    private Date createDate;

    /**
     * 不使用@JsonIgnore会直接序列化map：
     *     "unknown_properties": {
     *          "unknown1": 15,
     *          "unknown2": "测试未知字段"
     *     }
     *
     * 使用@JsonIgnore + @JsonAnyGetter取map中的键值对序列化(推荐)
     *     {
     *          ...
     *          "unknown1": 15,
     *          "unknown2": "测试未知字段"
     *      }
     */
    @JsonIgnore
    private Map<String, Object> unknownProperties = new HashMap<>(16);

    // @JsonAnySetter：Jackson在反序列化过程中未找到对应属性则调用此方法，将未知属性存储map中
    @JsonAnySetter
    public void setUnknown(String unknownProperty, Object value) {
        unknownProperties.put(unknownProperty, value);
    }

    // @JsonAnyGetter：Jackson在序列化过程取出map中的键值对进行序列化
    @JsonAnyGetter
    public Map<String, Object> getUnknown() {
        return unknownProperties;
    }
}
