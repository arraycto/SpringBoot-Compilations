package com.wjwcloud.iot.voicecontrol.aligenie;

import com.geer2.base.model.ResultResponse;
import com.geer2.base.utils.redis.RedisProxy;
import com.geer2.zwow.iot.voicecontrol.aligenie.controller.AuthzController;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.aligenie.service.IAligenieAuthService;
import com.geer2.zwow.iot.voicecontrol.service.OauthCallBackService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Authorization Code 授权码模式
 * Created by zhoulei on 2019/4/25.
 * Impl OAth2  http://oauth.net/2/
 */
@Controller
@RequestMapping("/aligenie")
public class AligenieController {
    private static Logger logger = LoggerFactory.getLogger(AuthzController.class);
    private Cache cache;

    /**
     * 注入天猫精灵登录服务
     */
    @Resource(name = "aligenieLoginServiceImpl")
    private IAligenieAuthService iAligenieAuthService;

    @Resource (name = "oauthCallBackServiceImpl")
    private OauthCallBackService oauthCallBackService;

    /**
     * 注入redis服务
     */
    @Autowired
    private RedisProxy redisProxy;

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

    //用户登录
    @RequestMapping(value = "/login")
    @ResponseBody
    public ResultResponse authorize(HttpServletRequest request, HttpSession session, @RequestBody Map<String, Object> params) {
        try {
            if(!params.containsKey("mobilePhone")){
                return ResultResponse.FAILED("请输入手机号");
            }
            if(!params.containsKey("loginType")){
                return ResultResponse.FAILED("请传入正确的参数");
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

    //天猫精灵调用登录服务
    @RequestMapping(value = "/aligenielogin" ,method = RequestMethod.GET)
    public void login(HttpServletRequest request , HttpServletResponse response, BufferedReader br) throws Exception{
        iAligenieAuthService.aligenielogin(request,response,br);
    }

    /**
     * 登录成功后回调服务
     * @param request
     * @param session
     * @param params
     * @return
     */
    @RequestMapping(value = "/oauthCallback")
    @ResponseBody
    public ResultResponse oauthCallback( @RequestBody Map<String, Object> params) {
        logger.info("登录、授权成功后回调、获得令牌并返回");
        //发布访问令牌，解码url并且将code加在url后面返回
        String redirect_uri = oauthCallBackService.oauthCallback(params);
        return ResultResponse.SUCCESSFUL(redirect_uri);
    }

    /**
     * 认证服务器申请令牌(AccessToken) [验证client_id、client_secret、auth code的正确性或更新令牌 refresh_token]
     * @param request
     * @param response
     * @return
     * @url http://wechat.tunnel.geer2.com:8047/oauth2/accessToken?client_id={AppKey}&client_secret={AppSecret}&grant_type=authorization_code&redirect_uri={YourSiteUrl}&code={code}
     */
    @RequestMapping(value = "/accessToken",method = RequestMethod.POST)
    public void accessToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, OAuthSystemException {
        logger.info("智能音箱授权成功回调");
//        UrlUtil.getAllRequestParam(request);
//        UrlUtil.getAllHeadParam(request);
//        String requestURI = request.getRequestURL().toString();
        PrintWriter out = null;
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            out = response.getWriter();
            HttpSession session = request.getSession();
            String client_id = (String) session.getAttribute("client_id");
            String resultData = AligenieUtil.getJsonData(AligenieUtil.requestGetParams(request , "code"));
            logger.info("返回天猫精灵token：" + resultData);
            response.setContentType("application/json");
            out.write(resultData);
            out.flush();
            out.close();
            return;
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 刷新令牌
     * @param request
     * @param response
     * @throws IOException
     * @throws OAuthSystemException
     * @url http://wechat.tunnel.geer2.com:8047/oauth2/refresh_token?client_id={AppKey}&grant_type=refresh_token&refresh_token={refresh_token}
     */
    @RequestMapping(value = "/refreshToken",method = RequestMethod.POST)
    public void refresh_token(HttpServletRequest request, HttpServletResponse response)
            throws IOException, OAuthSystemException {
        PrintWriter out = null;
    }


}