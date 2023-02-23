package com.leyunone.dbsync.dao.source.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyunone.dbsync.model.LastSyncInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author leyunone
 * @since 2021-11-04
 */
public interface BaseSourceMapper<DO> extends BaseMapper<DO> {

    List<Map> selectData(@Param("tableName")String tableName,@Param("lastCreateTime")String lastCreateTime, @Param("lastUpdateTime")String lastUpdateTime);

    LastSyncInfo selectLastSyncInfo(@Param("tableName")String tableName, @Param("lastCreateTime")String lastCreateTime, @Param("lastUpdateTime")String lastUpdateTime);

    List<Map> selectListInDays(@Param("tableName")String tableName,@Param("primaryKeys")String primaryKeys,@Param("startTime") String startTime);

    int selectCountWithSameMaxDate(@Param("tableName")String tableName,@Param("lastCreateTime")String lastCreateTime, @Param("lastUpdateTime")String lastUpdateTime);

    String selectNewMaxDate(@Param("tableName")String tableName,@Param("lastCreateTime")String lastCreateTime, @Param("lastUpdateTime")String lastUpdateTime);

    String selectLastCreateTimeByMaxDate(@Param("tableName")String tableName,@Param("maxDate")String maxDate);

    List<Map> selectAll(@Param("tableName")String tableName);
}
