package Gin.panther.dao;

import Gin.panther.entity.OperLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 操作日志(YdlOperLog)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-04 15:47:57
 */
public interface YdlOperLogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param operId 主键
     * @return 实例对象
     */
    OperLog queryById(Integer operId);

    /**
     * 查询指定行数据
     *
     * @param ydlOperLog 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<OperLog> queryAllByLimit(OperLog ydlOperLog, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param ydlOperLog 查询条件
     * @return 总行数
     */
    long count(OperLog ydlOperLog);

    /**
     * 新增数据
     *
     * @param ydlOperLog 实例对象
     * @return 影响行数
     */
    int insert(OperLog ydlOperLog);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<YdlOperLog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<OperLog> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<YdlOperLog> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<OperLog> entities);

    /**
     * 修改数据
     *
     * @param ydlOperLog 实例对象
     * @return 影响行数
     */
    int update(OperLog ydlOperLog);

    /**
     * 通过主键删除数据
     *
     * @param operId 主键
     * @return 影响行数
     */
    int deleteById(Integer operId);

}

