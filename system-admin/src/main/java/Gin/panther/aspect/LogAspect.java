package Gin.panther.aspect;

import Gin.panther.annotation.Log;
import Gin.panther.entity.OperLog;
import Gin.panther.service.YdlOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Resource
    private YdlOperLogService operLogService;

    @AfterReturning("@annotation(log)")
    public void afterReturning(JoinPoint joinPoint, Log log) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        logHandler(joinPoint, request, log, null);
    }

    @AfterThrowing(value = "@annotation(log)", throwing = "ex")
    public void afterTrowing(JoinPoint joinPoint, Log log, Exception ex) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        logHandler(joinPoint, request, log, ex);
    }

    /**
     * 异步实现 执行添加日志的方法
     *
     * @param joinPoint
     * @param request
     * @param log
     * @param ex
     */
    private void logHandler(JoinPoint joinPoint, HttpServletRequest request, Log log, Exception ex) {

        // 封装日志实例
        OperLog operLog = new OperLog();
        operLog.setTitle(log.title());
        operLog.setBusinessType(log.businessType());
        if (ex != null) {
            operLog.setErrormsg(ex.getMessage());
            operLog.setStatus(500);
        } else {
            // 异常500 正常200
            operLog.setStatus(200);
        }
        operLog.setOperIp(request.getRemoteAddr());
        operLog.setRequestMethod(request.getMethod());
        operLog.setMethod(joinPoint.getSignature().getName());
        operLog.setOperName(request.getHeader("username"));
        operLog.setOperUrl(request.getRequestURI());
        operLog.setOpertime(new Date());
        //保存日志对象
        operLogService.insert(operLog);

    }
}
