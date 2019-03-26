package com.clsaa.dop.server.code.service;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.code.dao.UserMapper;
import com.clsaa.dop.server.code.model.bo.user.TokenBo;
import com.clsaa.dop.server.code.model.po.User;
import com.clsaa.dop.server.code.util.RequestUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    /**
     * 注册一个用户，并且获得access_token插入数据库
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     */
    public void addUser(String username,String password,String email){

        //首先创建gitlab 用户

        List<NameValuePair> params= new ArrayList<>();
        params.add(new BasicNameValuePair("username",username));
        //name和username相同，因为dop系统注册不需要填name
        params.add(new BasicNameValuePair("name",username));
        params.add(new BasicNameValuePair("email",email));
        params.add(new BasicNameValuePair("password",password));
        params.add(new BasicNameValuePair("skip_confirmation","true"));

        RequestUtil.post("/users","root",params);

        //获得用户的access_token
        params=new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type","password"));
        params.add(new BasicNameValuePair("username",username));
        params.add(new BasicNameValuePair("password",password));
        String access_token= JSON.parseObject(RequestUtil.httpPost1("http://gitlab.dop.clsaa.com/oauth/token",params),TokenBo.class).getAccess_token();


        //将access_token插入数据库
        userMapper.addUser(new User(username,access_token));

    }




}
