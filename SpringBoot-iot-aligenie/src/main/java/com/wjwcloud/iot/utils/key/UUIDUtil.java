package com.wjwcloud.iot.utils.key;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UUIDUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(UUIDUtil.class);
    private static UIDFactory uuid = null;

    private UUIDUtil() {
    }

    public static String getUUID() {
        return uuid.getNextUID();
    }

    static {
        try {
            uuid = UIDFactory.getInstance("UUID");
        } catch (Exception var1) {
            LOGGER.info("Init UIDFactory Failed", var1);
        }

    }
}
