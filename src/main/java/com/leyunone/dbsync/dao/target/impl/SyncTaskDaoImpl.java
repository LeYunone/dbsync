package com.leyunone.dbsync.dao.target.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyunone.dbsync.dao.target.SyncTaskDao;
import com.leyunone.dbsync.dao.target.mapper.SyncTaskMapper;
import com.leyunone.dbsync.model.SyncTask;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author leyunone
 * @date 2021/10/14
 */
@Repository
public class SyncTaskDaoImpl extends ServiceImpl<SyncTaskMapper, SyncTask> implements SyncTaskDao {

    @Resource
    private SyncTaskMapper syncTaskMapper;

    @Override
    public SyncTask selectById(int id) {
        return syncTaskMapper.selectById(id);
    }

    @Override
    public int updateSyncTask(SyncTask oldSyncTask, SyncTask newSyncTask) {
        LambdaUpdateWrapper<SyncTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SyncTask::getId, newSyncTask.getId());
        // 并发更新初始状态判断，保证数据一致性
        if (StringUtils.isNotEmpty(newSyncTask.getLastCreateTime())){
            updateWrapper.eq(SyncTask::getLastCreateTime, oldSyncTask.getLastCreateTime());
        }
        if (StringUtils.isNotEmpty(newSyncTask.getLastUpdateTime())){
            updateWrapper.eq(SyncTask::getLastUpdateTime, oldSyncTask.getLastUpdateTime());
        }
        if (newSyncTask.getReadCount() != null){
            updateWrapper.eq(SyncTask::getReadCount, oldSyncTask.getReadCount());
        }
        if (newSyncTask.getWriteCount() != null){
            updateWrapper.eq(SyncTask::getWriteCount, oldSyncTask.getWriteCount());
        }
        if (newSyncTask.getRollbackCount() != null){
            updateWrapper.eq(SyncTask::getRollbackCount, oldSyncTask.getRollbackCount());
        }
        if (newSyncTask.getReadSkipCount() != null){
            updateWrapper.eq(SyncTask::getReadSkipCount, oldSyncTask.getReadSkipCount());
        }
        if (newSyncTask.getProcessSkipCount() != null){
            updateWrapper.eq(SyncTask::getProcessSkipCount, oldSyncTask.getProcessSkipCount());
        }
        if (newSyncTask.getWriteSkipCount() != null){
            updateWrapper.eq(SyncTask::getWriteSkipCount, oldSyncTask.getWriteSkipCount());
        }
        return syncTaskMapper.update(newSyncTask, updateWrapper);
    }
}
