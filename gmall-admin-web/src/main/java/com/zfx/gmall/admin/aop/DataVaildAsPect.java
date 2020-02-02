package com.zfx.gmall.admin.aop;

import com.zfx.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/*
    切面如何编写
        1、导入切面场景
            <dependency>
                <groupId>org. springframework. boot</groupId>
                <artifactId>spring- boot - starter- aop</artifactId>
            </dependency>
        2、编写切面
            1)、@Aspect
            2)、切入点表达式
            3)、通知
                前置通知:方法执行之前触发
                后置通知:方法执行之后触发
                返回通知:方法正常返回之后触发
                异常通知:方法出现异常触发
                正常执行:
                前置通知==>返回通知==>后置通知.
                异常执行:
                前置通知==>异常通知==>后置通知
                环绕通知: 4合1
 */
//利用aop完成统一 的数据校验，数据校验出错就返回给前端错误提示
@Slf4j
@Aspect
@Component
public class DataVaildAsPect {

    //@AspectJ 语法基础
    @Around("execution(* com.zfx.gmall.admin..*Controller.*(..))")
    public Object vaildAround(ProceedingJoinPoint point){

        Object proceed = null;

        try {
            //获取目标方法的参数值
            Object[] args = point.getArgs();

            for (Object arg : args) {
                //判断获取的对象类型是否为BindingResult的实例，是就进行类型强转
               if(arg instanceof BindingResult){
                   BindingResult result = (BindingResult)arg;
                   if(result.getErrorCount()>0){
                       //框架的自动校验检测到错误了
                        return new CommonResult().validateFailed(result);
                   }

               }
            }

            log.debug("校验切面介入工作......");

            //下面 就是我反射的 method.invoke()
            proceed = point.proceed(args);

            log.debug("校验切面将目标方法已执行......{}",proceed);
        } catch (Throwable throwable) {
            log.error("---异常通知---");
        }finally {
            log.debug("---后置通知---");
        }
        return proceed;
    }
}
