package com.leyunone.dbsync.dao.target.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leyunone.dbsync.dao.target.BaseTargetDao;
import com.leyunone.dbsync.dao.target.mapper.BaseTargetMapper;
import com.leyunone.dbsync.model.BaseModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leyunone
 * @create 2022/8/29
 */
@Data
public class BaseTargetDaoImpl<DO> implements BaseTargetDao<DO> {

    private Class<?> do_class;
    //表名
    private String tableName;
    //主键 存储获取DO主键值的方法
    private List<String> primaryKeys;

    private BaseTargetMapper baseTargetMapper;

    private List<String> columns;

    /**
     * #{item.XXX} 指定属性
     */
    private List<String> fields;

    @Override
    public int batchInsertOrUpdate(List list) {
        return this.baseTargetMapper.batchInsert(tableName, columns, list, fields);
    }

    @Override
    public List<?> selectListInDays(String startTime) {
        List<Map> list = this.baseTargetMapper.selectListInDays(tableName, startTime);
        return this.mapsToBeans(list);
    }

    private List<?> mapsToBeans(List<Map> mapList) {
        List<?> dos = JSONObject.parseArray(JSONObject.toJSONString(mapList), do_class);
        return dos;
    }

    @Override
    public int batchDelete(List list) {
        QueryWrapper<DO> queryWrapper = new QueryWrapper<>();
        for (Object dx : list) {
            Map<String, Object> doMap = BeanUtil.beanToMap(dx);
            queryWrapper.or(wp -> {
                for (String key : primaryKeys) {
                    wp.eq(key, doMap.get(key));
                }
                return wp;
            });
        }
        return this.baseTargetMapper.batchDelete(tableName, queryWrapper);
    }

    @Override
    public void fillMajorKey(List<? extends BaseModel> list) {
        for (BaseModel d_o : list) {
            Map<String, Object> doMap = BeanUtil.beanToMap(d_o);
            List primaryValues = new ArrayList();
            for (String key : primaryKeys) {
                primaryValues.add(doMap.get(key));
            }
            d_o.setMajorKey(CollectionUtil.join(primaryValues, "-"));
        }
    }

    @Override
    public int deleteAll() {
        return this.baseTargetMapper.deleteAll(tableName);
    }
}
