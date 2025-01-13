package com.insomnia.parseISOMsg.utils;

import java.io.IOException;

/**
 * @author: yingxu.pi@transsnet.com
 * @date: 2021/2/20 15:46
 */
public class CEFormatExceptionWrap extends IOException {
    static final long serialVersionUID = -7139121221067081482L;

    public CEFormatExceptionWrap(String var1) {
        super(var1);
    }
}
