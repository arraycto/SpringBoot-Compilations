package com.wjwcloud.iot.voicecontrol.aligenie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Authorization Code 授权码模式
 * Created by zhoulei on 2019/4/25.
 * Impl OAth2  http://oauth.net/2/
 */
//@Controller
//@RequestMapping("/aligenie/oauth2")
public class AuthzController {
    private static Logger logger = LoggerFactory.getLogger(AuthzController.class);
    private Cache cache;

    @Resource(name = "aligenieLoginServiceImpl")
    private IAligenieAuthService iAligenieAuthService;

    @Autowired
    public AuthzController(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("oauth2-cache");
    }
    /* *
     * 构建OAuth2授权请求 [需要client_id与redirect_uri绝对地址]
     * @param request
     * @param session
     * @param model
     * @return 返回授权码(code)有效期10分钟，客户端只能使用一次[与client_id和redirect_uri一一对应关系]
     * @throws OAuthSystemException
     * @throws IOException
     * @url  http://wechat.tunnel.geer2.com:8047/oauth2/authorize?client_id={AppKey}&response_type=code&redirect_uri={YourSiteUrl}
     * @test http://wechat.tunnel.geer2.com:8047/oauth2/authorize?client_id=fbed1d1b4b1449daa4bc49397cbe2350&response_type=code&redirect_uri=http://wechat.tunnel.geer2.com:8047/client/oauthCallback
     */

//    @RequestMapping(value = "/aligenie/authorize")
//    @ResponseBody
    public ResultResponse authorize(HttpServletRequest request, HttpSession session, @RequestBody Map<String, Object> params) {
        try {
            if(!params.containsKey("userName")){
                return ResultResponse.FAILED("请输入用户名");
            }
            if(!params.containsKey("password")){
                return ResultResponse.FAILED("请输入密码");
            }
            Map token = iAligenieAuthService.login(params);
            if (null != token) {
                return ResultResponse.SUCCESSFUL(token);
            } else {
                return ResultResponse.FAILED("登录失败");
            }
        } catch (Exception ex) {
            return ResultResponse.FAILED(ex.getMessage());
        }
    }


//    /**
//     * 用户登录
//     * @param request
//     * @return
//     */
//    private boolean validateOAuth2Pwd(Map params) {
////        if("get".equalsIgnoreCase(request.getMethod())) {
////            return false;
////        }
//        String name = params.get("username").toString();
//        String pwd = params.get("password").toString();
//        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)) {
//            return false;
//        }
//        try {
//            if(name.equalsIgnoreCase("Irving")&&pwd.equalsIgnoreCase("123456")){
//                //登录成功
////                request.getSession().setAttribute(ConstantKey.MEMBER_SESSION_KEY,"Irving");
//                request.getSession().setAttribute(ConstantKey.MEMBER_SESSION_KEY,"w1s8trbwghc3tryyuqdetz7oxiwlushw2h7bn5ry");
//                return true;
//            }
//            return false;
//        } catch (Exception ex) {
//            logger.error("validateOAuth2Pwd Exception: " + ex.getMessage());
//            return false;
//        }
//    }
}