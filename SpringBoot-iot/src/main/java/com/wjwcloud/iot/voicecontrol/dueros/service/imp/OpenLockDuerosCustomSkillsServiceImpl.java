package com.wjwcloud.iot.voicecontrol.dueros.service.imp;


import com.geer2.base.utils.DateUtils;
import com.geer2.zwow.iot.product.service.ProductDeviceLogMobileService;
import com.geer2.zwow.iot.product.vo.ProductDeviceLogVo;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.service.AbsCustomSkillsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 * 百度音箱自定义技能服务  开锁记录播放
 */
@Service("openLockDuerosCustomSkillsServiceImpl")
public class OpenLockDuerosCustomSkillsServiceImpl extends AbsCustomSkillsService {
    String base64Security = "eda1782204cf41efaca1e051ccc610be62acdcf24c09f011f343583c41cfb93f";
    private static Logger logger = LoggerFactory.getLogger(OpenLockDuerosCustomSkillsServiceImpl.class);



    @Resource(name = "productDeviceLogMobileServiceImpl")
    private ProductDeviceLogMobileService productDeviceLogMobileService;


    /**
     * 根据用户信息查询要返回的语音播放内容
     * @param customerId
     * @return
     */
    private String getResultString(){
        StringBuffer context = new StringBuffer();
        context.append("开锁记录");
//        context.append("15时30分小明用指纹开锁,门未上锁");
        try {

            List<ProductDeviceLogVo> productDeviceLogList  = findSpeechContentList();
            //没有设备
            if(null == productDeviceLogList){
                context = new StringBuffer();
                context.append("您还没有智能设备");
            }else if(productDeviceLogList.size() == 0 ){
                context = new StringBuffer();
                context.append("没有开锁记录");
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
        String token = params.get("token").toString();
        long customerId = AligenieUtil.getCustomerIdByToken(token,base64Security);
        setCustomerId(customerId);
        Map dateMap = AligenieUtil.getDateRange(DateUtils.getDate());
        setMessageType("lockOpened");
        setTimeTrue("1");
        setStartTime(dateMap.get("startTime").toString());
        setEndTime(dateMap.get("endTime").toString());
        return getResultString();
    }
}
