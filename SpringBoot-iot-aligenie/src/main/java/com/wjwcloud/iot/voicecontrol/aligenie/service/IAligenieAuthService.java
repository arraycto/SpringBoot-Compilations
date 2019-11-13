package com.wjwcloud.iot.voicecontrol.aligenie.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 */
public interface IAligenieAuthService {

     /**
      * 用户登录
      * @param params
      * @return
      */
     Map login(Map<String, Object> params);

     /**
      * 天猫精灵调用登录服务
      */
     void aligenielogin(HttpServletRequest request, HttpServletResponse response, BufferedReader br);
}
