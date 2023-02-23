package com.leyunone.dbsync.dao.target.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author leyunone
 * @create 2022/8/29
 */
public interface BaseTargetMapper<DO> extends BaseMapper<DO> {

    int batchDelete(@Param("tableName") String tableName, @Param("ew") QueryWrapper<DO> ew);

    List<Map> selectListInDays(@Param("tableName") String tableName, @Param("startTime") String startTime);

    int batchInsert(@Param("tableName") String tableName, @Param("columns") List<String> columns, @Param("list") List list, @Param("fields") List<String> fields);

    int deleteAll(@Param("tableName")String tableName);
}
