package Gin.panther.service.impl;

import Gin.panther.configuration.CustomObjectMapper;
import Gin.panther.configuration.RedisTemplate;
import Gin.panther.constant.Constants;
import Gin.panther.entity.LoginUser;
import Gin.panther.entity.YdlMenu;
import Gin.panther.entity.YdlRole;
import Gin.panther.entity.YdlUser;
import Gin.panther.dao.YdlUserDao;
import Gin.panther.exception.PasswordIncorrectException;
import Gin.panther.exception.UserNotFoundException;
import Gin.panther.service.YdlUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户信息表(YdlUser)表服务实现类
 *
 * @author makejava
 * @since 2022-10-04 15:47:58
 */
@Service("ydlUserService")
@Slf4j
public class YdlUserServiceImpl implements YdlUserService {
    @Resource
    private YdlUserDao ydlUserDao;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private CustomObjectMapper objectMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public LoginUser login(String username, String password) throws JsonProcessingException {

        // 数据库校验数据是否正确
        YdlUser user = ydlUserDao.queryByUserName(username);
        if(user == null)
            throw new UserNotFoundException("登录失败！用户名错误。");
        if(!password.equals(user.getPassword())){
            log.info("密码输错了！！！");
            throw new PasswordIncorrectException("登录失败！密码错误。");
        }
        // if 正确
        // （1） 生成token
        String token = UUID.randomUUID().toString();

        // 获取ip
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取浏览器信息
        UserAgent userAgent = new UserAgent(request.getHeader("User-Agent"));
        // 解析ip获取JSON格式的地址保存到LoginUser中
        ResponseEntity<String> result = restTemplate.getForEntity("https://whois.pconline.com.cn/ipJson.jsp?ip=76762142&json=true", String.class);
        String body = result.getBody();
        Map<String,String> localMap = objectMapper.readValue(body, new TypeReference<>() {});
        String location =localMap.get("addr")+localMap.get("region");
        // （2） 封装一个登录用户
        LoginUser loginUser = LoginUser.builder()
                .userId(user.getUserId())
                .token(token)
                .ipaddr(request.getRemoteAddr())
                .loginTime(new Date())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .loginLocation(location)
                .ydlUser(user).build();
        // 将登陆用户存入redis 半小时过期
        // 1.生成带用户名的key前缀 token:username:uuid
        String keyPrefix = Constants.TOKENPREFIX+username+":";
        // 2. 根据生成的key查询前缀数据
        Set<String> keys = redisTemplate.keys(keyPrefix + "*");
        // 3. 删除原来的数据
       keys.forEach( temp ->{
           redisTemplate.remove(temp);
       });
        // 存入新的数据加入redis
        redisTemplate.setObject(keyPrefix+token,loginUser,30*60L);

        return loginUser;
    }

    /**
     * 用户退出
     */
    @Override
    public void logout() {
        // 1. 删除redis中的token数据
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        // 2. 获取首部信息的token
        String token = request.getHeader(Constants.HEAD_AUTHORIZATION);

        // 删除
        Long remove = redisTemplate.remove(Constants.TOKENPREFIX+token);
    }

    /**
     * 获取用户全部信息
     */
    @Override
    public HashMap<String,List<String>> getInfo() {

        // 1.获取当前登录用户
        LoginUser loginUser = getLoginUser();
        // 2. 查询当前用户的角色和权限
        YdlUser info = ydlUserDao.getInfo(loginUser.getUserId());

        // 3. 处理权限和角色的相关信息
        List<String> roleTags = info.getYdlRoles().stream().map(YdlRole::getRoleTag).collect(Collectors.toList());
        redisTemplate.setObject(Constants.ROLEPREFIX + loginUser.getToken(),roleTags,30*60L);

        List<String> prems = new ArrayList<>();
        info.getYdlRoles().stream().map(YdlRole::getYdlMenus).forEach(menus -> {
            prems.addAll(menus.stream().map(YdlMenu::getPerms).collect(Collectors.toList()));
        });
        redisTemplate.setObject(Constants.PERMPREFIX + loginUser.getToken(),prems,30*60L);

        // 整合数据
        HashMap<String,List<String>> data = new HashMap<>();
        data.put("roles",roleTags);
        data.put("perms",prems);

        return data;
    }

    private LoginUser getLoginUser(){

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

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public YdlUser queryById(Long userId) {
        return this.ydlUserDao.queryById(userId);
    }

    /**
     * 分页查询
     *
     * @param ydlUser 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<YdlUser> queryByPage(YdlUser ydlUser, PageRequest pageRequest) {
        long total = this.ydlUserDao.count(ydlUser);
        return new PageImpl<>(this.ydlUserDao.queryAllByLimit(ydlUser, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    @Override
    public YdlUser insert(YdlUser ydlUser) {
        this.ydlUserDao.insert(ydlUser);
        return ydlUser;
    }

    /**
     * 修改数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    @Override
    public YdlUser update(YdlUser ydlUser) {
        this.ydlUserDao.update(ydlUser);
        return this.queryById(ydlUser.getUserId());
    }

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long userId) {
        return this.ydlUserDao.deleteById(userId) > 0;
    }
}
