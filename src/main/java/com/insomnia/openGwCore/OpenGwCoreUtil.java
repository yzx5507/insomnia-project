package com.insomnia.openGwCore;

import com.alibaba.fastjson2.JSON;
import com.insomnia.openGwCore.utils.Constants;
import com.insomnia.openGwCore.utils.PalmPayUtil;
import com.insomnia.openGwCore.utils.RsaUtil;
import com.insomnia.openGwCore.utils.SignType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.Map;

/**
 * 开放平台加签逻辑
 *
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/28
 */
@Slf4j
public class OpenGwCoreUtil {

    public static final String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDtNnjGIt7oHNN2K0Q7FoVppLk7TsHbL8yUsR0vlzQgWS+054d9YvB4/X46oaAec/YC0M+pJtwSvhfQxUuVa7NBD8bICqYbpNwtCuhoP4xob99Jo+e6EZa1COXoLFqR4Xfxax90ZyFsHzrvuW/DLBeG34DZVrL0q2E+9u33IVwz5IwTvQiLDSQGA34jT2m+9fEv+CJdvh66JfZTjlcwbf1kA0akJI1ccv8Lg2yQ/KOQeVxrGTZ9th4UHzR+X9LTetWd5Ra8ZfmJErQs+eAekKidWBZ8qjGZIOI9xlkqd377XxX8X/yyOVQJK+VhClTaBoMIbPnb7pX4tlXSoFRQpbW1AgMBAAECggEAa03uyEJR71HK3nOoA8qmQym+mPOBgfNXahPMjhCqBdASYpUxlyjAexrAlSkz01LFgHSQ24W2m8vd3hTBoi/8AHxwgWE/ztV+u+lpRG8qxruEmD1Kv8LhF7eUuLgHqGID7fN1YSSd29kswfByfClf4yULlkU/4qn77Y2qC9bNbLMhH5t0dL+6kiq2g0lAf/ySlUEVNgHYo1+9Wi1kN2guHbWGRxxxb1pMNX6UVlH5+teY07oZ/W3AHJP/9wqeesB4Fzu2eiWyJo9Hkx0pv60Km56SIpc4HFghJPrsPf95FmKt8OhCdqoWD8WVd1M9rXgo9qstfHb0CZovcybKTnVqwQKBgQD8UoIW8jgKcWWNtHWrd6mq3Xqxaay0u9uBBSG86jVGW8xGEsakodngxX61owS7QcU0xl0K9abIEywhF4VKAZVN/4bFNg4RD6dyOSQpd5tPuBjxzfTb77quLjaoEPdqeimMVkS3V4HXWiAfvGJlsemmWGlXYAwvSqUkG781QzP4/QKBgQDwq5Xa2ZzAljZGqhcbaErjXS7s+vv+O35844frzR4fYZNHRIHPRiVObOMtQkwdmNUKaRMp7exeA61igj1TWyMBXFPxAFuRHjxiauQXosWpO2GrR0ONAAItYoK+5AiPJLpT0gYIn5qWWVGItIV8/ioJInJwzMaBRYtfpGpJt4+JGQKBgQCIwS9AwIcfIdaotuYPyQtsUB3vihbQ3gBaLS3b9hRpzdcpI4QGNrKf4GqMh45I75yyvdAv4HREfB+uz9CuC62gWJ3yxRlapeFbRx0SXIEQ8/aRF89eZNAJF4MIFCoEy7E5Hoif1g3Jak9+49twwrygK8rsUGJKP2rB50f61jGVAQKBgQDXLWJvaG8pFWRzq7G7NHT4caD6CrDxLno56SjmX99VyWTnrE2vZ5UFWH+JQKRTNgkMJMQYkfvqVrlXwQBZnbtfhRAcAujbh8APcmh65XJ6i3gYrj+sOly3/ULOr40jhn/9X2M7pI+Pa05lvpk3Fvu4bQ8K4Vt9yxMAsTzCqhbwSQKBgQCPa2M3IT74YXndSa0cbXdnodlTmUzTGWT+LgzD5WtY+p0qT6J2qbeQYgAXUYXoPAzz25Ll0BMInSkzPNu5BKS/BxtmoVFnFDbAt8kh8Y2YG9On1fCoYGjbsASf5Er5WYDXhvYII7IMe5o3uz7F1CkkaFy+KzJ3oehk+nsc4cVHag==";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7TZ4xiLe6BzTditEOxaFaaS5O07B2y/MlLEdL5c0IFkvtOeHfWLweP1+OqGgHnP2AtDPqSbcEr4X0MVLlWuzQQ/GyAqmG6TcLQroaD+MaG/fSaPnuhGWtQjl6CxakeF38WsfdGchbB8677lvwywXht+A2Vay9KthPvbt9yFcM+SME70Iiw0kBgN+I09pvvXxL/giXb4euiX2U45XMG39ZANGpCSNXHL/C4NskPyjkHlcaxk2fbYeFB80fl/S03rVneUWvGX5iRK0LPngHpConVgWfKoxmSDiPcZZKnd++18V/F/8sjlUCSvlYQpU2gaDCGz52+6V+LZV0qBUUKW1tQIDAQAB";

    public static void main(String[] args) throws Exception {
        long time = 1730102301712L;
        log.info("now date: {}", time);

        // 2.加签
        String json = """
                {
                    "transType": 1,
                    "virtualAccount": "1176297314",
                    "virtualAccountName": "FLW-FLW-Adeyinka Samuel",
                    "sourceBankCode": "000027",
                    "sourceBankName": "Globus Bank",
                    "sourceAccountNo": "1000020913",
                    "sourceAccountName": "ITEX INTEGRATED SERVICES LIMITED - AGENCY SERVICES",
                    "sessionId": "35297865437094876598",
                    "amount": "50",
                    "transactionReference": "Test35297865437094876624",
                    "transactionDateTime": 1735278915858,
                    "transactionStatus": 2,
                    "transactionMessage": "Failed",
                    "narration": "palmpay test transaction",
                    "requestTime": 1735278915858,
                    "version": "V2.0",
                    "nonceStr": "ddBhOdqGltbnzOEiAED5GSy6jHkCiA26"
                }
                """;
        Map<String, Object> newHashMap = JSON.parseObject(json, Map.class);
        String sign = PalmPayUtil.generateSign(newHashMap, PRIVATE_KEY, SignType.RSA);
        System.out.println("sign： " + sign);
        // 3.验签
        boolean result = PalmPayUtil.verifySign(newHashMap, PUBLIC_KEY, sign, SignType.RSA);
        log.info("result: {}", result);
    }

    private Map<String, String> generateKey() throws Exception {
        Map<String, String> keyPair = RsaUtil.generateKeyPair();
        String privateKey = keyPair.get(Constants.PRIVATE_KEY);
        String publicKey = keyPair.get(Constants.PUBLIC_KEY);
        log.info("privateKey: {}", privateKey);
        log.info("publicKey: {}", publicKey);
        return keyPair;
    }
}
