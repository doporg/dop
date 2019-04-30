package com.clsaa.dop.server.user.testfeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestFeign {
    @Autowired
    private UserFeign userFeign;

    @GetMapping("/test")
    public Object test() {
        Object o =  this.userFeign.searchUserByOrganizationIdAndEmailOrPassword(null, null, 1, 10);
        return o;
    }
}
