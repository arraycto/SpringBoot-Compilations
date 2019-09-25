package com.wjwcloud.iot.customer.service;


import com.wjwcloud.iot.customer.entity.Customer;
import com.wjwcloud.iot.customer.vo.CustomerVo;
import com.wjwcloud.iot.model.IBaseService;
import com.wjwcloud.iot.model.page.PageBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface CustomerService extends IBaseService<CustomerVo, Customer> {

    /**
     * 根据Map条件查询单条记录
     */
    CustomerVo findOne4Map(Map<String, Object> params) throws Exception;

    /**
     * 修改会员密码
     */
//    long changePassword(HttpServletRequest request, CustomerVo customerVo, String oldPassword, String newPassword, String newPasswordAgain);

    /**
     * 根据设备ID查询设备关联的管理员会员信息
     */
//    CustomerVo selectCustomerByDeviceId(Map<String, Object> paramMap) throws Exception;

    /**
     * 根据房屋ID查询管理员会员信息
     */
//    CustomerVo selectCustomerByHouseId(Map<String, Object> paramMap) throws Exception;

    /**
     * 新增会员数量
     */
//    int customerCountByToDay(Map<String, Object> params) throws Exception;

    /**
     * 查询会员列表分页
     *
     * @param params
     * @param pageBean
     * @return
     * @throws Exception
     */
//    public PageBean findCustomerPageBean(Map<String, Object> params, PageBean pageBean) throws Exception;

    /**
     * 验证用户名密码
     * wfj 2017-09-08
     * */
//    boolean checkSysUserByPwd(Map<String, Object> params) throws Exception;

    /**
     * 共享设备会员列表
     */
//    List<CustomerVo> findListBySharedDeviceId(Map<String, Object> params) throws Exception;
}
