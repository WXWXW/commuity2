package com.work.commuity2.dto;

import com.work.commuity2.model.User;
import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    private Long parentId;

    private Integer type;


    private Long commentator;


    private Long gmtCreate;


    private Long gmtModified;


    private Long likeCount;


    private String content;


    private User user;

    private Integer commentCount;

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", type=" + type +
                ", commentator=" + commentator +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", likeCount=" + likeCount +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", commentCount=" + commentCount +
                '}';
    }
}
