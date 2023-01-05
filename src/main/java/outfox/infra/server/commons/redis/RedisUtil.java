package outfox.infra.server.commons.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * author: zhn4528
 * create: 2020/12/2 11:39
 */
@Slf4j
@Component
@ConditionalOnClass(RedisOperations.class)
public class RedisUtil<T> {

    @Value("${infra.redis.key.prefix}")
    String redisKeyPrefix;

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * get value
     *
     * @param key redis key
     * @return cache value object type
     */
    public T get(final String key) {
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplate.opsForValue();
            T data = operations.get(wholeKey);
            log.debug("get redis::key:{} value::{}", wholeKey, data);
            return data;
        } catch (Exception e) {
            log.error("RedisUtil get key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * exist key
     *
     * @param key redis key
     * @return exist: true/not exist: false
     */
    public Boolean exists(final String key) {
        String wholeKey = getKey(key);
        return redisTemplate.hasKey(wholeKey);
    }

    /**
     * set cache
     *
     * @param key   redis key
     * @param value cache value
     * @return success: true failure: false
     */
    public boolean set(final String key, T value) {
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplate.opsForValue();
            operations.set(wholeKey, value);
            log.debug("set redis::key:{} value::{}", wholeKey, operations.get(wholeKey));
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * incr by 1
     *
     * @param key redis key
     * @return value
     */
    public long incr(final String key) {
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplate.opsForValue();
            return operations.increment(wholeKey);
        } catch (Exception e) {
            log.error("RedisUtil set key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return -1;
        }
    }

    /**
     * incr by delta
     *
     * @param key   redis key
     * @param delta incr delta
     * @return value
     */
    public long incr(final String key, long delta) {
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplate.opsForValue();
            return operations.increment(wholeKey, delta);
        } catch (Exception e) {
            log.error("RedisUtil set key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return -1;
        }
    }

    /**
     * set cache, value not null
     *
     * @param key     redis key
     * @param value   cache value
     * @param seconds expire time in seconds
     * @return success: true failure: false
     */
    public boolean setNonNull(final String key, T value, long seconds) {
        if (Objects.isNull(value)) {
            return false;
        }
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplate.opsForValue();
            operations.set(wholeKey, value, seconds, TimeUnit.SECONDS);
            log.debug("set redis::key:{} value::{}", wholeKey, operations.get(wholeKey));
            return true;
        } catch (Exception e) {
            log.error("RedisUtil set key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * set cache only when key is not absent
     *
     * @param key     redis key
     * @param value   cache value
     * @param seconds expire time in seconds
     * @return success: true failure: false
     */
    public Boolean setIfAbsent(final String key, T value, long seconds) {
        if (Objects.isNull(value)) {
            return false;
        }
        String wholeKey = getKey(key);
        try {
            ValueOperations<String, T> operations = redisTemplate.opsForValue();
            log.debug("set redis::key:{} value::{}", wholeKey, operations.get(wholeKey));
            return operations.setIfAbsent(wholeKey, value, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("RedisUtil set key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return Boolean.FALSE;
        }
    }

    /**
     * add key and value to redis hash
     *
     * @param key       redis hash name
     * @param hashKey   hash key
     * @param hashValue hash value
     * @return success: true failure: false
     */
    public Boolean hset(final String key, String hashKey, T hashValue) {
        if (Objects.isNull(hashValue)) {
            return false;
        }
        String wholeKey = getKey(key);
        try {
            HashOperations<String, String, T> operations = redisTemplate.opsForHash();
            operations.put(wholeKey, hashKey, hashValue);
            log.debug("hadd redis::key:{} hashKey:{} value::{}", wholeKey, hashKey, hashValue);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil hadd key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * get value of key from redis hash
     *
     * @param key     redis hash name
     * @param hashKey redis hash key
     * @return hash value
     */
    public T hget(final String key, String hashKey) {
        String wholeKey = getKey(key);
        try {
            HashOperations<String, String, T> operations = redisTemplate.opsForHash();
            T hashValue = operations.get(wholeKey, hashKey);
            log.debug("hget redis::key:{} hashKey:{} value::{}", wholeKey, hashKey, hashValue);
            return hashValue;
        } catch (Exception e) {
            log.error("RedisUtil hadd key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * delete key from hash
     *
     * @param key     hash name
     * @param hashKey hash key
     * @return success: true failure: false
     */
    public boolean hdel(final String key, String hashKey) {
        String wholeKey = getKey(key);
        try {
            HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
            operations.delete(wholeKey, hashKey);
            log.debug("hdel redis::key:{} hashKey:{}", wholeKey, hashKey);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil hdel key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * add set to redis set
     *
     * @param key      redis key
     * @param valueSet value set
     * @return success: true failure: false
     */
    public boolean sadd(final String key, Set<T> valueSet) {
        String wholeKey = getKey(key);
        try {
            SetOperations operations = redisTemplate.opsForSet();
            Object[] valueArray = valueSet.toArray(new Object[valueSet.size()]);
            operations.add(wholeKey, valueArray);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil sadd key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * add value to redis set
     *
     * @param key   redis set name
     * @param value redis value
     * @return success: true failure: false
     */
    public boolean sadd(final String key, T value) {
        String wholeKey = getKey(key);
        try {
            SetOperations<String, T> operations = redisTemplate.opsForSet();
            operations.add(wholeKey, value);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil sadd key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * judge value exists in set
     *
     * @param key   redis set name
     * @param value value
     * @return success: true failure: false
     */
    public Boolean smember(final String key, T value) {
        String wholeKey = getKey(key);
        SetOperations<String, T> operations = redisTemplate.opsForSet();
        return operations.isMember(wholeKey, value);
    }

    /**
     * return all values in redis set
     *
     * @param key redis key
     * @return value set
     */
    public Set<T> smembers(final String key) {
        String wholeKey = getKey(key);
        SetOperations<String, T> operations = redisTemplate.opsForSet();
        return operations.members(wholeKey);
    }

    /**
     * set expire time in seconds for key
     *
     * @param key     redis key
     * @param seconds expire time in seconds
     * @return success: true failure: false
     */
    public boolean expire(final String key, Long seconds) {
        String wholeKey = getKey(key);
        try {
            BoundValueOperations boundValueOperations = redisTemplate
                    .boundValueOps(wholeKey);
            boundValueOperations.expire(seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil expire key:{} expire time failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * set expire time in milliseconds for key
     *
     * @param key          redis key
     * @param milliseconds expire time in milliseconds
     * @return success: true failure: false
     */
    public boolean pexpire(final String key, Long milliseconds) {
        String wholeKey = getKey(key);
        try {
            BoundValueOperations boundValueOperations = redisTemplate
                    .boundValueOps(wholeKey);
            boundValueOperations.expire(milliseconds, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            log.error("RedisUtil pexpire key:{} expire time failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * delete key
     *
     * @param key redis key
     * @return success: true failure: false
     */
    public Boolean del(final String key) {
        String wholeKey = getKey(key);
        try {
            return redisTemplate.delete(wholeKey);
        } catch (Exception e) {
            log.error("RedisUtil delete key:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return Boolean.FALSE;
        }
    }

    /**
     * delete keys
     *
     * @param keys key set
     */
    public Long del(final Set<String> keys) {
        Set<String> wholeKeys = keys
                .stream()
                .map(this::getKey)
                .collect(Collectors.toSet());
        return redisTemplate.delete(wholeKeys);
    }

    /**
     * acquire redis lock
     *
     * @param key        lock key value
     * @param expireTime lock expire time in seconds
     * @return success: true/ failure: false
     */
    public boolean acquireLock(final String key, Long expireTime) {
        boolean result;
        if (null == key || expireTime == null) {
            return false;
        }
        String wholeKey = getKey(key);
        try {
            Object execute = redisTemplate.execute(
                    (RedisCallback) redisConnection -> {
                        RedisSerializer stringSerializer = redisTemplate
                                .getKeySerializer();
                        Object rlt = redisConnection.execute(
                                "SET", stringSerializer.serialize(wholeKey),
                                stringSerializer.serialize("1"),
                                stringSerializer.serialize("EX"),
                                stringSerializer.serialize(expireTime > 0 ?
                                        String.valueOf(expireTime) :
                                        ""),
                                stringSerializer.serialize("NX")
                        );
                        return rlt;
                    }
            );
            result = execute != null && execute.equals("OK");
        } catch (Exception e) {
            log.error("RedisUtil acquire Lock:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            result = false;
        }
        return result;
    }

    /**
     * release redis lock
     *
     * @param key lock key
     * @return success: true / failure: false
     */
    public boolean releaseLock(final String key) {
        String wholeKey = getKey(key);
        try {
            return redisTemplate.delete(wholeKey);
        } catch (Exception e) {
            log.error("RedisUtil release Lock:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * check redis lock
     *
     * @param key lock key
     * @return exist: true / not exist: false
     */
    public boolean checkLock(final String key) {
        String wholeKey = getKey(key);
        try {
            return redisTemplate.hasKey(wholeKey);
        } catch (Exception e) {
            log.error("RedisUtil check Lock:{} failure::{}", wholeKey, ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    private String getKey(String key) {
        if (StringUtils.isBlank(redisKeyPrefix)) {
            throw new RuntimeException("RedisUtil::redisKeyPrefix not configured.");
        }
        return redisKeyPrefix + "::" + key;
    }
}
