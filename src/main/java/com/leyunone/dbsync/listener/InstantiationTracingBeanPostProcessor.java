package com.leyunone.dbsync.listener;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyunone.dbsync.dao.source.impl.BaseSourceDaoImpl;
import com.leyunone.dbsync.dao.source.mapper.BaseSourceMapper;
import com.leyunone.dbsync.dao.target.impl.BaseTargetDaoImpl;
import com.leyunone.dbsync.dao.target.mapper.BaseTargetMapper;
import com.leyunone.dbsync.utils.AnnotationUtil;
import com.leyunone.dbsync.utils.ApplicationContextProvider;
import com.leyunone.dbsync.utils.BeanNameUtil;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leyunone
 * @create 2022/8/15
 */
@Configuration
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //加载来源
        loadingSourceDao();
        //加载目标源
        loadingTargetDao();
    }

    /**
     * 加载sourceDao配置
     */
    public void loadingSourceDao() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        BaseSourceMapper sourceMapper = applicationContext.getBean(BaseSourceMapper.class);
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext;

        String tableName = "";
        //@tablename绑定的类
        List<Class<?>> tableNameAnnotationInClass = AnnotationUtil.getEntryAnnotationInClass(TableName.class);
        for (Class<?> clazz : tableNameAnnotationInClass) {

            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            if (ObjectUtil.isNotNull(tableNameAnnotation)) {
                tableName = tableNameAnnotation.value();
            }
            Field[] declaredFields = clazz.getDeclaredFields();
            List<String> primarys = new ArrayList<>();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(TableId.class)) {
                    TableId annotation = field.getAnnotation(TableId.class);
                    String primary = annotation.value();
                    primarys.add(primary);
                }
            }

            ResolvableType targetType = ResolvableType.forClassWithGenerics(BaseSourceDaoImpl.class, clazz);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(targetType.toClass());
            beanDefinitionBuilder.addPropertyValue("do_class", clazz);
            beanDefinitionBuilder.addPropertyValue("tableName", tableName);
            beanDefinitionBuilder.addPropertyValue("primaryKeys", primarys);
            beanDefinitionBuilder.addPropertyValue("baseSourceMapper", sourceMapper);
            registry.registerBeanDefinition(BeanNameUtil.getSourceDaoName(clazz), beanDefinitionBuilder.getBeanDefinition());
        }
    }

    public void loadingTargetDao() {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        BaseTargetMapper targetMapper = applicationContext.getBean(BaseTargetMapper.class);
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext;

        String tableName = "";
        //@tablename绑定的类
        List<Class<?>> tableNameAnnotationInClass = AnnotationUtil.getEntryAnnotationInClass(TableName.class);
        for (Class<?> clazz : tableNameAnnotationInClass) {
            TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
            if (ObjectUtil.isNotNull(tableNameAnnotation)) {
                tableName = tableNameAnnotation.value().toLowerCase();
            }
            Field[] declaredFields = clazz.getDeclaredFields();
            List<String> primaryKeys = new ArrayList<>();
            List<String> columns = new ArrayList<>();
            List<String> fields = new ArrayList<>();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(TableId.class) || field.isAnnotationPresent(TableField.class)) {
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (null != tableId) {
                        String primary = tableId.value();
                        primaryKeys.add(primary.toLowerCase());
                        columns.add(primary);
                    }
                    TableField tableField = field.getAnnotation(TableField.class);
                    if (null != tableField) {
                        String column = tableField.value();
                        columns.add(column);
                    }
                    String name = field.getName();
                    fields.add("#{item." + name + "}");
                }

            }

            ResolvableType targetType = ResolvableType.forClassWithGenerics(BaseTargetDaoImpl.class, clazz);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(targetType.toClass());
            beanDefinitionBuilder.addPropertyValue("do_class", clazz);
            beanDefinitionBuilder.addPropertyValue("tableName", tableName);
            beanDefinitionBuilder.addPropertyValue("primaryKeys", primaryKeys);
            beanDefinitionBuilder.addPropertyValue("baseTargetMapper", targetMapper);
            beanDefinitionBuilder.addPropertyValue("columns", columns);
            beanDefinitionBuilder.addPropertyValue("fields", fields);
            registry.registerBeanDefinition(BeanNameUtil.getTargetDaoName(clazz), beanDefinitionBuilder.getBeanDefinition());
        }
    }
}
