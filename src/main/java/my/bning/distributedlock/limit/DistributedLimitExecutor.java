package my.bning.distributedlock.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 执行limit脚本
 * @author Bernix Ning
 * @since 2019-02-15
 */
@Slf4j
@Component
public class DistributedLimitExecutor {

    final private StringRedisTemplate stringRedisTemplate;
    final private RedisScript<Long> limitScript;

    @Autowired
    public DistributedLimitExecutor(StringRedisTemplate stringRedisTemplate, @Qualifier("limitScript") RedisScript<Long> limitScript) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.limitScript = limitScript;
    }

    public boolean distributedLimit(String key, String limit) {
        Long id = 0L;
        try {
            id = stringRedisTemplate.execute(limitScript, Collections.singletonList(key), limit);
            if (id == 0) {
                log.info(">> [limit] exceeded limit");
            } else {
                log.info(">> [limit] request count: {}", id);
            }
        } catch (Exception e) {
            log.error(">> [limit] error", e);
        }

        return id > 0L;
    }
}
