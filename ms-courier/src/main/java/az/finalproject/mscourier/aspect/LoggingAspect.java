package az.finalproject.mscourier.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* az.finalproject.mscourier.service.*.*(..))")
    public void courierServiceMethods() {}

    @Around("courierServiceMethods()")
    public Object logAndMeasure(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.info("[{}] Method [{}] started with arguments: {}", className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("[{}] Method [{}] finished successfully in {} ms. Result: {}",
                    className, methodName, executionTime, result);

            return result;

        } catch (Throwable throwable) {
            long timeUntilFailure = System.currentTimeMillis() - startTime;
            log.error("[{}] Method [{}] failed after {} ms. Message: {}",
                    className, methodName, timeUntilFailure, throwable.getMessage());

            throw throwable;
        }
    }
}