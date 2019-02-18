package my.bning.distributedlock.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Bernix Ning
 * @since 2019-02-15
 */
@Slf4j
@Component
public class DistributedLockExecutor {

    final private StringRedisTemplate stringRedisTemplate;
    final private RedisScript<Boolean> lockScript;
    final private RedisScript<Long> unlockScript;

    @Autowired
    public DistributedLockExecutor(StringRedisTemplate stringRedisTemplate,
                                   @Qualifier("lockScript") RedisScript<Boolean> lockScript,
                                   @Qualifier("unlockScript") RedisScript<Long> unlockScript) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockScript = lockScript;
        this.unlockScript = unlockScript;
    }

    public boolean distributedLock(String key, String uuid, String secondsToLock) {
        boolean locked = false;
        try {
            String millSeconds = String.valueOf(Integer.parseInt(secondsToLock) * 1000);
            Boolean lockedRet = stringRedisTemplate.execute(lockScript, Collections.singletonList(key), uuid, millSeconds);
            log.info(">> [lock] distributedLock.key:{} - uuid:{} - timeToLock:{} - locked:{} - milliSeconds:{}",
                    key, uuid, secondsToLock, lockedRet, millSeconds);
            locked = lockedRet == null ? false : lockedRet;
        } catch (Exception e) {
            log.error(">> [lock] error", e);
        }
        return locked;
    }

    public void distributedUnlock(String key, String uuid) {
        Long unlocked = stringRedisTemplate.execute(unlockScript, Collections.singletonList(key), uuid);
        log.info(">> [unlock] distributedLock.key:{} - uuid:{} - unlocked:{}", key, uuid, unlocked);
    }
}
