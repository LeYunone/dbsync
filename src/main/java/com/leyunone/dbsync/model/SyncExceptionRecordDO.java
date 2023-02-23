package com.leyunone.dbsync.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 变更数据捕获(cdc)临时表
 * </p>
 *
 * @author leyunone
 * @since 2021-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sync_exception_record")
public class SyncExceptionRecordDO extends Model<SyncExceptionRecordDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer syncTaskId;

    private Long stepExecutionId;

    /**
     * 上次更新的数据最新时间
     */
    private String lastUpdateTime;

    /**
     * 状态,INIT:初始状态，未进行同步,FAILED:同步失败,COMPLETED:同步完成
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createDt;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
