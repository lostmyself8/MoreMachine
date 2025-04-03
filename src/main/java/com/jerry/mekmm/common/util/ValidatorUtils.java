package com.jerry.mekmm.common.util;

import java.util.regex.Pattern;

public class ValidatorUtils {
    // 正则表达式规则：命名空间和物品名由小写字母、数字、下划线组成，数字部分为整数
    public static final String FORMAT_REGEX = "^([a-z0-9_]+:[a-z0-9_]+)#(\\d+)$";
    public static final Pattern PATTERN = Pattern.compile(FORMAT_REGEX);

    public static boolean validateList(String list) {
        if (list == null) return false; // 处理空列表或null列表
        return PATTERN.matcher(list).matches(); // 任意元素不匹配则返回false
    }
}
