package Gin.panther.controller;

import Gin.panther.configuration.RedisTemplate;
import Gin.panther.constant.Constants;
import Gin.panther.entity.LoginUser;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class BaseController  {

    @Resource
    protected RedisTemplate redisTemplate;

    protected LoginUser getLoginUser(){

        // 1. 删除redis中的token数据
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 2. 获取首部信息的token
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);
        if (token == null) {
            throw new RuntimeException("当前用户未登录！");
        }
        String tokenPrefix = Constants.TOKENPREFIX + request.getHeader("username")+":";
        String tokenKey = tokenPrefix+token;
        //3. 根据token去redis查看是否有对应信息
        return redisTemplate.getObject(tokenKey, new TypeReference<>() {
        });
    }
}
