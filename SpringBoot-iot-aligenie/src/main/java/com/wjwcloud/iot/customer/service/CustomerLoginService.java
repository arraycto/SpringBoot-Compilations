package com.wjwcloud.iot.customer.service;


import com.wjwcloud.iot.customer.vo.CustomerVo;

import java.util.Map;

public interface CustomerLoginService {

    /**
     * 用户登录
     * @return
     */
//    String login(Map<String, Object> params) throws Exception ;


    /**
     * 用户登录
     * @param customerVo
     * @return
     */
//    String login(CustomerVo customerVo) throws Exception;
    /**
     * 公司用户登录
     * @param userName
     * @param password
     * @return
     */
//    String companyLogin(String userName, String password) throws Exception;

    /**
     * 公司用户登录
     * @param customerVo
     * @return
     */
//    String companyLogin(CustomerVo customerVo) throws Exception;

    /**
     * 天猫精灵用户登录
     * @return
     */
    String aligenieLogin(Map<String, Object> params) throws Exception ;
}
