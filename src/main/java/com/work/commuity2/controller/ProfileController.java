package com.work.commuity2.controller;

import com.work.commuity2.dto.NotificationDto;
import com.work.commuity2.dto.PaginationDto;
import com.work.commuity2.model.User;
import com.work.commuity2.service.NotificationService;
import com.work.commuity2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name="action") String action, Model model,HttpServletRequest request,
                           @RequestParam(name="nowPage",defaultValue = "1") Integer nowPage,
                          @RequestParam (name="size",defaultValue = "5") Integer size){




        User user=(User) request.getSession().getAttribute("user");

        if(user == null){
            return "redirect:/";
        }

        if("questions".equals(action)){

            System.out.println(user.getId());

            PaginationDto paginationDto = questionService.list(user.getId(), nowPage, size);

            model.addAttribute("paginationDto",paginationDto);
          model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");


        }else if("replies".equals(action)){


            PaginationDto paginationDto = notificationService.list(user.getId(),nowPage, size);
            Long unreadCount =notificationService.unreadCount(user.getId());

            model.addAttribute("paginationDto",paginationDto);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");

        }






        return "profile";
    }
}
