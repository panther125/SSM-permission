package Gin.panther.interceptor;

import Gin.panther.configuration.CustomObjectMapper;
import Gin.panther.configuration.RedisTemplate;
import Gin.panther.constant.Constants;
import Gin.panther.entity.LoginUser;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CustomObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 设置相应体
        ResponseEntity<String> res = ResponseEntity.status(401).body("Bad Credentials!");

        // 判断请求头是否有Authorization信息，获取首部信息Authorization
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);
        if (token == null) {
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(res));
            return false;
        }
        // 拼接token前缀
        String tokenPrefix = Constants.TOKENPREFIX + request.getHeader("username")+":";
//        Set<String> keys = redisTemplate.keys(Constants.TOKENPREFIX + "*" + token);
//        if (keys== null || keys.size() == 0){
//            response.setStatus(401);
//            response.getWriter().write(objectMapper.writeValueAsString(res));
//            return false;
//        }
        //String tokenKey = (String)keys.toArray()[0];
        String tokenKey = tokenPrefix+token;
        //3. 根据token去redis查看是否有对应信息
        LoginUser loginUser = redisTemplate.getObject(tokenKey, new TypeReference<>() {
        });

        if (loginUser == null) {
            response.setStatus(401);
            response.getWriter().write(objectMapper.writeValueAsString(res));
            return false;
        }
        redisTemplate.expire(tokenKey,30*60L);
        return true;
    }
}
