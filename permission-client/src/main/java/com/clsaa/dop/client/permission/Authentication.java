package com.clsaa.dop.client.permission;

/**
 * 权限管理的切面类
 *
 * @author lzy
 */
import com.clsaa.dop.client.permission.FeignClient.AuthenticService;
import com.clsaa.dop.client.permission.annotation.PermissionName;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;


@Aspect

public class Authentication {

    @Autowired
    AuthenticService authenticService;


//    第一个参数: args(userId, ..)
//    第二个参数: args(*, userId, ..)
//    第三个参数: args(*, *, userId, ..)

    @Pointcut("@annotation(com.clsaa.dop.client.permission.annotation.PermissionName)" )
    public void getAnnotation() {
    }

    @Around("getAnnotation()&&@annotation(permissionName)&&args(userId,..)")
    public Object check(ProceedingJoinPoint pjp, PermissionName permissionName, Long userId) throws Throwable {

        Object obj = null;
        String name=permissionName.name();
        System.out.println("切入了 ，下面执行feign调用");

        try {
            if(authenticService.checkUserPermission(name,userId))
            {
                System.out.println("可以执行切点!!!!!!!!!!");
                obj=pjp.proceed();
            }
            else
            {
                obj=false;
            }
        }
        catch (Throwable throwable)
        {
            return throwable;
        }

        return obj;
    }

}
