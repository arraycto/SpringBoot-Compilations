package com.wjwcloud.iot.voicecontrol.dueros.bot.config;

import com.geer2.zwow.iot.voicecontrol.dueros.bot.DuerOsService;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.impl.DuerOsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DuerOsConfiguration {
    /**
     * duerOsService 服务
     * @return 服务
     */
    @Bean
    public DuerOsService duerOsService(){
        return new DuerOsServiceImpl();
    }
}
