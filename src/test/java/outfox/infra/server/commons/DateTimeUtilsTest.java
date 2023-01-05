package outfox.infra.server.commons;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: gongzhanjing
 * @Date: 2020/12/10
 */
public class DateTimeUtilsTest {

    @Test
    public void testGetDayStartTime() {
        Assert.assertEquals(1607529600000L, DateTimeUtils.getDayStartTime(1607596363600L));
        Assert.assertEquals(1609344000000L, DateTimeUtils.getDayStartTime(1609344000000L));
        Assert.assertEquals(1609344000000L, DateTimeUtils.getDayStartTime(1609344000001L));
        Assert.assertEquals(1609430400000L, DateTimeUtils.getDayStartTime(1609430400011L));
    }

    @Test
    public void testGetDayEndTime() {
        Assert.assertEquals(1607615999999L, DateTimeUtils.getDayEndTime(1607596363600L));
        Assert.assertEquals(1609430399999L, DateTimeUtils.getDayEndTime(1609344000000L));
        Assert.assertEquals(1609430399999L, DateTimeUtils.getDayEndTime(1609344000001L));
        Assert.assertEquals(1609516799999L, DateTimeUtils.getDayEndTime(1609430400011L));
        Assert.assertEquals(1607615999999L, DateTimeUtils.getDayEndTime(1607615999999L));
        Assert.assertEquals(1607615999999L, DateTimeUtils.getDayEndTime(1607615999998L));
    }

    @Test
    public void testGetScheduleTime() {
        Assert.assertEquals(1607682763600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 1, 0, 0)
        );
        Assert.assertEquals(1607599963600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 0, 1, 0)
        );
        Assert.assertEquals(1607596423600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 0, 0, 1)
        );
        Assert.assertEquals(1607600023600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 0, 1, 1)
        );
        Assert.assertEquals(1607682823600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 1, 0, 1)
        );
        Assert.assertEquals(1607686363600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 1, 1, 0)
        );
        Assert.assertEquals(1607686423600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 1, 1, 1)
        );
        Assert.assertEquals(1609410763600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 21, 0, 0)
        );
        Assert.assertEquals(1609497163600L,
                DateTimeUtils.getScheduleTime(1607596363600L, 22, 0, 0)
        );
    }


    @Test
    public void testWaitUntilNextTime() {
        long currentTime = System.currentTimeMillis();
        Assert.assertEquals(DateTimeUtils.waitUntilNextTime(currentTime), currentTime + 1);
    }

    @Test
    public void testGetMonthStartTime() {
        // 时间戳为2021-08-17 00:00:00
        long timeStamp = 1629129600000L;
        long monthStartTime = DateTimeUtils.getMonthStartTime(timeStamp);
        Assert.assertEquals(1627747200000L, monthStartTime);
    }
}