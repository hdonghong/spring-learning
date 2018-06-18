package pers.hdh.c_json.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * JacksonConfigure class<br/>
 *
 * @author hdonghong
 * @date 2018/06/18
 */
@Configuration
public class JacksonConfigure {

    /**
     * 疑问：这里如何用DateTimeFormatter替代SimpleDateFormat？
     * DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }
}
