package my.bning.distributedlock.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author Bernix Ning
 * @since 2019-02-15
 */
@Slf4j
@Configuration
@SuppressWarnings("unchecked")
public class BeanConfig {

    /**
     * The script result type should be one of Long, Boolean, List, or a deserialized value type.
     * It can also be null if the script returns a throw-away status (specifically, OK).
     * @return
     */
    @Bean("limitScript")
    public RedisScript<Long> limitScript() {
        RedisScript<Long> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("scripts/limit.lua"));
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Long.class);
        } catch (Exception e) {
            log.error(">> error initializing limit script", e);
        }

        return redisScript;
    }

    @Bean("lockScript")
    public RedisScript<Boolean> lockScript() {
        RedisScript<Boolean> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("scripts/lock.lua"));
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Boolean.class);
        } catch (Exception e) {
            log.error(">> error initializing lock script", e);
        }

        return redisScript;
    }

    @Bean("unlockScript")
    public RedisScript<Long> unlockScript() {
        RedisScript<Long> redisScript = null;
        try {
            ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("scripts/unlock.lua"));
            redisScript = RedisScript.of(scriptSource.getScriptAsString(), Long.class);
        } catch (Exception e) {
            log.error(">> error initializing unlock script", e);
        }

        return redisScript;
    }

    @Bean("limitAnother")
    public RedisScript<Long> limitAnother() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/limit.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
