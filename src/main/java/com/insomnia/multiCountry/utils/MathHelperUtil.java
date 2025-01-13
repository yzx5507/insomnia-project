package com.insomnia.multiCountry.utils;

import java.util.*;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
public class MathHelperUtil {
    public static Long min(Long... numbers) {
        if (numbers == null || numbers.length <= 0) {
            return 0L;
        }
        return Arrays.stream(numbers).min(Comparator.comparing(Long::longValue)).orElse(0L);
    }

    public static Long min(List<Long> numbers) {
        if (numbers == null || numbers.size() <= 0) {
            return 0L;
        }
        return numbers.stream().filter(Objects::nonNull).min(Comparator.comparing(Long::longValue)).orElse(0L);
    }

    public static Long max4LongList(List<Long> numbers) {
        if (numbers == null || numbers.size() <= 0) {
            return 0L;
        }
        return numbers.stream().filter(Objects::nonNull).max(Comparator.comparing(Long::longValue)).orElse(0L);
    }

    public static Long generateBatchNo(){
        String dateStr = DateHelpUtil.dateToStr(new Date(), DateHelpUtil.YYYYMMDDHHMMSS);
        return Long.parseLong(dateStr);
    }
}
