package com.wjwcloud.iot.voicecontrol.aligenie.enums;

/**
 * 语音平台对照表
 * Created by zhoulei on 2019/4/826
 */
public enum VoicePlatformType {
    UN_KNOWN("unKnown", "未知"),
    DUEROS("https://xiaodu.baidu","百度音箱"),
    ALIGENIE("https://open.bot.tma","天猫精灵"),
    OPEN_LOCK("openLockAligenieCustomSkillsServiceImpl" , "小爱同学");
    String code;  // 类型编码
    String name; // 类型名称
    VoicePlatformType(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /**
     * 根据code 获取
     *
     * @param code
     * @return
     */
    public static VoicePlatformType getCustomSkillsType(String code) {
        for (VoicePlatformType customSkillsType : values()) {
            if (customSkillsType.code.equals(code)) {
                return customSkillsType;
            }
        }
        return UN_KNOWN;
    }
}
