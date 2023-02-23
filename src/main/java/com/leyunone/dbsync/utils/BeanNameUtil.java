package com.leyunone.dbsync.utils;

/**
 * @author leyunone
 * @create 2022/8/26
 */
public class BeanNameUtil {

    public static String getSourceDaoName(Class<?> clazz){
        String name = clazz.getSimpleName();
        name = name.substring(0,name.length()-2);
        return "source"+name+"Dao";
    }

    public static String getTargetDaoName(Class<?> clazz){
        String name = clazz.getSimpleName();
        name = name.substring(0,name.length()-2);
        return "target"+name+"Dao";
    }
}
