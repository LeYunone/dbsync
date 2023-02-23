package com.leyunone.dbsync.sync;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.dbsync.model.XXLJobResult;
import com.leyunone.dbsync.model.enums.SyncTaskEnum;
import com.leyunone.dbsync.model.enums.TableEnum;
import com.leyunone.dbsync.service.SyncServiceImpl;
import com.leyunone.dbsync.utils.XXLJobUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author leyunone
 * @create 2022/12/26
 */
@Component
public class SyncHandler extends IJobHandler {

    private static Logger logger = LoggerFactory.getLogger(SyncHandler.class);

    @Autowired
    private SyncServiceImpl syncService;

    @XxlJob(value = "Sync_Task")
    @Override
    public void execute() throws Exception {
        String jobParam = XxlJobHelper.getJobParam();
        String[] split = jobParam.split("#");
        String lastTime = null;
        if(split.length>1){
            //自定义时间场景
            jobParam = split[0];
            lastTime = split[1];
        }
        TableEnum enumByTableName = TableEnum.getEnumByTableName(jobParam);

        if(ObjectUtil.isNull(enumByTableName)){
            XxlJobHelper.handleFail(jobParam+"is not exist task");
            return;
        }
        logger.info("SyncTask is Running ====================> {}",jobParam);
        try {
            XXLJobResult sync = syncService.sync(enumByTableName, Objects.requireNonNull(SyncTaskEnum.getEnumByServiceName(enumByTableName.getSyncName())),lastTime);
            XXLJobUtil.handle(sync);
        }catch (Exception e){
            XxlJobHelper.handleFail(e.getMessage());
            logger.error(e.getMessage());
        }
    }
}
