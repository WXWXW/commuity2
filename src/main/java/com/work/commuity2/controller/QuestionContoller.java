package com.work.commuity2.controller;


import com.work.commuity2.dto.CommentDto;
import com.work.commuity2.dto.QuestionDto;
import com.work.commuity2.enums.CommentTypeEnum;
import com.work.commuity2.service.CommentService;
import com.work.commuity2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionContoller {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String Question(@PathVariable(name = "id") Long questionid,
                           Model model){

        QuestionDto questionDto=questionService.getById(questionid);
        List<QuestionDto> relatedQuestion = questionService.selectRelated(questionDto);
        List<CommentDto> commentDtoList = commentService.listByTargetId(questionid, CommentTypeEnum.QUESTION);

        //增加阅读数
        questionService.incView(questionid);

        model.addAttribute("question",questionDto);
        model.addAttribute("comments",commentDtoList);

        model.addAttribute("relatedQuestion",relatedQuestion);

        return "question";
    }
}
