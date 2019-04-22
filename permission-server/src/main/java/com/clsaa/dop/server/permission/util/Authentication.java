package com.clsaa.dop.server.permission.util;

/**
 * 切面拦截注解及权限校验处理
 *
 * @author lzy
 */

import com.clsaa.dop.server.permission.annotation.PermissionName;
import com.clsaa.dop.server.permission.service.AuthenticationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class Authentication {

    @Autowired
    AuthenticationService authenticationService;

    //解析 权限注解，判断权限是否生效
    @Pointcut("@annotation(com.clsaa.dop.server.permission.annotation.PermissionName)" )
    public void authentication()
    {
    }
//    第一个参数: args(userId, ..)
//    第二个参数: args(*, userId, ..)
//    第三个参数: args(*, *, userId, ..)

    @Around("authentication()&&@annotation(permissionName)&&args(userId,..)")
    public Object check(ProceedingJoinPoint pjp, PermissionName permissionName, Long userId) throws Throwable {
        String name=permissionName.name();


        if(authenticationService.checkUserPermission(name,userId))
        {
            System.out.println("权限验证通过");
            return pjp.proceed();
        }
        else
        {
            System.out.println("验证权限失败，您没有权限！");
            throw new Exception("验证权限失败，您没有权限！");
        }
    }

}