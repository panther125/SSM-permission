package Gin.panther.aspect;

import Gin.panther.annotation.HasPermission;
import Gin.panther.annotation.HasRole;
import Gin.panther.configuration.RedisTemplate;
import Gin.panther.constant.Constants;
import Gin.panther.exception.PermissionNeedHasException;
import Gin.panther.exception.RoleNeedHasException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Component
@Aspect
public class PermissionAspect {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 执行接口所需角色的切面
     * @param joinPoint
     * @param hasRole
     */
    @Before("@annotation(hasRole)")
    public void roleBefore(JoinPoint joinPoint, HasRole hasRole){
        // 获得当前方法所需要的角色
        String[] needRoles = hasRole.value();
        // 获得拥有的角色
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);
        List<String> hasRoles = redisTemplate.getObject(Constants.ROLEPREFIX + token, new TypeReference<>() {
        });
        // 只要所需的角色有一个在你拥有的角色中就放行
        boolean flag = false;
        for (String needRole : needRoles) {
            if (hasRoles.contains(needRole)) {
                flag = true;
                break;
            }
        }
        if(!flag) throw new RoleNeedHasException("您没有该接口锁需要的角色");
    }

    @Before("@annotation(hasPermission)")
    public void roleBefore(JoinPoint joinPoint, HasPermission hasPermission){
        // 获得当前方法所需要的权限
        String[] needPermissions = hasPermission.value();
        // 获得拥有的权限
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);
        List<String> hasPermissions = redisTemplate.getObject(Constants.PERMPREFIX + token, new TypeReference<>() {
        });
        // 只要所需的角色有一个在你拥有的角色中就放行
        boolean flag = false;
        for (String needPermission : needPermissions) {
            if (hasPermissions.contains(needPermission)) {
                flag = true;
                break;
            }
        }
        if(!flag) throw new PermissionNeedHasException("您没有该接口锁需要的角色");
    }
}
