package com.work.commuity2.dto;

import com.work.commuity2.model.User;
import lombok.Data;

@Data
public class QuestionDto {
    private Long id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private User user;


    @Override
    public String toString() {
        return "QuestionDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", creator=" + creator +
                ", commentCount=" + commentCount +
                ", viewCount=" + viewCount +
                ", likeCount=" + likeCount +
                ", tag='" + tag + '\'' +
                ", user=" + user +
                '}';
    }
}
