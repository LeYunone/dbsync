package com.leyunone.dbsync.service.target;


import com.leyunone.dbsync.model.BaseModel;

import java.util.List;

/**
 * @author leyunone
 * @date 2021/10/18
 */
public interface TargetService {

    /**
     * 添加或更新数据
     * @param list
     * @return
     */
    int addOrSaveData(List<? extends BaseModel> list);

    /**
     * 查询n天内的数据
     * @param startTime
     * @return
     */
    List<? extends BaseModel> selectListInDays(String startTime);

    /**
     * 移除数据
     * @param purthDOS
     * @return
     */
    int removeData(List<? extends BaseModel> purthDOS);

    int removeAllData();

}
