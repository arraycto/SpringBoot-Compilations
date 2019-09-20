package com.wjwcloud.iot.voicecontrol.aligenie.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.iot.product.entity.ProductDevice;
import com.wjwcloud.iot.product.service.ProductDeviceService;
import com.wjwcloud.iot.utils.redis.RedisProxy;
import com.wjwcloud.iot.voicecontrol.aligenie.service.IAligenieDeviceService;
import com.wjwcloud.iot.voicecontrol.aligenie.utils.AligenieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 * 天猫精灵设备服务
 */
@Service("aligenieDeviceServiceImpl")
public class AligenieDeviceServiceImpl implements IAligenieDeviceService {
    private String base64Security = "eda1782204cf41efaca1e051ccc610be62acdcf24c09f011f343583c41cfb93f";
//    @Resource(name = "customerLoginServiceImpl")
//    private CustomerLoginService customerLoginService;

    /**
     * 注入设备服务
     */
    @Resource(name = "productDeviceServiceImpl")
    private ProductDeviceService productDeviceService;

    /**
     * 注入redis服务
     */
    @Autowired
    private RedisProxy redisProxy;

//    public AligenieDeviceServiceImpl(CustomerLoginService customerLoginService) {
//        this.customerLoginService = customerLoginService;
//    }

//    /**
//     * 远程开锁服务
//     */
//    @Resource(name = "lockSecretMobileServiceImpl")
//    private LockSecretMobileService lockSecretMobileService;
//
//    @Resource(name = "lockSettingMobileServiceImpl")
//    private LockSettingMobileService lockSettingMobileService;

