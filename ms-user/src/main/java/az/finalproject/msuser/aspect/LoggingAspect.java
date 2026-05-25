package az.finalproject.msuser.aspect;

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

    @Pointcut("execution(* az.finalproject.msuser.service.*.*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object logAndMeasure(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Method [{}] started with arguments: {}", methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;
            log.info("Method [{}] finished successfully in {} ms. Result: {}",
                    methodName, executionTime, result);

            return result;

        } catch (Throwable throwable) {

            log.error("Method [{}] failed after {} ms. Error: {}",
                    methodName, (System.currentTimeMillis() - startTime), throwable.getMessage());

            throw throwable;
        }
    }
}