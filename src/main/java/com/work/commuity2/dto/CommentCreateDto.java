package com.work.commuity2.dto;

import lombok.Data;

@Data
public class CommentCreateDto {
    private Long parent_id;
    private String content;
    private int type;
}
