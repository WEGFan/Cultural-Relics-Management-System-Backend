package cn.wegfan.relicsmanagement.util;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;

public class EscapeUtil {

    public static String escapeSqlLike(String str) {
        if (StrUtil.isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([\\\\%_])", "\\\\$1");
    }

}
