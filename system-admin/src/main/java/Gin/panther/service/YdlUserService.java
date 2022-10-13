package Gin.panther.service;

import Gin.panther.entity.LoginUser;
import Gin.panther.entity.YdlUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;

/**
 * 用户信息表(YdlUser)表服务接口
 *
 * @author makejava
 * @since 2022-10-04 15:47:58
 */
public interface YdlUserService {

    /**
     *  后端登录逻辑
     * @param username
     * @param password
     * @return
     */
    LoginUser login(String username,String password) throws JsonProcessingException;

    /**
     * 用户登出方法
     */
    void logout();

    /**
     * 用户鉴权
     * @return
     */
    HashMap<String, List<String>> getInfo();

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    YdlUser queryById(Long userId);

    /**
     * 分页查询
     *
     * @param ydlUser 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<YdlUser> queryByPage(YdlUser ydlUser, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    YdlUser insert(YdlUser ydlUser);

    /**
     * 修改数据
     *
     * @param ydlUser 实例对象
     * @return 实例对象
     */
    YdlUser update(YdlUser ydlUser);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Long userId);

}
