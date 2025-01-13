package com.insomnia.multiCountry.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Arrays;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
public class DES3 {

    static final char[] CS = "0123456789ABCDEF".toCharArray();

    private DES3() {
    }

    public static byte[] encrypt(byte[] key, byte[] str) throws Exception {
        if (key.length != 8) {
            throw new RuntimeException("key length err:" + key.length);
        } else {
            int needLen = str.length + 7 & -8;
            if (needLen != str.length) {
                str = Arrays.copyOf(str, needLen);
            }

            byte[] rs = new byte[str.length];
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey skey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
            cipher.init(1, skey);
            cipher.doFinal(str, 0, str.length, rs, 0);
            return rs;
        }
    }

    public static byte[] decrypt(byte[] key, byte[] str) throws Exception {
        if (key.length != 8) {
            throw new RuntimeException("key length err:" + key.length);
        } else {
            int needLen = str.length + 7 & -8;
            if (needLen != str.length) {
                str = Arrays.copyOf(str, needLen);
            }

            byte[] rs = new byte[str.length];
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey skey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
            cipher.init(2, skey);
            cipher.doFinal(str, 0, str.length, rs, 0);
            return rs;
        }
    }

    public static byte[] changeKey(byte[] bs) {
        byte[] rs = new byte[bs.length];

        for(int i = 0; i < bs.length; ++i) {
            int n = bs[i] & 255;
            int r = 0;

            for(int m = 0; m < 8; ++m) {
                r |= (n >> m & 1) << 7 - m;
            }

            rs[i] = (byte)r;
        }

        return rs;
    }

    public static byte[] des3encrypt(byte[] key, byte[] data) throws Exception {
        if (key.length != 16 && key.length != 24) {
            throw new RuntimeException("key length err:" + key.length);
        } else {
            byte[] k1 = new byte[8];
            byte[] k2 = new byte[8];
            System.arraycopy(key, 0, k1, 0, 8);
            System.arraycopy(key, 8, k2, 0, 8);
            byte[] k3;
            if (key.length == 16) {
                k3 = k1;
            } else {
                k3 = new byte[8];
                System.arraycopy(key, 16, k3, 0, 8);
            }

            data = encrypt(k1, data);
            data = decrypt(k2, data);
            data = encrypt(k3, data);
            return data;
        }
    }

    public static byte[] des3decrypt(byte[] key, byte[] data) throws Exception {
        if (key.length != 16 && key.length != 24) {
            throw new RuntimeException("key length err:" + key.length);
        } else {
            byte[] k1 = new byte[8];
            byte[] k2 = new byte[8];
            System.arraycopy(key, 0, k1, 0, 8);
            System.arraycopy(key, 8, k2, 0, 8);
            byte[] k3;
            if (key.length == 16) {
                k3 = k1;
            } else {
                k3 = new byte[8];
                System.arraycopy(key, 16, k3, 0, 8);
            }

            data = decrypt(k1, data);
            data = encrypt(k2, data);
            data = decrypt(k3, data);
            return data;
        }
    }

}
