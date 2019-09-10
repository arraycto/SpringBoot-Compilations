package com.wjwcloud.iot.voicecontrol.dueros.service.imp;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geer2.base.utils.redis.RedisProxy;
import com.geer2.zwow.iot.customer.service.CustomerLoginService;
import com.geer2.zwow.iot.product.entity.ProductDevice;
import com.geer2.zwow.iot.product.enums.ProductTypeEnum;
import com.geer2.zwow.iot.product.service.LockSecretMobileService;
import com.geer2.zwow.iot.product.service.LockSettingMobileService;
import com.geer2.zwow.iot.product.service.ProductDeviceService;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.dueros.service.IDuerosDeviceService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 * 小度音响设备服务
 */
@Service("duerosDeviceServiceImpl")
public class DuerosDeviceServiceImpl implements IDuerosDeviceService {
    private String base64Security = "eda1782204cf41efaca1e051ccc610be62acdcf24c09f011f343583c41cfb93f";
    @Resource(name = "customerLoginServiceImpl")
    private CustomerLoginService customerLoginService;

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

    /**
     * 远程开锁服务
     */
    @Resource(name = "lockSecretMobileServiceImpl")
    private LockSecretMobileService lockSecretMobileService;

    @Resource(name = "lockSettingMobileServiceImpl")
    private LockSettingMobileService lockSettingMobileService;

