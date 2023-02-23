package com.leyunone.dbsync.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leyunone
 * @create 2022/12/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XXLJobResult {

    private String message;

    private boolean status;

    public static XXLJobResult buildSuccess(String message){
        return XXLJobResult.builder().message(message).status(true).build();
    }

    public static XXLJobResult buildSuccess(){
        return XXLJobResult.buildSuccess(null);
    }

    public static XXLJobResult buildFail(String message){
        return XXLJobResult.builder().message(message).status(false).build();
    }

    public static XXLJobResult buildFail(){
        return XXLJobResult.buildFail(null);
    }
}
