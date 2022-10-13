package Gin.panther.controller;

import Gin.panther.annotation.HasPermission;
import Gin.panther.annotation.Log;
import Gin.panther.constant.Constants;
import Gin.panther.entity.LoginUser;
import Gin.panther.entity.YdlUser;
import Gin.panther.service.YdlUserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 用户信息表(YdlUser)表控制层
 *
 * @author makejava
 * @since 2022-10-04 15:47:58
 */
@RestController
@RequestMapping("ydlUser")
public class YdlUserController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private YdlUserService ydlUserService;

    /**
     * 分页查询
     *
     * @param ydlUser 筛选条件
     * @return 查询结果
     */
    @GetMapping
    @Log(title = "查询用户",businessType = "用户操作")
    public ResponseEntity<Page<YdlUser>> queryByPage(YdlUser ydlUser) {
        return ResponseEntity.ok(this.ydlUserService.queryByPage(ydlUser, PageRequest.of(ydlUser.getPage(),ydlUser.getSize())));
    }

    @GetMapping("/getInfo")
    public ResponseEntity<HashMap<String, List<String>>> getInfo() {
        return ResponseEntity.ok(this.ydlUserService.getInfo());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @HasPermission("system:user")
    public ResponseEntity<YdlUser> queryById(@PathVariable("id") Long id) {

        // 获取用户，根据用户信息判断用户角色和权限限制用户的操作
        LoginUser loginUser = getLoginUser();
        List<String> hasPerms = redisTemplate.getObject(Constants.PERMPREFIX + loginUser.getToken(), new TypeReference<>() {
        });

        if(!hasPerms.contains("system:user")){
            throw new RuntimeException("您没有权限操作");
        }

        return ResponseEntity.ok(this.ydlUserService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param ydlUser 实体
     * @return 新增结果
     */
    @PostMapping
    @HasPermission("system:user")
    @Log(title = "添加用户",businessType = "用户操作")
    public ResponseEntity<YdlUser> add(@RequestBody YdlUser ydlUser) {
        return ResponseEntity.ok(this.ydlUserService.insert(ydlUser));
    }

    /**
     * 编辑数据
     *
     * @param ydlUser 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<YdlUser> edit(YdlUser ydlUser) {
        return ResponseEntity.ok(this.ydlUserService.update(ydlUser));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.ydlUserService.deleteById(id));
    }

}

