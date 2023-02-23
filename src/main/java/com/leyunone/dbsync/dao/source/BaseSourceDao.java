package com.leyunone.dbsync.dao.source;


import com.leyunone.dbsync.model.BaseModel;
import com.leyunone.dbsync.model.LastSyncInfo;

import java.util.List;

/**
 * @author leyunone
 * @create 2022/8/12
 */
public interface BaseSourceDao<DO> {

    /**
     * 获取数据
     * @param lastCreateTime
     * @param lastUpdateTime
     * @return
     */
    List<?> selectData(String lastCreateTime, String lastUpdateTime);

    /**
     * 查询数据最新时间
     * @param lastCreateTime
     * @param lastUpdateTime
     * @return
     */
    LastSyncInfo selectLastSyncInfoWithSameMaxDate(String lastCreateTime, String lastUpdateTime);


    List<? extends BaseModel> selectListInDays(String beforeTime);

    int selectCountWithSameMaxDate(String lastCreateTime, String lastUpdateTime);

    String selectNewMaxDate(String lastCreateTime, String lastUpdateTime);

    String selectLastCreateTimeByMaxDate(String maxDate);

    void fillMajorKey(List<? extends BaseModel> list);

    List selectAll();
}
