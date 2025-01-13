package com.insomnia.multiCountry.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
@Slf4j
public class MkUtil {

    public static String makData = "11111111111111111111111111111111"; // TMK

    public static String sekData = "22222222222222222222222222222222"; // TSK

    public static String pikData = "33333333333333333333333333333333"; // TPK

    public static String zeroData = "00000000000000000000000000000000";

    public static String zeroTwoData = "0000000000000000000000000000000000000000000000000000000000000000";

    public static List<String> keyPool = new ArrayList<>();

    static {
        keyPool.add("0");
        keyPool.add("1");
        keyPool.add("2");
        keyPool.add("3");
        keyPool.add("4");
        keyPool.add("5");
        keyPool.add("6");
        keyPool.add("7");
        keyPool.add("8");
        keyPool.add("9");
        keyPool.add("A");
        keyPool.add("B");
        keyPool.add("C");
        keyPool.add("D");
        keyPool.add("E");
        keyPool.add("F");
        keyPool.add("0");
        keyPool.add("1");
        keyPool.add("2");
        keyPool.add("3");
        keyPool.add("4");
        keyPool.add("5");
        keyPool.add("6");
        keyPool.add("7");
        keyPool.add("8");
        keyPool.add("9");
        keyPool.add("A");
        keyPool.add("B");
        keyPool.add("C");
        keyPool.add("D");
        keyPool.add("E");
        keyPool.add("F");
        keyPool.add("0");
        keyPool.add("1");
        keyPool.add("2");
        keyPool.add("3");
        keyPool.add("4");
        keyPool.add("5");
        keyPool.add("6");
        keyPool.add("7");
        keyPool.add("8");
        keyPool.add("9");
        keyPool.add("A");
        keyPool.add("B");
        keyPool.add("C");
        keyPool.add("D");
        keyPool.add("E");
        keyPool.add("F");
        keyPool.add("0");
        keyPool.add("1");
        keyPool.add("2");
        keyPool.add("3");
        keyPool.add("4");
        keyPool.add("5");
        keyPool.add("6");
        keyPool.add("7");
        keyPool.add("8");
        keyPool.add("9");
        keyPool.add("A");
        keyPool.add("B");
        keyPool.add("C");
        keyPool.add("D");
        keyPool.add("E");
        keyPool.add("F");
    }

    /*
     * 发卡方的区域秘钥成份
     */
    public static String getZmk() {
        //成分1
        String zmkPart1 = "5D25072F04832A2329D93E4F91BA23A2";    //checkValue = A891
        //成分2
        String zmkPart2 = "86CBCDE3B0A22354853E04521686863D";    //checkValue = DF29
        return zmk(zmkPart1, zmkPart2);
    }

    public static String getZmk(String zmkPart1, String zmkPart2) {
        return zmk(zmkPart1, zmkPart2);
    }
/*
    public static void main(String[] args) {
        System.out.println(getZmk());
        String makDataEncode = "FEFCF9A2B1538554EF9D3E27E0759B2AB464910000000000";
        String makDataDecode = MacUtil.DES_3_32(makDataEncode.substring(0, 32), getZmk(), 1);
        System.out.println(makDataDecode);
        System.out.println(MacUtil.DES_3_32(makDataDecode.substring(0, 32), getZmk(), 0) + MacUtil.DES_3_32(makDataDecode, MkUtil.zeroData, 0));
    }
*/

    /*
     * 对秘钥成份按位异或
     */
    public static String zmk(String zmkPart1, String zmkPart2) {
        if (null == zmkPart1 || null == zmkPart2)
            return null;
        if (zmkPart1.length() != zmkPart2.length())
            return null;

        StringBuilder result = new StringBuilder();
        for (int index = 0; index < zmkPart1.length(); index++) {
            int temp = Integer.valueOf(String.valueOf(zmkPart1.charAt(index)), 16) ^ Integer.valueOf(String.valueOf(zmkPart2.charAt(index)), 16);
            result.append(Integer.toHexString(temp));
        }
        return result.toString().toUpperCase();
    }

