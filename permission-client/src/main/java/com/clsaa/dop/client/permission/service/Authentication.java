package com.clsaa.dop.client.permission.service;

/**
 * 切面拦截注解及权限校验处理
 *
 * @author lzy
 */
import com.clsaa.dop.client.permission.annotation.PermissionName;
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
    @Pointcut("@annotation(com.clsaa.dop.client.permission.annotation.PermissionName)" )
    public void authentication()
    {
    }
//    第一个参数: args(userId, ..)
//    第二个参数: args(*, userId, ..)
//    第三个参数: args(*, *, userId, ..)

    @Around("authentication()&&@annotation(permissionName)&&args(userId,..)")
    public void check(ProceedingJoinPoint pjp, PermissionName permissionName, Long userId) throws Throwable {
        String name=permissionName.name();

        if(authenticationService.checkUserPermission(name,userId))
        {
            pjp.proceed();
        }
        else
        {
            System.out.println("验证权限失败，您没有权限！");
            throw new Exception("验证权限失败，您没有权限！");
        }
    }

}