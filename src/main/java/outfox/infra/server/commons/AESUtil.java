package outfox.infra.server.commons;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AESUtil {
    private static final String AES = "AES";
    private static final String AES_KEY = "B59A4D87032B795D8539F8BDF28B14FC";

    public static String encryptStr(String content) throws Exception {
        return AESUtil.byte2hex(AESUtil.encrypt(AESUtil.hex2byte(AES_KEY), content.getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] encrypt(byte[] kv, byte[] src) throws Exception {
        SecretKey key = new SecretKeySpec(kv, AES);
        Cipher cp = Cipher.getInstance(AES);
        cp.init(Cipher.ENCRYPT_MODE, key);
        return cp.doFinal(src);
    }

    public static String decryptStr(String encryptStr) throws Exception {
        return AESUtil.decrypt(AESUtil.hex2byte(AES_KEY), AESUtil.hex2byte(encryptStr));
    }

    private static String decrypt(byte[] kv, byte[] srcString) throws Exception {
        SecretKey key = new SecretKeySpec(kv, AES);
        Cipher cp = Cipher.getInstance(AES);
        cp.init(Cipher.DECRYPT_MODE, key);
        return new String(cp.doFinal(srcString), StandardCharsets.UTF_8);
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (byte value : b) {
            stmp = (Integer.toHexString(value & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(String s) {
        byte[] b = s.getBytes();
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("Illegal length");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}