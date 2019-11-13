package com.wjwcloud.iot.voicecontrol.aligenie.enums;


/**
 * 自定义技能，不同意图对应不同service对照表
 * Created by zhoulei on 2019/4/826
 */
public enum CustomSkillsType {
    UN_KNOWN("unKnown", "未知"),
    ALARM_RECORD("alarmRecordAligenieCustomSkillsServiceImpl","报警记录"),
    VOICE_MESSAGE("voiceMessagelAligenieCustomSkillsServiceImpl","语音留言"),
    OPEN_LOCK("openLockAligenieCustomSkillsServiceImpl" , "开锁记录");
    String code;  // 类型编码
    String name; // 类型名称
    CustomSkillsType(String code, String name) {
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
    public static CustomSkillsType getCustomSkillsType(String code) {
        for (CustomSkillsType customSkillsType : values()) {
            if (customSkillsType.code.equals(code)) {
                return customSkillsType;
            }
        }
        return UN_KNOWN;
    }
}
