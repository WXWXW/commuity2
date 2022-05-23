package com.work.commuity2.dto;

import com.work.commuity2.expection.CustomErrorCode;
import com.work.commuity2.expection.CustomException;
import lombok.Data;

import java.util.List;

@Data
public class ResultDto<T> {
    private Integer code;
    private String message;
    private T Data;

    public static ResultDto errorOf(Integer code, String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setMessage(message);

        return resultDto;
    }

    public static ResultDto errorOf(CustomErrorCode noLogin) {
        return errorOf(noLogin.getCode(),noLogin.getMessage());
    }

    public static ResultDto okOf() {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");

        return resultDto;
    }

    public static ResultDto errorOf(CustomException throwable) {
        return errorOf(throwable.getCode(),throwable.getMessage());
    }

    public static <T> ResultDto okOf(T t) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setData(t);
        return resultDto;
    }
}
