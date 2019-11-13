//package com.wjwcloud.iot.voicecontrol.aligenie.service.impl;
//
//
//import com.alibaba.da.coin.ide.spi.meta.ResultType;
//import com.alibaba.da.coin.ide.spi.standard.ResultModel;
//import com.alibaba.da.coin.ide.spi.standard.TaskQuery;
//import com.alibaba.da.coin.ide.spi.standard.TaskResult;
//import com.alibaba.da.coin.ide.spi.trans.MetaFormat;
//import com.geer2.base.utils.DateUtils;
//import com.geer2.base.utils.redis.RedisProxy;
//import com.geer2.zwow.iot.customer.service.CustomerLoginService;
//import com.geer2.zwow.iot.product.service.LockSecretMobileService;
//import com.geer2.zwow.iot.product.service.ProductDeviceLogMobileService;
//import com.geer2.zwow.iot.product.service.ProductDeviceService;
//import com.geer2.zwow.iot.product.vo.ProductDeviceLogVo;
//import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
//import com.geer2.zwow.iot.voicecontrol.service.AbsCustomSkillsService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by zhoulei on 2019/5/7.
// * 天猫精灵自定义技能服务 语音播放
// */
//@Service("voiceMessagelAligenieCustomSkillsServiceImpl")
//public class VoiceMessagelAligenieCustomSkillsServiceImpl extends AbsCustomSkillsService {
//    String base64Security = "eda1782204cf41efaca1e051ccc610be62acdcf24c09f011f343583c41cfb93f";
//    private static Logger logger = LoggerFactory.getLogger(VoiceMessagelAligenieCustomSkillsServiceImpl.class);
//
//    @Resource(name = "customerLoginServiceImpl")
//    private CustomerLoginService customerLoginService;
//
//    /**
//     * 注入设备服务
//     */
//    @Resource(name = "productDeviceServiceImpl")
//    private ProductDeviceService productDeviceService;
//
//    /**
//     * 注入redis服务
//     */
//    @Autowired
//    private RedisProxy redisProxy;
//
//    /**
//     * 远程开锁服务
//     */
//    @Resource(name = "lockSecretMobileServiceImpl")
//    private LockSecretMobileService lockSecretMobileService;
//
//    @Resource(name = "productDeviceLogMobileServiceImpl")
//    private ProductDeviceLogMobileService productDeviceLogMobileService;
//
//
//    /**
//     * 执行自定义技能
//     * @param bodyStr
//     * @return
//     */
//    @Override
//    public ResultModel<TaskResult> execute(String securityWrapperTaskQuery , HttpServletRequest request) {
////        long customerId = AligenieUtil.getCustomerIdByToken(bodyStr ,base64Security);
//        logger.info("播放语音留言记录");
//        TaskQuery query = null;
//        long customerId = 0L;
//        try {
//            AligenieUtil.getAllRequestParam(request);
////            AligenieUtil.getAllBodyParam(request);
//            Map headParams = AligenieUtil.getAllHeadParam(request);
//            /**
//             * 将开发者平台识别到的语义理解的结果（json字符串格式）转换成TaskQuery
//             */
//            query = MetaFormat.parseToQuery(securityWrapperTaskQuery);
//            String token = query.getToken();
//            if(null == token){
//                Map  map = new HashMap();
//                map.put("mobilePhone" , "18126143867");
//                map.put("password" , "123456");
//                map.put("loginType" , "userPassword");
//                token = customerLoginService.aligenieLogin(map);
//            }
//            customerId = AligenieUtil.getCustomerIdByToken(token,base64Security);
//            Map dateMap = AligenieUtil.getDateRange(DateUtils.getDate());
//            setCustomerId(customerId);
//            setMessageType("lockOpened");
//            setTimeTrue("1");
//            setStartTime(dateMap.get("startTime").toString());
//            setEndTime(dateMap.get("endTime").toString());
//            logger.info("query参数" + query);
//            //进行处理执行
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return getResultModel(customerId);
//    }
//
//    /**
//     * 根据用户信息查询要返回的语音播放内容
//     * @param customerId
//     * @return
//     */
//    private ResultModel<TaskResult> getResultModel(long customerId){
//        TaskResult result = new TaskResult();
//        StringBuffer context = new StringBuffer();
//        context.append("语音留言记录");
//        ResultModel<TaskResult> resultModel = new ResultModel<TaskResult>();
//        try {
//
//            List<ProductDeviceLogVo> productDeviceLogList  = findSpeechContentList();
//            //没有设备
//            if(null == productDeviceLogList){
//                context = new StringBuffer();
//                context.append("您还没有智能设备");
//            }else if(productDeviceLogList.size() == 0 ){
//                context = new StringBuffer();
//                context.append("没有语音留言");
//            }else {
//                context.append(assembleSpeechContent(productDeviceLogList));
//            }
//            logger.info("返回信息：" + context.toString());
//            result.setReply(context.toString());
//            resultModel.setReturnCode("0");
//            result.setResultType(ResultType.RESULT);
//            resultModel.setReturnValue(result);
//        } catch (Exception e) {
//            resultModel.setReturnCode("-1");
//            resultModel.setReturnErrorSolution(e.getMessage());
//        }
//        logger.info("resultModel" + resultModel);
//        return resultModel;
//    }
//}
