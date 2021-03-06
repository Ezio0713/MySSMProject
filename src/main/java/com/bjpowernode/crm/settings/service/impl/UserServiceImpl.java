package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        User user = userMapper.selectUserByLoginact(map);
        return user;
    }

    @Override
    public List<User> queryAllUsers() {
        List<User> users = userMapper.queryAllUsers();
        return  users;
    }

    @Override
    public User queryUserById(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }
}
