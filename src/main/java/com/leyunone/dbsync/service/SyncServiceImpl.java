package com.leyunone.dbsync.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leyunone
 * @create 2022/12/26 同步服务
 */
@Service
public class SyncServiceImpl {

    @Autowired
    private SyncTaskService syncTaskService;
    @Autowired
    private SyncExceptionRecordDao syncExceptionRecordDao;
    @Autowired
    private SyncTaskDao syncTaskDao;

    private Integer objCode = 0;

    private Logger logger = LoggerFactory.getLogger(SyncServiceImpl.class);

    public XXLJobResult sync(TableEnum tableEnum, SyncTaskEnum syncTaskEnum, String lastTime) {
        logger.info("{} is start sync",syncTaskEnum.getServiceName());
        SyncTask currentSyncTask = syncTaskService.getCurrentSyncTask(syncTaskEnum.getTaskId());
        String lastCreateTime = currentSyncTask.getLastCreateTime();
        String lastUpdateTime = currentSyncTask.getLastUpdateTime();

        if(StringUtils.isNotEmpty(lastTime)){
            lastCreateTime = lastTime;
            lastUpdateTime = lastTime;
        }

        //读
        SourceService sourceService = SourceServiceFactory.buildSourceServiceFactory().getSourceService(tableEnum.getTableName());

        List read = read(lastCreateTime, lastUpdateTime, sourceService);
        logger.info("======read size() => {}",read.size());
        if(read.hashCode() == objCode) return XXLJobResult.buildSuccess();

        //写
        TargetService targetService = TargetServiceFactory.buildTargetServiceFactory().getTargetService(tableEnum.getTableName());
        boolean writer = writer(read, targetService);

        if (writer) {
            LastSyncInfo lastSyncInfo = sourceService.getLastSyncInfo(lastCreateTime, lastUpdateTime);
            if(currentSyncTask.getLastCreateTime().equals(lastSyncInfo.getLastCreateTime())){
                //如果时间相同数目超过1000 手动+1
                lastSyncInfo.setLastCreateTime(String.valueOf(Long.parseLong(lastSyncInfo.getLastCreateTime().trim())+1));
                lastSyncInfo.setLastUpdateTime(String.valueOf(Long.parseLong(lastSyncInfo.getLastUpdateTime().trim())+1));
            }
            SyncTask syncTask = new SyncTask();
            syncTask.setId(currentSyncTask.getId());
            syncTask.setCurrentUpdateTime(DateUtil.date());
            syncTask.setLastCreateTime(lastSyncInfo.getLastCreateTime());
            syncTask.setLastUpdateTime(lastSyncInfo.getLastUpdateTime());
            syncTask.setReadCount(currentSyncTask.getReadCount() + read.size());
            syncTask.setWriteCount(currentSyncTask.getWriteCount() + read.size());
            syncTask.setStatus("COMPLETED");

            syncTaskDao.updateSyncTask(currentSyncTask, syncTask);
            logger.info("========== {} sync success ============",tableEnum.getTableName());
            return XXLJobResult.buildSuccess(tableEnum.getTableName()+"  sync size =====> :"+read.size());
        } else {
            SyncExceptionRecordDO syncExceptionRecordDO = new SyncExceptionRecordDO();
            syncExceptionRecordDO.setSyncTaskId(syncTaskEnum.getTaskId());
            syncExceptionRecordDO.setLastUpdateTime(currentSyncTask.getLastUpdateTime());
            syncExceptionRecordDO.setStatus("FAILED");
            syncExceptionRecordDao.insertSyncExceptionRecord(syncExceptionRecordDO);
            logger.info("========= {} sync error ==============",tableEnum.getTableName());
            return XXLJobResult.buildFail(JSONObject.toJSONString(read));
        }
    }

    private List read(String lastCreateTime, String lastUpdateTime, SourceService sourceService) {
        //向前回拉1.2分钟
        if(lastCreateTime.trim().length() == 17){
            lastCreateTime = String.valueOf(Long.parseLong(lastCreateTime) - 120000);
        }
        if(lastUpdateTime.trim().length() == 17){
            lastUpdateTime = String.valueOf(Long.parseLong(lastUpdateTime) - 120000);
        }
        List<? extends BaseModel> data = sourceService.getData(lastCreateTime, lastUpdateTime);
        return data;
    }

    private boolean writer(List list, TargetService targetService) {
        objCode = list.hashCode();
        try {
            if (CollectionUtil.isNotEmpty(list)) {
                logger.info("======write size() => {}",list.size());
                int count = targetService.addOrSaveData(list);
                return count > 0;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
