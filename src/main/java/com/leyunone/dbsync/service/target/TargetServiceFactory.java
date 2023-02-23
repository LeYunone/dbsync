package com.leyunone.dbsync.service.target;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyunone.dbsync.utils.AnnotationUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leyunone
 * @create 2022/8/23
 */
public class TargetServiceFactory {

    private volatile static TargetServiceFactory factory;

    private TargetServiceFactory() {
    }

    private Map<String, BaseTargetService> targetRedis = new ConcurrentHashMap<>();

    public static TargetServiceFactory buildTargetServiceFactory() {
        if (ObjectUtil.isNull(factory)) {
            synchronized (TargetServiceFactory.class) {
                if (ObjectUtil.isNull(factory)) {
                    factory = new TargetServiceFactory();
                }
            }
        }
        return factory;
    }

    /** 懒加载
     * @param tableName
     * @return
     */
    public TargetService getTargetService(String tableName) {
        if (targetRedis.containsKey(tableName)) {
            return targetRedis.get(tableName);
        }
        //唤醒缓存 TODO 配置文件动态更新
        packService();
        return targetRedis.get(tableName);
    }

    /**
     * 组装targetService服务
     */
    private void packService() {
        List<Class<?>> tableNameAnnotationInClass = AnnotationUtil.getEntryAnnotationInClass(TableName.class);
        for (Class<?> clazz : tableNameAnnotationInClass) {
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            String tableName = tableNameAnnotation.value();
//            ResolvableType targetType = ResolvableType.forClassWithGenerics(BaseSourceDaoImpl.class, clazz);
            BaseTargetService baseTargetService = new BaseTargetService(clazz);
            targetRedis.putIfAbsent(tableName,baseTargetService);
        }
    }
}
