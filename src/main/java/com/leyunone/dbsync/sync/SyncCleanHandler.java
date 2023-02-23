package com.leyunone.dbsync.sync;

import cn.hutool.core.util.ObjectUtil;
import com.leyunone.dbsync.model.XXLJobResult;
import com.leyunone.dbsync.model.enums.SyncTaskEnum;
import com.leyunone.dbsync.model.enums.TableEnum;
import com.leyunone.dbsync.service.SyncCleanServiceImpl;
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
public class SyncCleanHandler extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(SyncCleanHandler.class);

    @Autowired
    private SyncCleanServiceImpl syncCleanService;

    @XxlJob(value = "Sync_Clean_Task")
    @Override
    public void execute() throws Exception {
        String jobParam = XxlJobHelper.getJobParam();
        TableEnum enumByTableName = TableEnum.getEnumByTableName(jobParam);

        if(ObjectUtil.isNull(enumByTableName)){
            XxlJobHelper.handleFail(jobParam+"is not exist task");
            return;
        }
        logger.info("SyncCleanTask is Running ================> {}",jobParam);

        try {
            XXLJobResult sync = syncCleanService.sync(enumByTableName, Objects.requireNonNull(SyncTaskEnum.getEnumByServiceName(enumByTableName.getSyncCleanName())));
            XXLJobUtil.handle(sync);
        }catch (Exception e){
            XxlJobHelper.handleFail(e.getMessage());
            logger.error(e.getMessage());
        }
    }
}
