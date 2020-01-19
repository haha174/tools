package com.wen.tools.domain.utils;


import com.wen.tools.domain.config.IConstantsDomain;

public class BaseResponse {

    private int statusCode;
    private String statusMsg;
    public static final int RESPONSE_STATUS_ERROR_CODE = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_ERROR_CODE;
    public static final int RESPONSE_STATUS_404_CODE = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_404_CODE;
    public static final int RESPONSE_STATUS_400_CODE = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_400_CODE;
    public static final int RESPONSE_STATUS_SUCCESS_CODE = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_SUCCESS_CODE;
    ;
    public static final String RESPONSE_STATUS_SUCCESS_MSG = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_SUCCESS_MSG;
    public static final String RESPONSE_STATUS_FAILED_MSG = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_FAILED_MSG;
    public static final String RESPONSE_STATUS_ERROR_MSG = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_ERROR_MSG;

    public BaseResponse() {
        statusCode = RESPONSE_STATUS_SUCCESS_CODE;
        statusMsg = RESPONSE_STATUS_SUCCESS_MSG;
    }

    public BaseResponse(int statusCode, String statusMsg) {
        super();
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public boolean ifSuccess() {
        return this.statusCode == IConstantsDomain.ResponseConfig.RESPONSE_STATUS_SUCCESS_CODE;
    }

    public void switchErrorResponse() {
        this.setStatusCode(RESPONSE_STATUS_ERROR_CODE);
        this.setStatusMsg(RESPONSE_STATUS_ERROR_MSG);
    }

    public void switchFailResponse() {
        this.setStatusCode(RESPONSE_STATUS_ERROR_CODE);
        this.setStatusMsg(RESPONSE_STATUS_FAILED_MSG);
    }

    public void switchSuccessResponse() {
        this.setStatusCode(RESPONSE_STATUS_SUCCESS_CODE);
        this.setStatusMsg(RESPONSE_STATUS_SUCCESS_MSG);
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "statusCode=" + statusCode +
                ", statusMsg='" + statusMsg + '\'' +
                '}';
    }
}
