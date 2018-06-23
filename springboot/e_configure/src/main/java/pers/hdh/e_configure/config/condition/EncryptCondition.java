package pers.hdh.e_configure.config.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Objects;

/**
 * EncryptCondition class<br/>
 *
 * @author hdonghong
 * @date 2018/06/23
 */
public class EncryptCondition implements Condition {

    @Bean
    @Conditional(EncryptCondition.class)
    public EncryptCondition encryptCondition() {
        return new EncryptCondition();
    }

    /**
     *
     * @param conditionContext 用于帮助条件判断的辅助类，用来获取Environment、ResourceLoader、ConfigurableListableBeanFactory
     * @param annotatedTypeMetadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // 读取配置文件中是否要求加密
        String encryptEnable = conditionContext.getEnvironment().getProperty("encrypt.enable");

        if (Objects.equals("true", encryptEnable)) {
            String salt = "";
            // 读取salt.txt文件的盐
            Resource resource = conditionContext.getResourceLoader().getResource("salt.txt");
            if (resource.exists()) {
                try {
                    File saltFile = resource.getFile();
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(saltFile));
                    salt = properties.getProperty("salt");
                    return !StringUtils.isEmpty(salt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return false;
    }
}
