package com.leyunone.dbsync.sync;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.leyunone.dbsync.model.XXLJobResult;
import com.leyunone.dbsync.model.enums.TableEnum;
import com.leyunone.dbsync.service.SyncRefreshServiceImpl;
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
 * @create 2022/12/27
 *
 * 手动刷表
 */
@Component
public class SyncManualHandler extends IJobHandler {

    private static Logger logger = LoggerFactory.getLogger(SyncManualHandler.class);

    @Autowired
    private SyncRefreshServiceImpl syncRefreshService;

    @XxlJob("Sync_Manual")
    @Override
    public void execute() throws Exception {
        String jobParam = XxlJobHelper.getJobParam();

        if(StringUtils.isEmpty(jobParam) || ObjectUtil.isNull(TableEnum.getEnumByTableName(jobParam))){
            XxlJobHelper.handleFail("not exist sync service");
            return;
        }

        try {
            XXLJobResult xxlJobResult = syncRefreshService.refreshTable(Objects.requireNonNull(TableEnum.getEnumByTableName(jobParam)));
            XXLJobUtil.handle(xxlJobResult);
        }catch (Exception e){
            XxlJobHelper.handleFail(e.getMessage());
            logger.error(e.getMessage());
        }
    }
}
