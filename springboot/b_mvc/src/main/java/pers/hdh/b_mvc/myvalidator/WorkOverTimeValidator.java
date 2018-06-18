package pers.hdh.b_mvc.myvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * WorkOverTimeValidator class<br/>
 *
 * @author hdonghong
 * @date 2018/06/17
 */
public class WorkOverTimeValidator implements ConstraintValidator<WorkOverTime, Integer> {

    private WorkOverTime work;

    private int max;

    @Override
    public void initialize(WorkOverTime work) {
        // 获取注解的定义
        this.work = work;
        max = work.max();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext ontext) {
        // 校验
        return value == null || value < max;
    }
}
