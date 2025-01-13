package com.insomnia.openGwCore;

import com.alibaba.fastjson2.JSON;
import com.insomnia.openGwCore.utils.Constants;
import com.insomnia.openGwCore.utils.PalmPayUtil;
import com.insomnia.openGwCore.utils.RsaUtil;
import com.insomnia.openGwCore.utils.SignType;
import lombok.extern.slf4j.Slf4j;

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
public class ProdOpenGwCoreUtil {

    public static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDC7UXTy5XkUwjcQ1VeUPM1/L8JP3L07LO2zj49/Fi7OTcW9CucuWaxtIN7TDLDX5Gsqq9fpSOFfgUD3NXdnybYr06IjEEs0TvKl06s3pMdJo5vIh+0t3QDYPwojGM7f2AP0gFOfPS/Dg7qLMYdmB0QBefCGd0w+FGhHennrXF5sAVJ10S5W6g7cp1Pz7fUIChsSg7fIrKhqMmtBJlqGyV0ZfDG7vLhnG/tIz+K255iRUXgNxRQwKiTw27b15oTdJtV7MDBLXdeiR3asK3v3ginLNE/w6kCw5HrzkU1Dpts9RCoDgVOkNw3c0I8VJGDD/Lt0+ZI4bGiYSlPaujM9qWBAgMBAAECggEAIqaPHSE5mgbwskb96/Az68rsIra2trdFEyACjH5JP67iDiCFiCylBr3yCX74Hybj/G+zESuH7vkB5zfghK7/9tDjAv77rv3axR60i6JdQHdUJolIXZWm99rLuGGMWk1ARE+uj8dCspvLJvGiniwmLmz+T9DNMyFRUXTB2b9JRrol2sebj3QnwyPZ60jrsS7JLRVJCR6DlIICYfb8ag4niIxXHEt002EiPK7hoxsl7EIvkypGtUTM7Zl0qWzCGSheiMdsL+zODFQpddReV33ikX3Rbx68NT3ugjcXAq3iAN+LXV/PjZ0dTep4b6hjSPfzUCJdJ0nYcmvK6mFtEU2pqQKBgQD0HkmgOarz3H32Us/bPeRwvpuyajYKECvaN4zh7PC14JsKvZ/3cfICkWx9TwEtnpdsdmGVwt3krtKR3XmKpPKlof831+160t0zjHbM8+YfmOJhv7sahqiNx7hLHWLbV06mdi2auN99jFAB/K3iGLr+eHyJW1aX0aSjEC8IjA6pMwKBgQDMag9Tvfi1nfE+t6yaC+q2aCT5vVFJAHeN2Y1gAi90e+5qnXJKye/Ujaik66FI6ob1YQDrErV+cmcUN7Bi5EeD2ds7Ivn6JcAXnd1RzZkGNRa7VanRgbpVwAYg53CUpZdhE8o+9N7hWUQ/oWsdQb2AkHbvBTWrt/G3dyw+EUs+ewKBgQCZN01EmA0442HXsWAuHal8LdXw5AKozD+JxaXRwvHNZXxKAHwfRU/81qM9/tEhfdFSTPawlU7JkgQtctEu+Jom5QcXWJn/pcsZ6IZD2qzLlhw396uKWYv++KQt2PLL8f82MSQsYO48kU1CAgJsztLGln6rgz1VKkpn4edzONyWywKBgFM+qek/hwZnQLR61kaRrc8E0HIH4A9rqkRRIVrE6RB129z9K2s7m3ijC9fFwZPRNo69J0OUBedtrX61QxaywlD2qmoFz2lIcFbtyhCjOrslaZF2zEZunzSGk7hYqCdQ3DoDdix3SkDZOorzMIzklcjCjDzAkV/R9BTz6keIgWz7AoGAGtqTeI56L5ZzRZnE743D3XuPw6Is55M+qBwGiX8bubJ0rfgrRP71fGI6tHaeFMDj88ilMM2T5bEjErkdGbLBnR5AKe1HlYGOMCIMUkZ+ZzTDzOTx3Amqp1ETVvv3ukBbJzcWapVE8OaybgXdtu1HP07EotYCmGWYNx8lZYv5v0k=";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwu1F08uV5FMI3ENVXlDzNfy/CT9y9Oyzts4+PfxYuzk3FvQrnLlmsbSDe0wyw1+RrKqvX6UjhX4FA9zV3Z8m2K9OiIxBLNE7ypdOrN6THSaObyIftLd0A2D8KIxjO39gD9IBTnz0vw4O6izGHZgdEAXnwhndMPhRoR3p561xebAFSddEuVuoO3KdT8+31CAobEoO3yKyoajJrQSZahsldGXwxu7y4Zxv7SM/itueYkVF4DcUUMCok8Nu29eaE3SbVezAwS13Xokd2rCt794IpyzRP8OpAsOR685FNQ6bbPUQqA4FTpDcN3NCPFSRgw/y7dPmSOGxomEpT2rozPalgQIDAQAB";

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
                     "transactionReference": "Test35297865437094876625",
                     "transactionDateTime": 1735279512358,
                     "transactionStatus": 1,
                     "transactionMessage": "Successed",
                     "narration": "palmpay test transaction",
                     "requestTime": 1735279512358,
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
