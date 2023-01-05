package outfox.infra.server.commons.lock.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import outfox.infra.server.commons.lock.api.LockUtil;
import outfox.infra.server.commons.redis.RedisUtil;

/**
 * author: zhn4528
 * create: 2021/4/13 14:35
*/
@Component
@ConditionalOnClass(RedisUtil.class)
public class RedisLockUtil implements LockUtil {

    @Autowired
    private RedisUtil<String> redisUtil;

    @Override
    public boolean acquireLock(String key, long lockTime) {
        return redisUtil.acquireLock(key, lockTime);
    }

    @Override
    public boolean releaseLock(String key) {
        return redisUtil.releaseLock(key);
    }

    @Override
    public boolean checkLock(String key) {
        return redisUtil.checkLock(key);
    }

}
