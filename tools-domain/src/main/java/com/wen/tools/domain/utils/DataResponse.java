package com.wen.tools.domain.utils;

public class DataResponse<T> extends BaseResponse {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public DataResponse() {
        super();
    }

    public DataResponse(T t) {
        super();
        value = t;
    }

    public void switchErrorResponse(T t) {
        super.switchErrorResponse();
        setValue(t);
    }

    public void switchFailResponse(T t) {
        super.switchFailResponse();
        setValue(t);
    }

    public void switchSuccessResponse(T t) {
        super.switchSuccessResponse();
        setValue(t);
    }

}
