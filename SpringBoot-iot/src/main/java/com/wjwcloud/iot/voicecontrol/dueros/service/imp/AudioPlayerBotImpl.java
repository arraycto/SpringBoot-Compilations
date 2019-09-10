package com.wjwcloud.iot.voicecontrol.dueros.service.imp;

import com.baidu.dueros.bot.AudioPlayer;
import com.baidu.dueros.data.request.LaunchRequest;
import com.baidu.dueros.data.request.audioplayer.event.PlaybackNearlyFinishedEvent;
import com.baidu.dueros.data.request.audioplayer.event.PlaybackStartedEvent;
import com.baidu.dueros.data.response.OutputSpeech;
import com.baidu.dueros.data.response.Reprompt;
import com.baidu.dueros.data.response.card.TextCard;
import com.baidu.dueros.data.response.directive.AudioPlayerDirective;
import com.baidu.dueros.data.response.directive.Play;
import com.baidu.dueros.model.Response;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @auther:zhoulei
 * @description:视频播放
 * @date :2019/5/20 9:38;
 * QQ：20971053
 */

public class AudioPlayerBotImpl extends AudioPlayer {

    /**
     * 重写构造方法
     *
     * @param request
     *            HttpServletRequest作为参数类型
     * @throws IOException
     *             抛出异常
     */
    public AudioPlayerBotImpl(HttpServletRequest request) throws IOException {
        super(request);
    }

    /**
     * 重写onLaunch方法，处理LaunchRequest事件
     *
     * @param launchRequest
     *            LaunchRequest请求体
     * @see com.baidu.dueros.bot.BaseBot#onLaunch(com.baidu.dueros.data.request.LaunchRequest)
     */
    @Override
    protected Response onLaunch(LaunchRequest launchRequest) {

        // 构造TextCard
        TextCard textCard = new TextCard();
        textCard.setContent("处理端上报事件");
        textCard.setUrl("http://116.62.227.53:9999/group1/M00/00/09/rBCAP1yZ0HaACrrjAAETwDQPN64591.wav");
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("端上报事件");

        // 构造OutputSpeech
        OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "处理端上报事件");

        // 构造Reprompt
        Reprompt reprompt = new Reprompt(outputSpeech);

        // 构造Response
        Response response = new Response(outputSpeech, textCard, reprompt);

        return response;
    }

    /**
     * 重写onPlaybackNearlyFinishedEvent方法，处理onPlaybackNearlyFinishedEvent端上报事件
     *
     * @param playbackNearlyFinishedEvent
     *            PlaybackNearlyFinishedEvent请求体
     * @see com.baidu.dueros.bot.AudioPlayer#onPlaybackNearlyFinishedEvent(com.baidu.dueros.data.request.audioplayer.event.PlaybackNearlyFinishedEvent)
     */
    @Override
    protected Response onPlaybackNearlyFinishedEvent(PlaybackNearlyFinishedEvent playbackNearlyFinishedEvent) {

        TextCard textCard = new TextCard();
        textCard.setContent("处理即将播放完成事件");
        textCard.setUrl("www:...");
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("即将完成");

        OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "处理即将播放完成事件");

        // 新建Play指令
        Play play = new Play(AudioPlayerDirective.PlayBehaviorType.ENQUEUE, "url", 1000);
        // 添加返回的指令
        this.addDirective(play);

        Reprompt reprompt = new Reprompt(outputSpeech);

        Response response = new Response(outputSpeech, textCard, reprompt);

        return response;
    }

    /**
     * 重写onPlaybackStartedEvent方法，处理PlaybackStartedEvent事件
     *
     * @param playbackStartedEvent
     *            PlaybackStartedEvent事件
     *
     * @see com.baidu.dueros.bot.AudioPlayer#onPlaybackStartedEvent(com.baidu.dueros.data.request.audioplayer.event.PlaybackStartedEvent)
     */
    @Override
    protected Response onPlaybackStartedEvent(PlaybackStartedEvent playbackStartedEvent) {

        TextCard textCard = new TextCard();
        textCard.setContent("处理开始播放完成事件");
        textCard.setUrl("www:...");
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("开始播放");

        OutputSpeech outputSpeech = new OutputSpeech(OutputSpeech.SpeechType.PlainText, "处理开始播放完成事件");

        Play play = new Play(AudioPlayerDirective.PlayBehaviorType.ENQUEUE, "url", 1000);

        this.addDirective(play);

        Reprompt reprompt = new Reprompt(outputSpeech);

        Response response = new Response(outputSpeech, textCard, reprompt);

        return response;
    }

}