    /**
     * 小度音响设备发现
     * @return
     */
    @Override
    public JSONObject deviceDiscovery(String bodyStr) {
        JSONObject merchineList = new JSONObject();
        //根据body参数获取ployMap参数
        Map ployMap = AligenieUtil.getBodyJsonObjectByKey(bodyStr , "payload");
        //根据小度音响调用传过来的参数获取token
        String accessToken = ployMap.get("accessToken").toString();
        //解析出用户信息
        Claims claims = AligenieUtil.getClaimsByToken(accessToken , base64Security);
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
                    merchineList = getMerchineList(bodyStr , productDeviceList);
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
     * 小度音响设备控制
     * @return
     */
    @Override
    public JSONObject deviceControl(String bodyStr) {
        //根据body参数获取ployMap参数
        Map ployMap = AligenieUtil.getBodyJsonObjectByKey(bodyStr , "payload");
        Map applianceMap = (Map) ployMap.get("appliance");
        long customerId = AligenieUtil.getCustomerIdByBodyStr(bodyStr , base64Security);
        long deviceId = Long.valueOf(applianceMap.get("applianceId").toString());
        Map map = new HashMap();
        map.put("deviceId" ,deviceId);
        map.put("customerId" , customerId);
        map.put("deviceStatus" , "lockBacklock");
        Map headMap = AligenieUtil.getBodyJsonObjectByKey(bodyStr , "header");
        JSONObject result = new JSONObject();
        try{
            if("TurnOffRequest".equals(headMap.get("name"))){
                //调用智能锁开锁服务
                lockSettingMobileService.onlineBacklock(map);
                result = getControlResult(bodyStr , "TurnOffRequest");
            }else if("TurnOnRequest".equals(headMap.get("name"))){
                //调用智能锁开锁服务
                lockSecretMobileService.onlineOpenLock(customerId, deviceId);
                result = getControlResult(bodyStr , "TurnOnRequest");
            }
        }catch (Exception e){
            e.printStackTrace();
        }



        return result;
    }

    /**
     * 小度音响设备查询
     * @return
     */
    @Override
    public JSONObject deviceQuery(String bodyStr) {
        return null;
    }
    /**
     * 获取控制设备结果
     * @param bodyStr
     * @param requestType
     * @return
     */
    private JSONObject getControlResult(String bodyStr , String requestType){
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
        header.put("namespace", "DuerOS.ConnectedHome.Control");
        if("TurnOffRequest".equals(requestType)){
            header.put("name","TurnOffConfirmation");
        }else if("TurnOnRequest".equals(requestType)){
            header.put("name","TurnOnConfirmation");
        }

        header.put("messageId", headerJSON.getString("messageId"));
        header.put("payLoadVersion", "1");
        controlResult.put("header", header);
        payload.put("attributes", new ArrayList<>());
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


        //页面传过来的的JSON参数信息
        JSONObject recieveHeader = new JSONObject();
        recieveHeader = JSON.parseObject(str);
        //保存返回的header信息
        JSONObject header = new JSONObject();
        header.put("namespace", "DuerOS.ConnectedHome.Discovery");
        header.put("name", "DiscoverAppliancesResponse");
        //获取header参数
        String headerParams = recieveHeader.getString("header");
//        System.out.println("header:" + recieveHeader.getString("header"));
        //header参数转为JSON参数对象
        JSONObject headerJSON = new JSONObject();
        headerJSON = JSON.parseObject(headerParams);
        header.put("messageId", headerJSON.getString("messageId"));
        header.put("payLoadVersion", "1");
        //discoveredAppliances以对象数组返回客户关联设备云帐户的设备、场景。如客户关联帐户没有设备、场景则返回空数组。如果在发现过程中出现错误，字段值设置为null, 用户允许接入的最大的设备数量是300。
        List<JSON> discoveredAppliances = new ArrayList<>();
        //discoveredGroups 对象的数组，该对象包含可发现分组，与用户设备帐户相关联的。 如果没有与用户帐户关联的分组，此属性应包含一个空数组。 如果发生错误，该属性可以为空数组[]。阵列中允许的最大项目数量为10。
        List<JSON> discoveredGroups = new ArrayList<>();

        for(int i  = 0 ; i < productDeviceList.size() ; i ++ ){
            ProductDevice productDevice = productDeviceList.get(i);
            //智能设备信息
            JSONObject appliance = new JSONObject();
            //设备支持的操作类型数组。详细情况请参见
            appliance.put("actions" , getActions(productDevice));
            //支持的设备、场景类型。目前支持以下设备。数组
            appliance.put("applianceTypes" ,getApplianceTypes(productDevice));
            //提供给设备云使用，存放设备或场景相关的附加信息，是键值对。DuerOS不了解或使用这些数据。该属性的内容不能超过5000字节。
            appliance.put("additionalApplianceDetails" ,getAdditionalApplianceDetails(productDevice));
            //设备标识符。标识符在用户拥有的所有设备上必须是唯一的。此外，标识符需要在同一设备的多个发现请求之间保持一致。标识符可以包含任何字母或数字和以下特殊字符：_ - = # ; : ? @ &。标识符不能超过256个字符。
            appliance.put("applianceId" ,productDevice.getId());
            //设备相关的描述，描述内容提需要提及设备厂商，使用场景及连接方式，长度不超过128个字符。
            appliance.put("friendlyDescription" ,"展现给用户的详细介绍");
            //设备名称
            appliance.put("friendlyName",productDevice.getAlias());
            //设备当前是否能够到达。true表示设备当前可以到达，false表示当前设备不能到达。
            appliance.put("isReachable" , true);
            //设备厂商的名字
            appliance.put("manufacturerName" , "设备厂商");
            //设备型号名称，是字符串类型，长度不能超过128个字符。
            appliance.put("modelName" , "型号");
            //版本号
            appliance.put("version" , "11");
            //设置设设备的属性信息。当设备没有属性信息时，协议中不需要传入该字段。每个设备允许同步的最大的属性数量是10。详细信息请参考设备属性及设备属性上报。
            appliance.put("attributes" , getAttributes(productDevice));

            //位置信息discoveredGroups
            JSONObject discoveredGroup = getDiscoveredGroups(productDevice);

            //将位置信息加入到返回列表中
            discoveredGroups.add(discoveredGroup);
            //将智能设备信息添加到列表中
            discoveredAppliances.add(appliance);
        }
        // 设备对象
        JSONObject payload = new JSONObject();
        payload.put("discoveredGroups" ,discoveredGroups );
        payload.put("discoveredAppliances", discoveredAppliances);
        MerchineList.put("header", header);
        MerchineList.put("payload", payload);
        System.out.println("设备列表" + MerchineList);
        return MerchineList;
    }

    /**
     * 设备支持的操作类型数组。详细情况请参见
     * @param productDevice
     * @return
     */

    private List<String> getActions(ProductDevice productDevice){
        List<String> actions = new ArrayList();
        actions.add("TurnOn");
        actions.add("turnOff");
//        actions.add("incrementBrightnessPercentage");
//        actions.add("decrementBrightnessPercentage");
        return actions;
    }

    /**
     * 获取支持的设备、场景类型。目前支持以下设备。
     * @return
     */
    private List<String> getApplianceTypes(ProductDevice productDevice){
        List<String> applianceTypes = new ArrayList();
        applianceTypes.add("SWITCH");
        return applianceTypes;
    }

    /**
     * 提供给设备云使用，存放设备或场景相关的附加信息，是键值对。DuerOS不了解或使用这些数据。该属性的内容不能超过5000字节。
     * @param productDevice
     * @return
     */
    private JSONObject getAdditionalApplianceDetails(ProductDevice productDevice){
        JSONObject additionalApplianceDetails = new JSONObject();
        additionalApplianceDetails.put("extraDetail1" , "optionalDetailForSkillAdapterToReferenceThisDevice");
        additionalApplianceDetails.put("extraDetail2" , "There can be multiple entries");
        return  additionalApplianceDetails;
    }


    /**
     * 设置设设备的属性信息。当设备没有属性信息时，协议中不需要传入该字段。每个设备允许同步的最大的属性数量是10。详细信息请参考设备属性及设备属性上报。
     * @param productDevice
     * @return
     */
    private List<JSON> getAttributes(ProductDevice productDevice){
        List attributes = new ArrayList();
        return attributes;
    }

    /**
     * 获取设备位置信息
     */
    private JSONObject getDiscoveredGroups(ProductDevice productDevice){
        JSONObject discoveredGroup = new JSONObject();
        discoveredGroup.put("groupName" , "客厅");
        discoveredGroup.put("groupNotes" , "客厅照片分组控制");
        discoveredGroup.put("additionalGroupDetails" , getAdditionalApplianceDetails(productDevice) );
        discoveredGroup.put("applianceIds" , getApplianceIds(productDevice));
        return discoveredGroup;
    }

    private JSONObject getAdditionalGroupDetails(ProductDevice productDevice){
        JSONObject additionalGroupDetail = new JSONObject();
        additionalGroupDetail.put("extraDetail1" , "detail about the group");
        additionalGroupDetail.put("extraDetail2" , "only be used for reference group");
        return additionalGroupDetail;
    }

    private List getApplianceIds (ProductDevice productDevice){
        List applianceids = new ArrayList();
        applianceids.add(productDevice.getId());
        return applianceids;
    }

}
