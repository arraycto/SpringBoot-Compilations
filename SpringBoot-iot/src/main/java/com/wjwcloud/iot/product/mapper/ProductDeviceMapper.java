package com.wjwcloud.iot.product.mapper;


import com.wjwcloud.iot.model.AbstructBaseEntity;
import com.wjwcloud.iot.product.entity.ProductDevice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Mapper
 *
 */
 @Mapper
public interface ProductDeviceMapper {
//    /**
//     * 删除记录
//     *
//     * @param id 删除记录 {@link Long }
//     * @return int {@link Integer }
//     */
//    int deleteByPrimaryKey(Long id);
//
//    /**
//     * 添加记录
//     *
//     * @param entity {@link ProductDevice}
//     * @return int {@link Integer }
//     */
//    int insert(ProductDevice entity);
//
//    /**
//     * 查询ID
//     *
//     * @return int {@link long }
//     */
//    long selectLastId();
//
//    /**
//     * 添加记录，只插入有值的字段
//     *
//     * @param entity {@link ProductDevice}
//     * @return int {@link Integer }
//     */
//    int insertSelective(ProductDevice entity);
//
//    /**
//     * 更新记录，只更新有值的字段
//     *
//     * @param entity {@link ProductDevice}
//     * @return int {@link Integer }
//     */
//    int updateByPrimaryKeySelective(ProductDevice entity);
//
//    /**
//     * 根据ID获取记录
//     *
//     * @param id 记录ID {@link Long}
//     * @return ProductDevice {@link ProductDevice}
//     */
//    ProductDevice selectByPrimaryKey(Long id);
//
//    /**
//     * 按条件查询总条数
//     */
//    int findCount(ProductDevice entity);
//
//    /**
//     * 按条件查询总条数
//     */
//    int findCount(Map<String, Object> params);
//
//    /**
//     * 根据ID修改记录
//     *
//     * @param entity 记录 {@link ProductDevice}
//     * @return int {@link Integer }
//     */
//    int updateByPrimaryKey(ProductDevice entity);
//
//
//    /**
//     * 查询列表
//     *
//     * @param entity
//     * @return
//     */
//    public List<ProductDevice> findList(ProductDevice entity);
//
//    /**
//     * 查询列表
//     *
//     * @param params
//     * @return
//     */
//    public List<ProductDevice> findList(Map<String, Object> params);
//
//    /**
//     * 查询需要查询的字段列表
//     *
//     * @param params
//     * @return
//     */
//    public List<AbstructBaseEntity> findFieldList(Map<String, Object> params);
//
    /**
     * 根据ID集合查询
     *
     * @param ids
     * @return
     */
    public List<AbstructBaseEntity> findListByIds(List<Object> ids);
//
//    /**
//     *根据房间id 查询房间设备列表
//     * @return
//     */
//    List<ProductDevice> findProductDeviceListByRoomId(Map<String, Object> params);
//
//    /**
//     * 根据家庭id 查询房间设备列表
//     */
//    List<ProductDevice> findListByFamilyId(Map<String, Object> params);
//
//    /**
//     * 通过成员id 查询设备秘钥列表
//     */
//    List<ProductDevice> findListByMemberId(Map<String, Object> params);
//
//    /**
//     * 通过customerId查询所有关联的设备列表
//     * @param params
//     * @return
//     */
//    ProductDevice findListByCustomerId(Map<String, Object> params);
//
//    /**
//     * 根据设备ID查询关联的设备列表
//     */
//    List<ProductDevice> findListByAssocDeviceId(Map<String, Object> params);
//
//    /**
//     * 根据设备ID查询关联的设备列表
//     */
//    List<ProductDevice> findListByUpgradeVersionId(Map<String, Object> params);
//
//
//    /**
//     * 通过houseId查询设备列表
//     *
//     * @param params
//     * @return
//     */
//    List<ProductDevice> findListByHouseId(Map<String, Object> params);

    /**
     * 查询当前会员的所有设备
     *
     * @param params
     * @return
     */
    List<ProductDevice> findListOfCustomer(Map<String, Object> params);


    /**
     * 共享设备列表
     *
     * @param params
     * @return
     */
    List<ProductDevice> findSharedDeviceList(Map<String, Object> params);

    /**
     * @param
     * @return
     * @description 查询产品型号
     * @author ZCZ
     * @version 1.0
     * @date 2019/6/25 15:24
     */
    List<ProductDevice> findProductModel(Map<String, Object> params);

    /**
     * @param
     * @return
     * @description 根据IMEI及companyiD查询锁版信息
     * @author ZL
     * @version 1.0
     * @date 2019/7/10 15:24
     */
    List<ProductDevice> findDeviceListByIMEI(Map<String, Object> params);
    
    /**
     * 查询nb版imei码
     * @param params
     * @return
     */
    List<ProductDevice> findDeviceNbIMEI(Map<String, Object> params);
}
