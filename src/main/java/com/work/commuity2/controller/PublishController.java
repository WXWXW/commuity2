package com.work.commuity2.controller;


import com.work.commuity2.cache.TagCache;
import com.work.commuity2.dto.QuestionDto;
import com.work.commuity2.model.Question;
import com.work.commuity2.model.User;
import com.work.commuity2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {



    @Autowired
    public QuestionService questionService;




    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Long id,
                       Model model){


        QuestionDto question = questionService.getById(id);


        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.get());



        return "/publish";
    }


    @GetMapping("/publish")
    public String Publish(Model model){

        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    @PostMapping ("/publish")
    public String toPublish(
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tag",required = false) String tag,
            @RequestParam(value = "id",required = false) Long id,
            Model model,
            HttpServletRequest request
    ){


        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());


        if(title==null||title==""){
            model.addAttribute("msg","标题不能为空");
            return "publish";
        }
        if(description==null||description==""){
            model.addAttribute("msg","问题补充不能为空");
            return "publish";
        }
        if(tag==null||tag==""){
            model.addAttribute("msg","标签不能为空");
            return "publish";
        }

        String invalited = TagCache.isValited(tag);
        if(!StringUtils.isEmptyOrWhitespace(invalited)) {
            model.addAttribute("msg","输入非法标签"+invalited);
            return  "publish";
        }

        User user=(User) request.getSession().getAttribute("user");

        if(user==null){
            model.addAttribute("msg","未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());


        question.setId(id);


        questionService.createOrUpdate(question);
        return "redirect:/";
    }

}
