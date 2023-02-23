package com.leyunone.dbsync.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.leyunone.dbsync.dao.target.SyncExceptionRecordDao;
import com.leyunone.dbsync.dao.target.SyncTaskDao;
import com.leyunone.dbsync.model.*;
import com.leyunone.dbsync.model.enums.SyncTaskEnum;
import com.leyunone.dbsync.model.enums.TableEnum;
import com.leyunone.dbsync.service.source.SourceService;
import com.leyunone.dbsync.service.source.SourceServiceFactory;
import com.leyunone.dbsync.service.target.SyncTaskService;
import com.leyunone.dbsync.service.target.TargetService;
import com.leyunone.dbsync.service.target.TargetServiceFactory;
import com.leyunone.dbsync.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author leyunone
 * @create 2022/12/26
 */
@Service
public class SyncCleanServiceImpl {

    private Logger logger = LoggerFactory.getLogger(SyncCleanServiceImpl.class);

    @Autowired
    private SyncTaskService syncTaskService;
    @Autowired
    private SyncTaskDao syncTaskDao;
    @Autowired
    private SyncExceptionRecordDao syncExceptionRecordDao;

    public XXLJobResult sync(TableEnum tableName, SyncTaskEnum syncTaskEnum){
        logger.info("{} is start clean sync",syncTaskEnum.getServiceName());
        TargetService targetService = TargetServiceFactory.buildTargetServiceFactory().getTargetService(tableName.getTableName());

        List<? extends BaseModel> read = read(targetService);

        //数据比对
        SourceService sourceService = SourceServiceFactory.buildSourceServiceFactory().getSourceService(tableName.getTableName());
        List<? extends BaseModel> sourceList = sourceService.selectListInDays(DateUtils.getBeforeDaysDateTime(1));
        Set<String> set = new HashSet<>();
        if(CollectionUtil.isNotEmpty(sourceList)){
            set = sourceList.stream().map(BaseModel::getMajorKey).collect(Collectors.toSet());
        }

        Set<String> finalSet = set;
        read = read.stream().filter(baseModel -> !finalSet.contains(baseModel.getMajorKey())).collect(Collectors.toList());

        boolean write = write(read, targetService);
        if(write){
            SyncTask currentSyncTask = syncTaskService.getCurrentSyncTask(syncTaskEnum.getTaskId());
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String str = df.format(date);
            LastSyncInfo lastSyncInfo = new LastSyncInfo();
            lastSyncInfo.setLastCreateTime(str);
            lastSyncInfo.setLastUpdateTime(str);

            SyncTask syncTask = new SyncTask();
            syncTask.setId(currentSyncTask.getId());
            syncTask.setCurrentUpdateTime(DateUtil.date());
            syncTask.setLastCreateTime(lastSyncInfo.getLastCreateTime());
            syncTask.setLastUpdateTime(lastSyncInfo.getLastUpdateTime());
            syncTask.setReadCount(currentSyncTask.getReadCount() + read.size());
            syncTask.setWriteCount(currentSyncTask.getWriteCount() + read.size());
            syncTask.setStatus("COMPLETED");
            syncTaskDao.updateSyncTask(currentSyncTask, syncTask);
            return XXLJobResult.buildSuccess("clean:"+read.size());
        }else{
            SyncTask currentSyncTask = syncTaskService.getCurrentSyncTask(syncTaskEnum.getTaskId());
            SyncExceptionRecordDO syncExceptionRecordDO = new SyncExceptionRecordDO();
            syncExceptionRecordDO.setSyncTaskId(syncTaskEnum.getTaskId());
            syncExceptionRecordDO.setLastUpdateTime(currentSyncTask.getLastUpdateTime());
            syncExceptionRecordDO.setStatus("FAILED");
            syncExceptionRecordDao.insertSyncExceptionRecord(syncExceptionRecordDO);
            return XXLJobResult.buildFail();
        }
    }

    private List read(TargetService targetService){
        return targetService.selectListInDays(DateUtils.getBeforeDaysDateTime(1));
    }

    private boolean write (List list,TargetService targetService){
        if(CollectionUtil.isNotEmpty(list)){
            int count = targetService.removeData(list);
            return count == list.size();
        }
        return true;
    }
}
