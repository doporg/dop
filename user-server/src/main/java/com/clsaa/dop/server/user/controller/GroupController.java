package com.clsaa.dop.server.user.controller;

import com.clsaa.dop.server.user.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户组接口实现类
 *
 * @author joyren
 */
@RestController
@CrossOrigin
public class GroupController {
    @Autowired
    private GroupService groupService;
}
