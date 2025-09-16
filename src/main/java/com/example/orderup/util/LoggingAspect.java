package com.example.orderup.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.example.orderup.service.OrderService.placeOrder(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        long start=System.currentTimeMillis();
        System.out.println("[Aspect] Starting placeOrder " + java.util.Arrays.toString(pjp.getArgs()));
        try{
            Object result=pjp.proceed();
            System.out.println("[Aspect] Completed in " + (System.currentTimeMillis()-start)+"ms");
            return result;
        }catch(Throwable t){
            System.out.println("[Aspect] Failed with "+t.getMessage());
            throw t;
        }
    }
}
