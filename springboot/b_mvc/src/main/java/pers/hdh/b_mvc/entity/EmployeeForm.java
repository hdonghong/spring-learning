package pers.hdh.b_mvc.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import pers.hdh.b_mvc.myvalidator.WorkOverTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * EmployeeForm class<br/>
 * 员工信息表单验证，为了展示Spring Boot的验证框架
 *
 * @author hdonghong
 * @date 2018/06/17
 */
@Data
@ToString
public class EmployeeForm {

    /** 定义Update接口，更新时校验组 */
    public interface Update {}

    /** 定义Add接口，添加时校验组 */
    public interface Add {}

    /** 以下代码表示，当校验上下文为Add.class时，@Null生效；为Update.class时，@NotNull生效 */
    @Null(groups = Add.class, message = "添加时不能已存在id！")
    @NotNull(groups = Update.class, message = "更新时必须指定id")
    private String id;

    /** 验证对象长度，可支持字符串、集合，null时不校验 */
    @Size(min = 11, max = 11, message = "需要11位手机号码", groups = {Add.class, Update.class})
    private String phoneNumber;

    /** null时不校验 */
    @Range(min = 3000, max = 8000, message = "你工资范围是3000~8000", groups = {Add.class, Update.class})
    private Integer wage;

    /** 验证邮件格式，null时不校验 */
    @Email(message = "注意邮件格式", groups = {Add.class, Update.class})
    private String email;

    /** 自定义校验 */
    @WorkOverTime(max = 8, groups = {Add.class, Update.class})
    private Integer workTime;

}
