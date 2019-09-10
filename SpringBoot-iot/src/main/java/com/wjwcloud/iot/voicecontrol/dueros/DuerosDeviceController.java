package com.wjwcloud.iot.voicecontrol.dueros;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geer2.zwow.iot.product.service.ProductDeviceService;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.dueros.enums.DuerosDeviceRequestType;
import com.geer2.zwow.iot.voicecontrol.dueros.service.IDuerosDeviceService;
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
@RequestMapping("/dueros/device")
public class DuerosDeviceController {
    private static Logger logger = LoggerFactory.getLogger(DuerosDeviceController.class);

    /**
     * 注入设备服务
     */
    @Resource(name = "productDeviceServiceImpl")
    private ProductDeviceService productDeviceService;


    /**
     * 小度音响控制服务
     */
    @Resource(name = "duerosDeviceServiceImpl")
    private IDuerosDeviceService iDuerosDeviceService;


    /**
     * 小度音响智能家居调用
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
        Map paramsMap = AligenieUtil.getAllRequestParam(request);
        Map headMap = AligenieUtil.getAllHeadParam(request);
        JSONObject merchineList = new JSONObject();
          String bodyStrin  = AligenieUtil.getAllBodyParam(br);
          JSONObject bodyJson = new JSONObject();
          bodyJson = JSON.parseObject(bodyStrin);
         //页面传过来的的JSON参数信息
            Map HeaderMap = (Map)bodyJson.get("header");
        System.out.println(HeaderMap);
          //获取设备列表
          if(HeaderMap.get("namespace").equals(DuerosDeviceRequestType.DISCOVERY.getCode())){
              merchineList = iDuerosDeviceService.deviceDiscovery(bodyStrin);
               return merchineList;
          }else if (HeaderMap.get("namespace").equals(DuerosDeviceRequestType.CONTROL.getCode())){
              merchineList = iDuerosDeviceService.deviceControl(bodyStrin);
              logger.info("设备操作");
              return merchineList;
          }else  if(HeaderMap.get("namespace").equals(DuerosDeviceRequestType.QUERY.getCode())){
              logger.info("设备属性查询");
              merchineList = iDuerosDeviceService.deviceQuery(bodyStrin);
              return  merchineList;
          }
        return null;

    }

}