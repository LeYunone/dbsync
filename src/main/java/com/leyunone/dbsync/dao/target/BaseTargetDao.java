package com.leyunone.dbsync.dao.target;



import com.leyunone.dbsync.model.BaseModel;

import java.util.List;

/**
 * @author leyunone
 * @create 2022/8/29
 */
public interface BaseTargetDao<DO> {

    int batchInsertOrUpdate(List moctfDOS);

    List selectListInDays(String startTime);

    int batchDelete(List moctfDOS);

    int deleteAll();

    void fillMajorKey(List<? extends BaseModel> list);
}
