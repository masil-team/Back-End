package com.masil.global.aop.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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

    @Before("domainController()")
    public void apiLogRequest(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 로그에 출력할 요청(request) 값들을 가져옵니다.
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String remoteAddr = request.getRemoteAddr();
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        Object[] methodArgs = joinPoint.getArgs();

        // 요청(request) 값들을 로그에 출력합니다.
        log.debug("====================================API Request====================================");
        log.debug("Request : {} {} from {} user={}", httpMethod, uri, remoteAddr, username);
        log.debug("Query String : {}", queryString);
        log.debug("Method Args : {}", Arrays.toString(methodArgs));
        log.debug("===================================================================================");
    }

    @AfterReturning(pointcut = "domainController()" ,returning = "result")
    public void apiLogResponse(JoinPoint joinPoint, Object result) {
        log.debug("====================================API Response====================================");
        log.debug("Response : {}" , result);
        log.debug("====================================================================================");
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
