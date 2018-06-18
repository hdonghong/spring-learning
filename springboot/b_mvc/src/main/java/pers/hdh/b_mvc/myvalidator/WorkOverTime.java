package pers.hdh.b_mvc.myvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * WorkOverTime anntation<br/>
 *
 * @author hdonghong
 * @date 2018/06/17
 */
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