    /**
     * 天猫精灵设备发现
     * @return
     */
    @Override
    public JSONObject deviceDiscovery(String bodyStr) {
        JSONObject merchineList = new JSONObject();
        //根据body参数获取ployMap参数
        Map ployMap = AligenieUtil.getBodyJsonObjectByKey(bodyStr , "payload");
        //根据天猫精灵调用传过来的参数获取token
        String accessToken = ployMap.get("accessToken").toString();
        //解析出用户信息
//        Claims claims = AligenieUtil.getClaimsByToken(accessToken , base64Security);
        long userId = 0L ;
//        if(null != claims){
            try {
                //获取用户ID
//                userId = Long.parseLong(claims.get("userId").toString());
                Map map = new HashMap();
//                map.put("customerId" , userId);
//                map.put("type" , ProductTypeEnum.LOCK.getCode());
//                map.put("isDeleted" , 0);
                //获取该用户下所有设备列表
                List<ProductDevice> productDeviceList = productDeviceService.findListOfCustomer(map);
                System.out.println("该用户下设备列表为：" + productDeviceList.size());
                if(productDeviceList.size() > 0 ){
                    merchineList = getMerchineList(bodyStr , productDeviceList);
//                        merchineList = getMercherListTest(bodyStrin );
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
//        }
        return merchineList;
    }

    /**
     * 天猫精灵设备控制
     * @return
     */
    @Override
    public JSONObject deviceControl(String bodyStr) {
        //根据body参数获取ployMap参数
        Map ployMap = AligenieUtil.getBodyJsonObjectByKey(bodyStr , "payload");
        //根据天猫精灵调用传过来的参数获取token
        String accessToken = ployMap.get("accessToken").toString();
        //解析出用户信息
//        Claims claims = AligenieUtil.getClaimsByToken(accessToken ,base64Security);
//        long customerId = Long.parseLong(claims.get("userId").toString());
        long deviceId = Long.valueOf(ployMap.get("deviceId").toString());
        Map map = new HashMap();
        map.put("deviceId" ,deviceId);
//        map.put("customerId" , customerId);
        map.put("deviceStatus" , "lockBacklock");
        if("off".equals(ployMap.get("value"))){
            //调用智能锁关锁服务
//            new Thread(()->{try {
//                lockSettingMobileService.onlineBacklock(map);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }}).start();
        }else if("on".equals(ployMap.get("value"))){
            //调用智能锁开锁服务
//            new Thread(()->{try {
//                lockSecretMobileService.onlineOpenLock(customerId, deviceId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }}).start();
        }



        return getControlResult(bodyStr);
    }

    /**
     * 天猫精灵设备查询
     * @return
     */
    @Override
    public JSONObject deviceQuery(String bodyStr) {
        return null;
    }
    /**
     * 获取控制设备结果
     * @param bodyStr
     * @return
     */
    private JSONObject getControlResult(String bodyStr){
        //要返回的设备列表
        JSONObject controlResult = new JSONObject();
        //保存返回的header信息
        JSONObject header = new JSONObject();
        // 设备对象
        JSONObject payload = new JSONObject();
        //页面传过来的的JSON参数信息
        JSONObject recieveHeader = new JSONObject();
        recieveHeader = JSON.parseObject(bodyStr);
        //获取header参数
        String headerParams = recieveHeader.getString("header");
//        System.out.println("header:" + recieveHeader.getString("header"));
        //header参数转为JSON参数对象
        JSONObject headerJSON = new JSONObject();
        headerJSON = JSON.parseObject(headerParams);
        System.out.println("messageId:" + headerJSON.getString("messageId"));
        header.put("namespace", "AliGenie.Iot.Device.Discovery");
        header.put("name","TurnOnResponse");
        header.put("messageId", headerJSON.getString("messageId"));
        header.put("payLoadVersion", "1");
        controlResult.put("header", header);
        payload.put("deviceId","1");
        controlResult.put("payload", payload);
        return controlResult;

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
        String headerParams = recieveHeader.getString("header");
//        System.out.println("header:" + recieveHeader.getString("header"));
        //header参数转为JSON参数对象
        JSONObject headerJSON = new JSONObject();
        headerJSON = JSON.parseObject(headerParams);
        System.out.println("messageId:" + headerJSON.getString("messageId"));

        //获取body参数
        String bodyParams = recieveHeader.getString("payload");
//        System.out.println("header:" + recieveHeader.getString("header"));
        //header参数转为JSON参数对象
        JSONObject bodyJSON = new JSONObject();
        bodyJSON = JSON.parseObject(bodyParams);
        String accessToken = bodyJSON.getString("accessToken");
        System.out.println("token:" + bodyJSON.getString("accessToken"));

        header.put("namespace", "AliGenie.Iot.Device.Discovery");
        header.put("name", "DiscoveryDevicesResponse");
        header.put("messageId", headerJSON.getString("messageId"));
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
//            device.put("deviceName", productDevice.getAlias());
            device.put("deviceName", productDevice.getId());
            //设备类型(门锁)
            device.put("deviceType", "fan");
            //设备位置
            device.put("zone", "");
            //品牌
//            device.put("brand", productDevice.getModel());
            device.put("brand", productDevice.getId());
            //设备型号
//            device.put("model", productDevice.getModel());
            device.put("model", productDevice.getId());
            //设备图片地址  产品icon(https协议的url链接),像素最好160*160 以免在app显示模糊
            device.put("icon", "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1531878000&di=c989660f4b827a0049c3b7aec4fe38e1&src=http://img.czvv.com/sell/599adfe4d2f0b1b2f118606f/20170905113247194.jpg");


            //返回当前设备支持的属性状态列表，产品支持的属性列表参考 设备控制与设备状态查询页 的 第二部分 设备状态查询 2.2 章节
            //设备是否在线
//            propertieOnLine.put("name", "onlinestate");
//            propertieOnLine.put("value", productDevice.getIsOnline()==1?"online":"offline");
//            properties.add(propertieOnLine);
//            //设备当前状态
            propertieIsopen.put("name", "remotestatus");
            propertieIsopen.put("value", productDevice.getIsOnline().equals("lock_opened")?"on":"off");
            properties.add(propertieIsopen);
//            propertieOnLine.put("name", "windspeed");
//            propertieOnLine.put("value", "1");
            properties.add(propertieOnLine);
            device.put("properties", properties);

            actions.add("TurnOn");
            actions.add("TurnOff");
//            actions.add("SetWindSpeed");
            device.put("actions", actions);


            extentions.put("request_token", accessToken);
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


}
