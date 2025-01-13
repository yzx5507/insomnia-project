package com.insomnia.multiCountry;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.insomnia.multiCountry.utils.AesUtils;
import com.insomnia.multiCountry.utils.MacUtil;
import com.insomnia.multiCountry.utils.MkUtil;
import com.insomnia.multiCountry.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 多国签名生成逻辑
 *
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
@Slf4j
public class ParseSignContentUtil {

    public static final String COUNTRY_CODE = "NG";
    public static final String DC = "dc1427";
    public static final String AND = "&";
    public static final String EQUAL = "=";
    public static final String BODY = "body";

    public static final String BODY_STR = "{\"orderNo\":\"q21877563737087660032\"}";
    public static final String HEADERS = "appVersion=1.0.1-250110&blackBox=lGPVt1736496320M4yV0EQCWH9&body=" + BODY_STR + "&countryCode=NG&deviceModel=P2-EU&netType=WIFI&s1=180b&sn=PBE5224S30559&timestamp=20250110094552&version=1.0";

    public static String parse(String headers) {
        // 解析查询字符串
        Map<String, String> params = Maps.newHashMap();
        for (String param : headers.split(AND)) {
            String[] pair = param.split(EQUAL);
            if (pair.length == 1) {
                params.put(pair[0], "");
            } else {
                params.put(pair[0], URLDecoder.decode(pair[1], StandardCharsets.UTF_8));
            }
        }

        // 将body字段从URL编码转换为JSON格式
        if (params.containsKey(BODY)) {
            String body = params.get(BODY);
            if (StringUtils.isNotBlank(body)) {
                params.put(BODY, new JSONObject(URLDecoder.decode(body, StandardCharsets.UTF_8)).toString());
            }
        }

        String sign = sign();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("************** headers **************\n");
        stringBuilder.append("pm.request.headers.upsert(\"appSource: " + 12 + "\");\n");
        stringBuilder.append("pm.request.headers.upsert(\"countryCode: " + COUNTRY_CODE + "\");\n");
        stringBuilder.append("pm.request.headers.upsert(\"x-mse-tag: " + DC + "\");\n");
        stringBuilder.append("pm.request.headers.upsert(\"sign: ").append(sign).append("\");\n");
        AtomicReference<String> body = new AtomicReference<>("");
        params.forEach((k, v) -> {
            if (k.equals(BODY)) {
                body.set(v);
                return;
            }
            String args = "pm.request.headers.upsert(\"" + k + ": " + v + "\");";
            stringBuilder.append(args).append("\n");
        });
        stringBuilder.append("\n************** body **************\n").append(body);
        return stringBuilder.toString();
    }

