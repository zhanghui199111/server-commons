package outfox.infra.server.commons.redis;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import outfox.infra.server.commons.BaseTestCase;

import java.util.Set;

/**
 * author: zhn4528
 * create: 2020/12/2 14:32
*/
@Ignore
public class RedisUtilTest extends BaseTestCase {

    @Autowired
    private RedisUtil<String> redisUtil;

    String testExistsKey = "testExistsKey";

    String testGetKey = "testGetKey";
    String testGetValue = "testGetValue123!@#$%^&*()_-=+;',./:";

    String testIncrByOneKey = "testIncrByOneKey";
    String testIncrByDeltaKey = "testIncrByDeltaKey";

    String testSetNonNullKey = "testSetNonNullKey";
    String testSetNonNullValue = "testSetNonNullValue123!@#$%^&*()_-=+;',./:";

    String testSetIfAbsentKey = "testSetIfAbsentKey";
    String testSetIfAbsentValue1 = "testSetIfAbsentValue1123!@#$%^&*()_-=+;',./:";
    String testSetIfAbsentValue2 = "testSetIfAbsentValue2123!@#$%^&*()_-=+;',./:";

    String testHgetKey = "testHgetKey";
    String testHgetHashKey = "testHgetHashKey";
    String testHgetHashValue = "testHgetHashValue123!@#$%^&*()_-=+;',./:";

    String testHdelKey = "testHdelKey";
    String testHdelHashKey = "testHdelHashKey";
    String testHdelHashValue = "testHdelHashValue123!@#$%^&*()_-=+;',./:";

    String testSaddSetKey = "testSaddSetKey";
    String testSaddSetValue1 = "testSaddSetValue1123!@#$%^&*()_-=+;',./:";
    String testSaddSetValue2 = "testSaddSetValue2123!@#$%^&*()_-=+;',./:";
    Set<String> testSaddValueSet = Sets.newSet(testSaddSetValue1, testSaddSetValue2);

    String testSaddKey = "testSaddKey";
    String testSaddValue = "testSaddValue123!@#$%^&*()_-=+;',./:";

    String testExpireKey = "testExpireKey";
    String testExpireValue = "testExpireValue123!@#$%^&*()_-=+;',./:";

    String testPexpireKey = "testPexpireKey";
    String testPexpireValue = "testPexpireValue123!@#$%^&*()_-=+;',./:";

    String testDeleteKey = "testDeleteKey";
    String testDeleteValue = "testDeleteValue123!@#$%^&*()_-=+;',./:";

    String testDeleteSetKey1 = "testDeleteSetKey1";
    String testDeleteSetKey2 = "testDeleteSetKey2";
    String testDeleteSetValue1 = "testDeleteSetValue1123!@#$%^&*()_-=+;',./:";
    String testDeleteSetValue2 = "testDeleteSetValue2123!@#$%^&*()_-=+;',./:";

    String testAcquireLockKey = "testAcquireLockKey";
    String testRelaseLockKey = "testRelaseLockKey";

    @Before
    public void init() {
        redisUtil.hset(testHdelKey, testHdelHashKey, testHdelHashValue);
    }

    @Test
    public void testExists() {
        boolean exists1 = redisUtil.exists(testExistsKey);
        Assert.assertFalse(exists1);
        boolean setResult = redisUtil.set(testExistsKey, "");
        Assert.assertTrue(setResult);
        boolean exists2 = redisUtil.exists(testExistsKey);
        Assert.assertTrue(exists2);
    }

    @Test
    public void testGet() {
        boolean result = redisUtil.set(testGetKey, testGetValue);
        Assert.assertTrue(result);
        String value = redisUtil.get(testGetKey);
        Assert.assertEquals(testGetValue, value);
    }

    @Test
    public void testIncrByOne() {
        long result1 = redisUtil.incr(testIncrByOneKey);
        Assert.assertEquals(result1, 1);
        long result2 = redisUtil.incr(testIncrByOneKey);
        Assert.assertEquals(result2, 2);
        long result3 = redisUtil.incr(testIncrByOneKey);
        Assert.assertEquals(result3, 3);
    }

