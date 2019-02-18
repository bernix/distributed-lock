package my.bning.distributedlock.annotation;

import java.lang.annotation.*;

/**
 * @author Bernix Ning
 * @since 2019-02-15
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLimit {

    /**
     * 限流的key
     * @return
     */
    String key() default "limit";

    /**
     * 结合limit.lua里的EXPIRE的秒数，限定该秒数内允许多少个请求
     * @return
     */
    int limit() default 1;
}
