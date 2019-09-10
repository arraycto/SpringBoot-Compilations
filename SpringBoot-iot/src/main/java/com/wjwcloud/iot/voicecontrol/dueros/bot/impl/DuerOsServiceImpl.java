package com.wjwcloud.iot.voicecontrol.dueros.bot.impl;

import com.baidu.dueros.data.request.IntentRequest;
import com.baidu.dueros.data.request.SessionEndedRequest;
import com.baidu.dueros.data.request.videoplayer.event.*;
import com.geer2.zwow.iot.product.service.ProductDeviceLogMobileService;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.DuerContext;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.DuerOsService;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.DuerSession;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.IntentHandler;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.cus.IntentDesc;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.cus.SlotDesc;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.DuerRequest;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.DuerResponse;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.Slot;
import com.geer2.zwow.iot.voicecontrol.dueros.enums.DuerosCustomSkillsType;
import com.geer2.zwow.iot.voicecontrol.dueros.service.imp.VideoPlayerBotImpl;
import com.geer2.zwow.iot.voicecontrol.service.ICustomSkillsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuerOsServiceImpl implements DuerOsService{
    private static Logger log= LoggerFactory.getLogger(DuerOsServiceImpl.class);



    /**
     * 意图的定义
     */
    private Map<String,IntentDesc> intents=new HashMap<>();


    /**
     * 意图的处理类
     */
    private Map<String,IntentHandler> intentHandlerMap=new HashMap<>();

    //日志查询
    @Resource(name = "productDeviceLogMobileServiceImpl")
    private ProductDeviceLogMobileService productDeviceLogMobileService;



    private Map<Long,DuerSession> duerSessionMap=new HashMap<>();

    @Autowired
    private AutowireCapableBeanFactory autowireBean;
    @Autowired
    private ApplicationContext applicationContext;



    @Override
    public DuerResponse processRequest(DuerRequest request , HttpServletResponse response) {


        if(null == request){
            return null;
        }
        DuerResponse.Builder msgBuilder = DuerResponse.Builder.buildFromRequest(request);

        if(request==null||request.request==null){
            //response.setStatus(400);
            return msgBuilder.textMessage("我靠，数据好像有问题了").build();
        }

        log.debug("[dueros] context:{}",request.context);



        DuerRequest.RequestType duerReq = request.request;
        log.debug("[dueros] request type:{},requestId:{},timestamp：{}",duerReq.type,duerReq.requestId,duerReq.timestamp);
        if(duerReq.getClass()==DuerRequest.RequestType.class){
            msgBuilder.newSession(true);
            log.info("进入设备控制");
            msgBuilder.textMessage("我要做点什么");
        }else if(duerReq.getClass()==DuerRequest.IntentRequest.class){

            /**
             * 当前的用户id ,当前的设备id也是可以拿到的
             */
            long userId = request.context.path("System").path("user").path("userId").asLong();
            String token = request.context.path("System").path("user").path("accessToken").asText();

            if(userId==0){
                msgBuilder.textMessage("请求有误");
                return msgBuilder.build();
            }

            DuerSession session = duerSessionMap.get(userId);
            if(session==null||System.currentTimeMillis()-session.getCreationTime()>30*3600L){
                session=new DefaultDuerSession(String.valueOf(userId));
                duerSessionMap.put(userId,session);
            }else{
                session.refreshTime();
            }
            DuerContext context=new DuerContext(request,msgBuilder,session);

            DuerRequest.IntentRequest intentRequest=(DuerRequest.IntentRequest)duerReq;

            for (DuerRequest.Intent intent : intentRequest.intents) {

                for (Map.Entry<String, Slot> stringSlotEntry : intent.slots.entrySet()) {
                    Slot slot=stringSlotEntry.getValue();
                    log.debug("\t\t\t{}->{name:{},value:{}},values:{},confirmationStatus{}"
                            ,stringSlotEntry.getKey(),slot.name,slot.value,slot.values,slot.confirmationStatus);
                }
                IntentDesc desc = intents.get(intent.name);

                //是否包含多意图
                if (desc != null) {
                    log.debug("[dueros] 开始处理意图:{}", intent.name);
                    List<SlotDesc> slotDescs = desc.getSlots();
                    if (slotDescs != null&&!slotDescs.isEmpty()) {
                        boolean bComp = true;
                        for (SlotDesc slotDesc : slotDescs) {

                            if (slotDesc.isRequired()) {
                                Slot slot = intent.slots.get(slotDesc.getName());
                                if (slot == null) {
                                    DuerResponse.DirectiveBuilder directiveBuilder = DuerResponse.DirectiveBuilder.newBuilder()
                                            .updatedIntent(intent);
                                    String msg=slotDesc.getMsg();
                                    if(msg==null){
                                        //或者直接交给百度自己处理
                                        directiveBuilder.toDelegate();
                                    }else{
                                        //向用户询问未知的槽值
                                        directiveBuilder.toElicitSlot(slotDesc.getName());
                                        msgBuilder.textMessage(slotDesc.getMsg());
                                    }
                                    msgBuilder.addDirective(directiveBuilder.build());
                                    bComp = false;
                                    break;
                                }
                            }
                        }
                        IntentHandler handler = intentHandlerMap.get(intent.name);
                        if(handler!=null){
                            if(bComp){
                                msgBuilder.shouldEndSession(handler.handleIntent(context,intent));
                            }
                        }else{
                            msgBuilder.shouldEndSession(true).textMessage("我靠，还没有实现该意图！");
                        }

                    }else{
                        //如果意图空
                        IntentHandler handler = intentHandlerMap.get(intent.name);
                        if(handler!=null){
                            msgBuilder.shouldEndSession(handler.handleIntent(context,intent));
                        }
                    }

                } else {
                    String beanName = null;
                    if("alarmRecord".equals(intent.name)){
                        //报警记录
                        beanName = DuerosCustomSkillsType.ALARM_RECORD.getCode();
                    }else if("openLock".equals(intent.name)){
                        //开门记录
                        beanName = DuerosCustomSkillsType.OPEN_LOCK.getCode();
                    }else if("voiceMessage".equals(intent.name)){
                        //语音留言
                        beanName = DuerosCustomSkillsType.VOICE_MESSAGE.getCode();
                    }else if("ai.dueros.common.cancel_intent".equals(intent.name)){

                        msgBuilder.textMessage("已退出该意图").shouldEndSession(true);
                        return msgBuilder.build();
                    }else if("anti_lock_door".equals(intent.name)){
                        //门反锁
                        beanName = DuerosCustomSkillsType.ANTI_LOCK_DOOR.getCode();
                    }else if ("voide_list".equals(intent.name)){
                        //视频列表
                        VideoPlayerBotImpl videoPlayerBot = null;
//                        try {
//                            videoPlayerBot = new VideoPlayerBotImpl(request);
//                            // 调用run方法
//                            String responseJson = videoPlayerBot.run();
//                            JSONObject jsonObject = JSONObject.parseObject(responseJson);
//                            System.out.println("responseJson值：" + responseJson);
//                            // 设置response的编码UTF-8
//                            response.setCharacterEncoding("UTF-8");
//                            // 返回response
//                            response.getWriter().append(responseJson);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        return null;
                    }else if ("video_player".equals(intent.name)){
                        return null;
                    }else {
                        return null;
                    }
                    Object object = applicationContext.getBean(beanName);
                    ICustomSkillsService iCustomSkillsService = (ICustomSkillsService) object;
//                    if("voiceMessage".equals(intent.name)){
//                        iCustomSkillsService = (VoiceMessageDuerosCustomSkillsServiceImpl)object;
//                    }else if("openLock".equals(intent.name)){
//                        iCustomSkillsService = (OpenLockDuerosCustomSkillsServiceImpl)object;
//                    }else if("alarmRecord".equals(intent.name)){
//                        iCustomSkillsService = (AlarmRecordDuerosCustomSkillsServiceImpl)object;
//                    }
                    if(null == iCustomSkillsService){
                        msgBuilder.textMessage("不可识别").shouldEndSession(false);
                    }
                    Map map = new HashMap();
                    map.put("token" , token);
                    String text = iCustomSkillsService.getText(map).toString();
                    log.warn("[dueros] 意图：{},没有找到合适大的意图处理类", intent.name);
                    msgBuilder.textMessage(text).shouldEndSession(false);
                }
                if (intentRequest.query != null)
                    log.info("请求内容: {}", intentRequest.query.original);
                if (msgBuilder.build().response.outputSpeech != null) {
                    log.info("返回内容: {}", msgBuilder.build().response.outputSpeech.text);
                }
            }

        }else if(duerReq.getClass()==DuerRequest.SessionEndedRequest.class){
            DuerRequest.SessionEndedRequest sessionEndedRequest= (DuerRequest.SessionEndedRequest) duerReq;
            DuerRequest.ErrorInfo error = sessionEndedRequest.error;
            if(error!=null){
                log.debug("[dueros] 会话结束,状态：{},error:{},message:{}",sessionEndedRequest.reason,error.type,error.message);
            }else{
                log.debug("[dueros] 会话结束,状态：{},error:{}",sessionEndedRequest.reason,"未知错误");
            }
            msgBuilder.textMessage("不清楚要做什么").shouldEndSession(true);
        }
        return msgBuilder.build();
    }

    @Override
    public void addIntentHandler(IntentDesc intentDesc, IntentHandler handler) {

        if(StringUtils.isEmpty(intentDesc.getName())){
            log.warn("意图名为空：{}",intentDesc);
            return;
        }
        /**
         * 自动装配
         */
        autowireBean.autowireBean(handler);
        intents.put(intentDesc.getName(),intentDesc);
        intentHandlerMap.put(intentDesc.getName(),handler);
    }



}