    private static String sign() {
        // 解密ctmk、snKey
        // from pos_device_manage
        String enSnKey = "128A1B101B736722566CDDAF65A10A65";
        String enCtmk1 = "A88E9DCB4D528B5D7AC5226C6FF92EA7";
        String enCtmk2 = "2A1A998DB65D2D512D87F845237DC348";
        // from pos_key_manage
        String enTmk = "79F5C95B26C127FAECA7C15D0091BAC9";
        String enTpk = "FAAA06C999333597759F878D0EAC903E";
        String enPublicKey = "0VGXzZCqVUelw+7VYaZkCHLRFiKwaA2WWxZPvhtWB7eP+78XjCfCqgfr2u2p/MKNIC/rGMZkQpA2KrniiLxzjHIo76SoIY/DIQc77F+mYu3fw2OpwSNGwP/XyeJjmSU2rZPXoM3jkJCSm5GfYNGo7LP3TSlZ7nBMq8d1pU29SHIO1emPP1Tz3bc2ylYo1QhIEqUzKjoeeG8EbFnho0430T/YiveTrpMQ/Qk+wzmMVQ/maD6PMWJYpj2nQ06j2K/9O0ltAupcIxykhwgtgJ2i0ffH63jmMn9VpYn6SatsRTA=";
        String enPrivateKey = "+ULZSigiqFe+OFXVtfostoz1yewLJQ+EzNLIaJX+mbv460uCC7RptjLGQs4FE/tpt/7zykQzqCAWTn0XD0nx97K44gDHM1qhugIKx6OHykYbjvxrvAkFSGCZsB0PMIcru+47y/uKKb+ms1uCaRyyevLXNtwdIGaSzDCg0dTf/qfVEquNz3DJ1JhObAmM5Fqwdvv27Y7vAfrWNAQJ1sZ6OrdysYZzhgT9jQAV77xSIEwZfd1qrVgMrsi0DtuIXClsf1tFkV5feF/Twyu1uXWja9cBMzZFaLCLm4Mufh2hbuPeJHbZWaTZedk+NaJ4Ceozvt5xiivnKTyCFCZjYoSxD8fakKrmJdpEFmr5OCw5zMYBrxVvhK6DGQ4MXOdYU+2Zt7pzWorm/DzzHt80FD+JQzkqhuQDWi0z7IZvYdUDcbUcTDguCnS42FCH474k/QGcHAHIDUjuntaTCkGrBndP6y9wAXqsZofYBzRWvgwzhyRcPfVzpmOrs+fxZn4tjAMhVhLk2IOmO7N3Qh+iR1eEUhfBu4uZEPnLNcCDm9FyOCMiX/f0/IKl5OYhxwfECViNNl1fkUF2BgWXpGZlWif/SX6yoFeu8rbzk5QzKB8J608MqMDJ7yLpoMEEGD3bPt4g6j6TQ6a5B2d3nkScreUcbJ8Hf/c5/3nNICef7opXlY15RBcVKQUyZ2m2p3BXuQcZocUE8cA2pJUr/j6wKM/C4LL0hPszy4oR9ij0depEqY80miIVzwcXcEBiGDaAIH27Em+SXCO41ZGvBY2D5j1UKx7sWYi4AUFNAM8DdliP50ivWFdGLXqm24snwGOKskElWktBWvp1e2+WUIcGcbd/DQN9xWtISJPRTHqh6pyUa+EqaMyI8t5by2+nBmwu4ykGgvKvL5sI+Ci1YCnaMR5JF6WLKkSuCmNfO0mKhV4I5iUWOwSqB5/3w5P8YmM/JcbMZv4eXgwrH00lh7+uV7/uBkmlSVeCN9ezrpSW8Pqnh3Uc1T3vpAheKeLWaix/MWRElXpd9yl0iwt9dZHJAVartwfwld+tx/OZ1WKOWm/EmQMVzq1llxhd/5YTTMcF6YEvmT3V76+FN1M/raWy1Zj1+X1TrpdmGcyXEKRdwLMGgzk=";
        // from apollo
        String component1 = "F1C14724DD9B824A437D4B72F215DEE9";
        String component2 = "F4679D9E3330B1D31BFD2FAE93CBE889";

        String lmk = generateLmkOrCtmk(component1, component2);
        String snKey = decryptKey(enSnKey, lmk);
        String ctmk1 = decryptKey(enCtmk1, lmk);
        String ctmk2 = decryptKey(enCtmk2, lmk);

        String ctmk = generateLmkOrCtmk(ctmk1, ctmk2);

        String tmk = decryptKey(enTmk, ctmk);
        String publicKey = AesUtils.decrypt(enPublicKey, tmk);

        String tpk = decryptKey(enTpk, tmk);
        String tsk = AesUtils.decrypt(enPrivateKey, tmk);

        log.info("snKey: {}", snKey);
        log.info("ctmk1: {}", ctmk1);
        log.info("ctmk2: {}", ctmk2);
        log.info("ctmk: {}", ctmk);
        log.info("tmk: {}", tmk);
        log.info("publicKey: {}", publicKey);
        log.info("tpk: {}", tpk);
        log.info("tsk: {}\n", tsk);

        return SignUtil.sign(HEADERS, tsk);
    }

    public static String generateLmkOrCtmk(String component1, String component2) {
        String key = MkUtil.zmk(component1, component2);
        if (key.length() == 32) {
            return key;
        } else if (key.length() > 32) {
            return key.substring(0, 32);
        } else {
            return MkUtil.addZero(key, 32);
        }
    }

    public static String decryptKey(String key, String decryptRule) {
        return MacUtil.DES_3_32(key, decryptRule, 1);
    }

    public static void main(String[] args) {
        String parse = parse(HEADERS);
        System.out.println(parse);
    }
}
