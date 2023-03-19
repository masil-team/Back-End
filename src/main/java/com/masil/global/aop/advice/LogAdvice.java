package com.masil.global.aop.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAdvice {

    @Pointcut("execution(* com.masil..*Controller.*(..))")
    public void allController() {}

    @Around("allController()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        log.info("[Api 시작] {}", joinPoint.getSignature());
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            log.info("[Exception] {}" ,e.getMessage());
            throw e;
        } finally {
            log.info("[Api 종료] {}",joinPoint.getSignature());
            log.info("[실행시간] {} ms", System.currentTimeMillis() - start);
        }
        return result;
    }

}
