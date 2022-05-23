package com.work.commuity2.expection;

public class CustomException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomException(CustomErrorCode customErrorCode){
        this.message=customErrorCode.getMessage();
        this.code=customErrorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
