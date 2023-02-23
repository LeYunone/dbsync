package com.leyunone.dbsync.dao.target.impl;

import com.leyunone.dbsync.dao.target.SyncExceptionRecordDao;
import com.leyunone.dbsync.dao.target.mapper.SyncExceptionRecordMapper;
import com.leyunone.dbsync.model.SyncExceptionRecordDO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author leyunone
 * @date 2021/10/15
 */
@Repository
public class SyncExceptionRecordDaoImpl implements SyncExceptionRecordDao {

    @Resource
    private SyncExceptionRecordMapper syncExceptionRecordMapper;

    @Override
    public int insertSyncExceptionRecord(SyncExceptionRecordDO syncExceptionRecordDO) {
        return syncExceptionRecordMapper.insert(syncExceptionRecordDO);
    }
}
