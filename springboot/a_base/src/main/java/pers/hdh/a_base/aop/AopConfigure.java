package pers.hdh.a_base.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * AopConfigure class<br/>
 *
 * @author hdonghong
 * @date 2018/06/17
 */
@Configuration
@Aspect
public class AopConfigure {

    /**
     * 用来织入的代码， @Around 声明一个表达式，描述要织入的目标的特性
     *
     * @param joinPoint 可获取被调用的方法的参数，执行被调用的方法
     * @return Object
     * @throws Throwable
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object simpleAop(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        // 查看方法的参数
        System.out.println("args: " + Arrays.asList(args));
        // 继续执行方法
        Object o = joinPoint.proceed();
        return o;
    }
}
