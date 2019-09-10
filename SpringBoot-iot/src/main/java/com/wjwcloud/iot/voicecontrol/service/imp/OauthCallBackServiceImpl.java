package com.wjwcloud.iot.voicecontrol.service.imp;

import com.geer2.base.utils.redis.RedisProxy;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.aligenie.enums.VoicePlatformType;
import com.geer2.zwow.iot.voicecontrol.service.AbstractOauthCallBack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("oauthCallBackServiceImpl")
public class OauthCallBackServiceImpl extends AbstractOauthCallBack {
    /**
     * 注入redis服务
     */
    @Autowired
    private RedisProxy redisProxy;

    @Override
    public String oauthCallback(Map<String, Object> params) {
        System.out.println("登录、授权成功后回调、获得令牌并返回");
        //确认了授权,获取第三方接收code的重定向地址
//        Map map = (Map)redisProxy.read(params.get("client_id").toString());
        String redirect_uri = (String)params.get("redirect_uri");
        String state = (String)params.get("state");
        //获取授权码
        String code = (String) params.get("code");
        String token = (String) params.get("token");
        String client_id = (String) params.get("client_id");
        //发布访问令牌，解码url并且将code加在url后面返回
        if(redirect_uri.substring(0,20).equals(VoicePlatformType.ALIGENIE.getCode())){
            redirect_uri = AligenieUtil.getURLDecoderString(redirect_uri) + "&token=" + token + "&state=" + state + "&code=" + code;
        }else if(redirect_uri.substring(0,20).equals(VoicePlatformType.DUEROS.getCode())){
            redirect_uri = AligenieUtil.getURLDecoderString(redirect_uri) + "?code=" + code + "&state=" + 0 + "&client_id=" + client_id;
        }
        System.out.println(redirect_uri);
        return redirect_uri;
    }

}
