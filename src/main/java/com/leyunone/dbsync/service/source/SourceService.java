package com.leyunone.dbsync.service.source;


import com.leyunone.dbsync.model.BaseModel;
import com.leyunone.dbsync.model.LastSyncInfo;

import java.util.List;

/**
 * @author leyunone
 * @date 2021/10/18
 */
public interface SourceService {

    /**
     * 获取最新的数据
     * @param
     * @return
     */
    List<? extends BaseModel> getData(String lastCreateTime, String lastUpdateTime);

    /**
     * 获取上次同步的最新时间
     * @param lastCreateTime
     * @param lastUpdateTime
     * @return
     */
    LastSyncInfo getLastSyncInfo(String lastCreateTime, String lastUpdateTime);

    /**
     * 查询n天内的数据
     * @param startTime
     * @return
     */
    List<? extends BaseModel> selectListInDays(String startTime);

    List getAllData();
}
