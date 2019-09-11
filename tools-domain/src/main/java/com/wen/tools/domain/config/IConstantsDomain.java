package com.wen.tools.domain.config;

public interface IConstantsDomain {
    String ENV_PROPERTIES_FILE = "env.properties";

    interface DefaultValue {
        String NO_VALUE_KEY = "__NO_VALUE_KEY";
        String DEFAULT_UNDEFINED = "<undefined>";
        String DEFAULT_UNDEFINED_CN = "未知";
    }

    interface COMMON_CONFIG {
        String CHAR_SPOT = ".";
    }

    interface ResponseConfig {
        int RESPONSE_STATUS_CODE_ERROR = 500;
        int RESPONSE_STATUS_CODE_404 = 404;
        int RESPONSE_STATUS_CODE_400 = 400;
        int RESPONSE_STATUS_CODE_SUCCESS = 200;
        String RESPONSE_STATUS_CODE_SUCCESS_MSG = "success";
        String RESPONSE_STATUS_CODE_FAILED_MSG = "failed";
        String RESPONSE_STATUS_CODE_ERROR_MSG = "error";
    }

    interface ChinaMobileType {
        String CHINA_TELECOM_PATTERN_STRING = "(^1(33|53|77|73|99|8[019])\\d{8}$)|(^1700\\d{7}$)";
        String CHINA_UNICOM_PATTERN_STRING = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";
        String CHINA_MOBILE_PATTERN_STRING = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";
        // 0、未知 1、移动 2、联通 3、电信
        String CHINA_MOBILE_TYPE[] = {DefaultValue.DEFAULT_UNDEFINED_CN, "移动", "联通", "电信"};
    }
}
