package com.wjwcloud.iot.product.service;


import com.wjwcloud.iot.model.IBaseService;
import com.wjwcloud.iot.product.entity.ProductDevice;
import com.wjwcloud.iot.product.vo.ProductDeviceVo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ProductDeviceService extends IBaseService<ProductDeviceVo, ProductDevice> {

//    /**
//    * 根据Map条件查询单条记录
//    */
//    ProductDeviceVo findOne4Map(Map<String, Object> params) throws Exception;
//
//    /**
//     * 创建二维码
//     */
//    String createDeviceQrCode(Map<String, Object> params) throws Exception;
//
//    /**
//     * 批量删除
//     */
//    int batchDeleteProductDevice(Map<String, Object> params) throws Exception;
//
//    /**
//     * 批量添加设备
//     */
////    ImportRecord register(Map<String, Object> params) throws Exception;
//
//    /**
//     * 导入设备
//     */
////    ImportRecord importDeviceKeys(InputStream is, String category, String prodictId) throws Exception;
//
//    /**
//     * 导入设备（不做保存动作-仅公租房管理系统用）
//     */
//    List<ProductDeviceVo> importDevice(InputStream is, String category) throws Exception;
//
//    /**
//     * 根据房间id 查询房间设备列表
//     */
//    List<ProductDeviceVo> findProductDeviceListByRoomId(Map<String, Object> params) throws Exception;
//
//    /**
//     * 根据设备ID查询关联的设备列表
//     */
//    List<ProductDeviceVo> findListByAssocDeviceId(Map<String, Object> params) throws Exception;
//
//    /**
//     * 根据设备ID查询关联的设备列表
//     */
//    List<ProductDeviceVo> findListByUpgradeVersionId(Map<String, Object> params) throws Exception;
//
//    /**
//     * 根据条件查询设备数量
//     */
//    int deviceCountByToDay(Map<String, Object> params) throws Exception;
//
//    /**
//     * 通过houseId查询设备列表
//     *
//     * @param params
//     * @return
//     */
//    List<ProductDeviceVo> findListByHouseId(Map<String, Object> params) throws Exception;

    /**
     * 查询家庭所有列表
     *
     * @param params
     * @return
     * @throws Exception
     */
    List<ProductDevice> findListOfCustomer(Map<String, Object> params) throws Exception;

//    /**
//     * 共享设备列表
//     *
//     * @param params
//     * @return
//     */
//    List<ProductDeviceVo> findSharedDeviceList(Map<String, Object> params) throws Exception;
//
//    /**
//     * 根据imei创建猫眼设备
//     * @param imei
//     * @throws Exception
//     */
//    long createDevice4Peephole(String imei) throws Exception;
//
//    /**
//     * @param
//     * @return
//     * @Description 根据imei创建锁设备
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/4/18 11:10
//     */
//    long createDevice4Lock(String imei, String productCode) throws Exception;
//
//    /**
//     * @param deviceVo NB设备
//     * @return
//     * @Description 创建线程控制NB在线睡眠
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/4/27 21:21
//     */
//    void executeResetNBSleepTime(ProductDeviceVo deviceVo) throws Exception;
//
//    /**
//     * @param
//     * @return
//     * @description 查询产品型号
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/6/25 15:24
//     */
//    List<Map> findProductModel(Map<String, Object> params) throws Exception;
//
//
//    /**
//     * 绑定
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    long updBindingStatus(Map<String, Object> params) throws Exception;
//
//    /**
//     * 解绑
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    long updUntyingStatus(Map<String, Object> params) throws Exception;
}
