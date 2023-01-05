package outfox.infra.server.commons.lock.api;

/**
 * author: zhn4528
 * create: 2021/4/13 14:35
*/
public interface LockUtil {

    boolean acquireLock(String key, long lockTime);

    boolean releaseLock(String key);

    boolean checkLock(String key);

}
