package com.leyunone.dbsync.service.source;


import com.leyunone.dbsync.dao.source.BaseSourceDao;
import com.leyunone.dbsync.model.BaseModel;
import com.leyunone.dbsync.model.LastSyncInfo;
import com.leyunone.dbsync.utils.ApplicationContextProvider;
import com.leyunone.dbsync.utils.BeanNameUtil;

import java.util.List;

/**
 * @author leyunone
 * @create 2022/8/23
 */
public class BaseSourceService implements SourceService {

    private BaseSourceDao<?> baseSourceDao;

    /**
     * BO对象
     */
    private Class<?> DO;

    public BaseSourceService(Class<?> clazz) {
        this.baseSourceDao = (BaseSourceDao<?>) ApplicationContextProvider.getBean(BeanNameUtil.getSourceDaoName(clazz));
        this.DO = clazz;
    }

    @Override
    public List<? extends BaseModel> getData(String lastCreateTime, String lastUpdateTime) {
        List<? extends BaseModel> dos = (List<? extends BaseModel>)baseSourceDao.selectData(lastCreateTime, lastUpdateTime);
        return dos;
    }

    @Override
    public LastSyncInfo getLastSyncInfo(String lastCreateTime, String lastUpdateTime) {
        int count = baseSourceDao.selectCountWithSameMaxDate(lastCreateTime, lastUpdateTime);
        if (count >= 1000) {
            return baseSourceDao.selectLastSyncInfoWithSameMaxDate(lastCreateTime, lastUpdateTime);
        }
        String maxDate = baseSourceDao.selectNewMaxDate(lastCreateTime, lastUpdateTime);
        String newLastCreateTime = baseSourceDao.selectLastCreateTimeByMaxDate(maxDate);
        LastSyncInfo lastSyncInfo = new LastSyncInfo();
        lastSyncInfo.setLastCreateTime(newLastCreateTime);
        lastSyncInfo.setLastUpdateTime(maxDate);
        return lastSyncInfo;
    }

    /**
     * 一个空泛型的DO 转化为 BO  ，往BO的markey中set DO中的主键-主键-主键形式
     *
     * @param startTime
     * @return
     */
    @Override
    public List<? extends BaseModel> selectListInDays(String startTime) {
        List<? extends BaseModel> dos = baseSourceDao.selectListInDays(startTime);
        baseSourceDao.fillMajorKey(dos);
        return dos;
    }

    @Override
    public List getAllData() {
        return baseSourceDao.selectAll();
    }
}
