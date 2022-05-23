package com.work.commuity2.dto;

import lombok.Data;

/**
 * Created by codedrinker on 2019/6/14.
 */
@Data
public class NotificationDto {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
