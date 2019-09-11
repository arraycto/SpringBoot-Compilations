package com.wjwcloud.iot.voicecontrol.aligenie.service.impl;


import com.wjwcloud.iot.customer.service.CustomerLoginService;
import com.wjwcloud.iot.utils.key.UUIDUtil;
import com.wjwcloud.iot.utils.redis.RedisProxy;
import com.wjwcloud.iot.voicecontrol.aligenie.common.AligenieConstantKey;
import com.wjwcloud.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.wjwcloud.iot.voicecontrol.aligenie.service.IAligenieAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 * 天猫精灵授权服务
 */
@Service("aligenieLoginServiceImpl")
public class AligenieAuthServiceImpl implements IAligenieAuthService {

    @Resource(name = "customerLoginServiceImpl")
    private CustomerLoginService customerLoginService;

    /**
     * 注入redis服务
     */
    @Autowired
    private RedisProxy redisProxy;

    /**
     * 天猫精灵登录
     * @param params
     * @return
     */
    @Override
    public Map login(Map<String,Object> params) {
        Map map = new HashMap();
        try {
            String token = customerLoginService.aligenieLogin(params);
            map.put("token" , token);
            map.put("callBackUrl" , AligenieConstantKey.OAUTH_CLIENT_CALLBACK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return map;
    }
    /**
     * 天猫精灵调用登录服务
     */
    @Override
    public void aligenielogin(HttpServletRequest request , HttpServletResponse response, BufferedReader br) {
        AligenieUtil.getAllRequestParam(request);
        AligenieUtil.getAllBodyParam(br);
        AligenieUtil.getAllHeadParam(request);
        HttpSession session = request.getSession();
        Map map = AligenieUtil.getAllRequestParam(request);
//        String redirect_uri = "aa";
//        String state = "bb";
//        String client_id = UUIDUtil.getUUID();
//        String key = UUIDUtil.getUUID();
//        redisProxy.save(key, client_id);
        String redirect_uri = map.get("redirect_uri").toString();
        String state = map.get("state").toString();
        String client_id = UUIDUtil.getUUID();
        String key = UUIDUtil.getUUID();
        redisProxy.save(key, client_id);
//        map.put("logined" ,AligenieConstantKey.OAUTH_CLIENT_CALLBACK );
//        map.put("noLogin" , AligenieConstantKey.OAUTH_LOGIN);
        //将天猫精灵调用参数存入缓存中
        String filePath = this.getClass().getClassLoader().getResource("").getPath().substring(0,5);

        String rollbackUrl = AligenieConstantKey.OAUTH_LOGIN_SERVICE ;
        if("file:".equals(filePath.substring(0,5))){
            rollbackUrl = AligenieConstantKey.OAUTH_LOGIN_SERVICE;
        }

        String redirectUrl = rollbackUrl + "?redirect_uri=" + redirect_uri + "&state=" + state + "&client_id=" + client_id;
        Cookie userCookie=new Cookie("client_id",key);
        userCookie.setMaxAge(30*24*60*60);   //存活期为一个月 30*24*60*60
        userCookie.setPath("/");
        response.addCookie(userCookie);
        System.out.println(redirectUrl);
        try {
//            response.getWriter().write("hello world");
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
