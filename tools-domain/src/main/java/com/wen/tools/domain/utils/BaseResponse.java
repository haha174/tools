package com.wen.tools.domain.utils;


import com.wen.tools.domain.config.IConstantsDomain;

public class BaseResponse
{

  private int statusCode ;
  private String statusMsg;

  public BaseResponse()
  {
    statusCode = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_SUCCESS;
    statusMsg = IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_SUCCESS_MSG;
  }

  public BaseResponse(int statusCode, String statusMsg)
  {
    super();
    this.statusCode = statusCode;
    this.statusMsg = statusMsg;
  }

  public int getStatusCode()
  {
    return statusCode;
  }

  public void setStatusCode(int statusCode)
  {
    this.statusCode = statusCode;
  }

  public String getStatusMsg()
  {
    return statusMsg;
  }

  public void setStatusMsg(String statusMsg)
  {
    this.statusMsg = statusMsg;
  }

  public boolean isSuccess(){
    return this.statusCode== IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_SUCCESS;
  }

  public void switchErrorResponse(){
    this.setStatusCode(IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_ERROR);
    this.setStatusMsg(IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_ERROR_MSG);
  }

  public void switchFailResponse(){
    this.setStatusCode(IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_ERROR);
    this.setStatusMsg(IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_FAILED_MSG);
  }

  public void switchSuccessResponse(){
    this.setStatusCode(IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_SUCCESS);
    this.setStatusMsg(IConstantsDomain.ResponseConfig.RESPONSE_STATUS_CODE_SUCCESS_MSG);
  }

  @Override
  public String toString() {
    return "BaseResponse{" +
            "statusCode=" + statusCode +
            ", statusMsg='" + statusMsg + '\'' +
            '}';
  }
}
