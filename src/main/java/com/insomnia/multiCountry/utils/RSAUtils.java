package com.insomnia.multiCountry.utils;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
@Slf4j
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 测试公钥
     */
    public static final String SEL_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3A8TbEPntSUALw6JcKQmpL+lzuQTrZpqX5W4UytPQCs+q2QIEpnwWV0MFOKoOqM/JOaT/CsjvA/lpBb7Y/TA7phn84XH8Rho2vBjShb647wEeKKomWYw9pfqYH048qaOJHGi1R2ggFZF3xj9dYieVBMY4V4cZOY/jrQrpiHfhU2lKYxzYYje1d2T9/HIICMxNtYW35oioPhLGWRvF+n/fMqBrhT8C3MvR++jD6wPSCga+ePpfTyZhb9d4gQ/NrIm5wtBYtVWNJ82YKgW9k+o0fFCWz8dEpWWXh/JnG13KtVpWRLI7fD4487AlfN0gp9QpX3YR+yUz8T/ai5V+NWd3QIDAQAB";
    /**
     * 测试私钥
     */
    public static final String SEL_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDcDxNsQ+e1JQAvDolwpCakv6XO5BOtmmpflbhTK09AKz6rZAgSmfBZXQwU4qg6oz8k5pP8KyO8D+WkFvtj9MDumGfzhcfxGGja8GNKFvrjvAR4oqiZZjD2l+pgfTjypo4kcaLVHaCAVkXfGP11iJ5UExjhXhxk5j+OtCumId+FTaUpjHNhiN7V3ZP38cggIzE21hbfmiKg+EsZZG8X6f98yoGuFPwLcy9H76MPrA9IKBr54+l9PJmFv13iBD82sibnC0Fi1VY0nzZgqBb2T6jR8UJbPx0SlZZeH8mcbXcq1WlZEsjt8PjjzsCV83SCn1ClfdhH7JTPxP9qLlX41Z3dAgMBAAECggEBAKWWxpPKSToa3JeMRcm4C8ge2kLjhxc0QeUpQWl8BoePwvmvLQ/qPFzSUnwIznRJUQOQajzvrVUfVTrzfDmL+/3OOzKZMYnvRz+wcdZXknT5jxfDtkCwEBoO2HPA5rBYuk8cH15ki9jmkm89W8QsiI9VS3ySl6UpSRw14T6C8LJaBFIdDIXtO/daslQGvwm0W+laWnHPMbr1kw6Pm5yeiuqBvZzvUD+MVwzEvYZDW3Y7Z+cbmhA4K1mBxisQqPk5aIHWpRwRYBO51MOW9wI4g9rSY1DAmFNBqPWrv+Okom0EL/mbUJSj8zghtKu2jknLOQhqwAb/cQTL9DYJUzlC7vUCgYEA+U8FaFwmm6DVvlP+6M4AUkXQMYq+7H/ntOpRNMLYQbXAZOzIhESMQ22umPYmdE6Ost3FAWGMQTH+xNLvM4/zVMDLBUv9Je8hGP5P3FCuPVWgTrtDTl4pyzWP/OQoFI0kEIPLCBMIqT1SVHtZFfboctIA5nQ/2qBkhpYdj6nouY8CgYEA4fcU+a15N9yLZ7L8rD+PcfHww4qorvjvlNlccUdzwy5i1UZqOPBu4PdZ8zpEoSuN1xU5bAlCWPOnWZfX1jrrL4KplA82BcAfdmuHdcCJ4LRaY242LmC63ROTVk2ckddmNKLzbYcKzTkat1DcAXzqv1iC+B6rGjWJ8Zw00LKoA9MCgYBY+U+nwANzVUITC+0UbriEpC+6Dpc21ir+UQWsO8FsKg5AtpSVR9V7FwDRzD9LEOIyiZ0Och7ZAZu36MO/didUXv3VpRnyWNZKEjC0IwMcFpwAnSpXJJnr0PDTQyXet8RgNHjYJ9rWc3EXf3H6ucSZfcMee+xx/P4DXj2skOnilwKBgCobFHmB7biPDBsCqzqdjWk6IBcP22bfVCV0a1lgax3PovMC8AA0LTUWYliEXw15RDDz74NGJmrU3DZBqSJuUCzGODsyqmpf5nz2hINYoViRwtYMT2RM+gUABNBsHeS/MnhUdO/P3h9nqKbIFnbghA2rvJvnexKjL1UqOl05LT2pAoGBAJy7OqFonELLtinE4s6ouVlJjjlt3TKByZSVzJwsS00q09QBQ8NLtDQHr/rOotaEXUxpt+/6fwwD7dzLvEqo9VPf7jGYzaQGOkP6shCBWrAr+jbsd0XhST9F1ikWtpNcWlhs+JviKRS71Mhn1lKb4xErBOfI2KYjfLFCTVgD5dAV";


    /**
     * 生产公钥--旺POS
     */
    public static final String WIEASY_PUBLIC_KEY_PRO = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDmzVAECfMEaL0GEufGy5gcHWPyFBQgh4U+PWhJMd+artc/ijRM4Bnwjugae5ev1Hu76immRYZ5o5U8rDckqGAxeFY4t4Z6pmj9HUpuJgjh24KRW96u+cQVVXSwcpbTu5IhiFfxq8DbP0KHAUD3hC/7VwyzGgBmLNjbVzj+iPi4QwIDAQAB";

    /**
     * 生产私钥--旺POS
     */
    public static final String WIEASY_PRIVATE_KEY_PRO = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAObNUAQJ8wRovQYS58bLmBwdY/IUFCCHhT49aEkx35qu1z+KNEzgGfCO6Bp7l6/Ue7vqKaZFhnmjlTysNySoYDF4Vji3hnqmaP0dSm4mCOHbgpFb3q75xBVVdLByltO7kiGIV/GrwNs/QocBQPeEL/tXDLMaAGYs2NtXOP6I+LhDAgMBAAECgYAdxqknDD2M+RJB4ViDniqOngAR8CM0mydGg1eb8dI+O4ZLjUH4FQVjspwhJobISWp5MNmUuO2DbeZIAJPQp+RY3r00bHdE50AX8RcPG8Fq4E6/RQFPFwzB3iVYXZcJ3Hkg53SOnDUU8qGhZs6tmRnw+mw4LNNZqaxIXA5ztrurcQJBAP3ZnjHb2qWDNPVs8ccvK7kN1z24rpNIf5tc81sdZdCxqW0StMyIox4uxxKWZovwUiIYUmqyGdfHNgfEZXsBzekCQQDowbkkoo9VvImYj0Wac6faCuNdI6rS8WYaR4ByasLcpYGv3FKeX6Ltng7xyFE2B5V0YYL1QyQfW4XfIUepbh1LAkEAm/0uCNVg9nQ6BtZ7fDqwU3b93R+vpzHVuFLV+BeIiUWjJzNnhogwNEvqaLGjMjrrPahUfNwUpVP1D7DP61aggQJAV8wjW3daW/vnZz28ApvX1/69bveySym12vnXsNEmufZQahbQr17h5YH2nytxvtCzhpf1vzBZYt6yK82w629W6wJBAI/yh0BvG3UswHASpdmPBsdWlYQK8aZR1uRD5E9v8aNH7d/VO5brBT1PZdu10lLdPY2NlVA54+8528zrODIEUK0=";


    /**
     * 生产公钥--旺POS--2048
     */
    public static final String WIEASY_PUBLIC_KEY_PRO_2048 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsiKhFU9IfDxR/Y5Asje9HpuXAB/wTo0pf830rmlQGHDiYU4xtK+eiaZi3xBiGYq/EXmpb7UDl7X73Xwftun5J8YcSC3utfcMRSGowlAa8MokoDFPpyBtK1AdTfDvoeJcWGC2th0TBe4bhmF91tsnJkke2fhOdbBfFVKiKR/qAFHAAo17u7gPF7aMLKEtLPQvbFq/b/GAX9OFz6Rq0EjVAGo4sVIHUrXfy69VBQqziS7WktMRhkJNkRiuYpIyWJ4q22nH+t2qzd0eYYzuTGE3UVo2bSH6PegxODI3uq0gSjy53QSjuqFkzet4ZgAT7c5ViPduyZXWeSV9PzaxVRCcxQIDAQAB";

    /**
     * 生产私钥--旺POS--2048
     */
    public static final String WIEASY_PRIVATE_KEY_PRO_2048 = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCyIqEVT0h8PFH9jkCyN70em5cAH/BOjSl/zfSuaVAYcOJhTjG0r56JpmLfEGIZir8RealvtQOXtfvdfB+26fknxhxILe619wxFIajCUBrwyiSgMU+nIG0rUB1N8O+h4lxYYLa2HRMF7huGYX3W2ycmSR7Z+E51sF8VUqIpH+oAUcACjXu7uA8XtowsoS0s9C9sWr9v8YBf04XPpGrQSNUAajixUgdStd/Lr1UFCrOJLtaS0xGGQk2RGK5ikjJYnirbacf63arN3R5hjO5MYTdRWjZtIfo96DE4Mje6rSBKPLndBKO6oWTN63hmABPtzlWI927JldZ5JX0/NrFVEJzFAgMBAAECggEAKXbkFgjBb6G+2A1/w7wzumcvK+lDT5WOPmCbfummqjnEiC/iZBOpDJN9Wjz/CMECHrg6F3WEI5FJ0lFtNG+b/oUf2jFO6m5aHdFq7egljT0Qb3Vq/BURNE7Os3DM5trA8hV5EkUMQ4ocQBh7aULUBxZoJwakhMKML3NvqbDzECWT5puCfagMVhstEu7ttXYcdClyTq15TTrUGxNAkPuDRxfu4mCTNSV/4S2SqrI5JxtFtoBScVlShg17TepFk0NS+uzaVHBHhoKPhVnapfIinQPc8xkdcRN/SolwihHMYjY68bOR++Bl4LeJNH3U0xae2ZnbXOvfxIeIWsbcYDY9AQKBgQDYUG4SrcQ6B6F+0XhRhbvJcKODluda2Tk1ugYaah0HWAVqnFwnoctPOeLgZRxLA4DHMk4vt5W08VcUcf4aqQlH5vngBEC0rtVXhhzdsU6GPpOB95nutrtTPjWYeLQZ+9gVy++YoTpjZUpfBR3Zb7I3awFzmtysyxec7xFABskPBQKBgQDS0Q+IlXyVeb76OTX4ZKbN9MUapvNsyNYzMELg2WygHqYNKGtM0zjqo/+1YSu2lWnp9VX2ViMxPpM+woVMxe+i3M/Os9Rsqx6zG2KjyCbFBh1yz5zg4iGiWgf7WGuq3UJwFEMUePdHeBP4ESNRc23tjEleVKUucGT07qSPG7lCwQKBgEyJ4PjA/V6fFph927nUvQZg0aqc+Y2esOx+vuwXwd+4WGpJmx321K5ayBkoDaPJQIa7GQkvR0x33J0CXKvoRb1oT9/hAVgtSJWogYw70ZIUkDL7fj8F7zBcIbCqfOd4whOsTkHOQo+q5m7Kq64lJIURWrkiAZUuY1OHD658+CVVAoGAE5SVU8NGof9/zG/Z4ms+zuxIDlSpUVZKBtEZtRacrzaJh7lUVftcUvbOfkM7MycngjJmPI39kMpWFMMubFYKDt9hcbpkJX+zR0Fo5PtnFm+d9pbv5Dc8ln60NRUeUu1gCVbocBNYLEwr1qOI2zzUXfbkrZ1BBlIsHk+3+w7BI0ECgYAa/HMW45jU0aV6XRDANn7FkY9zMssuDpE6LkmKTGqeMkXZnWQWcgPAfvGe1nMbh2an8FBZyS8rgnSAbq6wvAB2s/ZDQZcB6VXXkTkcQo4ZL4hhQQstYN5WS0BB7RLIXKQ1Wm8e3hG5cv5lKvja3F5DxxJobtjHlZOH/SZEpTxObg==";


    /**
     * 灰度公钥--旺POS
     */
    public static final String WIEASY_PUBLIC_KEY_PRE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChyZRu0ZMIt5rfytovlyJl/1ndtMfyS4Up2TjQkd0mtlT0iL06AGs281la0wn0mujkjssG7+nHJjuUgu/5oFiTTuqJUSbjOjTHoe0PTN2+qt7p9WhbfEJN5U8wdk29tPqR4bfzFMuhinmelUAT22wwbIuOnMNiQuVGMYE/cwZGkQIDAQAB";
    /**
     * 灰度私钥--旺POS
     */
    public static final String WIEASY_PRIVATE_KEY_PRE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKHJlG7Rkwi3mt/K2i+XImX/Wd20x/JLhSnZONCR3Sa2VPSIvToAazbzWVrTCfSa6OSOywbv6ccmO5SC7/mgWJNO6olRJuM6NMeh7Q9M3b6q3un1aFt8Qk3lTzB2Tb20+pHht/MUy6GKeZ6VQBPbbDBsi46cw2JC5UYxgT9zBkaRAgMBAAECgYAXB6vSbpDAj+pC6wxN8/SYklXHgoFn0yo8f8hfoDPb296SBJjzFbYtV0yj6iCjqh1204DBFwuvJpkaldz1KscQzxzC8eF/3E8kcZfYM3rgykMuhFhlBDWjUm2JyitrIuyR0Y55VgrbaKcGfpl5bIJUjUa3pzwhRXT66nMHKqAE5QJBAM1pAot78uEXWw2y2mS0bduSrgqsnANQI78/CS0SuL6dhiS9R3VSPqsBhI2RuJcBJFAtodPQmPsQI25nVb4ldu8CQQDJoi4XdUVPyOljrP/TlAcLLO34GYhMgCGj25/0GhTXavAvBGo+WqO67s8LDqW+UbehOvd1/86WjgZuTyHVZBp/AkBlRwZbMbTyKKrbWTj2+ENGRM8NMkgGFihZ7kGe+KEgUtjGGpE2FmQRusDZcGTcB1MZOTaWEj6rYyylDcwn4Z7/AkB3I8EgU0a1Mf3Vy68OEXVoARBLfKqwqTU3YNpOneqarrFk1rMATQZZuinUWr9MiXR7bCA0NQaYpyrdmxxevlt5AkBJidrBUDqpGNoH7S34QGcCmUKB4Nn/XLxL/VzvzA4VrnvfGieoZRr6d+lpnyZt3td2tl1kpQQfnaTi8k04bV3v";


    /**
     * 测试公钥--旺POS
     */
    public static final String WIEASY_PUBLIC_KEY_TEST = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDi4YIDezxWfL9xk10i0OiopxaoGhW9UANezWY3UaW1pJKzqIRmeOvgI/8vcJYa/CY2jEyG3Y9Hp1XGGqC+NVNqdviBYPeWb8xwg8wn/sChhbsUpAnM9OeU9AI3ZK8TChTGWccGieBAxFCAf9Z4pvJEsx8o+gWwNGSKKETTP1hSAwIDAQAB";
    /**
     * 测试私钥--旺POS
     */
    public static final String WIEASY_PRIVATE_KEY_TEST = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOLhggN7PFZ8v3GTXSLQ6KinFqgaFb1QA17NZjdRpbWkkrOohGZ46+Aj/y9wlhr8JjaMTIbdj0enVcYaoL41U2p2+IFg95ZvzHCDzCf+wKGFuxSkCcz055T0AjdkrxMKFMZZxwaJ4EDEUIB/1nim8kSzHyj6BbA0ZIooRNM/WFIDAgMBAAECgYA5PfpGlJYxoAHRTwPln93Dh3yw5vvrKr/e8GXEuUXc4WveBjt5ezLP92UP/BDzOAEQLC+2qznWM0Lx25AIzX2A2mzRcr6uWRYHAQwanJvtIMpjEySQXINiCdsJli4IrvndyDXOj2th2v5PmgdPEUyIIQ9eF8br8TIwBUnSlIYueQJBAPHXOlj94RclVe1OLZw4vG/jAsGdAPf1y5ikAhBwajhztPSMoU3YB3xZF0GFiyBBw4EzdmR/qPQaGD8nNby3X50CQQDwKg7SV7sj6ghRTMvJnoTCPz794Xa11pNZ1YYM7HNTy8GlFWF7+Za2BxHq/eEVnOW+UnOJseDqROIqXE4giVYfAkAsFgsp6+YbnQ/D+ctwvhsD3oNDHkbt7N3XjpZdvtBuSBGFvxksoqQCtA78P8bz82kbZXFAstDu5s7ivFgcdeU1AkBG4FKhduyuq6mqvJxDszmAzgN8D2bmQR4ZW++Ert9WMoLCgm3TlZQJwkpibGxZ4YTXxyN71ngHCy8nyzfvWDiHAkEA78mZ3Y1oAqP6NKPbFX9DiiD7CHy9YcWZnVjMhq/B8fuinJ/w1YZ82SEbGju7jIZAdMCqyMn3/snq+XBORuxH0Q==";


    /**
     * 生成密钥对(公钥和私钥)
     *
     * @throws Exception
     */
	/*public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}*/
    public static PublicKey getPublicKeyByStr(String publicKeyStr) {
        PublicKey publicK = null;
        try {
            byte[] keyBytes = Base64.decode(publicKeyStr.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            publicK = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("getPriKeyByStr exception=", e);
        }
        return publicK;
    }

    public static PrivateKey getPriKeyByStr(String priKeyStr) {
        PrivateKey privateK = null;
        try {
            byte[] keyBytes = Base64.decode(priKeyStr.getBytes());
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            log.error("getPriKeyByStr exception=", e);
        }
        return privateK;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @throws Exception
     */
    public static String sign(byte[] data, String rsaEnv) throws Exception {
        String priKeyStr = WIEASY_PRIVATE_KEY_PRO;
        if ("test".equals(rsaEnv)) {
            priKeyStr = WIEASY_PRIVATE_KEY_TEST;
        } else if ("pre".equals(rsaEnv)) {
            priKeyStr = WIEASY_PRIVATE_KEY_PRE;
        } else if ("pro_2048".equals(rsaEnv)) {
            priKeyStr = WIEASY_PRIVATE_KEY_PRO_2048;
        }
        byte[] keyBytes = Base64.decode(priKeyStr.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return new String(Base64.encode(signature.sign()));
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @return
     * @throws Exception
     */
    public static boolean doVerify(byte[] data, String reqSign, String rsaEnv) throws Exception {
        String publicKeyStr = WIEASY_PUBLIC_KEY_PRO;
        if ("test".equals(rsaEnv)) {
            publicKeyStr = WIEASY_PUBLIC_KEY_TEST;
        } else if ("pre".equals(rsaEnv)) {
            publicKeyStr = WIEASY_PUBLIC_KEY_PRE;
        } else if ("pro_2048".equals(rsaEnv)) {
            publicKeyStr = WIEASY_PUBLIC_KEY_PRO_2048;
        }
        byte[] keyBytes = Base64.decode(publicKeyStr.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(reqSign.getBytes()));
    }

    public static boolean verify(byte[] data, String reqSign, String rsaEnv) {
        boolean flag = false;
        try {
            flag = doVerify(data, reqSign, "pro_2048");
        } catch (Exception e) {
            try {
                // 兼容旧的秘钥
                flag = doVerify(data, reqSign, rsaEnv);
                log.info("validate newSignFlag=false, oldSignFlag={}", flag);
            } catch (Exception e1) {
                log.warn("verify error e", e1);
            }
        }
        return flag;
    }

    /*
    public static void main(String[] args) throws Exception {
        String name = "pyx";
        String desc = "test";
        String signDataBefore = name + desc;
        String signData = sign(signDataBefore.getBytes(), "pro_2048");
        System.out.println(signData);
        System.out.println(verify(signDataBefore.getBytes(), signData, "dev"));
    }
    */

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decode(publicKey.getBytes());
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return new String(Base64.encode(key.getEncoded()));
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return new String(Base64.encode(key.getEncoded()));
    }

    /**
     * 获取WIEASY_PUBLIC_KEY
     */
    public static String getTestPublicKey() throws Exception {
        return WIEASY_PUBLIC_KEY_TEST;
    }

    /**
     * 获取WIEASY_PRIVATE_KEY
     */
    public static String getTestPrivateKey() throws Exception {
        return WIEASY_PRIVATE_KEY_TEST;
    }

    public static String strMask(String source) {
        try {
            if (StringUtils.isBlank(source) || source.length() < 4) {
                return "****";
            }
            source = source.substring(source.length() - 4, source.length());
            source = "**** " + source;
        } catch (Exception e) {
        }
        return source;
    }

}