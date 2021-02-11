package com.wen.tools.domain.utils;

import com.wen.tools.domain.config.IConstantsDomain;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by li on 2019/1/5.
 */
public class EmailUtils {

    /**
     * @param email
     * @return
     */
    public static String getEmailCompanyName(String email){
        String emailType = IConstantsDomain.DefaultValue.DEFAULT_UNDEFINED_CN;
        if(StringUtils.endsWith(email,"@163.com")||StringUtils.endsWith(email,"@126.com")){
            emailType = "网易邮箱";
        }else if (StringUtils.endsWith(email,"@139.com")){
            emailType = "移动邮箱";
        }else if (StringUtils.endsWith(email,"@sohu.com")){
            emailType = "搜狐邮箱";
        }else if (StringUtils.endsWith(email,"@qq.com")){
            emailType = "qq邮箱";
        }else if (StringUtils.endsWith(email,"@189.cn")){
            emailType = "189邮箱";
        }else if (StringUtils.endsWith(email,"@tom.com")){
            emailType = "tom邮箱";
        }else if (StringUtils.endsWith(email,"@aliyun.com")){
            emailType = "阿里邮箱";
        }else if (StringUtils.endsWith(email,"@sina.com")){
            emailType = "新浪邮箱";
        }
        else if (StringUtils.endsWith(email,"@google.com")){
            emailType = "Google邮箱";
        }
        return emailType;
    }
}
