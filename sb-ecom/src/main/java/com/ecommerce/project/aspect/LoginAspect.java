package com.ecommerce.project.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginAspect {
    //named pointcut
    //@Pointcut("execution(* com.ecommerce.project.service.CategoryServiceImpl.createCategory(..))")
    @Pointcut("within(com.ecommerce.project.service.CategoryServiceImpl)")
    public void createCategory() {}

    //point cut @After
    @Before("createCategory()")
    public void log() {
        System.out.println("aspect is invoked");
    }

    @Around("execution(* com.ecommerce.project.service.CategoryServiceImpl.createCategory(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("log around aspect is invoked before");
        //method/jointpoint
        Object object = joinPoint.proceed();
        System.out.println("log around aspect is executed after");
        return object;
    }

    @After("@within(org.springframework.stereotype.Service)")
    public void logAfter() {
        System.out.println("log after aspect is invoked");
    }
}
