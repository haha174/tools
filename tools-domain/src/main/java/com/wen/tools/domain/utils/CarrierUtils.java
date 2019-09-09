package com.wen.tools.domain.utils;

import com.wen.tools.domain.config.IConstantsDomain;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by li on 2019/1/5.
 */
public class CarrierUtils {

    /**
     * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700,173,199
     **/
    private static final String CHINA_TELECOM_PATTERN = IConstantsDomain.ChinaMobileType.CHINA_TELECOM_PATTERN_STRING;

    /**
     * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1709
     **/
    private static final String CHINA_UNICOM_PATTERN = IConstantsDomain.ChinaMobileType.CHINA_UNICOM_PATTERN_STRING;

    /**
     * 中国移动号码格式验证
     * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184,187,188,147,178,1705
     **/
    private static final String CHINA_MOBILE_PATTERN =IConstantsDomain.ChinaMobileType.CHINA_MOBILE_PATTERN_STRING;


    /**
     * 0、未知 1、移动 2、联通 3、电信
     * @param telPhone
     * @return
     */
    public static int getCarrierIdByTel(String telPhone){
        boolean b1 = StringUtils.isBlank(telPhone) ? false : match(CHINA_MOBILE_PATTERN, telPhone);
        if (b1) {
            return 1;
        }
        b1 =  StringUtils.isBlank(telPhone)  ? false : match(CHINA_UNICOM_PATTERN, telPhone);
        if (b1) {
            return 2;
        }
        b1 =  StringUtils.isBlank(telPhone)  ? false : match(CHINA_TELECOM_PATTERN, telPhone);
        if (b1) {
            return 3;
        }
        return 0;
    }

    /**
     * 0、未知 1、移动 2、联通 3、电信
     * @param telPhone
     * @return
     */
    public static String getCarrierNameByTel(String telPhone){
        int carrierId=0;
        boolean b1 = StringUtils.isBlank(telPhone) ? false : match(CHINA_MOBILE_PATTERN, telPhone);
        if (b1) {
            carrierId= 1;
        }
        b1 =  StringUtils.isBlank(telPhone)  ? false : match(CHINA_UNICOM_PATTERN, telPhone);
        if (b1) {
            carrierId= 2;
        }
        b1 =  StringUtils.isBlank(telPhone)  ? false : match(CHINA_TELECOM_PATTERN, telPhone);
        if (b1) {
            carrierId= 3;
        }
        return IConstantsDomain.ChinaMobileType.CHINA_MOBILE_TYPE[carrierId];
    }
    /**
     * 匹配函数
     * @param regex
     * @param tel
     * @return
     */
    private static boolean match(String regex, String tel) {
        return Pattern.matches(regex, tel);
    }


}
