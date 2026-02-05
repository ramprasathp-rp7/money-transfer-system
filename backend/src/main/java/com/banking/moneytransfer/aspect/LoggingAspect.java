package com.banking.moneytransfer.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * AOP Aspect for logging method execution
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut for all methods in service package
     */
    @Pointcut("execution(* com.banking.moneytransfer.service..*(..))")
    public void serviceMethods() {}

    /**
     * Pointcut for all methods in controller package
     */
    @Pointcut("execution(* com.banking.moneytransfer.controller..*(..))")
    public void controllerMethods() {}

    /**
     * Around advice for logging method execution time
     */
    @Around("serviceMethods() || controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Entering method: {}.{}() with arguments: {}",
                className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        Object result;
        try {
            result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;

            log.info("Exiting method: {}.{}() - Execution time: {} ms",
                    className, methodName, executionTime);

            return result;

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;

            log.error("Exception in method: {}.{}() after {} ms - Exception: {}",
                    className, methodName, executionTime, e.getMessage());

            throw e;
        }
    }
}