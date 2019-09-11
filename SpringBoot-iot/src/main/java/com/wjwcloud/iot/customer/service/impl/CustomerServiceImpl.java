package com.wjwcloud.iot.customer.service.impl;

import com.wjwcloud.iot.customer.entity.Customer;
import com.wjwcloud.iot.customer.mapper.CustomerMapper;
import com.wjwcloud.iot.customer.service.CustomerService;
import com.wjwcloud.iot.customer.vo.CustomerVo;
import com.wjwcloud.iot.model.AbstructBaseEntity;
import com.wjwcloud.iot.model.ResultResponse;
import com.wjwcloud.iot.model.page.PageBean;
import com.wjwcloud.iot.utils.redis.AbstructRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("customerServiceImpl")
@Transactional
public class CustomerServiceImpl extends AbstructRedisService<CustomerVo, Customer> implements CustomerService {

    /**
     * 添加一个日志器
     */
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerMapper customerMapper;

//    @Resource(name="customerAddressServiceImpl")
//    private CustomerAddressService customerAddressService;

//    @Autowired
//    private SysDistrictFeignService districtFeignService;

//    @Resource (name = "companyRoleServiceImpl")
//    private CompanyRoleService companyRoleService;

//	/**
//	 * 保存
//	 *
//	 * @param entity
//	 * @return int
//	 */
//	@Override
//	public long save(Customer entity) throws Exception {
//		if (entity.getId() != null && entity.getId() != 0) {
//			updateByPrimaryKey(entity);
//		} else {
//            entity.setCode(entity.getName());
//            // 设置登录key
//            UUID uuid = UUID.randomUUID();
////            entity.setLoginKey(uuid.toString());
//            //生成盐值
//            String saltVal = SecretKeyUtils.getNewPsw();
//            String verifyCode = SecretKeyUtils.getVerifyCode(12);
//            //生成加密后的登陆密码
//            String pwd = SecretKeyUtils.getStoreLogpwd(entity.getCode(), entity.getPassword(), saltVal);
//            //设置用户的相关信息
//
//            entity.setSaltValue(saltVal);
//            entity.setPassword(pwd);
//			entity.setIsDeleted(0);
//			entity.setStatus(1);
//			entity.setVersion(1);
//			entity.setLoginErrorCount(0);
//			entity.setModifyTime(DateUtils.getTimestamp());
//            entity.setModifier(JwtSecurityUtils.getUserId());
//            entity.setCreator(JwtSecurityUtils.getUserId());
//            if(entity.getCreateTime() == null) {
//			    entity.setCreateTime(DateUtils.getTimestamp());
//			}
//			entity.setCreator(JwtSecurityUtils.getUserId());
//			int result = customerMapper.insert(entity);
//			long id = customerMapper.selectLastId();
//			entity.setId(id);
//		}
//
//		deleteCache(Customer.TABLE_NAME + entity.getId());
//
//		return entity.getId();
//	}
//
//	/**
//     * 保存
//     *
//     * @param entity
//     * @return int
//     */
//    @Override
//    public long saveSelective(Customer entity) throws Exception {
//        if (entity.getId() != null && entity.getId() != 0) {
//            updateByPrimaryKeySelective(entity);
//        } else {
//            entity.setIsDeleted(0);
//            entity.setStatus(0);
//            entity.setModifyTime(DateUtils.getTimestamp());
//            entity.setCreator(JwtSecurityUtils.getUserId());
//            entity.setModifyTime(DateUtils.getTimestamp());
//            entity.setModifier(JwtSecurityUtils.getUserId());
//            if(entity.getCreateTime() == null) {
//                entity.setCreateTime(DateUtils.getTimestamp());
//            }
//            entity.setCreator(JwtSecurityUtils.getUserId());
//            int result = customerMapper.insertSelective(entity);
//            long id = customerMapper.selectLastId();
//            entity.setId(id);
//        }
//
//        deleteCache(Customer.TABLE_NAME + entity.getId());
//
//        return entity.getId();
//    }
//
//	@Override
//	public long updateByPrimaryKeySelective(Customer entity) {
//	    deleteCache(Customer.TABLE_NAME + entity.getId());
//
//		entity.setModifyTime(DateUtils.getTimestamp());
//		entity.setModifier(JwtSecurityUtils.getUserId());
//        entity.setModifyTime(DateUtils.getTimestamp());
//        entity.setModifier(JwtSecurityUtils.getUserId());
//		return customerMapper.updateByPrimaryKeySelective(entity);
//	}
//
//	@Override
//    public long updateByPrimaryKey(Customer entity) {
//        deleteCache(Customer.TABLE_NAME + entity.getId());
//
//        //生成加密后的登陆密码d
//        String pwd = SecretKeyUtils.getStoreLogpwd(entity.getCode(), entity.getPassword(), entity.getSaltValue());
//
//        entity.setPassword(pwd);
//        entity.setModifyTime(DateUtils.getTimestamp());
//        entity.setModifier(JwtSecurityUtils.getUserId());
//        entity.setModifyTime(DateUtils.getTimestamp());
//        entity.setModifier(JwtSecurityUtils.getUserId());
//        entity.setCreator(JwtSecurityUtils.getUserId());
//        return customerMapper.updateByPrimaryKeySelective(entity);
//    }
//
//	@Override
//	public long deleteByPrimaryKey(Long id) throws Exception {
//	    deleteCache(Customer.TABLE_NAME + id);
//
//		long result = customerMapper.deleteByPrimaryKey(id);
//
//		return result;
//	}
//
//	@Override
//	public CustomerVo selectByPrimaryKey(Long id) throws Exception {
//	    if (redisProxy.getCacheOpen()) {
//            CustomerVo vo = readCache(Customer.TABLE_NAME + id);
//            if (vo != null) {
//                return vo;
//            }
//        }
//
//		Customer entity = customerMapper.selectByPrimaryKey(id);
//
//		if(entity == null) {
//            return null;
//        }
//
//		CustomerVo vo = assembleVo(entity);
//		saveCache(Customer.TABLE_NAME + vo.getId(), vo);
//
//		return vo;
//	}
//
//    @Override
//    public PageBean findList4Map(Map<String, Object> params, PageBean pageBean) throws Exception {
//        pageBean.setRows(customerMapper.findCount(params));
//        setRowNum(params, pageBean);
//
//        if (redisProxy.getCacheOpen()) {
//            List<String> fieldList = new ArrayList<String>();
//            fieldList.add("id");
//            params.put("fieldList", fieldList);
//            List<AbstructBaseEntity> baseList = customerMapper.findFieldList(params);
//            pageBean.setList(readCache(baseList));
//        } else {
//            //查询数据，将数据放入到分页对象中
//            pageBean.setList(assembleVoList(customerMapper.findList(params)));
//        }
//
//        return pageBean;
//    }

//    @Override
//    public PageBean findCustomerPageBean(Map<String, Object> params, PageBean pageBean) throws Exception {
//        pageBean.setRows(customerMapper.findCustomerCount(params));
//        setRowNum(params, pageBean);
//
//        if (redisProxy.getCacheOpen()) {
//            List<String> fieldList = new ArrayList<String>();
//            fieldList.add("id");
//            params.put("fieldList", fieldList);
//            List<AbstructBaseEntity> baseList = customerMapper.findCustomerFieldList(params);
//            pageBean.setList(readCache(baseList));
//        } else {
//            //查询数据，将数据放入到分页对象中
//            pageBean.setList(assembleVoList(customerMapper.findCustomerList(params)));
//        }
//
//        return pageBean;
//    }

