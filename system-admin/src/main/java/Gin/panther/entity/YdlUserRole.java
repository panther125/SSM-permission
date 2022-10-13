package Gin.panther.entity;

import java.io.Serializable;

/**
 * 用户和角色关联表(YdlUserRole)实体类
 *
 * @author makejava
 * @since 2022-10-04 15:47:58
 */
public class YdlUserRole implements Serializable {
    private static final long serialVersionUID = 800041196679891694L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}

