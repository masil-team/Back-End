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

    @Pointcut("execution(public * com.masil..*Controller.*(..))")
    public void domainController() {}

    @Pointcut("execution(public * com.masil..*Service*.*(..))")
    public void domainService() {}

    @Pointcut("execution(public * com.masil..*Repository.*(..))")
    public void domainRepository() {}

    @Around("domainController()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[Api 시작] {}", joinPoint.getSignature());
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.info("[Exception] {}" ,e.getMessage());
            throw e;
        } finally {
            log.info("[Api 종료] {}",joinPoint.getSignature());
            log.info("[Api 실행시간] {} ms", System.currentTimeMillis() - start);
        }
    }

    @Around("domainController()")
    public Object doDebugController(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("[Api 시작] {}", joinPoint.getSignature());
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.debug("[Exception] {}" ,e.getMessage());
            throw e;
        } finally {
            log.debug("[Api 종료] {}",joinPoint.getSignature());
        }
    }

    @Around("domainService()")
    public Object debugLogService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.debug("[Service 시작] {}", joinPoint.getSignature());
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.debug("[Exception] {}" ,e.getMessage());
            throw e;
        } finally {
            log.debug("[Service 종료] {}",joinPoint.getSignature());
            log.debug("[Service 실행시간] {} ms", System.currentTimeMillis() - start);
        }
    }

    @Around("domainRepository()")
    public Object debugLogRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.debug("[Repository 시작] {}", joinPoint.getSignature());
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.debug("[Exception] {}" ,e.getMessage());
            throw e;
        } finally {
            log.debug("[Repository 종료] {}",joinPoint.getSignature());
            log.debug("[Repository 실행시간] {} ms", System.currentTimeMillis() - start);
        }
    }

}
