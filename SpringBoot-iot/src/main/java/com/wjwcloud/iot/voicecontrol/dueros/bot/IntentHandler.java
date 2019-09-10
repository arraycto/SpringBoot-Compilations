package com.wjwcloud.iot.voicecontrol.dueros.bot;


import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.DuerRequest;

/**
 * 意图处理
 */
public interface IntentHandler {
    /**
     * 自定义处理意图
     * @param duerContext 当前的上下文包含Session等信息
     * @param intent 当前意图
     * @return 是否完成本地会话。  如果意图完全符合要求，应该返回true
     */
    boolean handleIntent(DuerContext duerContext, DuerRequest.Intent intent);
}
