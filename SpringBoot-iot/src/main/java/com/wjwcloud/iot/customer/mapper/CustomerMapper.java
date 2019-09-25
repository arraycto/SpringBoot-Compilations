package com.wjwcloud.iot.customer.mapper;

import com.wjwcloud.iot.customer.entity.Customer;
import com.wjwcloud.iot.model.AbstructBaseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Mapper
 *
 */
@Mapper
public interface CustomerMapper {
    /**
     * 删除记录
     *
     * @param id 删除记录 {@link Long }
     * @return int {@link Integer }
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 添加记录
     *
     * @param entity {@link Customer}
     * @return int {@link Integer }
     */
    int insert(Customer entity);

    /**
     * 查询ID
     *
     * @return int {@link long }
     */
    long selectLastId();

    /**
     * 添加记录，只插入有值的字段
     *
     * @param entity {@link Customer}
     * @return int {@link Integer }
     */
    int insertSelective(Customer entity);

    /**
     * 更新记录，只更新有值的字段
     *
     * @param entity {@link Customer}
     * @return int {@link Integer }
     */
    int updateByPrimaryKeySelective(Customer entity);

    /**
     * 根据ID获取记录
     *
     * @param id 记录ID {@link Long}
     * @return Customer {@link Customer}
     */
    Customer selectByPrimaryKey(Long id);

    /**
     * 按条件查询总条数
     */
    int findCount(Customer entity);

    /**
     * 按条件查询总条数
     */
    int findCount(Map<String, Object> params);

    /**
     * 根据ID修改记录
     *
     * @param entity 记录 {@link Customer}
     * @return int {@link Integer }
     */
    int updateByPrimaryKey(Customer entity);


    /**
     * 查询列表
     *
     * @param entity
     * @return
     */
    public List<Customer> findList(Customer entity);

    /**
     * 按条件查询总条数
     */
    int findCustomerCount(Map<String, Object> params);

    /**
     *
     *查询需要查询的字段列表
     * @param params
     * @return
     */
    public List<AbstructBaseEntity> findCustomerFieldList(Map<String, Object> params);
    /**
     * 查询列表
     *
     * @param params
     * @return
     */
    public List<Customer> findList(Map<String, Object> params);

    /**
     * 查询列表
     *
     * @param params
     * @return
     */
    public List<Customer> findCustomerList(Map<String, Object> params);

    /**
     * 查询需要查询的字段列表
     *
     * @param params
     * @return
     */
    public List<AbstructBaseEntity> findFieldList(Map<String, Object> params);

    /**
     * 根据ID集合查询
     *
     * @param ids
     * @return
     */
    public List<AbstructBaseEntity> findListByIds(List<Object> ids);

    /**
     * 根据设备ID查询设备关联的管理员会员信息
     *
     */
    Customer selectCustomerByDeviceId(Map<String, Object> paramMap);

    /**
     * 根据房屋ID查询管理员会员信息
     *
     */
    Customer selectCustomerByHouseId(Map<String, Object> paramMap);

    /**
     * 共享设备会员列表
     *
     * @param params
     * @return
     */
    List<Customer> findListBySharedDeviceId(Map<String, Object> params);
}
