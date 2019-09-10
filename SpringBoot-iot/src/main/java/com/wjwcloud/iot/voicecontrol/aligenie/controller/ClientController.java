package com.wjwcloud.iot.voicecontrol.aligenie.controller;

import com.geer2.base.model.ResultResponse;
import com.geer2.base.utils.redis.RedisProxy;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.aligenie.service.IAligenieAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by zhoulei on 2019/4/25.
 * OAuth2 客户端实现
 */
//@Controller
//@RequestMapping("/aligenie/client")
public class ClientController {
    @Autowired
    private RedisProxy redisProxy;

    @Resource(name = "aligenieLoginServiceImpl")
    private IAligenieAuthService iAligenieAuthService;
     Logger logger = LoggerFactory.getLogger(ClientController.class);

    /**
     * 登录成功后回调服务
     * @param request
     * @param session
     * @param params
     * @return
     */
//    @RequestMapping(value = "/oauthCallback")
//    @ResponseBody
    public ResultResponse oauthCallback(HttpServletRequest request, HttpSession session, @RequestBody Map<String, Object> params) {
        System.out.println("登录、授权成功后回调、获得令牌并返回");
        //确认了授权,获取第三方接收code的重定向地址
        Map map = (Map)redisProxy.read(params.get("client_id").toString());
        String redirect_uri = (String)params.get("redirect_uri");
        String state = (String)params.get("state");
        if(null != map){
            redirect_uri = (String)map.get("redirect_uri");
            state = (String)map.get("state");
        }
        //获取授权码
        String code = (String) params.get("code");
        String token = (String) params.get("token");
        //发布访问令牌，解码url并且将code加在url后面返回
        redirect_uri = AligenieUtil.getURLDecoderString(redirect_uri) + "&token=" + token + "&state=" + state + "&code=" + code;
        System.out.println(redirect_uri);
        return ResultResponse.SUCCESSFUL(redirect_uri);
    }
}