    @Override
    public List<CustomerVo> findList4Map(Map<String, Object> params) throws Exception {
        if (redisProxy.getCacheOpen()) {
            List<String> fieldList = new ArrayList<String>();
            fieldList.add("id");
            params.put("fieldList", fieldList);
            List<AbstructBaseEntity> baseList = customerMapper.findFieldList(params);
            return readCache(baseList);
        } else {
            return assembleVoList(customerMapper.findList(params));
        }
    }

    @Override
    public CustomerVo findOne4Map(Map<String, Object> params) throws Exception {
        List<CustomerVo> list = findList4Map(params);
        if(!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    @Override
    protected List<AbstructBaseEntity> findList4Ids(List<Object> ids) throws Exception {
        return customerMapper.findListByIds(ids);
    }

    /**
     * 根据设备ID查询设备关联的管理员会员信息
     *
     */
//    @Override
//    public CustomerVo selectCustomerByDeviceId(Map<String, Object> paramMap) throws Exception {
//        return assembleVo(customerMapper.selectCustomerByDeviceId(paramMap));
//    }

    /**
     * 根据房屋ID查询管理员会员信息
     *
     */
//    @Override
//    public CustomerVo selectCustomerByHouseId(Map<String, Object> paramMap) throws Exception {
//        return assembleVo(customerMapper.selectCustomerByHouseId(paramMap));
//    }

    /**
     * 查询today新增会员的数量
     */
//    @Override
//    public int customerCountByToDay(Map<String, Object> params) throws Exception {
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        params.put(CustomerField.CREATE_TIME,formatter.format(date));
//        return customerMapper.findCount(params);
//    }
//
//    @Override
//    public List<CustomerVo> findFieldList(List<String> fieldList, Map<String, Object> params) throws Exception {
//        List<CustomerVo> voList = new ArrayList<CustomerVo>();
//
//        params.put("fieldList", fieldList);
//        List<AbstructBaseEntity> entityList = customerMapper.findFieldList(params);
//        for (AbstructBaseEntity abstructBaseEntity : entityList) {
//            Customer entity = (Customer) abstructBaseEntity;
//            voList.add(assembleVo(entity));
//        }
//        return voList;
//    }
//
    protected List<CustomerVo> assembleVoList(List<Customer> entityList) throws Exception {
        List<CustomerVo> voList = new ArrayList<CustomerVo>();

        for (Customer entity : entityList) {
            voList.add(assembleVo(entity));
        }

        return voList;
    }

	/**
	 * 拼装VO
	 *
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@Override
    public CustomerVo assembleVo(Customer entity) throws Exception {
	    if(entity == null) {
            return null;
         }

		CustomerVo vo = new CustomerVo();
//		CustomerAssembler.assembleVo(vo, entity);
        BeanUtils.copyProperties(entity,vo);

//		Map<String,Object> customerAddressMap=new HashMap<>();
//		customerAddressMap.put("customerId",vo.getId());
//        CustomerAddressVo customerAddressVo=customerAddressService.findOne4Map(customerAddressMap);
        //获取用户角色列表
//        List<CompanyRoleVo> roles = companyRoleService.selectRoleByCustomerId(entity.getId());
//        if (roles != null) {
//            vo.setRoleNames(roles.stream().map(e -> e.getName()).collect(Collectors.joining(",")));//设置用户角色
//            vo.setRoleIds(roles.stream().map(e -> e.getId() + "").collect(Collectors.joining(",")));//设置用户角色ID
//        }


//        if(customerAddressVo!=null){
//            //查询省名称
//            Map<String,Object> districtMap=new HashMap<>();
//            districtMap.put("id",customerAddressVo.getProvinceId());
//            ResultResponse provinceDetail=districtFeignService.detail(districtMap);
//            Map<String,Object> province=(HashMap)provinceDetail.getData();
//            vo.setProvinceName(province==null?"":province.get("name").toString());
//            //查询市名称
//            districtMap.put("id",customerAddressVo.getCityId());
//            ResultResponse cityDetail=districtFeignService.detail(districtMap);
//            Map<String,Object> city=(HashMap)cityDetail.getData();
//            vo.setCityName(city==null?"":city.get("name").toString());
//            //查询区域名称
//            districtMap.put("id",customerAddressVo.getDistrictId());
//            ResultResponse districtDetail=districtFeignService.detail(districtMap);
//            Map<String,Object> district=(HashMap)districtDetail.getData();
//            vo.setDistrictName(district==null?"":district.get("name").toString());
//            //查询街道名称
//            districtMap.put("id",customerAddressVo.getStreetId());
//            ResultResponse streetDetail=districtFeignService.detail(districtMap);
//            Map<String,Object> street=(HashMap)streetDetail.getData();
//            vo.setStreetName(street==null?"":street.get("name").toString());
//            //详细地址
//            vo.setAddress(customerAddressVo.getAddress());
//            vo.setCustomerAddressVo(customerAddressVo==null?null:customerAddressVo);
//        }
		return vo;
	}


//更改用户密码
//    @Override
//    public long changePassword(HttpServletRequest request, CustomerVo customerVo,
//                               String oldPassword,
//                               String newPassword,
//                               String newPasswordAgain) {
//        try {
//            //加密后的密码
//            oldPassword=SecretKeyUtils.getStoreLogpwd(customerVo.getName(), oldPassword, customerVo.getSaltValue());
//            newPassword=SecretKeyUtils.getStoreLogpwd(customerVo.getName(), newPassword, customerVo.getSaltValue());
//            newPasswordAgain=SecretKeyUtils.getStoreLogpwd(customerVo.getName(), newPasswordAgain, customerVo.getSaltValue());
//            // 两次密码输入不一致
//            if (!newPassword.equals(newPasswordAgain)) {
//
//                return 2;
//
//            }
//
//            Map<String, Object> paramMap = new HashMap<String, Object>();
//            paramMap.put("name", customerVo.getName());
//            paramMap.put("password", oldPassword);
//
//            CustomerVo userVo = findOne4Map(paramMap);
//
//            // 原密码输入错误
//            if (userVo == null) {
//
//                return 3;
//
//            }
//
//            paramMap.put("password", newPassword);
//
//            userVo = findOne4Map(paramMap);
//
//            // 新旧密码一致
//            if (userVo != null) {
//
//                return 4;
//
//            }
//
////            sysUser.setUserName(sysUser.getUserName());
//            customerVo.setPassword(newPassword);
//            // 修改密码，1成功，0失败
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Customer customer=new Customer();
//        CustomerAssembler.assembleEntity(customer,customerVo);
//        return updateByPrimaryKeySelective(customer);
//
//    }
//
//    /**
//     * 验证用户名密码
//     * wfj 2017-09-08
//     *
//     * @return SysUser statusName  success=成功，其它= 失败  ，keyName =错误信息
//     */
//    @Override
//    public boolean checkSysUserByPwd(Map<String, Object> params) throws Exception {
//        //查询当前登录用户信息
//        CustomerVo customerVo = selectByPrimaryKey(JwtSecurityUtils.getUserId());
//        String saltValue = customerVo.getSaltValue();
//        String userName = customerVo.getName();    //登录过来的用户名
//        String password = params.get("userPwd").toString();
//        //根据用户信息生成密码
//        String encodePwd = SecretKeyUtils.getStoreLogpwd(String.valueOf(customerVo.getCode()), password, customerVo.getSaltValue());
//        //判断密码是否有效
//        if(customerVo.getPassword().equals(encodePwd)){
//            return true;
//        }else{
//            throw new RuntimeException("您输入的密码错误，请重新输入");
//        }
//    }
//
//    /**
//     * 共享设备会员列表
//     */
//    @Override
//    public List<CustomerVo> findListBySharedDeviceId(Map<String, Object> params) throws Exception {
//        List<Customer> customerList = customerMapper.findListBySharedDeviceId(params);
//        List<CustomerVo> customerVoList = new ArrayList<>();
//        for (Customer customer : customerList) {
//            CustomerVo customerVo = CustomerAssembler.assembleVo(customer);
//            customerVoList.add(customerVo);
//        }
//        return customerVoList;
//    }
}
