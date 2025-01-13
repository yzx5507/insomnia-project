package com.insomnia.parseISOMsg;

import com.insomnia.parseISOMsg.utils.PackUtil;
import org.jpos.iso.ISOMsg;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/11/7
 */
public class ParseIsoMsgUtil {

    public static void main(String[] args) {
        String responseMsg = "AYMwMjEwRjIzRTQ0ODVBRUUwODQwMDAwMDAwMDAwMDAwMDAwMjExNjUzOTk4MzQ0NDc5ODE0NTcw%0AMTAwMDAwMDAwMDAwMDExMDAxMTA3MDQ0NjI3NjcxMzg2MDQ0NjI3MTEwNzI4MDMwODA5NjAxMjkw%0AMTAwQzAwMDAwMDAwMDY2Mjc0ODAwNjQyNDM2NzM0NTM5OTgzNDQ0Nzk4MTQ1Nz0yODAzMjIxMDAx%0AMjg4OTM4NjI0MTEwNzA0NDYyNzQ2MjM0ODU1MjA1N1pZWTQyMDU3TEEzMTA5MjE5OTFQQUxNUEFZ%0AIExJTUlURUQgICAgICAgIExBICAgICAgICAgICBMQU5HNTY2MDQwMDAwMk5HTkMwMDAwOTUxMDA1%0AMDUwMDAxTkdORDAwMDAwMzUwMjM2NDAxNTUxMDEwMTUxMTM0NDAwMjMyOUQyRDY5NUQyMjk1OEND%0AMjY5MzJEQzlGMkZDRkY4QUIyOTlFQUU1NkZGOEVENTE1MzA1NTUwNUVBODc0MzU%3D";
        ISOMsg isoMsg = PackUtil.unPackRequestPackage(responseMsg);
        System.out.println(PackUtil.printlnMessage(isoMsg));
    }
}
