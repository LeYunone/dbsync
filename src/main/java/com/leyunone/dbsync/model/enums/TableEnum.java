package com.leyunone.dbsync.model.enums;

/**
 * @author leyunone
 * @date 2021/11/5
 */
public enum TableEnum {

    ;

    /**
     * 表名
     */
    private String tableName;

    private String syncName;

    private String syncCleanName;

    TableEnum(String tableName,String syncName,String syncCleanName){
        this.tableName = tableName;
        this.syncName = syncName;
        this.syncCleanName = syncCleanName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSyncName() {
        return syncName;
    }

    public void setSyncName(String syncName) {
        this.syncName = syncName;
    }

    public String getSyncCleanName() {
        return syncCleanName;
    }

    public void setSyncCleanName(String syncCleanName) {
        this.syncCleanName = syncCleanName;
    }

    public static TableEnum getEnumByTableName(String tableName) {
        for (TableEnum type : TableEnum.values()) {
            if (type.getTableName().equals(tableName)) {
                return type;
            }
        }
        return null;
    }
}