    public static String addZero(String str, int strLength) {
        int strLen = str.length();
        StringBuffer sb = null;
        while (strLen < strLength) {
            sb = new StringBuffer();
//            sb.append("0").append(str);// 左补0
            sb.append(str).append("0");//右补0
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

    public static String getMak() {
        //密文秘钥解密，不通的渠道方，加解密方式可能不同，同时，这个过程可能是由加密机来完成
        return MacUtil.DES_3_32(makData.substring(0, 32), getZmk(), 0);
    }

    public static String getSekCiphertext() {
        //密文秘钥加密，不通的渠道方，加解密方式可能不同，同时，这个过程可能是由加密机来完成
        return MacUtil.DES_3_32(sekData.substring(0, 32), MkUtil.makData, 0);
    }

    public static String getPinCiphertext() {
        //密文秘钥加密，不通的渠道方，加解密方式可能不同，同时，这个过程可能是由加密机来完成
        return MacUtil.DES_3_32(pikData.substring(0, 32), MkUtil.makData, 0);
    }


    public static String calMacVal(String tsk, byte[] dataArr) {
        String macVal = null;
        try {
            String macValNew = MkUtil.sha256(tsk, dataArr);
            log.info("calMacVal macDataReqStr={}, macValNew={}, checkFlag={}", macValNew);
        } catch (Exception e) {
            log.warn("calMacVal fail e", e);
        }
        return macVal;
    }

    public static String sha256(String tsk, byte[] withOutLenData) {
        String macValNew = null;
        try {
            byte[] keyArr = EncodeUtil.bcd(tsk);
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(keyArr, 0, keyArr.length);
            messageDigest.update(withOutLenData, 0, withOutLenData.length);
            macValNew = (new BigInteger(1, messageDigest.digest())).toString(16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            log.warn("sha256 fail e", e);
        }
        return macValNew;
    }


    // 随机生成len位由数字和A-F组成的字符串
    public static String generateKey(int len) {
        Collections.shuffle(keyPool);
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < len; i++) {
            key.append(keyPool.get(i));
        }
        return key.toString();
    }

    /**
     * 随机生成32位字符串
     *
     * @return
     */
    public static String generate32Key() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            int index = random.nextInt(64);
            key.append(keyPool.get(index));
        }
        return key.toString();
    }


    public static String getMacVal(String tsk, ISOMsg body) throws ISOException {
        SWISO87APackager swPackager = (SWISO87APackager) body.getPackager();
        byte[] dataArr = body.pack();
        int dataLen = dataArr.length;
        byte[] withOutLenData = new byte[dataLen - 64];
        byte[] macDataReq = new byte[64];
        System.arraycopy(dataArr, 0, withOutLenData, 0, dataLen - 64);
        System.arraycopy(dataArr, dataLen - 64, macDataReq, 0, 64);
        String macValNew = MkUtil.sha256(tsk, withOutLenData);
        if (StringUtils.isNotEmpty(macValNew) && macValNew.length() < 64) {
            macValNew = padStart(macValNew, 64, "0");
        }
        return macValNew;
    }

    public static String responseMac(String unTsk, byte[] dataArr) {
        int dataLen = dataArr.length;
        byte[] withOutLenData = new byte[dataLen - 64];
        System.arraycopy(dataArr, 0, withOutLenData, 0, dataLen - 64);
        String macValNew = MkUtil.sha256(unTsk, withOutLenData);
        if (StringUtils.isNotEmpty(macValNew) && macValNew.length() < 64) {
            macValNew = padStart(macValNew, 64, "0");
        }
        return macValNew;
    }

    public static String calculateMac(String unTsk, byte[] dataArr) {
        if (unTsk == null || dataArr == null) {
            throw new IllegalArgumentException();
        }
        String macValNew = MkUtil.sha256(unTsk, dataArr);
        if (StringUtils.isNotEmpty(macValNew) && macValNew.length() < 64) {
            macValNew = padStart(macValNew, 64, "0");
        }
        return macValNew;
    }

    public static String padStart(String targetStr, int len, String fillStr) {
        String ret = targetStr;
        // 目前只支持填充1位字符
        if (targetStr.length() >= len || fillStr.length() != 1) {
            return ret;
        }
        int needLen = len - targetStr.length();
        for (int i = 0; i < needLen; i++) {
            ret = fillStr + ret;
        }
        return ret;
    }


}
