package outfox.infra.server.commons;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: gongzhanjing
 * @Date: 2020/12/10
 */
public class AESUtilTest {

    String str = "motian";

    @Test
    public void testAES() throws Exception {
        Assert.assertEquals(str, AESUtil.decryptStr(AESUtil.encryptStr(str)));
        Assert.assertEquals(StringUtils.EMPTY, AESUtil.decryptStr(AESUtil.encryptStr(StringUtils.EMPTY)));
        Assert.assertNotEquals(str + "1", AESUtil.decryptStr(AESUtil.encryptStr(str)));
    }
}