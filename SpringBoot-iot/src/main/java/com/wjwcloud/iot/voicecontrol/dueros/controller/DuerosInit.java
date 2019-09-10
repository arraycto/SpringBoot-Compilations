package com.wjwcloud.iot.voicecontrol.dueros.controller;

import com.geer2.zwow.iot.voicecontrol.dueros.bot.DuerOsService;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.cus.IntentDesc;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.cus.SlotDesc;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.Slot;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.impl.DefaultColorControlIntentHandler;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.impl.SwitchControlIntentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @auther:zhoulei
 * @description:
 * @date :2019/5/13 16:08;
 * QQ：20971053
 */
@Component
public class DuerosInit {
    @Autowired
    private DuerOsService duerOsService;

    @PostConstruct
    public void init(){
        /**
         * 开关控制处理意图（打开卧室的灯，或者关闭灯）
         */
        List<SlotDesc> slots=new ArrayList<>();
        slots.add(new SlotDesc("device_name",true,"什么设备？"));
        slots.add(new SlotDesc("switch_control",true,"打开还是关闭"));
        slots.add(new SlotDesc("home_loc",false,"哪里的？"));
        IntentDesc desc=new IntentDesc("switch_control",slots);

        /**
         * 定义处理类
         */
        SwitchControlIntentHandler handler=new SwitchControlIntentHandler();

        duerOsService.addIntentHandler(desc,handler);


        /**
         * 位置场景控制意图，比如小度小度  进入卧室
         */
        duerOsService.addIntentHandler(new IntentDesc("loc_control"), (duerContext, intent) -> {
            Slot homeLoc=intent.slots.get("home_loc");
            //msgBuilder.addSessionAttribute("home_loc",homeLoc.value);
            duerContext.session.setAttribute("home_loc",homeLoc.value);
            duerContext.msgBuilder.textMessage("需要我做点什么？");
            return false;
        });


        /**
         * 设备自动配网功能实现，主要是esp系列的wifi自动配网
         */
        duerOsService.addIntentHandler(new IntentDesc("smart_config_wifi"), (duerContext, intent)->{
            duerContext.msgBuilder.textMessage("已经打开设备查找功能，请确保设备已经打开。");

            /*
              开始esp touch 自动配网任务
              @see https://github.com/microxdd/esp-smart-config.git
             */
            new Thread(()->{
//                IEsptouchTask task = new EsptouchTask(wifi.getApSsid(), wifi.getApBssid(), wifi.getApPwd(), wifi.isSidHidden());
//                IEsptouchResult rs = task.executeForResult();
//                System.out.println(rs);
            }).start();

            return true;
        });

        List<SlotDesc> colorSlots= Collections.singletonList(new SlotDesc("sys.color",true,"是什么颜色"));

        DefaultColorControlIntentHandler colorHandler=new DefaultColorControlIntentHandler();
        duerOsService.addIntentHandler(new IntentDesc("light_color_control", colorSlots),colorHandler);

    }
}
