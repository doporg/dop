import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;


public class Authentication {

    public void before(JoinPoint joinPoint) throws Exception{

        //获得目标方法
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();

        System.out.println(target + "-------" + methodName);
        Method method = target.getClass().getMethod(methodName);
        //判断目标方法上面是否存在 注解

        boolean annotationPresent = method.isAnnotationPresent(PermissionName.class);
        String permissionName="";

        if (annotationPresent) {
            //得到方法上的注解,并取出对应的值
            permissionName = method.getAnnotation(PermissionName.class).value();
        }
    }
}