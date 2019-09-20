package com.wjwcloud.iot.voicecontrol.aligenie.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.iot.product.service.ProductDeviceService;
import com.wjwcloud.iot.voicecontrol.aligenie.enums.GenieCtrType;
import com.wjwcloud.iot.voicecontrol.aligenie.service.IAligenieDeviceService;
import com.wjwcloud.iot.voicecontrol.aligenie.utils.AligenieUtil;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhoulei on 2019/4/25.
 * OAuth2 资源服务
 */
@RestController
@RequestMapping("/aligenie")
public class AligenieDeviceController {
    private static Logger logger = LoggerFactory.getLogger(AligenieDeviceController.class);

    /**
     * 注入设备服务
     */
    @Resource(name = "productDeviceServiceImpl")
    private ProductDeviceService productDeviceService;


    /**
     * 天猫精灵控制服务
     */
    @Resource(name = "aligenieDeviceServiceImpl")
    private IAligenieDeviceService aligenieDeviceService;


    /**
     * 天猫精灵智能家居调用
     * @param request
     * @param response
     * @param br
     * @return
     * @throws IOException
     * @throws OAuthSystemException
     */
    @RequestMapping("/getResource")
    @ResponseBody
    public JSONObject getResource(HttpServletRequest request, HttpServletResponse response , BufferedReader br)
            throws IOException, OAuthSystemException{
        System.out.println("访问地址： " + request.getRequestURI());
        //要返回的设备列表
        AligenieUtil.getAllRequestParam(request);
        AligenieUtil.getAllHeadParam(request);
        JSONObject merchineList = new JSONObject();
          String bodyStrin  = AligenieUtil.getAllBodyParam(br);
          JSONObject bodyJson = new JSONObject();
          bodyJson = JSON.parseObject(bodyStrin);
         //页面传过来的的JSON参数信息
            Map HeaderMap = (Map)bodyJson.get("header");
        System.out.println(HeaderMap);
          //获取设备列表
//          if(HeaderMap.get("namespace").equals(GenieCtrType.设备发现.getValue())){
        if(HeaderMap.get("namespace").equals(GenieCtrType.DEVICE_DISCOVERY.getValue())){
              merchineList = aligenieDeviceService.deviceDiscovery(bodyStrin);
               return merchineList;
          }else if (HeaderMap.get("namespace").equals(GenieCtrType.DEVICE_CONTROL.getValue())){
              merchineList = aligenieDeviceService.deviceControl(bodyStrin);
              logger.info("设备操作");
              return merchineList;
          }else  if(HeaderMap.get("namespace").equals(GenieCtrType.DEVICE_QUERY.getValue())){
              logger.info("设备属性查询");
              merchineList = aligenieDeviceService.deviceQuery(bodyStrin);
              return  merchineList;
          }
        return null;

    }

}