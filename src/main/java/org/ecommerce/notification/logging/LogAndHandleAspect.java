package org.ecommerce.notification.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAndHandleAspect {
    @Around("execution(* org.ecommerce.notification.listener.OrderMessageListener.onMessage(..)) || " +
            "execution(* org.ecommerce.notification.service.EmailService.*(..)) || " +
            "execution(* org.ecommerce.notification.asynchronous.ScheduledEmailService.resendFailedEmails(..)) || " +
            "execution(* org.ecommerce.notification.util.dataFormatAdapterPattern.EmailFormatAdapter.*(..))")
    public Object logMessageProcessing(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("Calling method: " + joinPoint.getSignature().getName() + " in " + joinPoint.getTarget());
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info(joinPoint.getSignature().getName() +" Method execution time: " + (end - start) + " ms");
        return result;
    }
}
