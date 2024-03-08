package com.demo.flowable.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @author : gr
 * @date : 2024/3/7 13:59
 */
public class StringUtil extends StrUtil {

    /**
     * 将对象转为字符串, null -> ''
     * @param object 字符串对象
     * @return 字符串
     */
    public static String nullObject2Str(Object object){
        try {
            return StrUtil.emptyToNull(object.toString());
        } catch (Exception e) {
            return "";
        }
    }
}
