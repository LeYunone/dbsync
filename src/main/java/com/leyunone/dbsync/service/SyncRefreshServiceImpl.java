package com.leyunone.dbsync.service;

import cn.hutool.core.collection.CollectionUtil;
import com.leyunone.dbsync.model.XXLJobResult;
import com.leyunone.dbsync.model.enums.TableEnum;
import com.leyunone.dbsync.service.source.SourceService;
import com.leyunone.dbsync.service.source.SourceServiceFactory;
import com.leyunone.dbsync.service.target.TargetService;
import com.leyunone.dbsync.service.target.TargetServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leyunone
 * @create 2022/12/27
 */
@Service
public class SyncRefreshServiceImpl {

    private Logger logger = LoggerFactory.getLogger(SyncCleanServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    public XXLJobResult refreshTable(TableEnum tableEnum){

        logger.info("====={} is start refresh",tableEnum.getTableName());
        //拿到表所有数据
        SourceService sourceService = SourceServiceFactory.buildSourceServiceFactory().getSourceService(tableEnum.getTableName());
        List allData = sourceService.getAllData();

        //清空数据库
        TargetService targetService = TargetServiceFactory.buildTargetServiceFactory().getTargetService(tableEnum.getTableName());
        int remove = targetService.removeAllData();
        List batch = new ArrayList();
        for(int i=0;i<allData.size();i++){
            batch.add(allData.get(i));
            if(batch.size()==1000){
                targetService.addOrSaveData(batch);
                batch.clear();
            }
        }
        if(CollectionUtil.isNotEmpty(batch)){
            targetService.addOrSaveData(batch);
            batch = null;
        }

        logger.info("===== refresh success remove => size: {} ,  add => size: {}",remove,allData.size());

        return XXLJobResult.buildSuccess("read:"+allData.size()+"=====remove:"+remove);
    }
}
