package com.leyunone.dbsync.model.enums;

/**
 * @author leyunone
 * @create 2022/11/25
 */
public enum SyncTaskEnum {

    /**
     * 定时服务  表明 服务 编号
     */
    ;

    SyncTaskEnum(String tableName,String serviceName,Integer taskId){
        this.tableName = tableName;
        this.serviceName = serviceName;
        this.taskId = taskId;
    }

    private String tableName;

    private String serviceName;

    private Integer taskId;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }


    public static SyncTaskEnum getEnumBytaskId(Integer taskId) {
        for (SyncTaskEnum type : SyncTaskEnum.values()) {
            if (type.getTaskId().equals(taskId)) {
                return type;
            }
        }
        return null;
    }

    public static SyncTaskEnum getEnumByServiceName(String serviceName) {
        for (SyncTaskEnum type : SyncTaskEnum.values()) {
            if (type.getServiceName().equals(serviceName)) {
                return type;
            }
        }
        return null;
    }
}
