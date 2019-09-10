package com.wjwcloud.iot.voicecontrol.dueros.service.imp;

import com.baidu.dueros.bot.VideoPlayer;
import com.baidu.dueros.data.request.IntentRequest;
import com.baidu.dueros.data.request.LaunchRequest;
import com.baidu.dueros.data.request.SessionEndedRequest;
import com.baidu.dueros.data.request.audioplayer.event.PlaybackNearlyFinishedEvent;
import com.baidu.dueros.data.request.audioplayer.event.PlaybackStartedEvent;
import com.baidu.dueros.data.request.events.ElementSelectedEvent;
import com.baidu.dueros.data.request.videoplayer.event.*;
import com.baidu.dueros.data.response.OutputSpeech;
import com.baidu.dueros.data.response.directive.Play;
import com.baidu.dueros.data.response.directive.display.RenderTemplate;
import com.baidu.dueros.data.response.directive.display.templates.ListItem;
import com.baidu.dueros.data.response.directive.display.templates.ListTemplate1;
import com.baidu.dueros.data.response.directive.videoplayer.ClearQueue;
import com.baidu.dueros.data.response.directive.videoplayer.Stop;
import com.baidu.dueros.data.response.directive.videoplayer.Stream;
import com.baidu.dueros.data.response.directive.videoplayer.VideoItem;
import com.baidu.dueros.model.Response;
import com.baidu.dueros.nlu.Intent;
import com.baidu.dueros.nlu.Slot;
import com.geer2.base.model.PageBean;
import com.geer2.base.utils.SpringContextUtils;
import com.geer2.zwow.iot.product.entity.ProductDevice;
import com.geer2.zwow.iot.product.enums.ProductTypeEnum;
import com.geer2.zwow.iot.product.service.PeepholeVideoMobileService;
import com.geer2.zwow.iot.product.service.PeepholeVideoService;
import com.geer2.zwow.iot.product.service.ProductDeviceService;
import com.geer2.zwow.iot.product.vo.PeepholeVideoVo;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.dueros.common.DuerosConstantKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther:zhoulei
 * @description:视频播放
 * @date :2019/5/20 9:38;
 * QQ：20971053
 */

public class VideoPlayerBotImpl extends VideoPlayer {
    private final Logger logger = LoggerFactory.getLogger(VideoPlayerBotImpl.class);

    /**
     * 视频服务
     */
    private PeepholeVideoService peepholeVideoService = null;
    /**
     * 手机视频服务
     */
    private PeepholeVideoMobileService peepholeVideoMobileService = null;
    /**
     * 设备服务
     */
    private ProductDeviceService productDeviceService = null;

    /**
     * 重写构造方法
     *
     * @param request
     *            HttpServletRequest作为参数类型
     * @throws IOException
     *             抛出的异常
     */
    public VideoPlayerBotImpl(HttpServletRequest request) throws IOException {
        super(request);
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
        peepholeVideoMobileService = (PeepholeVideoMobileService) applicationContext.getBean("peepholeVideoMobileServiceImpl");
        productDeviceService = (ProductDeviceService) applicationContext.getBean("productDeviceServiceImpl");
        peepholeVideoService = (PeepholeVideoService) applicationContext.getBean("peepholeVideoServiceImpl");
    }

