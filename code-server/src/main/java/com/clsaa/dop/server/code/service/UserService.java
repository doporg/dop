package com.clsaa.dop.server.code.service;

import com.clsaa.dop.server.code.dao.UserMapper;
import com.clsaa.dop.server.code.model.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * user服务类
 * @author wsy
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询用户的gitlab的access_token
     * @param username 用户名
     * @return access_token
     */
    public String findUserAccessToken(String username){
        return userMapper.findUserAccessToken(username);
    }




}
