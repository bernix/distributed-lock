package my.bning.distributedlock.controller;

import lombok.extern.slf4j.Slf4j;
import my.bning.distributedlock.annotation.DistributedLimit;
import my.bning.distributedlock.lock.DistributedLockExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Bernix Ning
 * @since 2019-02-15
 */
@Slf4j
@RestController
public class DemoController {

    final private DistributedLockExecutor lock;

    @Autowired
    public DemoController(DistributedLockExecutor lock) {
        this.lock = lock;
    }

    @PostMapping("/distributed-lock")
    public String distributedLock(String key, String uuid, String secondsToLock, String userId) {
        // String uuid = UUID.randomUUID().toString();
        boolean locked = false;
        try {
            locked = lock.distributedLock(key, uuid, secondsToLock);
            if (locked) {
                log.info(">> userId:{} is locked - uuid:{}", userId, uuid);
                log.info(">> ...doing business logic...");
                TimeUnit.MICROSECONDS.sleep(3000);
            } else {
                log.info(">> userId:{} is not locked - uuid:{}", userId, uuid);
            }
        } catch (Exception e) {
            log.error(">> lock error", e);
        } finally {
            if (locked) {
                lock.distributedUnlock(key, uuid);
            }
        }
        return "ok";
    }

    @PostMapping("/distributed-limit")
    @DistributedLimit(key = "xxx-limit", limit = 10)
    public String distributedLimit(String userId) {
        log.info(">> it's running with userId:{}", userId);
        return "ok";
    }
}
