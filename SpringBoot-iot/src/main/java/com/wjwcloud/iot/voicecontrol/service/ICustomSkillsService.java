package com.wjwcloud.iot.voicecontrol.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 * 智能语音设备自定义技能执行
 */
public interface ICustomSkillsService {

     /**
      * 智能语音设备自定义技能执行
      * @param params
      * @return
      */
     Object execute(String securityWrapperTaskQuery, HttpServletRequest request);


     /**
      * 自定义技能获取返回内容
      * @param params
      * @return
      */
     Object getText(Map<String, Object> params);



}
