package com.leyunone.dbsync;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @date 2023-02-23
 */
@SpringBootApplication(scanBasePackages = {"com.leyunone.dbsync"})
@MapperScan("com.leyunone.dbsync.dao.*.mapper")
public class SyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncApplication.class,args);
    }
}
