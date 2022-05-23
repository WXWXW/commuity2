package com.work.commuity2.advice;

import com.alibaba.fastjson.JSON;
import com.work.commuity2.dto.ResultDto;
import com.work.commuity2.expection.CustomErrorCode;
import com.work.commuity2.expection.CustomException;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomerExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handler(Throwable throwable, Model model,
                   HttpServletRequest httpServletRequest,
                   HttpServletResponse httpServletResponse) throws IOException {

        String contentType = httpServletRequest.getContentType();

        ResultDto resultDto;

        if (contentType.equals("application/json")) {
            //返回json


            System.out.println(throwable.getMessage());
            System.out.println(throwable.getClass());

            if (throwable instanceof CustomException) {


                resultDto  = ResultDto.errorOf((CustomException) throwable);

            } else {
                System.out.println("SYSTEM_ERROR");

                resultDto  = ResultDto.errorOf(CustomErrorCode.SYSTEM_ERROR);

            }

            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(200);
            PrintWriter printWriter = httpServletResponse.getWriter();
            printWriter.write(JSON.toJSONString(resultDto));
            printWriter.close();

            return null;

        } else {

            //跳转错误页面
            if (throwable instanceof CustomException) {
                model.addAttribute("message", throwable.getMessage());

            } else {
                model.addAttribute("message", CustomErrorCode.SYSTEM_ERROR.getMessage());

            }
            return new ModelAndView("error");
        }

    }
}

