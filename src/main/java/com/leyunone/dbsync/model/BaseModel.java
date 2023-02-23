package com.leyunone.dbsync.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author leyunone
 * @date 2021/11/5
 */
@Data
public class BaseModel {

    @TableField(exist = false)
    private String tableName;

    @TableField(exist = false)
    private String majorKey;

    @TableField(exist = false)
    private String tag = "Y";
}
