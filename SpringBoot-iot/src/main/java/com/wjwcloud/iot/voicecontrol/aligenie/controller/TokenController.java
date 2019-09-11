package com.wjwcloud.iot.voicecontrol.aligenie.controller;

import com.wjwcloud.iot.voicecontrol.aligenie.entity.AligenieUtil;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zhoulei on 2019/4/25.
 * 登录授权成功后回调服务
 */
//@RestController
//@RequestMapping("/aligenie/oauth2")
public class TokenController {
    private static Logger logger = LoggerFactory.getLogger(TokenController.class);
    private Cache cache ;
    @Autowired
    public TokenController(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("oauth2-cache");
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
        logger.info("第三步，成功回调");
        AligenieUtil.getAllRequestParam(request);
        AligenieUtil.getAllHeadParam(request);
        String requestURI = request.getRequestURL().toString();
        PrintWriter out = null;
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        try {
            out = response.getWriter();
            HttpSession session = request.getSession();
            String client_id = (String) session.getAttribute("client_id");
            String resultData = AligenieUtil.getJsonData(AligenieUtil.requestGetParams(request , "code"));
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
    @RequestMapping(value = "/refresh_token",method = RequestMethod.POST)
    public void refresh_token(HttpServletRequest request, HttpServletResponse response)
            throws IOException, OAuthSystemException {
        PrintWriter out = null;
//        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
//        try {
//            out = response.getWriter();
//            //构建oauth2请求
//            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
//            //验证appkey是否正确
//            if (!validateOAuth2AppKey(oauthRequest)){
//                OAuthResponse oauthResponse = OAuthASResponse
//                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
//                        .setError(OAuthError.CodeResponse.ACCESS_DENIED)
//                        .setErrorDescription(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT)
//                        .buildJSONMessage();
//                out.write(oauthResponse.getBody());
//                out.flush();
//                out.close();
//                return;
//            }
//            //验证是否是refresh_token
//            if (GrantType.REFRESH_TOKEN.name().equalsIgnoreCase(oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE))) {
//                OAuthResponse oauthResponse = OAuthASResponse
//                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
//                        .setError(OAuthError.TokenResponse.INVALID_GRANT)
////                        .setErrorDescription(AligenieConstantKey.INVALID_CLIENT_GRANT)
//                        .buildJSONMessage();
//                out.write(oauthResponse.getBody());
//                out.flush();
//                out.close();
//                return;
//            }
//            /*
//            * 刷新access_token有效期
//             access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
//             1. 若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
//             2. 若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
//             refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。
//            * */
//            Object cache_refreshToken=cache.get(oauthRequest.getRefreshToken());
//            //access_token已超时
//            if (cache_refreshToken == null) {
//                //生成token
//                final String access_Token = oauthIssuerImpl.accessToken();
//                String refresh_Token = oauthIssuerImpl.refreshToken();
//                cache.put(refresh_Token,access_Token);
//                logger.info("access_Token : "+access_Token +"  refresh_Token: "+refresh_Token);
//                //构建oauth2授权返回信息
//                OAuthResponse oauthResponse = OAuthASResponse
//                        .tokenResponse(HttpServletResponse.SC_OK)
//                        .setAccessToken(access_Token)
//                        .setExpiresIn("3600")
//                        .setRefreshToken(refresh_Token)
//                        .buildJSONMessage();
//                response.setStatus(oauthResponse.getResponseStatus());
//                out.print(oauthResponse.getBody());
//                out.flush();
//                out.close();
//                return;
//            }
//            //access_token未超时
//            cache.put(oauthRequest.getRefreshToken(),cache_refreshToken.toString());
//            //构建oauth2授权返回信息
//            OAuthResponse oauthResponse = OAuthASResponse
//                    .tokenResponse(HttpServletResponse.SC_OK)
//                    .setAccessToken(cache_refreshToken.toString())
//                    .setExpiresIn("3600")
//                    .setRefreshToken(oauthRequest.getRefreshToken())
//                    .buildJSONMessage();
//            response.setStatus(oauthResponse.getResponseStatus());
//            out.print(oauthResponse.getBody());
//            out.flush();
//            out.close();
//        } catch(OAuthProblemException ex) {
//            OAuthResponse oauthResponse = OAuthResponse
//                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
//                    .error(ex)
//                    .buildJSONMessage();
//            response.setStatus(oauthResponse.getResponseStatus());
//            out.print(oauthResponse.getBody());
//            out.flush();
//            out.close();
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        }
//        finally
//        {
//            if (null != out){ out.close();}
//        }
    }



}