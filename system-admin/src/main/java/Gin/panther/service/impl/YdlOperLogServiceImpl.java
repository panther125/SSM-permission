package Gin.panther.service.impl;

import Gin.panther.dao.YdlOperLogDao;
import Gin.panther.entity.OperLog;
import Gin.panther.service.YdlOperLogService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 操作日志(YdlOperLog)表服务实现类
 *
 * @author makejava
 * @since 2022-10-04 15:47:57
 */
@Service("ydlOperLogService")
public class YdlOperLogServiceImpl implements YdlOperLogService {
    @Resource
    private YdlOperLogDao ydlOperLogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param operId 主键
     * @return 实例对象
     */
    @Override
    public OperLog queryById(Integer operId) {
        return ydlOperLogDao.queryById(operId);
    }

    @Override
    public Page<OperLog> queryByPage(OperLog ydlOperLog, PageRequest pageRequest) {
        return null;
    }

    /**
     * 新增数据
     *
     * @param ydlOperLog 实例对象
     * @return 实例对象
     */
    @Override
    public OperLog insert(OperLog ydlOperLog) {
        this.ydlOperLogDao.insert(ydlOperLog);
        return ydlOperLog;
    }

    /**
     * 修改数据
     *
     * @param ydlOperLog 实例对象
     * @return 实例对象
     */
    @Override
    public OperLog update(OperLog ydlOperLog) {
        this.ydlOperLogDao.update(ydlOperLog);
        return this.queryById(ydlOperLog.getOperId());
    }

    /**
     * 通过主键删除数据
     *
     * @param operId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer operId) {
        return this.ydlOperLogDao.deleteById(operId) > 0;
    }
}
