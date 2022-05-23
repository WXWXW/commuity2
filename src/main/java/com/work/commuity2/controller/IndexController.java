package com.work.commuity2.controller;

import com.work.commuity2.dto.PaginationDto;
import com.work.commuity2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {


    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam (name="nowPage",defaultValue = "1") Integer nowPage,
                        @RequestParam (name="size",defaultValue = "5") Integer size,
                        @RequestParam (name="search",required = false) String search){

        PaginationDto paginationDto= questionService.List(search,nowPage,size);

        model.addAttribute("paginationDto",paginationDto);
        model.addAttribute("search",search);

        return "index";
    };





}