    /*
     * 重写onLaunch方法，处理launchRequest事件
     *
     * @param launchRequest aunchRequest请求体
     *
     * @see com.baidu.dueros.bot.BaseBot#onLaunch(com.baidu.dueros.data.request.
     * LaunchRequest)
     */
    @Override
    public Response onLaunch(LaunchRequest launchRequest) {
        logger.info("onLaunch调用================================================================");
        String accessToken = getAccessToken();
        ListTemplate1 listTemplate1 = getVideoList();
// 定义RenderTemplate指令
        RenderTemplate renderTemplate1 = new RenderTemplate(listTemplate1);
        this.addDirective(renderTemplate1);
        OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "欢迎使用视频播放");
        Response response = new Response(outputSpeech);
        return response;
    }

    /*
     * 重写onInent方法，处理onInent对话事件
     *
     * @see com.baidu.dueros.bot.BaseBot#onInent(com.baidu.dueros.data.request.
     * IntentRequest)
     */
    @Override
    public Response onInent(IntentRequest intentRequest) {
        logger.info("onInent调用================================================================");
        // 判断NLU解析的意图名称是否匹配 video_play_intent
        if ("video_play_intent".equals(intentRequest.getIntentName())) {
            com.baidu.dueros.data.response.directive.videoplayer.Play play = new com.baidu.dueros.data.response.directive.videoplayer.Play("http://www.video");
            play.setPlayBehavior(com.baidu.dueros.data.response.directive.videoplayer.Play.PlayBehaviorType.REPLACE_ALL);
            play.setToken("token");
            // 也可以链式set信息
            play.setOffsetInMilliSeconds(1000).setVideoItemId("video_1");
            this.addDirective(play);
            OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "即将为您播放视频");
            Response response = new Response(outputSpeech);
            return response;
        }
        // 判断NLU解析的意图名称是否匹配 video_stop_intent
        else if ("ai.dueros.vidio.stop".equals(intentRequest.getIntentName())) {
            Stop stop = new Stop();
            this.addDirective(stop);
            OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "即将退出视频播放");
            Response response = new Response(outputSpeech);
            this.setExpectSpeech(false);
            return response;
        }
        // 判断NLU解析的意图名称是否匹配 video_stop_intent
        else if ("video_clearqueue_intent".equals(intentRequest.getIntentName())) {
            ClearQueue clear = new ClearQueue("CLEAR_ALL");
            this.addDirective(clear);

            OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "即将为您清除播放队列");
            Response response = new Response(outputSpeech);
            return response;
        } else if ("ai.dueros.video.goto".equals(intentRequest.getIntentName())){
            List<Intent> list = intentRequest.getIntents();
            Intent intent = list.get(0);
            Intent intent1 = this.getIntent(0);
            Map params = intent.getSlots();
            if(params.containsKey("num") && !"".equals(params.get("num"))){
                Slot slot = (Slot)params.get("num");
                int num = Integer.valueOf(slot.getValue());
                String token = this.getAccessToken();
                long customerId = AligenieUtil.getCustomerIdByToken(token , DuerosConstantKey.BASE64SECURITY);
                Map map = new HashMap();
                map.put("customerId" , customerId);
                map.put("type" , ProductTypeEnum.LOCK.getCode());
                map.put("isDeleted" , 0);

                PageBean pd = new PageBean();
                pd.setPageSize(10);
                pd.setPageNo(1);
                PageBean pageBean = null;
                try {
                    pageBean = peepholeVideoMobileService.findPageBean(map , pd);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("获取视频列表失败");
                    return null;
                }
                List<PeepholeVideoVo> videoList = pageBean.getList();
                PeepholeVideoVo videoVo = videoList.get(num - 1 );
                return getVideoPlayResponseById(videoVo.getId().toString());
            }
            return null;
        } else {
            logger.info("意图键" + intentRequest.getIntentName());
            OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "视频bot默认意图");
            Response response = new Response(outputSpeech);
            return response;
        }
    }

    /*
     * 重写onPlaybackStutterStartedEvent方法，处理playbackStutterStartedEvent事件
     *当DuerOS向技能上报PlaybackStarted事件之后，如果视频缓冲数据的速度慢于播放速度时，DuerOS会向技能上报此事件。
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackStutterStartedEvent(com.baidu.
     * dueros.data.request.videoplayer.event.PlaybackStutterStartedEvent)
     */
    @Override
    public Response onPlaybackStutterStartedEvent(PlaybackStutterStartedEvent playbackStutterStartedEvent) {
        logger.info("onPlaybackStutterStartedEvent调用调用================================================================");
        return getResponse();
    }

    /*
     * 重写onPlaybackStartedEvent方法，处理PlaybackStartedEvent事件
     *当视频开始播放时，DuerOS向技能上报VideoPlayer.PlaybackStarted事件。
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackStartedEvent(com.baidu.dueros.data
     * .request.videoplayer.event.PlaybackStartedEvent)
     */
    @Override
    public Response onPlaybackStartedEvent(com.baidu.dueros.data.request.videoplayer.event.PlaybackStartedEvent playbackStartedEvent) {
        logger.info("onPlaybackStartedEvent调用调用================================================================");
        return null;
    }

    /*
     * 重写onPlaybackStoppedEvent方法，处理PlaybackStoppedEvent事件
     *当用户说"停止播放"后，DuerOS会向技能上报该事件，请求技能保存视频播放信息。
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackStoppedEvent(com.baidu.dueros.data
     * .request.videoplayer.event.PlaybackStoppedEvent)
     */
    @Override
    public Response onPlaybackStoppedEvent(PlaybackStoppedEvent playbackStoppedEvent) {
        logger.info("onPlaybackStoppedEvent调用调用================================================================");
        //结束回话
        this.setExpectSpeech(false);
        this.declareEffect();
        ListTemplate1 listTemplate1 = getVideoList();
// 定义RenderTemplate指令
        RenderTemplate renderTemplate1 = new RenderTemplate(listTemplate1);
        this.addDirective(renderTemplate1);
        Response response = new Response();
        return null;
    }

    /*
     * 重写onPlaybackNearlyFinishedEvent方法，处理PlaybackNearlyFinishedEvent事件
     *当前video item播放快要结束，准备缓冲或下载播放队列中的下一个video item时，DuerOS会向技能上报此事件。技能收到该事件后可以返回VideoPlayer.Play指令，将下一个video item添加到播放队列中。
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackNearlyFinishedEvent(com.baidu.
     * dueros.data.request.videoplayer.event.PlaybackNearlyFinishedEvent)
     */
    @Override
    public Response onPlaybackNearlyFinishedEvent(com.baidu.dueros.data.request.videoplayer.event.PlaybackNearlyFinishedEvent playbackNearlyFinishedEvent) {
        logger.info("onPlaybackNearlyFinishedEvent调用调用================================================================");
        return this.response;
    }

    /*
     * 重写onPlaybackFinishedEvent方法，处理PlaybackFinishedEvent事件
     *当前视频播放结束时，DuerOS向技能上报此事件。
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackFinishedEvent(com.baidu.dueros.
     * data.request.videoplayer.event.PlaybackFinishedEvent)
     */
    @Override
    public Response onPlaybackFinishedEvent(PlaybackFinishedEvent playbackFinishedEvent) {
        logger.info("onPlaybackFinishedEvent调用调用================================================================");
        String accessToken = getAccessToken();
        ListTemplate1 listTemplate1 = getVideoList();
// 定义RenderTemplate指令
        RenderTemplate renderTemplate1 = new RenderTemplate(listTemplate1);
        this.addDirective(renderTemplate1);
        Response response = new Response();
        return null;
    }

    /*
     * 重写onProgressReportIntervalElapsedEvent方法，
     * 处理ProgressReportIntervalElapsedEvent事件
     *
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onProgressReportIntervalElapsedEvent(com.
     * baidu.dueros.data.request.videoplayer.event.
     * ProgressReportIntervalElapsedEvent)
     */
    @Override
    public Response onProgressReportIntervalElapsedEvent(
            ProgressReportIntervalElapsedEvent progressReportIntervalElapsedEvent) {
        logger.info("onProgressReportIntervalElapsedEvent调用调用================================================================");
        return getResponse();
    }

    /*
     * 重写onProgressReportDelayElapsedEvent方法，处理ProgressReportDelayElapsedEvent事件
     *
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onProgressReportDelayElapsedEvent(com.baidu.
     * dueros.data.request.videoplayer.event.ProgressReportDelayElapsedEvent)
     */
    @Override
    public Response onProgressReportDelayElapsedEvent(
            ProgressReportDelayElapsedEvent progressReportDelayElapsedEvent) {
        logger.info("onProgressReportDelayElapsedEvent调用调用================================================================");
        return getResponse();
    }

    /*
     * 重写onPlaybackStutterFinishedEvent方法，处理PlaybackStutterFinishedEvent事件
     *
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackStutterFinishedEvent(com.baidu.
     * dueros.data.request.videoplayer.event.PlaybackStutterFinishedEvent)
     */
    @Override
    public Response onPlaybackStutterFinishedEvent(PlaybackStutterFinishedEvent playbackStutterFinishedEvent) {
        logger.info("onPlaybackStutterFinishedEvent调用================================================================");
        return getResponse();
    }

    /*
     * 重写onPlaybackPausedEvent方法，处理PlaybackPausedEvent事件
     *在视频播放过程中，如果发生用户与设备对话交互、闹钟提醒等优先级高的通道，则视频暂停播放，DuerOS会向技能上报此事件。
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackPausedEvent(com.baidu.dueros.data.
     * request.videoplayer.event.PlaybackPausedEvent)
     */
    @Override
    public Response onPlaybackPausedEvent(PlaybackPausedEvent playbackPausedEvent) {
        logger.info("onPlaybackPausedEvent调用================================================================");
        return this.response;
    }

    /*
     * 重写onPlaybackResumedEvent方法，处理PlaybackResumedEvent事件
     *在视频播放过程中，如果发生用户与设备对话交互、闹钟提醒等优先级高的通道，则视频暂停播放，DuerOS会向技能上报VideoPlayer.PlaybackPaused事件，如果高优先级通道结束后，视频会继续播放，此时DuerOS会向技能上报PlaybackResumed事件。
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackResumedEvent(com.baidu.dueros.data
     * .request.videoplayer.event.PlaybackResumedEvent)
     */
    @Override
    public Response onPlaybackResumedEvent(PlaybackResumedEvent playbackResumedEvent) {
        logger.info("onPlaybackResumedEvent调用================================================================");
        return getResponse();
    }

    /*
     * 重写onPlaybackQueueClearedEvent方法，处理PlaybackQueueClearedEvent事件
     *
     * @see
     * com.baidu.dueros.bot.VideoPlayer#onPlaybackQueueClearedEvent(com.baidu.dueros
     * .data.request.videoplayer.event.PlaybackQueueClearedEvent)
     */
    @Override
    public Response onPlaybackQueueClearedEvent(PlaybackQueueClearedEvent playbackQueueClearedEvent) {
        logger.info("onPlaybackQueueClearedEvent调用================================================================");
        return getResponse();
    }

    /*
     * 重写onSessionEnded方法，处理SessionEndedRequest对话事件
     *
     * @see com.baidu.dueros.bot.BaseBot#onSessionEnded(com.baidu.dueros.data.request.
     * SessionEndedRequest)
     */
    @Override
    public Response onSessionEnded(SessionEndedRequest sessionEndedRequest) {
        logger.info("onSessionEnded调用================================================================");
        return super.onSessionEnded(sessionEndedRequest);
    }

    /*
     * 重写onDefaultEvent方法，处理没有订阅的事件
     *
     * @see com.baidu.dueros.bot.BaseBot#onDefaultEvent()
     */
    @Override
    public Response onDefaultEvent() {
        logger.info("onDefaultEvent调用================================================================");
        this.setExpectSpeech(false);
        return new Response();
    }

    /**
     * 用于测试视频播放的相关事件，事件都返回一个播放指令
     *
     * @return Response 响应
     */
    public Response getResponse() {
        this.setExpectSpeech(false);
        Stream stream = new Stream("http://dueroscdn.ubestkid.com/blk/m/526_mhcdxnh.mp4", 0);
        stream.setExpectedPreviousToken("setAnchorWord2");
//        stream.setProgressReport(400, 500);
        VideoItem videoItem = new VideoItem("id:12345", stream);
        com.baidu.dueros.data.response.directive.videoplayer.Play play = new com.baidu.dueros.data.response.directive.videoplayer.Play(com.baidu.dueros.data.response.directive.videoplayer.Play.PlayBehaviorType.ENQUEUE, videoItem);
        this.addDirective(play);
        OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "");
        Response response = new Response(outputSpeech);
        return response;
    }

    /**
     * 重写选择视频列表后调用方法
     * @param elementSelectedEvent
     * @return
     */
    @Override
    public Response onElementSelectedEvent(ElementSelectedEvent elementSelectedEvent) {
        return getVideoPlayResponseById(this.getAccessToken());
    }

    /**
     * 根据视频ID播放视频
     * @param token
     * @return
     */
    private Response getVideoPlayResponseById (String token){
        Response response = null;
        try {
            PeepholeVideoVo peepholeVideoVo = peepholeVideoService.selectByPrimaryKey(Long.valueOf(token));
            this.setExpectSpeech(false);
            com.baidu.dueros.data.response.directive.videoplayer.Play play =
                    new com.baidu.dueros.data.response.directive.videoplayer.Play(peepholeVideoVo.getVideoUrl());
            play.setPlayBehavior(com.baidu.dueros.data.response.directive.videoplayer.Play.PlayBehaviorType.REPLACE_ALL);
            play.setToken(token);
            // 也可以链式set信息
            play.setOffsetInMilliSeconds(1000).setVideoItemId(token);
            this.addDirective(play);
            OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "");
            response = new Response(outputSpeech);
//        Response response = new Response();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //获取视频列表
    private ListTemplate1 getVideoList(){
        //要返回的列表
        ListTemplate1 listTemplate1 = new ListTemplate1();
        String token = this.getAccessToken();
        long customerId = AligenieUtil.getCustomerIdByToken(token , DuerosConstantKey.BASE64SECURITY);
        List<ProductDevice> productDeviceList = null;
        Map map = new HashMap();
        map.put("customerId" , customerId);
        map.put("type" , ProductTypeEnum.LOCK.getCode());
        map.put("isDeleted" , 0);
        try {
            productDeviceList = productDeviceService.findListOfCustomer(map);
            List deviceIDS = new ArrayList();
            if(productDeviceList.size()>0){
                productDeviceList.forEach(productDevice -> {deviceIDS.add(productDevice.getId());});
//                map = new HashMap();
//                map.put("deviceIds" , deviceIDS);
                PageBean pd = new PageBean();
                pd.setPageSize(10);
                pd.setPageNo(1);
                PageBean pageBean = peepholeVideoMobileService.findPageBean(map , pd);
                List<PeepholeVideoVo> videoList = pageBean.getList();
                if (videoList.size() > 0 ){
                    listTemplate1.setToken(token);
                    videoList.forEach(video ->{
                        ListItem listItem1 = new ListItem();
                        listItem1.setAnchorWord("setAnchorWord");
                        listItem1.setPlainPrimaryText("猫眼视频");
                        listItem1.setToken(video.getId().toString());
                    // 也可以链式设置信息
                        listItem1.setPlainSecondaryText("")
                                .setPlainTertiaryText("")
                                .setImageUrl(video.getImageUrl());

                        // 把listItem添加到模版listTemplate
                        listTemplate1.addListItem(listItem1);
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("视频列表获取设备列表失败");
            throw new RuntimeException("视频列表获取设备列表失败");
        }
        return listTemplate1;
    }


}