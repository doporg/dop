package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.UserFeign;
import com.clsaa.dop.server.application.model.vo.UserV1;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = "UserService")
public class UserService {
    @Autowired
    private UserFeign userFeign;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String findUserNameById(Long userId) {
        logger.info("[findUserNameById] Request coming: userId={}",userId);
        return this.userFeign.findUserNameById(userId) == null ? "" : this.userFeign.findUserNameById(userId).getName();
    }

    public UserV1 findUserById(Long userId) {
        logger.info("[findUserById] Request coming: userId={}",userId);
        return this.userFeign.findUserById(userId);
    }


    public Pagination<UserV1> findUsersNotInProject(String key, Integer pageNo, Integer pageSize) {

        logger.info("[findUsersNotInProject] Request coming: key={}, pageNo={}, pageSize={}",key,pageNo,pageSize);
        return this.userFeign.searchUserByOrganizationIdAndEmailOrPassword(key, null, pageNo, pageSize);

    }
}


