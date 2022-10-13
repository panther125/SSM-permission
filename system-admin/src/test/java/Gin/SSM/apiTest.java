package Gin.SSM;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class apiTest {

    @Test
    public void testRequest(){
        // 使用java发送请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://whois.pconline.com.cn/ipJson.jsp?ip=76762142&json=true", String.class);
        System.out.println(forEntity.getBody());
    }
}
