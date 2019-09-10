package com.wjwcloud.iot.voicecontrol.aligenie.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geer2.base.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class AligenieUtil {
    private final static String ENCODE = "utf-8";
    private static Logger logger = LoggerFactory.getLogger(AligenieUtil.class);


    /**
     *
     * @param client_id OAuth用户名
     * @return token json
     */
    public static String getJsonData(String client_id){
        System.out.println("getJsonData-------");
        //刷新access_token
        String access_token = client_id;
        //刷新refresh_token
        String refresh_token = client_id;
        //保存token记录
//        SqlOperat.saveAccessOrRefreshToken(access_token,true,client_id);
//        SqlOperat.saveAccessOrRefreshToken(refresh_token,false,client_id);
        //token过期后进行刷新
        String token = JSON.toJSONString(new TokenBean(access_token, 259200, refresh_token, null));
        return token;
    }

    /**
     * 返回错误响应
     * @param resp s
     */
    public static void responseError(HttpServletResponse resp, int errorCode) throws IOException {
        resp.setContentType("application/json;charset=utf-8");
        System.out.println("error!!");
        //返回错误状态
        resp.getWriter().write("{error\":"+errorCode+",\"error_description\":\"description}");
    }

    /**
     * 校验code是否发放且正确未过期
     * @param code code
     * @param client_id oauth user
     * @return code result?ok
     */
    public static boolean checkAccessCodeOk(String code, String client_id){
//        String getCodeDate = SqlOperat.getAccessCode(code,client_id);
//        Date date = new Date();//获得系统时间.
//        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//        if (getCodeDate  != null){
////            long codeTime = Long.parseLong(SqlOperat.dateToStamp(getCodeDate));
////            long nowTime = Long.parseLong(SqlOperat.dateToStamp(nowDate));
//            System.out.println(codeTime+"--"+nowTime+"="+(nowTime-codeTime));
//            if (codeTime != 0 && (nowTime - codeTime)/1000 <= 60*2){
//                return true;
//            }else {
//                return false;
//            }
//        }else {
//            return false;
//        }
        return false;
    }


    /**
     * URL 解码
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 编码
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     *      * 获取客户端请求参数中所有的信息
     *      * @param request
     *      * @return
     *      
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        logger.info("params属性开始======================================================");
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                logger.info("key:" + en  + "             value: " + value);
                //如果字段的值为空，判断若值为空，则删除这个字段>
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        logger.info("params属性结束======================================================");
        return res;
    }

    /**
     * 根据键获取参数
     * @param request
     * @param key
     * @return
     */
    public static String requestGetParams (HttpServletRequest request , String key){
        Enumeration<?> temp = request.getParameterNames();
        String returnValue = "";
        logger.info("params属性开始======================================================");
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                if (null != en && !"".equals(en) && key.equals(en)) {
                    returnValue = value;
                    break;
                }
            }
        }
        logger.info("params属性结束======================================================");
        return returnValue;
    }

    /**
     *      * 获取客户端请求头参数中所有的信息
     *      * @param request
     *      * @return
     *      
     */
    public static Map<String, String> getAllHeadParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> enum1 = request.getHeaderNames();
        logger.info("head属性开始======================================================");
        while (enum1.hasMoreElements()) {
            String key = (String) enum1.nextElement();
            String value = request.getHeader(key);
            logger.info(key + "\t" + value);
            res.put(key,value);
        }
        logger.info("head属性结束======================================================");
        return res;
    }
    /**
     *      * 获取客户端请求body参数中所有的信息
     *      * @param request
     *      * @return
     *      
     */
    public static String getAllBodyParam(final BufferedReader br) {
        //body部分
        String inputLine;
        logger.info("body属性开始======================================================");
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            logger.info("IOException: " + e);
        }
        logger.info("str:" + str);
        logger.info("body属性结束======================================================");
        return str;
    }

    /**
     根据天猫精灵调用传过来的参数获取token并解析出用户信息
     */
    public static Claims getClaimsByToken (String token ,String base64Security){
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                .parseClaimsJws(token).getBody();
        return claims;
    }

    /**
     根据天猫精灵调用传过来的参数获取BodyStr并解析出用户信息
     */
    public static long getCustomerIdByBodyStr (String bodyStr ,String base64Security ,String paramsType){
        long result = 0L;
        //根据body参数获取ployMap参数
        Map ployMap = getBodyJsonObjectByKey(bodyStr , "payload");
        //根据天猫精灵调用传过来的参数获取token
        String accessToken = ployMap.get("accessToken").toString();
        //解析出用户信息
        result = getCustomerIdByToken(accessToken ,base64Security , paramsType);
        return result;
    }

    /**
     根据天猫精灵调用传过来的参数获取token并解析出用户信息
     */
    public static long getCustomerIdByBodyStr (String bodyStr ,String base64Security){
        long result = getCustomerIdByBodyStr(bodyStr,base64Security,null);
        return result;
    }

    /**
     根据天猫精灵调用传过来的参数获取token并解析出用户信息
     */
    public static long getCustomerIdByToken (String token ,String base64Security){
        long result = getCustomerIdByToken(token,base64Security,null);
        return result;
    }

    /**
     根据天猫精灵调用传过来的token值解析出用户信息
     */
    public static long getCustomerIdByToken (String token ,String base64Security ,String paramsType){
        long result = 0L;
        //解析出用户信息
        Claims claims = getClaimsByToken(token ,base64Security);
        if(null == paramsType){
            result = Long.parseLong(claims.get("userId").toString());
        }else{
            result = Long.parseLong(claims.get(paramsType).toString());
        }
        return result;
    }


    /**
     * 获取body对象中key所对应的数据
     */

    public static Map  getBodyJsonObjectByKey(String bodyStrin , String key){
        JSONObject bodyJson = new JSONObject();
        //body部分
        bodyJson = JSON.parseObject(bodyStrin);
        Map returnMap = (Map)bodyJson.get(key);
        return returnMap;
    }

    /**
     * 根据日期获取当前日期范围
     *
     * @param object 格式必须为"yyyy-MM-dd hh:mm:ss"
     * @return
     */
    public static Map getDateRange(Date object) {
        Map map = new HashMap();
        //获取时间范围
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(object);
        calendar.add(Calendar.DATE, -1);
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
        //开始时间
        String checkinTimeStr = DateUtils.dateToString(calendarTemp.getTime(), "yyyy-MM-dd HH:mm:ss");
        Calendar calendaroutTime = Calendar.getInstance();
        calendaroutTime.setTime(calendar.getTime());
        calendaroutTime.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        //结束时间
        String checkoutTimeStr = DateUtils.dateToString(calendaroutTime.getTime(), "yyyy-MM-dd HH:mm:ss");
        map.put("startTime", checkinTimeStr);
        map.put("endTime", checkoutTimeStr);
        return map;
    }





    public static void main(String[] args) {
        String ss = AligenieUtil.getURLDecoderString("https://wechat.tunnel.geer2.com:8047/aligenie/aligenielogin");
        String ss1 = AligenieUtil.getURLEncoderString("https://wechat.tunnel.geer2.com:8047/aligenie/accessToken");
        System.out.println(ss);
        System.out.println(ss1);
//        String s = "http://localhost:8580/index-dev.html#/aligenieLogin?client_id=%3F%3F%3F%3F%3F%3F%3F%3F";
//        logger.info(getURLDecoderString(s));
//        logger.info(getURLEncoderString(s));
//        AligenieUtil.getDateRange(new Date());
////        String s = "{\"header\":{\"messageId\":\"63af68cb-dcd6-40be-8073-7e0ea7b3c701\",\"name\":\"DiscoveryDevices\",\"namespace\":\"AliGenie.Iot.Device.Discovery\",\"payLoadVersion\":1},\"payload\":{\"accessToken\":\"2kvz2ivypzstggdfjdpwigkdllwujnst1xcz8ik6b8\"}}";
//        logger.info("ssss");
//        String url = "https://h5.bot.tmall.com/vue/#/device-manager?debug=true&token=5a828e2f0477f32942e69a38c412b9aec02189cf31b147719d8522fa62f2093c88930c0a21a88442";
//        logger.info(getURLDecoderString(url));
    }
}
