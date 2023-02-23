package com.leyunone.dbsync.service.target;


import com.leyunone.dbsync.model.SyncTask;

/**
 * @author leyunone
 * @date 2021/10/14
 */
public interface SyncTaskService {

    /**
     * 根据id获取sync_task的记录
     * @param id 记录ID
     * @return {@link SyncTask}
     */
    SyncTask getCurrentSyncTask(int id);
}
