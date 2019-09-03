package com.wen.tools.domain.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class CommonFormat {
    /**
     * 使用map 去替换字符串中的占位符
     * @param str
     * @param map
     * @return
     */
    public static String formatPlaceholderByMap(String str, Map<String,String> map){
        StringSubstitutor sub = new StringSubstitutor( map );
        return sub.replace ( str );
    }
}
