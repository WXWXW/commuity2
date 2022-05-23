package com.work.commuity2.service;

import com.work.commuity2.mapper.UserMapper;
import com.work.commuity2.model.User;
import com.work.commuity2.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);



        if(users.size()==0){

            user.setGmtCreate(Long.valueOf(System.currentTimeMillis()));
            user.setGmtModified(user.getGmtCreate());



            userMapper.insert(user);

        }else{

            User dbUser = users.get(0);

            User updateUser =new User();
            updateUser.setGmtModified(user.getGmtCreate());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser,example);
        }
    }
}
