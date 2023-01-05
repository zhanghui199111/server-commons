package outfox.infra.server.commons;


import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数字处理工具
 * author: gongzhanjing
 */
public class MathUtils {

    /**
     * 获取指定数的掩码
     */
    public static long getMask(long bits) {
        return (1L << bits) - 1L;
    }

    /**
     * 随机返回true/false
     */
    public static boolean randomBool() {
        return Math.random() < 0.5;
    }

    /**
     * 检测指定数中的指定位置是否为1
     *
     * @param number 输入的整数(>0)
     * @param bit    指定位数(从第0位开始)
     * @return true是1/false是0
     */
    public static boolean checkBit(int number, int bit) {
        Assert.isTrue((number >= 0) && (bit >= 0), "must be greater than 0.");

        return ((number >> bit) & 1) == 1;
    }

    /**
     * 将指定数中的指定位置为指定值
     *
     * @param number 输入的整数(>0)
     * @param bit    指定位数(从第0位开始)
     * @param result true置为1,false置为0
     * @return 更新后的数
     */
    public static int setAssignBit(int number, int bit, boolean result) {
        if (result) {
            return setAssignBitIsOne(number, bit);
        } else {
            return setAssignBitIsZero(number, bit);
        }
    }

    /**
     * 将指定数中的指定位置为1
     *
     * @param number 输入的整数(>0)
     * @param bit    指定位数(从第0位开始)
     * @return 更新后的数
     */
    public static int setAssignBitIsOne(int number, int bit) {
        Assert.isTrue((number >= 0) && (bit >= 0), "must be greater than 0.");

        number |= (1 << bit);
        return number;
    }

    /**
     * 将指定数中的指定位置为0
     *
     * @param number 输入的整数(>0)
     * @param bit    指定位数(从第0位开始)
     * @return 更新后的数
     */
    public static int setAssignBitIsZero(int number, int bit) {
        Assert.isTrue((number >= 0) && (bit >= 0), "must be greater than 0.");

        int mask = ~(1 << bit);
        return (number & (mask));
    }

    /**
     * 获取指定二进制位数中对应的bit位为指定值的结果集
     *
     * @param binaryDigits 二进制位数,例：5表示范围[00000,11111]
     * @param bit          指定位数(从第0位开始)
     * @param bitValues    指定位数的值,true表示1,false表示0
     * @return 更新后的数
     */
    public static List<Integer> getSpecifiedBitResult(int binaryDigits, int bit, boolean bitValues) {
        Assert.isTrue((binaryDigits >= 0) && (bit >= 0), "must be greater than 0.");
        Assert.isTrue((binaryDigits - 1) >= bit, "parameter error.");

        int maxValue = setAssignBitIsOne(0, binaryDigits) - 1;

        return Stream.iterate(0, o -> o + 1)
                .limit(maxValue + 1)
                .filter(o -> checkBit(o, bit) == bitValues)
                .collect(Collectors.toList());

    }
}
