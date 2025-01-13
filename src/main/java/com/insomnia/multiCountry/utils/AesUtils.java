package com.insomnia.multiCountry.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
public class AesUtils {

    private static final Logger log = LoggerFactory.getLogger(AesUtils.class);
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    private static final int KEY_SIZE = 128;

    private static final int KEY_SIZE_192 = 192;

    private static final int KEY_SIZE_256 = 256;
    /**
     * 加密/解密算法/工作模式/填充方法
     */
    public static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";

    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";


    private static final String KEY = "4OBlKVZxU+TZHfjTajsv+Q==";

    /**
     * 获取密钥
     *
     * @return
     */
    private static SecretKey getRandomKey() {
        // 实例化
        try {
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // AES 要求密钥长度为128位、192位或256位
            kg.init(KEY_SIZE);
            // 生成密钥
            SecretKey secretKey = kg.generateKey();
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化密钥
     *
     * @return
     */
    private static String getRandomKeyStr() {
        return Base64Utils.encode(getRandomKey().getEncoded());
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


    /**
     * 获取Key的BASE64编码串
     *
     * @param key
     * @return
     */
    public static String getKeyStr(Key key) {
        return Base64Utils.encode(key.getEncoded());
    }


    /**
     * 解密
     *
     * @param data 待解密数据 BASE64编码
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static String decrypt(String data, String key) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }
        byte[] decrybytes = Base64Utils.decode(data);
        return decryptBytes(decrybytes, key);
    }


    /**
     * 加密
     *
     * @param data
     * @param key
     * @return BASE64编码字符串
     */
    public static String encrypt(String data, String key) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }
        byte[] encrybytes = encrypt2Bytes(data, key);
        String base64Str = Base64Utils.encode(encrybytes);
        return base64Str;
    }

    public static String encrypt2Hex(String data, String key) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }
        byte[] bytes = encrypt2Bytes(data, key);
        return byteArrayToHexString(bytes);
    }

    /**
     * 使用BASE64编码的key加密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt2Bytes(String data, String key) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        byte[] dataBytes = data.getBytes();
        SecretKeySpec k = codeToKey(key);
        return encrypt(KEY_ALGORITHM, k, dataBytes);
    }

    public static String decryptBytes(byte[] decrybytes, String key) {
        SecretKeySpec k = codeToKey(key);
        return decrypt(CIPHER_ALGORITHM_ECB, k, decrybytes);
    }

    public static byte[] encryptWithIV(String data, String key, String iv) {
        return encryptWithIV(data, Base64Utils.decode(key), Base64Utils.decode(iv));
    }

    public static byte[] encryptWithIV(String data, byte[] keyBytes, byte[] ivBytes) {
        if (data == null) {
            return null;
        }
        byte[] dataBytes = data.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        return encryptIV(CIPHER_ALGORITHM_CBC, keySpec, ivSpec, dataBytes);
    }

    public static byte[] encrypt(String data, String key, String cipherAlgorithm) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        byte[] dataBytes = data.getBytes();
        byte[] keyBytes = Base64Utils.decode(key);
        return encrypt(dataBytes, keyBytes, cipherAlgorithm);
    }

    /**
     * @param data
     * @param key
     * @param cipherAlgorithm 加密模式
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) {
        SecretKeySpec keySpec = new SecretKeySpec(key, KEY_ALGORITHM);
        return encrypt(cipherAlgorithm, keySpec, data);
    }


    public static String decryptWithIV(byte[] decrybytes, String key, String iv) {
        return decryptWithIV(decrybytes, Base64Utils.decode(key), Base64Utils.decode(iv));
    }

    public static String decryptWithIV(byte[] decrybytes, byte[] keyBytes, byte[] ivBytes) {
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        return decryptIV(CIPHER_ALGORITHM_CBC, keySpec, ivSpec, decrybytes);
    }


    /**
     * 使用默认key加密
     *
     * @param data
     * @return
     */
    public static String encryptWithDefaultKey(String data) {
        return encrypt(data, KEY);
    }

    /**
     * 使用默认key解密
     *
     * @param encryData
     * @return
     */
    public static String decryptWithDefaultKey(String encryData) {
        return decrypt(encryData, KEY);
    }

    /**
     * 根据指定字符串生成秘钥
     *
     * @param password
     * @return
     */
    public static SecretKey generateKey(String password) {
        return generateKey(password, KEY_SIZE);
    }

    public static SecretKey generateKey(String password, int keySize) {
        // 创建AES的Key生产者
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
            // 利用用户密码作为随机数初始化出
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
//        kgen.init(KEY_SIZE, new SecureRandom(password.getBytes()));
            kgen.init(keySize, random);
            // 根据用户密码，生成一个密钥
            SecretKey secretKey = kgen.generateKey();
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据指定密码加密
     *
     * @param content
     * @param password
     * @return
     */
    public static String encryptWithPassword(String content, String password) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        return Base64Utils.encode(encryptBytesWithPassword(content, password));
    }

    public static byte[] encryptBytesWithPassword(String content, String password) {
        return encryptBytesWithPassword(content, password, KEY_SIZE);
    }

    /**
     * 指定密码和生成密码位数
     *
     * @param content
     * @param password
     * @param keySize
     * @return
     */
    public static byte[] encryptBytesWithPassword(String content, String password, int keySize) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        SecretKey secretKey = generateKey(password, keySize);
        // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
        byte[] enCodeFormat = secretKey.getEncoded();
        // 转换为AES专用密钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
        byte[] byteContent = content.getBytes();
        return encrypt(KEY_ALGORITHM, key, byteContent);
    }

    /**
     * 根据指定密码解密
     *
     * @param content
     * @param password
     * @return
     */
    public static String decryptWithPassword(String content, String password) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        byte[] dataBytes = Base64Utils.decode(content);
        SecretKey secretKey = generateKey(password);
        // 返回基本编码格式的密钥
        byte[] enCodeFormat = secretKey.getEncoded();
        // 转换为AES专用密钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
        return decrypt(CIPHER_ALGORITHM_ECB, key, dataBytes);
    }

    private static byte[] encrypt(String cipherAlgorithm, SecretKeySpec key, byte[] data) {
        try {
            // 创建密码器
            Cipher cipher = getCipher(cipherAlgorithm);
            // 初始化为加密模式的密码器
            initCipher(cipher, Cipher.ENCRYPT_MODE, key);
            // 加密
            return doFinal(cipher, data);
        } catch (Exception e) {
            log.warn("<=====aes encrypt error", e);
            return null;
        }
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
            log.warn("<=====aes decrypt error", e);
            return null;
        }
    }

    private static byte[] encryptIV(String cipherAlgorithm, SecretKeySpec key, IvParameterSpec ivSpec, byte[] data) {
        try {
            // 创建密码器
            Cipher cipher = getCipher(cipherAlgorithm);
            // 初始化为加密模式的密码器
            initCipher(cipher, Cipher.ENCRYPT_MODE, key, ivSpec);
            // 加密
            return doFinal(cipher, data);
        } catch (Exception e) {
            log.warn("<=====aes encrypt error", e);
            return null;
        }
    }

    private static String decryptIV(String cipherAlgorithm, SecretKeySpec key, IvParameterSpec ivSpec, byte[] data) {
        try {
            // 创建密码器
            Cipher cipher = getCipher(cipherAlgorithm);
            // 初始化为加密模式的密码器
            initCipher(cipher, Cipher.DECRYPT_MODE, key, ivSpec);
            // 解密
            return new String(doFinal(cipher, data));
        } catch (Exception e) {
            log.warn("<=====aes decrypt error", e);
            return null;
        }
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
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
     * 初始化密码器的模式
     *
     * @param cipher
     * @param mode
     * @param keySpec
     * @param ivSpec
     */
    private static void initCipher(Cipher cipher, int mode, Key keySpec, IvParameterSpec ivSpec) {
        try {
            cipher.init(mode, keySpec, ivSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}