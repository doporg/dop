package com.clsaa.dop.client.permission.service;

import com.clsaa.dop.client.permission.annotation.PermissionName;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @PermissionName(name="创建功能点")
    public void TestService(Long userId)
    {
        System.out.println("用户 "+userId+"执行了此条操作" );
    }
}
