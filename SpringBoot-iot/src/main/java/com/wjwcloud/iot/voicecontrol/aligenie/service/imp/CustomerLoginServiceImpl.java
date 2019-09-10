//package com.wjwcloud.iot.voicecontrol.aligenie.service.imp;
//
//package com.geer2.zwow.iot.customer.service.impl;
//
//import com.geer2.base.jwt.JwtAudience;
//import com.geer2.base.jwt.JwtHelper;
//import com.geer2.base.utils.Logger;
//import com.geer2.base.utils.key.SecretKeyUtils;
//import com.geer2.base.utils.redis.RedisProxy;
//import com.geer2.zwow.iot.company.entity.Company;
//import com.geer2.zwow.iot.company.mapper.CompanyMapper;
//import com.geer2.zwow.iot.company.service.CompanyService;
//import com.geer2.zwow.iot.company.vo.CompanyVo;
//import com.geer2.zwow.iot.customer.assembler.CustomerAssembler;
//import com.geer2.zwow.iot.customer.entity.Customer;
//import com.geer2.zwow.iot.customer.service.CustomerLoginService;
//import com.geer2.zwow.iot.customer.service.CustomerService;
//import com.geer2.zwow.iot.customer.vo.CustomerVo;
//import com.geer2.zwow.iot.gateway.telecom.TelecomDeviceManageServiceImpl;
//import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service("customerLoginServiceImpl")
//@Transactional
//public class CustomerLoginServiceImpl extends CustomerServiceImpl implements CustomerLoginService {
//
//    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CustomerLoginServiceImpl.class);
//
//    @Resource(name = "customerServiceImpl")
//    private CustomerService customerService;
//
//    @Autowired
//    private CompanyMapper companyMapper;
//
//    @Autowired
//    private Environment env;
//
//    @Autowired
//    private RedisProxy redisProxy;
//
//    /**
//     * 用户登录
//     * @return
//     */
//    @Override
//    public String login(Map<String,Object> params) throws Exception {
//        String userName = params.get("userName").toString();
//        String password = params.get("password").toString();
//
//        Map<String,Object> resourceMap = new HashMap<>();
//        resourceMap.put("name",userName);
//        resourceMap.put("isDeleted", "0");
//        CustomerVo customerVo = customerService.findOne4Map(resourceMap);
//
//        if(customerVo == null){
//            //查找手机号
//            resourceMap = new HashMap<>();
//            resourceMap.put("isDeleted","0");
//            resourceMap.put("mobilePhone",userName);
//            customerVo = customerService.findOne4Map(resourceMap);
//            if(customerVo == null) {
//                throw new Exception("没有找到对应的用户名");
//            }
//        }
//
//        Customer entity ;
//
//        //根据规则加密密码
//        String encodePwd = SecretKeyUtils.getStoreLogpwd(String.valueOf(customerVo.getCode()), password, customerVo.getSaltValue());
//        if (!customerVo.getPassword().equals(encodePwd)) {
//
//            //登录错误次数
//            entity = new Customer();
//            entity.setLoginErrorCount(customerVo.getLoginErrorCount()+1);
//            entity.setId(customerVo.getId());
//            updateByPrimaryKeySelective(entity);
//
//            throw new Exception("登录密码不正确");
//        }
//
//        //app平台 ios ad
//        entity = new Customer();
//        Object appPlatform = params.get("appPlatform");
//        entity.setId(customerVo.getId());
//        entity.setAppPlatform(appPlatform.toString());
//
//        if(params.containsKey("appClientId")){
//
//            Object appClientId = params.get("appClientId");
//
//            if(appClientId == null || "".equals(appClientId)){
//                logger.info("appClientId参数为空");
//            }else {
//                entity.setAppClientId(appClientId.toString());
//
//            }
//        }
//        updateByPrimaryKeySelective(entity);
//
//        return login(customerVo);
//    }
//
//
//    /**
//     * 用户登录
//     * @param customerVo
//     * @return
//     */
//    @Override
//    public String login(CustomerVo customerVo) throws Exception {
//        JwtAudience jwtAudience = new JwtAudience();
//        jwtAudience.setUserId(customerVo.getId().toString());
//        jwtAudience.setUserName(URLEncoder.encode(customerVo.getName()));
//        jwtAudience.setIssuer(env.getProperty("jwt.client-id"));
//        jwtAudience.setAudience(env.getProperty("jwt.secret-key"));
//        jwtAudience.setSecretKey(env.getProperty("jwt.secret-key"));
//        //有效期为365天
//        jwtAudience.setTtlMillis(365L * 24 * 60 *  60 * 1000);
//
//        return JwtHelper.createJWT(jwtAudience);
//    }
//
//
//    /**
//     * 公司用户登录
//     * @param userName
//     * @param password
//     * @return
//     */
//    @Override
//    public String companyLogin(String userName, String password) throws Exception{
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", userName);
//        params.put("isDeleted", "0");
//        CustomerVo customerVo = customerService.findOne4Map(params);
//        if(customerVo == null){
//            throw new RuntimeException("账号不存在，请重新登录");
//        }
//        Map companyParams = new HashMap();
//        companyParams.put("customerId" ,customerVo.getId());
//        List<Company> companyVoList = companyMapper.findList(companyParams);
//        if(companyVoList.size()>0){
//            Company companyVo = companyVoList.get(0);
//            if(companyVo.getIsDeleted() == 1){
//                throw new RuntimeException("该公司没有权限登录，请联系管理员");
//            }else if (companyVo.getStatus() == 0){
//                throw new RuntimeException("该公司已被冻结，请联系管理员");
//            }
//        }
//        //根据规则加密密码
//        String encodePwd = SecretKeyUtils.getStoreLogpwd(String.valueOf(customerVo.getCode()), password, customerVo.getSaltValue());
//        if (!customerVo.getPassword().equals(encodePwd)) {
//            throw new Exception("登录密码不正确");
//        }
//        return companyLogin(customerVo);
//    }
//
//    /**
//     * 公司用户登录
//     * @param customerVo
//     * @return
//     */
//    @Override
//    public String companyLogin(CustomerVo customerVo) throws Exception {
//        JwtAudience jwtAudience = new JwtAudience();
//        jwtAudience.setUserId(customerVo.getId().toString());
//        jwtAudience.setCompanyId(customerVo.getCompanyId().toString());
//        jwtAudience.setUserName(URLEncoder.encode(customerVo.getName()));
//        jwtAudience.setIssuer(env.getProperty("jwt.client-id"));
//        jwtAudience.setAudience(env.getProperty("jwt.secret-key"));
//        jwtAudience.setSecretKey(env.getProperty("jwt.secret-key"));
//        //有效期为7天
//        jwtAudience.setTtlMillis(7L * 24 * 60 *  60 * 1000);
//
//        return JwtHelper.createJWT(jwtAudience);
//    }
//
//    /**
//     * 天猫精灵登录用户登录
//     * @return
//     */
//    @Override
//    public String aligenieLogin(Map<String,Object> params) throws Exception {
//        String mobilePhone = params.get("mobilePhone").toString();
//        Map resourceMap = new HashMap();
//        resourceMap.put("mobilePhone" , mobilePhone);
//        resourceMap.put("isDeleted" , 0);
//        CustomerVo customerVo = customerService.findOne4Map(resourceMap);
//        if(null == customerVo){
////            Customer customer = new Customer();
////            customer.setMobilePhone(mobilePhone);
////            long customerId = createCustomerInformation(customer);
////            customerVo = customerService.selectByPrimaryKey(customerId);
//            logger.error("改手机号未注册");
//            throw new RuntimeException("改手机号未注册");
//        }
//
//        if("mobile".equals(params.get("loginType"))){
//            String verificationCode = params.get("verificationCode").toString();
//            Object object = redisProxy.read(mobilePhone);
//            if(null == object){
//                //验证码过期
//                logger.error("验证码过期");
//                throw new RuntimeException("验证码过期");
//            }else{
//                String verificationCodeValue  = (String)object;
//                if(!verificationCodeValue.equals(verificationCode)){
//                    //验证码输入错误，请重新输入
//                    logger.error("验证码输入错误，请重新输入");
//                    throw new RuntimeException("验证码输入错误，请重新输入");
//                }
//            }
//            redisProxy.delete(mobilePhone);
//        }else if("userPassword".equals(params.get("loginType"))){
//            //根据规则加密密码
//            String password = params.get("password").toString();
//            String encodePwd = SecretKeyUtils.getStoreLogpwd(String.valueOf(customerVo.getCode()), password, customerVo.getSaltValue());
//            Customer entity ;
//            if (!customerVo.getPassword().equals(encodePwd)) {
//                //登录错误次数
//                entity = new Customer();
//                entity.setLoginErrorCount(customerVo.getLoginErrorCount()+1);
//                entity.setId(customerVo.getId());
//                updateByPrimaryKeySelective(entity);
//                throw new Exception("登录密码不正确");
//            }
//        }
//
//        return login(customerVo);
//    }
//
//    /**
//     * 创建会员信息
//     * @param entity
//     * @param params
//     * @return
//     */
//    private long createCustomerInformation (Customer entity){
//        long customerId = 0L ;
//        //获取八位随机数
//        String randomStr = getRandomStr();
//        boolean isnotexists = true;
//        int max = 10 ;
//        int count = 0 ;
//        String name = "";
//        try {
//            while (isnotexists) {
//                if (count > max) {
//                    logger.error("获取用户名密码错误");
//                    throw new RuntimeException("获取用户名密码错误");
//                }
//                name = "";
//                int num = Integer.parseInt(randomStr.toString());
//                name = "ZW" + num;
//                Map map1 = new HashMap();
//                map1.put("status", 1);
//                map1.put("name", name);
//                CustomerVo customerVo = customerService.findOne4Map(map1);
//                if (null == customerVo) {
//                    isnotexists = false;
//                }
//                ++count;
//            }
//            String password = name.substring(4);
//            entity.setType("1");
//            entity.setStatus(1);
//            entity.setName(name);
//            entity.setPassword(password);
//            customerId = customerService.save(entity);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error("成员分享创建会员失败");
//            throw new RuntimeException("成员分享创建会员失败");
//        }
//        return customerId;
//    }
//
//    /**
//     获取八位随机数
//     */
//    protected String getRandomStr(){
//        int num = (int) ((Math.random() * 9 + 1) * 10000000);
//        return num + "";
//    }
//
//
//}
