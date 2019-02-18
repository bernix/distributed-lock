package my.bning.distributedlock.aspect;

import lombok.extern.slf4j.Slf4j;
import my.bning.distributedlock.annotation.DistributedLimit;
import my.bning.distributedlock.limit.DistributedLimitExecutor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 限流切面
 * @author Bernix Ning
 * @since 2019-02-15
 */
@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DistributedLimitAspect {

    final private DistributedLimitExecutor limitExecutor;

    @Autowired
    public DistributedLimitAspect(DistributedLimitExecutor limitExecutor) {
        this.limitExecutor = limitExecutor;
    }

    @Pointcut("@annotation(my.bning.distributedlock.annotation.DistributedLimit)")
    public void limit() {}

    @Before("limit()")
    public void limitBeforeExecute(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLimit limitAnnotation = method.getAnnotation(DistributedLimit.class);
        String key = limitAnnotation.key();
        int limit = limitAnnotation.limit();
        boolean notExceedlimit = limitExecutor.distributedLimit(key, String.valueOf(limit));
        if (!notExceedlimit) {
            throw new RuntimeException("exceeded limit");
        }
    }
}
