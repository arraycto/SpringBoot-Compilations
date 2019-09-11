package com.wjwcloud.iot.voicecontrol.aligenie.controller;

import com.wjwcloud.iot.utils.key.UUIDUtil;
import com.wjwcloud.iot.utils.redis.RedisProxy;
import com.wjwcloud.iot.voicecontrol.aligenie.common.AligenieConstantKey;
import com.wjwcloud.iot.voicecontrol.aligenie.entity.AligenieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.util.Map;

/**
 * Created by zhoulei on 2019/4/25.
 */
//@Controller
//@RequestMapping("/aligenie/LoginController")
public class LoginController {
    @Autowired
    private RedisProxy redisProxy;

    @RequestMapping(value = "/signin" ,method = RequestMethod.POST)
    public String signIn(String returnUrl) {
        return "login/login";
    }

    @RequestMapping(value = "/login" ,method = RequestMethod.GET)
    public void login(HttpServletRequest request , HttpServletResponse response, BufferedReader br) throws Exception{
        HttpSession session = request.getSession();
        Map map = AligenieUtil.getAllRequestParam(request);
        String redirect_uri = map.get("redirect_uri").toString();
        String state = map.get("state").toString();
        String client_id = UUIDUtil.getUUID();
        String key = UUIDUtil.getUUID();
        redisProxy.save(key, client_id);
//        map.put("logined" ,ConstantKey.OAUTH_CLIENT_CALLBACK );
//        map.put("noLogin" , ConstantKey.OAUTH_LOGIN);
        //将天猫精灵调用参数存入缓存中

        String redirectUrl = AligenieConstantKey.OAUTH_LOGIN + "?redirect_uri=" + redirect_uri + "&state=" + state + "&client_id=" + client_id;
        Cookie userCookie=new Cookie("client_id",key);
        userCookie.setMaxAge(30*24*60*60);   //存活期为一个月 30*24*60*60
        userCookie.setPath("/");
        response.addCookie(userCookie);
        System.out.println(redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
