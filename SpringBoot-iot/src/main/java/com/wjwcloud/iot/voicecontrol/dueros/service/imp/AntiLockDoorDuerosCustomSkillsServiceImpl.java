package com.wjwcloud.iot.voicecontrol.dueros.service.imp;


import com.geer2.base.utils.DateUtils;
import com.geer2.zwow.iot.product.entity.ProductDevice;
import com.geer2.zwow.iot.product.enums.ProductTypeEnum;
import com.geer2.zwow.iot.product.service.LockSecretMobileService;
import com.geer2.zwow.iot.product.service.LockSettingMobileService;
import com.geer2.zwow.iot.product.service.ProductDeviceLogMobileService;
import com.geer2.zwow.iot.product.service.ProductDeviceService;
import com.geer2.zwow.iot.product.vo.ProductDeviceLogVo;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.service.AbsCustomSkillsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 * 百度音箱自定义技能服务  门反锁
 */
@Service("antiLockDoorDuerosCustomSkillsServiceImpl")
public class AntiLockDoorDuerosCustomSkillsServiceImpl extends AbsCustomSkillsService {
    String base64Security = "eda1782204cf41efaca1e051ccc610be62acdcf24c09f011f343583c41cfb93f";
    private static Logger logger = LoggerFactory.getLogger(AntiLockDoorDuerosCustomSkillsServiceImpl.class);


    @Resource(name = "productDeviceLogMobileServiceImpl")
    private ProductDeviceLogMobileService productDeviceLogMobileService;

    /**
     * 注入设备服务
     */
    @Resource(name = "productDeviceServiceImpl")
    private ProductDeviceService productDeviceService;

    /**
     * 远程开、关锁服务
     */
    @Resource(name = "lockSecretMobileServiceImpl")
    private LockSecretMobileService lockSecretMobileService;

    @Resource(name = "lockSettingMobileServiceImpl")
    private LockSettingMobileService lockSettingMobileService;


    /**
     * 根据用户信息查询要返回的语音播放内容
     * @param customerId
     * @return
     */
    private String getResultString(long customerId){
        StringBuffer context = new StringBuffer();
        context.append("报警记录");
//        context.append("15时30分小明用指纹开锁,门未上锁");
        try {
            Map dateMap = AligenieUtil.getDateRange(DateUtils.getDate());
            Map map = new HashMap();
            map.put("customerId" , customerId);
            map.put("type" , "lockOpened");
            map.put("startTime" ,dateMap.get("startTime"));
            map.put("endTime" ,dateMap.get("endTime"));
            map.put("TimeTrue" , "1");

            List<ProductDeviceLogVo> productDeviceLogList  =productDeviceLogMobileService.findList(map);
            //没有设备
            if(null == productDeviceLogList){
                context = new StringBuffer();
                context.append("您还没有智能设备");
            }else if(productDeviceLogList.size() == 0 ){
                context = new StringBuffer();
                context.append("没有报警记录");
            }else{
                context.append(assembleSpeechContent(productDeviceLogList));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.toString();
    }

    @Override
    public Object getText(Map<String, Object> params) {
        if(!params.containsKey("token") || null == params.get("token") || "".equals(params.get("token"))){
            return null;
        }
        String result = "";
        String token = params.get("token").toString();
        long customerId = AligenieUtil.getCustomerIdByToken(token,base64Security);
        setCustomerId(customerId);
        setType(ProductTypeEnum.LOCK.getCode());
        setIsDeleted("0");
        //获取该用户下所有设备列表
        try {
            List<ProductDevice> productDeviceList = findListOfCustomer();
            StringBuffer sb = new StringBuffer();
            if(productDeviceList.size() > 0 ){
                for (ProductDevice productDevice : productDeviceList) {
                    Map backLockParams = new HashMap();
                    backLockParams.put("deviceId" , productDevice.getId());
                    backLockParams.put("customerId" , customerId);
                    backLockParams.put("deviceStatus" , "lockBacklock");
                    lockSettingMobileService.onlineBacklock(backLockParams);
                    sb.append(productDevice.getAlias() + ",");
                }
                sb.append("反锁成功");
                result = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据用户ID获取设备列表失败");
            result = e.getMessage();
        }
        return result;
    }

}
