package com.leyunone.dbsync.utils;

import com.leyunone.dbsync.model.XXLJobResult;
import com.xxl.job.core.context.XxlJobHelper;

/**
 * @author leyunone
 * @create 2022/12/27
 */
public class XXLJobUtil {

    public static void handle(XXLJobResult result){
        if(result.isStatus()){
            XxlJobHelper.handleSuccess(result.getMessage());
        }else{
            XxlJobHelper.handleFail(result.getMessage());
        }
    }
}
