package com.work.commuity2.controller;

import com.work.commuity2.dto.FileDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDto upload(){


        FileDto fileDto = new FileDto();
        fileDto.setSuccess(1);
        return fileDto;
    }
}
