package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "UserService")
public class UserService {
    @Autowired
    private UserFeign userFeign;

    public String findUserNameById(Long userId) {
        return this.userFeign.findUserNameById(userId) == null ? "" : this.userFeign.findUserNameById(userId).getName();
    }
}