    @Test
    public void setTestIncrByDelta() {
        long result1 = redisUtil.incr(testIncrByDeltaKey, 2);
        Assert.assertEquals(result1, 2);
        long result2 = redisUtil.incr(testIncrByDeltaKey, 5);
        Assert.assertEquals(result2, 7);
        long result3 = redisUtil.incr(testIncrByDeltaKey, 10);
        Assert.assertEquals(result3, 17);
    }

    @Test
    public void testSetNonNull() {
        long expireTimeInSeconds = 5L;
        boolean setNonResult = redisUtil
                .setNonNull(testSetNonNullKey, null, expireTimeInSeconds);
        Assert.assertFalse(setNonResult);
        boolean setNoNullResult = redisUtil
                .setNonNull(testSetNonNullKey, testSetNonNullValue, expireTimeInSeconds);
        Assert.assertTrue(setNoNullResult);
        String value1 = redisUtil.get(testSetNonNullKey);
        Assert.assertEquals(testSetNonNullValue, value1);
        try {
            Thread.sleep(expireTimeInSeconds * 1000L);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        String value2 = redisUtil.get(testSetNonNullKey);
        Assert.assertNull(value2);
    }

    @Test
    public void testSetIfAbsent() {
        long expireTimeInSeconds = 5L;
        boolean setNoAbsentResult = redisUtil
                .setIfAbsent(testSetIfAbsentKey, testSetIfAbsentValue1, expireTimeInSeconds);
        Assert.assertTrue(setNoAbsentResult);
        boolean setAbsentResult = redisUtil
                .setIfAbsent(testSetIfAbsentKey, testSetIfAbsentValue2, expireTimeInSeconds);
        Assert.assertFalse(setAbsentResult);
        String value1 = redisUtil.get(testSetIfAbsentKey);
        Assert.assertEquals(value1, testSetIfAbsentValue1);
        try {
            Thread.sleep(expireTimeInSeconds * 1000L);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        String value2 = redisUtil.get(testSetIfAbsentKey);
        Assert.assertNull(value2);
    }

    @Test
    public void testHget() {
        boolean result = redisUtil.hset(testHgetKey, testHgetHashKey, testHgetHashValue);
        Assert.assertTrue(result);
        String value = redisUtil.hget(testHgetKey, testHgetHashKey);
        Assert.assertEquals(value, testHgetHashValue);
    }

    @Test
    public void testHdel() {
        boolean result = redisUtil.hdel(testHdelKey, testHdelHashKey);
        Assert.assertTrue(result);
        String value = redisUtil.hget(testHdelKey, testHdelHashKey);
        Assert.assertNull(value);
    }

    @Test
    public void testSaddSet() {
        boolean result = redisUtil.sadd(testSaddSetKey, testSaddValueSet);
        Assert.assertTrue(result);
        boolean exist1 = redisUtil.smember(testSaddSetKey, testSaddSetValue1);
        Assert.assertTrue(exist1);
        boolean exist2 = redisUtil.smember(testSaddSetKey, testSaddSetValue2);
        Assert.assertTrue(exist2);
        boolean exist3 = redisUtil.smember(testSaddSetKey, "testSaddSetValue3");
        Assert.assertFalse(exist3);
        Set<String> valueSet = redisUtil.smembers(testSaddSetKey);
        Assert.assertEquals(valueSet, testSaddValueSet);
    }

    @Test
    public void testSaddValue() {
        boolean result = redisUtil.sadd(testSaddKey, testSaddValue);
        Assert.assertTrue(result);
        Boolean value = redisUtil.smember(testSaddKey, testSaddValue);
        Assert.assertTrue(value);
    }

    @Test
    public void testExpire() {
        long expireTime = 5L;
        boolean setResult = redisUtil.set(testExpireKey, testExpireValue);
        Assert.assertTrue(setResult);
        boolean expireResult = redisUtil.expire(testExpireKey, expireTime);
        Assert.assertTrue(expireResult);
        try {
            Thread.sleep(expireTime * 1000L);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        boolean exist = redisUtil.exists(testExpireKey);
        Assert.assertFalse(exist);
    }

    @Test
    public void testPexpire() {
        long expireTime = 5000L;
        boolean setResult = redisUtil.set(testPexpireKey, testPexpireValue);
        Assert.assertTrue(setResult);
        boolean expireResult = redisUtil.pexpire(testPexpireKey, expireTime);
        Assert.assertTrue(expireResult);
        try {
            Thread.sleep(expireTime);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        boolean exist = redisUtil.exists(testPexpireKey);
        Assert.assertFalse(exist);
    }

    @Test
    public void testDel() {
        boolean setResult = redisUtil.set(testDeleteKey, testDeleteValue);
        Assert.assertTrue(setResult);
        boolean deleteResult = redisUtil.del(testDeleteKey);
        Assert.assertTrue(deleteResult);
        boolean exist = redisUtil.exists(testDeleteKey);
        Assert.assertFalse(exist);
    }

    @Test
    public void testDelSet() {
        boolean setResult1 = redisUtil.set(testDeleteSetKey1, testDeleteSetValue1);
        boolean setResult2 = redisUtil.set(testDeleteSetKey2, testDeleteSetValue2);
        Assert.assertTrue(setResult1);
        Assert.assertTrue(setResult2);
        long deleteResult = redisUtil
                .del(Sets.newSet(testDeleteSetKey1, testDeleteSetKey2, "", "abcdefg"));
        Assert.assertEquals(deleteResult, 2L);
        long deleteResult1 = redisUtil
                .del(Sets.newSet("abcd", "1234"));
        Assert.assertEquals(deleteResult1, 0L);
        boolean exist1 = redisUtil.exists(testDeleteSetKey1);
        boolean exist2 = redisUtil.exists(testDeleteSetKey2);
        Assert.assertFalse(exist1);
        Assert.assertFalse(exist2);
    }

    @Test
    public void testAcquireLocK() {
        long expireTime = 5L;
        boolean acquireResult1 = redisUtil.acquireLock(null, expireTime);
        Assert.assertFalse(acquireResult1);
        boolean acquireResult2 = redisUtil.acquireLock(testAcquireLockKey, null);
        Assert.assertFalse(acquireResult2);
        boolean acquireResult3 = redisUtil.acquireLock(testAcquireLockKey, expireTime);
        Assert.assertTrue(acquireResult3);

        boolean checkLockResult1 = redisUtil.checkLock(testAcquireLockKey);
        Assert.assertTrue(checkLockResult1);

        try {
            Thread.sleep(expireTime * 1000L);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        boolean checkLockResult2 = redisUtil.checkLock(testAcquireLockKey);
        Assert.assertFalse(checkLockResult2);
    }

    @Test
    public void testRelaseLock() {
        long expireTime = 5L;
        boolean acquireResult = redisUtil.acquireLock(testRelaseLockKey, expireTime);
        Assert.assertTrue(acquireResult);

        boolean releaseResult1 = redisUtil.releaseLock(null);
        Assert.assertFalse(releaseResult1);

        boolean releaseResult2 = redisUtil.releaseLock(testRelaseLockKey);
        Assert.assertTrue(releaseResult2);

        boolean checkLockResult = redisUtil.checkLock(testRelaseLockKey);
        Assert.assertFalse(checkLockResult);
    }

    @After
    public void teardown() {
        redisUtil.del(testExistsKey);
        redisUtil.del(testGetKey);
        redisUtil.del(testIncrByOneKey);
        redisUtil.del(testIncrByDeltaKey);
        redisUtil.del(testSetNonNullKey);
        redisUtil.del(testSetIfAbsentKey);
        redisUtil.del(testHgetKey);
        redisUtil.del(testHdelKey);
        redisUtil.del(testSaddSetKey);
        redisUtil.del(testSaddKey);
        redisUtil.del(testExpireKey);
        redisUtil.del(testPexpireKey);
        redisUtil.del(testDeleteKey);
        redisUtil.del(testDeleteSetKey1);
        redisUtil.del(testDeleteSetKey2);
        redisUtil.del(testAcquireLockKey);
        redisUtil.del(testRelaseLockKey);
    }

}
