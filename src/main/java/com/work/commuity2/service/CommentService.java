package com.work.commuity2.service;

import com.work.commuity2.dto.CommentDto;
import com.work.commuity2.enums.CommentTypeEnum;
import com.work.commuity2.enums.NotificationStatusEnum;
import com.work.commuity2.enums.NotificationTypeEnum;
import com.work.commuity2.expection.CustomErrorCode;
import com.work.commuity2.expection.CustomException;
import com.work.commuity2.mapper.*;
import com.work.commuity2.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentor) {

        System.out.println(comment);

        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomException(CustomErrorCode.TARGET_NOT_FOUND);
        }

        if(comment.getType()==null|| !CommentTypeEnum.isExist(comment.getType())){


            throw new CustomException(CustomErrorCode.TYPE_ERROR);
        }

        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbcomment == null){

                throw new CustomException(CustomErrorCode.COMMENT_NOT_FOUND);
            }

            //回复评论
            Question question = questionMapper.selectByPrimaryKey(dbcomment.getParentId());
            if(question==null){
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
            }

            //插入评论
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

            //创建通知
            createNotify(comment, dbcomment.getCommentator(), commentor.getName(), question.getTitle(),question.getId(), NotificationTypeEnum.REPLY_COMMENT);

        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomException(CustomErrorCode.QUESTION_NOT_FOUND);
            }else {

                commentMapper.insert(comment);
                question.setCommentCount(1);
                questionExtMapper.incCommentCount(question);

                createNotify(comment,question.getCreator(),commentor.getName(),question.getTitle(),question.getId(),NotificationTypeEnum.REPLY_QUESTION);


            }
        }

    }

    //创建通知
    private void createNotify(Comment comment, Long Receiver,
                              String name, String title,Long id, NotificationTypeEnum notificationType) {

        if(Receiver == comment.getParentId()){
            return;
        }


        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(id);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(Receiver);
        notification.setNotifierName(name);
        notification.setOuterTitle(title);
        notificationMapper.insert(notification);
    }


    public List<CommentDto> listByTargetId(Long id , CommentTypeEnum type) {

        CommentExample commentExample = new CommentExample();
        System.out.println(id);
        System.out.println(type.getType());
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);



        if(comments.size()==0){
            System.out.println("查不到一级回复");
            return new ArrayList<>();
        }else{

            //获取去重的评论员id
            Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
            List<Long> usersId = new ArrayList<>();
            usersId.addAll(commentators);

            //获取评论人并转换为map
            UserExample userExample = new UserExample();
            userExample.createCriteria().andIdIn(usersId);
            List<User> users =  userMapper.selectByExample(userExample);

            for( User user : users){
                System.out.println(1111);
                System.out.println(user.toString());
            }
            Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


            //转换comment为commentDto
            List<CommentDto> commentDtos = comments.stream().map(comment -> {
                CommentDto commentDto = new CommentDto();
                BeanUtils.copyProperties(comment,commentDto);

                commentDto.setUser(userMap.get(comment.getCommentator()));
                System.out.println(commentDto.toString());
                return commentDto;
            }).collect(Collectors.toList());
            return commentDtos;
        }
    }
}
