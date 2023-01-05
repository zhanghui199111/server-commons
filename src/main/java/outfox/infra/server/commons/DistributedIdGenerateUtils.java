package outfox.infra.server.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import outfox.infra.server.commons.redis.RedisUtil;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 分布式唯一id生成器(参考https://github.com/twitter-archive/snowflake/tree/scala_28)
 * 总长度53位(41位时间戳+5位服务id+7位自增序列号)
 *
 * @Author: gongzhanjing
 * @Date: 2019/10/24
 */
@Slf4j
@Component
@ConditionalOnClass(RedisUtil.class)
public class DistributedIdGenerateUtils {
    private static final String WORK_ID = "workerId";
    // 时间戳所占位数
    private static final long TIMESTAMP_BITS = 41L;
    // 服务ID所占位数
    private final static long WORKER_ID_BITS = 5L;
    // 自增序列所占位数
    private static final long SEQUENCE_BITS = 7L;

    // 当前服务id
    private static long workerId;
    // 上次产生id的时间戳
    private static long lastTimestamp = System.currentTimeMillis();
    // 同一毫秒内生成的序列号
    private static long sequence;

    @Autowired
    private RedisUtil<Integer> redisUtil;

    public static synchronized long generateId() {
        long currentMillis = System.currentTimeMillis();

        if (lastTimestamp == currentMillis) {
            // 同一毫秒,序列值自增
            sequence = (sequence + 1) & MathUtils.getMask(SEQUENCE_BITS);
            if (0L == sequence) {
                // 序列值自增满,则等待下一毫秒
                currentMillis = DateTimeUtils.waitUntilNextTime(currentMillis);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = currentMillis;

        long time = (currentMillis) & MathUtils.getMask(TIMESTAMP_BITS);
        // 组合返回唯一Id
        return (time << (WORKER_ID_BITS + SEQUENCE_BITS)) | (workerId << SEQUENCE_BITS) | (sequence);
    }

    @PostConstruct
    public void initWorkId() {
        String redisKey = "DistributedIdGenerateUtils::workerId=" + WORK_ID;
        Integer workId = redisUtil.get(redisKey);
        if (Objects.isNull(workId)) {
            workId = 0;
        } else {
            workId = Math.toIntExact((workId + 1) & MathUtils.getMask(WORKER_ID_BITS));
        }
        redisUtil.set(redisKey, workId);
        workerId = workId;
        log.info("DistributedIdGenerateUtils::initWorkId:workerId={}", workerId);
    }
}
