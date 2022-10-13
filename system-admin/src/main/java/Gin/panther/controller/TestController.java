package Gin.panther.controller;

import Gin.panther.configuration.RedisTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
// 解决跨域 但是不完美，代理较好
//@CrossOrigin
@Slf4j
public class TestController {

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test(){
        redisTemplate.setObject("list", List.of("1001","1002"),200L);
        List<String> result = redisTemplate.getObject("list", new TypeReference<>() {});
        log.info(result.toString());
        return "hello SSM-project";
    }

}
