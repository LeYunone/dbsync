package com.leyunone.dbsync.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 变更数据捕获(cdc)临时表
 * </p>
 *
 * @author leyunone
 * @since 2021-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sync_task")
public class SyncTask {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;

    /**
     * 上次更新的数据最新时间
     */
    private String lastCreateTime;

    /**
     * 上次更新的数据最新时间
     */
    private String lastUpdateTime;

    /**
     * 当前更新时间
     */
    private Date currentUpdateTime;

    private Integer readCount;

    private Integer writeCount;

    private Integer rollbackCount;

    private Integer readSkipCount;

    private Integer processSkipCount;

    private Integer writeSkipCount;

    /**
     * 作业标识
     */
    private String jobTag;

    /**
     * 备注
     */
    private String comment;

    /**
     * 状态,INIT:初始状态，未进行同步,FAILED:同步失败,COMPLETED:同步完成
     */
    private String status;

}
