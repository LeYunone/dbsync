package com.leyunone.dbsync.dao.source.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.leyunone.dbsync.dao.source.BaseSourceDao;
import com.leyunone.dbsync.dao.source.mapper.BaseSourceMapper;
import com.leyunone.dbsync.model.BaseModel;
import com.leyunone.dbsync.model.LastSyncInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leyunone
 * @create 2022/8/12
 */
@Data
public class BaseSourceDaoImpl<DO> implements BaseSourceDao<DO> {

    private Class<? extends BaseModel> do_class;
    //表名
    private String tableName;

    private BaseSourceMapper<?> baseSourceMapper;

    //主键
    private List<String> primaryKeys;

    @Override
    public List<?> selectData(String lastCreateTime, String lastUpdateTime) {
        List<Map> maps = this.baseSourceMapper.selectData(tableName, lastCreateTime, lastUpdateTime);
        return this.mapsToBeans(maps);
    }

    @Override
    public LastSyncInfo selectLastSyncInfoWithSameMaxDate(String lastCreateTime, String lastUpdateTime) {
        return this.baseSourceMapper.selectLastSyncInfo(tableName,lastCreateTime, lastUpdateTime);
    }

    @Override
    public List<? extends BaseModel> selectListInDays(String beforeTime) {
        List<Map> maps = this.baseSourceMapper.selectListInDays(tableName, CollectionUtil.join(primaryKeys,","),beforeTime);
        return this.mapsToBeans(maps);
    }

    @Override
    public int selectCountWithSameMaxDate(String lastCreateTime, String lastUpdateTime) {
        return this.baseSourceMapper.selectCountWithSameMaxDate(tableName,lastCreateTime, lastUpdateTime);
    }

    @Override
    public String selectNewMaxDate(String lastCreateTime, String lastUpdateTime) {
        return this.baseSourceMapper.selectNewMaxDate(tableName,lastCreateTime, lastUpdateTime);
    }

    @Override
    public String selectLastCreateTimeByMaxDate(String maxDate) {
        return this.baseSourceMapper.selectLastCreateTimeByMaxDate(tableName,maxDate);
    }

    @Override
    public void fillMajorKey(List<? extends BaseModel> list) {
        for (BaseModel d_o : list) {
            Map<String, Object> doMap = BeanUtil.beanToMap(d_o);
            List primaryValues = new ArrayList();
            for(String key : primaryKeys){
                primaryValues.add(doMap.get(key.toLowerCase()));
            }
            d_o.setMajorKey(CollectionUtil.join(primaryValues,"-"));
        }
    }

    @Override
    public List selectAll() {
        List<Map> maps = this.baseSourceMapper.selectAll(tableName);
        return this.mapsToBeans(maps);
    }

    private List<? extends BaseModel> mapsToBeans(List<Map> mapList){
        List<? extends BaseModel> dos = JSONObject.parseArray(JSONObject.toJSONString(mapList), do_class);
        return dos;
    }
}
