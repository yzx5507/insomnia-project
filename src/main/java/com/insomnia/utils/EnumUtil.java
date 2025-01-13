package com.insomnia.utils;

import com.insomnia.enums.IEnum;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
public class EnumUtil {

    /**
     * 通过枚举值获取枚举对象
     * @param enumClass 枚举类
     * @param code 枚举值
     */
    public static <E extends IEnum<T>, T> E getByCode(Class<E> enumClass, T code) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(v -> Objects.equals(v.getCode(), code))
                .findFirst().orElse(null);
    }
}
