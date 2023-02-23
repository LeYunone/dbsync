package com.leyunone.dbsync.service.source;

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
public class SourceServiceFactory {

    private volatile static SourceServiceFactory factory;

    private SourceServiceFactory() {
    }

    private Map<String, BaseSourceService> sourceRedis = new ConcurrentHashMap<>();

    public static SourceServiceFactory buildSourceServiceFactory() {
        if (ObjectUtil.isNull(factory)) {
            synchronized (SourceServiceFactory.class) {
                if (ObjectUtil.isNull(factory)) {
                    factory = new SourceServiceFactory();
                }
            }
        }
        return factory;
    }

    /** 懒加载
     * @param tableName
     * @return
     */
    public SourceService getSourceService(String tableName) {
        if (sourceRedis.containsKey(tableName)) {
            return sourceRedis.get(tableName);
        }
        //唤醒缓存 TODO 配置文件动态更新
        packService();
        return sourceRedis.get(tableName);
    }

    /**
     * 组装sourceService服务
     */
    private void packService() {
        List<Class<?>> tableNameAnnotationInClass = AnnotationUtil.getEntryAnnotationInClass(TableName.class);
        for (Class<?> clazz : tableNameAnnotationInClass) {
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            String tableName = tableNameAnnotation.value();
//            ResolvableType targetType = ResolvableType.forClassWithGenerics(BaseSourceDaoImpl.class, clazz);
            BaseSourceService baseSourceService = new BaseSourceService(clazz);
            sourceRedis.putIfAbsent(tableName,baseSourceService);
        }
    }
}
