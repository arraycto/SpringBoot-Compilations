package com.wjwcloud.iot.voicecontrol.dueros;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.DuerOsService;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.DuerRequest;
import com.geer2.zwow.iot.voicecontrol.dueros.bot.entity.proto.DuerResponse;
import com.geer2.zwow.iot.voicecontrol.dueros.service.imp.AudioPlayerBotImpl;
import com.geer2.zwow.iot.voicecontrol.dueros.service.imp.VideoPlayerBotImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dueros/customSkills")
public class DuerOsControllerDemo {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DuerOsService duerOsService;


    @RequestMapping("execute")
    @ResponseBody
    public DuerResponse execute(@RequestBody(required = false) DuerRequest request , HttpServletResponse response){
        return duerOsService.processRequest(request ,response);
    }
//    @RequestMapping("execute")
//    @ResponseBody
//    public DuerResponse execute(HttpServletRequest request , HttpServletResponse response){
////        return duerOsService.processRequest(request ,response);
//        Map map = AligenieUtil.getAllHeadParam(request);
//        Map map1 = AligenieUtil.getAllRequestParam(request);
//        return null;
//    }


    /**
     * 播放语音
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/audio")
    @ResponseBody
    public void audio(HttpServletRequest request , HttpServletResponse response) {

        try {
            AudioPlayerBotImpl audioPlayerBot = new AudioPlayerBotImpl(request);
            // 调用run方法
            String responseJson = audioPlayerBot.run();
            // 设置response的编码UTF-8
            response.setCharacterEncoding("UTF-8");
            // 返回response
            response.getWriter().append(responseJson);
        } catch (Exception e) {
        }
    }

    /**
     * 播放视频
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/video")
    @ResponseBody
    public void video(HttpServletRequest request , HttpServletResponse response) {
        try {
            VideoPlayerBotImpl videoPlayerBot = new VideoPlayerBotImpl(request);
            // 调用run方法
            String responseJson = videoPlayerBot.run();
            JSONObject jsonObject = JSONObject.parseObject(responseJson);
           System.out.println("responseJson值：" + responseJson);
            // 设置response的编码UTF-8
            response.setCharacterEncoding("UTF-8");
            // 返回response
            response.getWriter().append(responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject getImageStructure(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url" , "http://116.62.227.53:9999/group1/M00/00/1A/rBCAP1zdJu2AAstbAAAZi2dsSaI139.png");
//        jsonObject.put("widthPixels" , 20);
//        jsonObject.put("heightPixels" , 20);
        jsonObject.put("ratio" , "2/3");
        return jsonObject;
    }

    private JSONObject getTextStructure(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type" , "PlainText");
        jsonObject.put("text" , "视频内容");
        return jsonObject;
    }

    private JSONObject getImageDirective(){
        JSONObject directiveObject = new JSONObject();
        directiveObject.put("type" , "Display.RenderTemplate");
        JSONObject templateObject = new JSONObject();
        templateObject.put("type" , "BodyTemplate2");
        templateObject.put("image" ,getImageStructure());

        templateObject.put("backgroundImage" , getImageStructure());
        templateObject.put("content" , getTextStructure());
        templateObject.put("title" , "视频标题");
        directiveObject.put("template" , templateObject);
        return directiveObject;
    }


    private JSONObject getVideoDirective(){
        JSONObject directiveObject = new JSONObject();
        directiveObject.put("type" , "Display.RenderVideoList");
        directiveObject.put("behavior" , "REPLACE");
        directiveObject.put("size" , 1);
        List videoItemsList = new ArrayList();

        JSONObject videoItem = new JSONObject();
        videoItem.put("title" , "视频标题");
        videoItem.put("mediaLengthInMilliseconds" , 30);
        videoItem.put("titleSubtext1","短标题");
        videoItem.put("titleSubtext2","长标题");
        JSONObject imageStructure = new JSONObject();
        imageStructure.put("src" , "http://116.62.227.53:9999/group1/M00/00/1A/rBCAP1zdJu2AAstbAAAZi2dsSaI139.png");
        videoItem.put("image" , imageStructure);
        videoItemsList.add(videoItem);
        directiveObject.put("videoItems" , videoItemsList);
        return directiveObject;
    }

}
