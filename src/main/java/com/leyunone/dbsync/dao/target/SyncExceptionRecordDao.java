package com.leyunone.dbsync.dao.target;


import com.leyunone.dbsync.model.SyncExceptionRecordDO;

/**
 * @author leyunone
 * @date 2021/10/15
 */
public interface SyncExceptionRecordDao {

    int insertSyncExceptionRecord(SyncExceptionRecordDO syncExceptionRecordDO);

}
