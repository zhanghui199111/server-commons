package outfox.infra.server.commons;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Author: gongzhanjing
 * @Date: 2020/12/10
 */
public class MathUtilsTest {

    @Test
    public void testGetMask() {
        Stream.iterate(1, o -> o + 1)
                .limit(10)
                .forEach(o -> {
                    System.out.println("o=" + o + ",mast=" + MathUtils.getMask(o));
                });

    }

    @Test
    @Ignore
    public void testRandomBool() {
        // 数据总量
        int size = 10_000_000;
        // 误差范围 true/false差值
        int deviation = 5000;
        Map<Boolean, Long> resultMap = Stream.iterate(1, o -> o + 1)
                .limit(size)
                .collect(Collectors.partitioningBy(o -> MathUtils.randomBool(), Collectors.counting()));

        System.out.println("result=" + Math.abs(resultMap.get(Boolean.TRUE) - resultMap.get(Boolean.FALSE))
                + ",deviation=" + deviation);

        Assert.assertTrue(Math.abs(resultMap.get(Boolean.TRUE) - resultMap.get(Boolean.FALSE)) <= deviation);
    }

    @Test
    public void testBit() {
        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.checkBit(-2, 1));
        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.setAssignBitIsOne(-2, 1));
        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.setAssignBitIsZero(-2, 1));

        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.checkBit(2, -1));
        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.setAssignBitIsOne(2, -1));
        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.setAssignBitIsZero(2, -1));

        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.checkBit(-2, -1));
        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.setAssignBitIsOne(-2, -1));
        Assert.assertThrows(IllegalArgumentException.class, () -> MathUtils.setAssignBitIsZero(-2, -1));


        Assert.assertFalse(MathUtils.checkBit(4, 1));
        Assert.assertTrue(MathUtils.checkBit(4, 2));


        Assert.assertEquals(3, MathUtils.setAssignBitIsOne(2, 0));
        Assert.assertEquals(2, MathUtils.setAssignBitIsZero(2, 0));

        Assert.assertEquals(2, MathUtils.setAssignBitIsOne(2, 1));
        Assert.assertEquals(0, MathUtils.setAssignBitIsZero(2, 1));

        Assert.assertEquals(1, MathUtils.setAssignBitIsOne(0, 0));
        Assert.assertEquals(0, MathUtils.setAssignBitIsZero(0, 0));

        Assert.assertEquals(2, MathUtils.setAssignBitIsOne(0, 1));
        Assert.assertEquals(0, MathUtils.setAssignBitIsZero(0, 1));
    }

    @Test
    public void testGetSpecifiedBitResult() {

        Assert.assertThrows(IllegalArgumentException.class,
                () -> MathUtils.getSpecifiedBitResult(1, 1, false));
        Assert.assertThrows(IllegalArgumentException.class,
                () -> MathUtils.getSpecifiedBitResult(-1, 1, false));
        Assert.assertThrows(IllegalArgumentException.class,
                () -> MathUtils.getSpecifiedBitResult(1, -1, false));
        Assert.assertThrows(IllegalArgumentException.class,
                () -> MathUtils.getSpecifiedBitResult(-1, -1, false));


        List<Integer> result = Lists.newArrayList(0);
        List<Integer> specifiedBitResult = MathUtils.getSpecifiedBitResult(1, 0, false);
        System.out.println(specifiedBitResult);
        Assert.assertEquals(result.size(), specifiedBitResult.size());
        specifiedBitResult.forEach(o -> Assert.assertTrue(result.contains(o)));


        List<Integer> result2 = Lists.newArrayList(4, 5, 6, 7);
        List<Integer> specifiedBitResult2 = MathUtils.getSpecifiedBitResult(3, 2, true);
        System.out.println(specifiedBitResult2);
        Assert.assertEquals(result2.size(), specifiedBitResult2.size());
        specifiedBitResult2.forEach(o -> Assert.assertTrue(result2.contains(o)));


        List<Integer> result3 = Lists.newArrayList(0, 2);
        List<Integer> specifiedBitResult3 = MathUtils.getSpecifiedBitResult(2, 0, false);
        System.out.println(specifiedBitResult3);
        Assert.assertEquals(result3.size(), specifiedBitResult3.size());
        specifiedBitResult3.forEach(o -> Assert.assertTrue(result3.contains(o)));
    }
}