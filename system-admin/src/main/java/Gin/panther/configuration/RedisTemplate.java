package Gin.panther.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import javax.annotation.Resource;
import java.util.Set;

@Component
@Slf4j
public class  RedisTemplate {

    @Resource
    private JedisPool jedisPool;

    @Resource
    private CustomObjectMapper objectMapper;

    // 保存字符串类型的数据
    public String set(String key,String value, Long expire){
        Jedis jedis = jedisPool.getResource();
        String result = null;
        try {
            result = jedis.setex(key, expire, value);
        }catch (JedisException je){
            log.error("redis execution exception!!!",je);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String result = null;
        try {
            result = jedis.get(key);
        }catch (JedisException je){
            log.error("redis execution exception!!!",je);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    // 将对象以序列化的方式存入redis 采用JSON格式
    public String setObject(String key,Object value, Long expire){
        Jedis jedis = jedisPool.getResource();
        String result = null;
        try {
            // 序列化对象
            String JsonValue = objectMapper.writeValueAsString(value);
            if(expire <= 0){
                result = jedis.set(key,JsonValue);
            }else{
                result = jedis.setex(key,expire,JsonValue);
            }
        }catch (JsonProcessingException je){
            log.error("redis execution exception!!!",je);
            jedisPool.returnBrokenResource(jedis);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    public <T> T getObject(String key, TypeReference<T> typeReference){
        Jedis jedis = jedisPool.getResource();
        String result = null;
        T object = null;
        try {
            result = jedis.get(key);
            object = objectMapper.readValue(result,typeReference);
        }catch (JsonProcessingException je){
            log.error("redis execution exception!!!",je);
            jedisPool.returnBrokenResource(jedis);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return object;
    }

    public Long remove(String... key){
        Jedis jedis = jedisPool.getResource();
        long result = 0L;
        try {
             result = jedis.del(key);
        }catch (JedisException je){
            log.error("redis execution exception!!!",je);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }
    // 给token续命
    public long expire(String key, Long expire){
        Jedis jedis = jedisPool.getResource();
        long result = -1L;
        try {
            result = jedis.expire(key, expire);
        }catch (JedisException je){
            log.error("redis execution exception!!!",je);
            jedisPool.returnBrokenResource(jedis);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

    // 查询特定的key
    public  Set<String> keys(String pattern){
        Jedis jedis = jedisPool.getResource();
        Set<String> result = null;
        try {
            result = jedis.keys(pattern);
        }catch (JedisException je){
            log.error("redis execution exception!!!",je);
            jedisPool.returnBrokenResource(jedis);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return result;
    }

}