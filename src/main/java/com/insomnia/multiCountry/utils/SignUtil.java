package com.insomnia.multiCountry.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
public class SignUtil {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    /**
     * 加密/解密算法/工作模式/填充方法
     */
    public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";

    /**
     * 构建签名内容
     *
     * @param map
     * @return
     */
    public static String buildSignContent(Map<String, Object> map) {
        Map<String, Object> sortedParams = new TreeMap<>(map);
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = sortedParams.get(key);
            if (key != null && !"".equals(key) && value != null) {
                content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
            }
        }
        return content.toString();
    }


    /**
     * 数字签名  建议使用SignUtils签名类
     *
     * @param encryData  加密数据
     * @param privateKey 私钥（Base64编码）
     *                   summer
     *                   2017年6月7日 下午2:24:55
     */
    public static String sign(String encryData, String privateKey) {
        byte[] data = encryData.getBytes();
        PrivateKey privateK = getPrivateKey(privateKey);
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(data);
            return Base64Utils.encode(signature.sign());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * 校验数字签名 建议使用SignUtils签名类
     * </p>
     *
     * @param
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     */
    public static boolean verify(String encryData, String publicKey, String sign) {
        byte[] data = encryData.getBytes();
        PublicKey publicK = getPublicKey(publicKey);
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(data);
            byte[] bsign = Base64Utils.decode(sign.getBytes());
            return signature.verify(bsign);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64字符串转换为私钥
     * summer
     * 2017年6月7日 上午10:45:17
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        byte[] keyBytes = Base64Utils.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            return privateK;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64字符串转换为公钥
     * summer
     * 2017年6月7日 上午10:45:41
     */
    public static PublicKey getPublicKey(String publicKey) {
        byte[] keyBytes = Base64Utils.decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey pubkey = keyFactory.generatePublic(keySpec);
            return pubkey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param data 待解密数据 BASE64编码
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static String decrypt(String data, String key) {
        if (data == null || data.trim().length() == 0) {
            return data;
        }
        byte[] decrybytes = Base64Utils.decode(data);
        return decryptBytes(decrybytes, key);
    }

    public static String decryptBytes(byte[] decrybytes, String key) {
        SecretKeySpec k = codeToKey(key);
        return decrypt(CIPHER_ALGORITHM_ECB, k, decrybytes);
    }

    /**
     * 通过Base64字符串获取密钥
     *
     * @param key 密钥
     * @return Key 密钥
     */
    private static SecretKeySpec codeToKey(String key) {
        byte[] keyBytes = Base64Utils.decode(key);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        return secretKey;
    }

    private static String decrypt(String cipherAlgorithm, SecretKeySpec key, byte[] data) {
        try {
            // 创建密码器
            Cipher cipher = getCipher(cipherAlgorithm);
            // 初始化为加密模式的密码器
            initCipher(cipher, Cipher.DECRYPT_MODE, key);
            // 解密
            return new String(doFinal(cipher, data));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建密码器
     *
     * @param cipherAlgorithm
     * @return
     */
    private static Cipher getCipher(String cipherAlgorithm) {
        try {
            return Cipher.getInstance(cipherAlgorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化密码器的模式
     *
     * @param cipher
     * @param mode
     * @param keySpec
     */
    private static void initCipher(Cipher cipher, int mode, Key keySpec) {
        try {
            cipher.init(mode, keySpec);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行加密、解密
     *
     * @param cipher
     * @param dataBytes
     * @return
     */
    private static byte[] doFinal(Cipher cipher, byte[] dataBytes) {
        try {
            return cipher.doFinal(dataBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

}
