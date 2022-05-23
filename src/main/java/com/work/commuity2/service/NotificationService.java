package com.work.commuity2.service;

import com.work.commuity2.dto.NotificationDto;
import com.work.commuity2.dto.PaginationDto;
import com.work.commuity2.enums.NotificationStatusEnum;
import com.work.commuity2.enums.NotificationTypeEnum;
import com.work.commuity2.expection.CustomErrorCode;
import com.work.commuity2.expection.CustomException;
import com.work.commuity2.mapper.NotificationMapper;
import com.work.commuity2.model.Notification;
import com.work.commuity2.model.NotificationExample;
import com.work.commuity2.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by codedrinker on 2019/6/14.
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;



    public PaginationDto list(Long id, Integer nowPage, Integer size) {

        PaginationDto<NotificationDto> paginationDto = new PaginationDto<>();

        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (nowPage < 1) {
            nowPage = 1;
        }
        if (nowPage > totalPage) {
            nowPage = totalPage;
        }

        paginationDto.setPagination(totalPage, nowPage);

        //size*(page-1)
        Integer offset = size * (nowPage - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create desc");

        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));

        if (notifications.size() == 0) {
            return paginationDto;
        }

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = new NotificationDto();
            BeanUtils.copyProperties(notification, notificationDto);
            notificationDto.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDtos.add(notificationDto);
        }
        paginationDto.setData(notificationDtos);
        return paginationDto;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDto read(Long id, User user) {

        Notification notification = notificationMapper.selectByPrimaryKey(id);

        if (notification == null) {
            throw new CustomException(CustomErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomException(CustomErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDto notificationDTO = new NotificationDto();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

}
