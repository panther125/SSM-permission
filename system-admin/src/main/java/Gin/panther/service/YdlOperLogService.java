package Gin.panther.service;

import Gin.panther.entity.OperLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 操作日志(YdlOperLog)表服务接口
 *
 * @author makejava
 * @since 2022-10-04 15:47:57
 */
public interface YdlOperLogService {

    /**
     * 通过ID查询单条数据
     *
     * @param operId 主键
     * @return 实例对象
     */
    OperLog queryById(Integer operId);

    /**
     * 分页查询
     *
     * @param ydlOperLog 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<OperLog> queryByPage(OperLog ydlOperLog, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param OperLog 实例对象
     * @return 实例对象
     */
    OperLog insert(OperLog OperLog);

    /**
     * 修改数据
     *
     * @param OperLog 实例对象
     * @return 实例对象
     */
    OperLog update(OperLog OperLog);

    /**
     * 通过主键删除数据
     *
     * @param operId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer operId);

}
