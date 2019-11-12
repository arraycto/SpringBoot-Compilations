package com.geer2.nettyMqtt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author JiaweiWu
 */
@Component
public class ConfigConstant {

    @Autowired
    private Environment env;

    /**
     *
     */
    public static String cleanmsgPeriod;
    /**
     *
     */
    public static String msgBeforeDate;
    /**
     *
     */
    public static String printBlankPeriod;

    @PostConstruct
    public void readConfig() {
        cleanmsgPeriod = env.getProperty("cleanmsgPeriod");
        msgBeforeDate = env.getProperty("msgBeforeDate");
        printBlankPeriod = env.getProperty("printBlankPeriod");
    }

}
