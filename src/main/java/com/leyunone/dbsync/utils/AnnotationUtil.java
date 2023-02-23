package com.leyunone.dbsync.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * @author leyunone
 * @create 2022/8/24
 */
public class AnnotationUtil {

    public static List<Class<?>> getAnnotationInClass(String pageName,Class<? extends Annotation> annotatedClass) {
        Reflections reflections = new Reflections(pageName);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotatedClass);
        return CollectionUtil.newArrayList(typesAnnotatedWith);
    }

    public static List<Class<?>> getEntryAnnotationInClass(Class<? extends Annotation> annotatedClass) {
        return AnnotationUtil.getAnnotationInClass("com.leyunone.dbsync.model.entry",annotatedClass);
    }
}
