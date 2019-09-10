package com.wjwcloud.iot.voicecontrol.dueros.bot;


import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.cus.IntentDesc;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.DuerRequest;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.DuerResponse;

import javax.servlet.http.HttpServletResponse;

public interface DuerOsService {
    /**
     * 处理duer os 请求
     * @param request
     * @return
     */
    DuerResponse processRequest(DuerRequest request, HttpServletResponse response);

    /**
     * 添加意图处理类
     * @param intentDesc
     * @param handler  实现类可以自动实现依赖注入。
     */
    void addIntentHandler(IntentDesc intentDesc, IntentHandler handler);
}
