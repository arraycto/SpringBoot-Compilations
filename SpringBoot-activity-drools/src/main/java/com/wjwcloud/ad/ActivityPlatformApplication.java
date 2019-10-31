package com.wjwcloud.ad;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wjwcloud.ad"})
@MapperScan("com.wjwcloud.ad.common.dao.mapper")
public class ActivityPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityPlatformApplication.class, args);
    }

}

