package com.insomnia.passwordEncry;

import com.insomnia.multiCountry.utils.AesUtils;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/11/18
 */
public class PasswordEncryUtil {

    public static void main(String[] args) {
        String palmpay = AesUtils.encryptWithPassword("c8fbTgDY8C7kHQHO", "palmpay");
        System.out.println(palmpay);

        String originPassword = AesUtils.decryptWithPassword("Sz8sqNQQQCq9YCnbfgDJ7dhce4h0l6F2k61VltT/UGw=", "palmpay");
        System.out.println(originPassword);
    }
}
