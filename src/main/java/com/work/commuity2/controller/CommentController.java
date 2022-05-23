package com.work.commuity2.controller;

import com.work.commuity2.dto.CommentCreateDto;
import com.work.commuity2.dto.CommentDto;
import com.work.commuity2.dto.ResultDto;
import com.work.commuity2.enums.CommentTypeEnum;
import com.work.commuity2.expection.CustomErrorCode;
import com.work.commuity2.model.Comment;
import com.work.commuity2.model.User;
import com.work.commuity2.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {



    @Autowired
    private CommentService commentService;


    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDto commentDto,
                       HttpServletRequest httpServletRequest){
        User user = (User)httpServletRequest.getSession().getAttribute("user");


        if(user == null){
            return ResultDto.errorOf(CustomErrorCode.NO_LOGIN);
        }


        if(commentDto == null || StringUtils.isEmptyOrWhitespace(commentDto.getContent())){
            return  ResultDto.errorOf(CustomErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment=new Comment();
        comment.setParentId(commentDto.getParent_id());
        comment.setContent(commentDto.getContent());
        comment.setType(commentDto.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setCommentCount(0);
        comment.setLikeCount(0);


        commentService.insert(comment,user);


        return ResultDto.okOf();
    }


    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDto<List<CommentDto>> comments(@PathVariable(name="id") Long id){

        System.out.println("走这个路径");
        List<CommentDto> commentDtos = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDto.okOf(commentDtos);
    }

}
