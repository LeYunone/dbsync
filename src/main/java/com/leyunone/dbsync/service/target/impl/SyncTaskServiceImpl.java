package com.leyunone.dbsync.service.target.impl;

import com.leyunone.dbsync.dao.target.SyncTaskDao;
import com.leyunone.dbsync.model.SyncTask;
import com.leyunone.dbsync.service.target.SyncTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author leyunone
 * @date 2021/10/14
 */
@Slf4j
@Service
public class SyncTaskServiceImpl implements SyncTaskService {

    private final SyncTaskDao syncTaskDao;

    public SyncTaskServiceImpl(SyncTaskDao syncTaskDao) {
        this.syncTaskDao = syncTaskDao;
    }

    /**
     * 根据id获取sync_task的记录
     * @param id 记录ID
     * @return {@link SyncTask}
     */
    @Override
    public SyncTask getCurrentSyncTask(int id) {
        SyncTask syncTask = syncTaskDao.selectById(id);
        return syncTask;
    }
}
