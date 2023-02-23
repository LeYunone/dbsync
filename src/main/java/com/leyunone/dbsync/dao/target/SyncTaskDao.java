package com.leyunone.dbsync.dao.target;


import com.baomidou.mybatisplus.extension.service.IService;
import com.leyunone.dbsync.model.SyncTask;

/**
 * @author leyunone
 * @date 2021/10/14
 */
public interface SyncTaskDao extends IService<SyncTask> {

    SyncTask selectById(int id);

    int updateSyncTask(SyncTask oldSyncTask, SyncTask newSyncTask);
}
