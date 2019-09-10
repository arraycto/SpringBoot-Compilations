package com.wjwcloud.iot.voicecontrol.aligenie.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geer2.base.utils.key.UUIDUtil;
import com.geer2.zwow.iot.product.entity.ProductDevice;
import com.geer2.zwow.iot.product.enums.ProductTypeEnum;
import com.geer2.zwow.iot.product.service.ProductDeviceService;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoulei on 2019/4/25.
 * OAuth2 资源服务
 */
//@RestController
//@RequestMapping("/aligenie/getResource")
public class ResourceController {
    private static Logger logger = LoggerFactory.getLogger(ResourceController.class);
    String base64Security = "eda1782204cf41efaca1e051ccc610be62acdcf24c09f011f343583c41cfb93f";


    /**
     * 注入设备服务
     */
    @Resource(name = "productDeviceServiceImpl")
    private ProductDeviceService productDeviceService;


    @RequestMapping("/get_resource")
    @ResponseBody
    public JSONObject get_resource(HttpServletRequest request, HttpServletResponse response , BufferedReader br)
            throws IOException, OAuthSystemException{
        System.out.println("访问地址： " + request.getRequestURI());
        //要返回的设备列表
        JSONObject merchineList = new JSONObject();
          String bodyStrin  = AligenieUtil.getAllBodyParam(br);
        AligenieUtil.getAllHeadParam(request);
        AligenieUtil.getAllRequestParam(request);
          //获取设备列表
          if(!"".equals(bodyStrin)){
              //根据天猫精灵调用传过来的参数获取token并解析出用户信息
              Claims claims = getClaimsByToken(bodyStrin);
              long userId = 0L ;
              if(null != claims){
                  try {
                      //获取用户ID
                      userId = Long.parseLong(claims.get("userId").toString());
                      Map map = new HashMap();
                      map.put("customerId" , userId);
                      map.put("type" , ProductTypeEnum.LOCK.getCode());
                      map.put("isDeleted" , 0);
                      //获取该用户下所有设备列表
                      List<ProductDevice> productDeviceList = productDeviceService.findListOfCustomer(map);
                      System.out.println("该用户下设备列表为：" + productDeviceList.size());
                      if(productDeviceList.size() > 0 ){
                          merchineList = getMerchineList(bodyStrin , productDeviceList);
//                        merchineList = getMercherListTest(bodyStrin );
                      }
                  } catch (NumberFormatException e) {
                      e.printStackTrace();
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }
              return merchineList;
          }
          //操作设备
          else{
              System.out.println("设备操作");
              return getMercherListTest("设备控制");
          }


    }

    /**
     * 天猫精灵回调设备控制服务
     * @param request
     * @param response
     * @param br
     * @return
     * @throws IOException
     * @throws OAuthSystemException
     */
    @RequestMapping("/DeviceControl")
    @ResponseBody
    public JSONObject DeviceControl(HttpServletRequest request, HttpServletResponse response , BufferedReader br)
            throws IOException, OAuthSystemException{
        //要返回的设备列表
        JSONObject merchineList = new JSONObject();
        String bodyStrin  = AligenieUtil.getAllBodyParam(br);
        //根据天猫精灵调用传过来的参数获取token并解析出用户信息
        Claims claims = getClaimsByToken(bodyStrin);
        long userId = 0L ;
        if(null != claims){
            try {
                //获取用户ID
                userId = Long.parseLong(claims.get("userId").toString());
                Map map = new HashMap();
                map.put("customerId" , userId);
                //获取该用户下所有设备列表
                List<ProductDevice> productDeviceList = productDeviceService.findListOfCustomer(map);
                System.out.println("该用户下设备列表为：" + productDeviceList.size());
                if(productDeviceList.size() > 0 ){
                    merchineList = getMerchineList(bodyStrin , productDeviceList);
//                        merchineList = getMercherListTest(bodyStrin );
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return merchineList;
    }


    /**
     根据天猫精灵调用传过来的参数获取token并解析出用户信息
     */
    private Claims getClaimsByToken (String bodyStrin){
        JSONObject recieveHeader = new JSONObject();
        //body部分
        recieveHeader = JSON.parseObject(bodyStrin);
        //获取head属性
        String str1 = recieveHeader.getString("header");
        JSONObject payload = new JSONObject();
        //获取带token的json数据
        payload = recieveHeader.getJSONObject("payload");
        System.out.println(payload);
        String token = (String)payload.get("accessToken");
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                .parseClaimsJws(token).getBody();
        return claims;
    }


    /**
     * 获取设备列表
     * @param str
     * @return
     */
    private JSONObject getMerchineList(String str , List<ProductDevice> productDeviceList){
        //要返回的设备列表
        JSONObject MerchineList = new JSONObject();
        //保存返回的header信息
        JSONObject header = new JSONObject();
        // 设备对象
        JSONObject payload = new JSONObject();
        //设备列表
        List<JSONObject> devices =  new ArrayList();
        //页面传过来的的JSON参数信息
        JSONObject recieveHeader = new JSONObject();
        recieveHeader = JSON.parseObject(str);
        //获取header参数
        String str1 = recieveHeader.getString("header");
//        System.out.println("header:" + recieveHeader.getString("header"));
        //header参数转为JSON参数对象
        JSONObject recieveMessageId = new JSONObject();
        recieveMessageId = JSON.parseObject(str1);
        System.out.println("messageId:" + recieveMessageId.getString("messageId"));

        header.put("namespace", "AliGenie.Iot.Device.Discovery");
        header.put("name", "DiscoveryDevicesResponse");
        header.put("messageId", recieveMessageId.getString("messageId"));
        header.put("payLoadVersion", "1");
        for(int i  = 0 ; i < productDeviceList.size() ; i ++ ){
            ProductDevice productDevice = productDeviceList.get(i);
            //添加设备信息
            JSONObject device = new JSONObject();
            JSONObject propertieIsopen = new JSONObject();
            JSONObject propertieOnLine = new JSONObject();
            List<JSON> properties = new ArrayList();
            List actions = new ArrayList();
            JSONObject extentions = new JSONObject();


//            设备ID
            device.put("deviceId", productDevice.getId() + "");
            //设备名称
            device.put("deviceName", productDevice.getAlias());
            //设备类型
            device.put("deviceType", "smart-gating");
            //设备位置
            device.put("zone", "");
            //品牌
            device.put("brand", productDevice.getModel());
            //设备型号
            device.put("model", productDevice.getModel());
            //设备图片地址  产品icon(https协议的url链接),像素最好160*160 以免在app显示模糊
            device.put("icon", "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1531878000&di=c989660f4b827a0049c3b7aec4fe38e1&src=http://img.czvv.com/sell/599adfe4d2f0b1b2f118606f/20170905113247194.jpg");


            //返回当前设备支持的属性状态列表，产品支持的属性列表参考 设备控制与设备状态查询页 的 第二部分 设备状态查询 2.2 章节
            //设备是否在线
            propertieOnLine.put("name", "onlinestate");
            propertieOnLine.put("value", productDevice.getIsOnline()==1?"online":"offline");
            properties.add(propertieOnLine);
            //设备当前状态
            propertieIsopen.put("name", "remotestatus");
            propertieIsopen.put("value", productDevice.getIsOnline().equals("lock_opened")?"on":"off");
            properties.add(propertieIsopen);
            device.put("properties", properties);

            actions.add("TurnOn");
            actions.add("TurnOff");
            device.put("actions", actions);


            extentions.put("extension1", "扩展1");
            extentions.put("extension2", "扩展2");
            device.put("extensions", extentions);
            devices.add(device);
        }
        payload.put("devices", devices);

        MerchineList.put("header", header);
        MerchineList.put("payload", payload);
        System.out.println("设备列表" + MerchineList);
        return MerchineList;
    }

    /**
     * 获取该设备可操作内容
     * @param str
     * @return
     */
    private JSONObject getMercherListTest (String str){
        JSONObject header = new JSONObject();
        // 设备对象
        JSONObject payload = new JSONObject();
        //要返回的设备列表
        JSONObject controllerList = new JSONObject();
        //扩展
        JSONObject extentions = new JSONObject();

        //头部文件设置
        header.put("namespace", "AliGenie.Iot.Device.Control");
        header.put("name", "TurnOn");
        header.put("messageId", UUIDUtil.getUUID());
        header.put("payLoadVersion", "1");

        payload.put("accessToken", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6IiVFNiU4QSVCMSVFNiVBRCU4OSVFRiVCQyU4QyVFNiU4OCU5MSVFNiU4OSU4RCVFNiU5OCVBRiVFNiVCNSVCNyVFOCVCNCVCQyVFNyU4RSU4QiVFNyU5QSU4NCVFNCVCQSVCQSIsInVzZXJJZCI6IjkwNiIsImlzcyI6IjA5OGY2YmNkNDYyMWQzNzMyZmRlNGU4MzI2MjdiNGY2IiwiaWF0IjoxNTU2MjQ3MjQxLCJhdWQiOiJlZGExNzgyMjA0Y2Y0MWVmYWNhMWUwNTFjY2M2MTBiZTYyYWNkY2YyNGMwOWYwMTFmMzQzNTgzYzQxY2ZiOTNmIiwiZXhwIjoxNTg3NzgzMjQxLCJuYmYiOjE1NTYyNDcyNDF9.95PwBjHANjkh9xreIZcaIYu8KhkS6TfKEfiCxr9L1Eg");
        payload.put("deviceId", "1347");
        payload.put("deviceType", "smart-gating" );
        payload.put("attribute", "powerstate");
        payload.put("value", "on");
        extentions.put("extension1", "tset");
        extentions.put("extension2", "test");
        payload.put("extensions", extentions);


        controllerList.put("header", header);
        controllerList.put("payload", payload);
        System.out.println("控制参数" +controllerList.toString());
        return controllerList;
    }
}