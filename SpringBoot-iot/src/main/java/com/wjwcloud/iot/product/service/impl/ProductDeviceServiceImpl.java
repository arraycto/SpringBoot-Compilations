package com.wjwcloud.iot.product.service.impl;


import com.wjwcloud.iot.model.AbstructBaseEntity;
import com.wjwcloud.iot.product.entity.ProductDevice;
import com.wjwcloud.iot.product.mapper.ProductDeviceMapper;
import com.wjwcloud.iot.product.service.ProductDeviceService;
import com.wjwcloud.iot.product.vo.ProductDeviceVo;
import com.wjwcloud.iot.utils.redis.AbstructRedisService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("productDeviceServiceImpl")
@Transactional
public class ProductDeviceServiceImpl extends AbstructRedisService<ProductDeviceVo, ProductDevice> implements ProductDeviceService {

    /**
     * 添加一个日志器
     */
    private static final Logger logger = LoggerFactory.getLogger(ProductDeviceServiceImpl.class);

    @Autowired
    private ProductDeviceMapper productDeviceMapper;

//    /**
//     * 产品
//     */
//    @Resource(name = "productServiceImpl")
//    private ProductService productService;
//
//    /**
//     * 分组
//     */
//    @Resource(name = "productDeviceGroupServiceImpl")
//    private ProductDeviceGroupService productDeviceGroupService;
//
//    /**
//     * 导入
//     */
//    @Resource(name = "exportExceptionProductExcelServiceImpl")
//    private ExportExceptionProductExcelService exportExceptionProductExcelService;
//
//    /**
//     * 向电信平台注册设备
//     */
//    @Resource(name = "telecomDeviceManageServiceImpl")
//    private TelecomDeviceManageServiceImpl telecomServiceImpl;
//
//    @Resource(name = "productDeviceImport")
//    private ProductDeviceImport productDeviceImport;
//
//    @Autowired
//    private SysCodeRuleFeignService sysCodeRuleFeignService;
//
//    @Resource(name = "productDeviceServiceImpl")
//    private ProductDeviceService productDeviceService;
//
//
//    @Resource(name = "qrtzScheduleJobServiceImpl")
//    private QrtzScheduleJobService qrtzScheduleJobService;

//    /**
//     * 保存
//     *
//     * @param entity
//     * @return int
//     */
//    @Override
//    public long save(ProductDevice entity) throws Exception {
//        if (entity.getId() != null && entity.getId() != 0) {
//            updateByPrimaryKeySelective(entity);
//        } else {
//            entity.setIsDeleted(0);
//            entity.setVersion(1);
//            entity.setSort(1);
//            if (entity.getIsActived() == null) {
//                //未激活
//                entity.setIsActived(0);
//            }
//            if (entity.getIsOnline() == null) {
//                //未注册
//                entity.setIsOnline(0);
//            }
//            if (entity.getCompanyId() == null) {
//                entity.setCompanyId(1L);
//            }
//            entity.setIsAbnormal(0);
//            //唯一标识
//            UUID uuid = UUID.randomUUID();
//            entity.setDeviceKey(uuid.toString());
//            entity.setModifyTime(DateUtils.getTimestamp());
//            entity.setModifier(JwtSecurityUtils.getUserId());
//            entity.setCreator(JwtSecurityUtils.getUserId());
//            if (entity.getCreateTime() == null) {
//                entity.setCreateTime(DateUtils.getTimestamp());
//            }
//            entity.setCreator(JwtSecurityUtils.getUserId());
//            int result = productDeviceMapper.insert(entity);
//            long id = productDeviceMapper.selectLastId();
//            entity.setId(id);
//        }
//
//        deleteCache(ProductDevice.TABLE_NAME + entity.getId());
//
//        return entity.getId();
//    }
//
//
//    /**
//     * 保存
//     *
//     * @param entity
//     * @return int
//     */
//    @Override
//    public long saveSelective(ProductDevice entity) throws Exception {
//        if (entity.getId() != null && entity.getId() != 0) {
//            updateByPrimaryKeySelective(entity);
//        } else {
//            entity.setIsDeleted(0);
//            entity.setIsDeleted(0);
//            entity.setStatus(0);
//            entity.setVersion(1);
//            entity.setSort(1);
//            entity.setModifyTime(DateUtils.getTimestamp());
//            entity.setModifier(JwtSecurityUtils.getUserId());
//            entity.setCreator(JwtSecurityUtils.getUserId());
//            if (entity.getCreateTime() == null) {
//                entity.setCreateTime(DateUtils.getTimestamp());
//            }
//            entity.setCreator(JwtSecurityUtils.getUserId());
//            int result = productDeviceMapper.insertSelective(entity);
//            long id = productDeviceMapper.selectLastId();
//            entity.setId(id);
//        }
//
//        deleteCache(ProductDevice.TABLE_NAME + entity.getId());
//
//        return entity.getId();
//    }
//
//    @Override
//    public long updateByPrimaryKeySelective(ProductDevice entity) {
//        deleteCache(ProductDevice.TABLE_NAME + entity.getId());
//
//        entity.setModifyTime(DateUtils.getTimestamp());
//        entity.setModifier(JwtSecurityUtils.getUserId());
//        entity.setCreator(JwtSecurityUtils.getUserId());
//        return productDeviceMapper.updateByPrimaryKeySelective(entity);
//    }
//
//    @Override
//    public long updateByPrimaryKey(ProductDevice entity) {
//        deleteCache(ProductDevice.TABLE_NAME + entity.getId());
//
//        entity.setModifyTime(DateUtils.getTimestamp());
//        entity.setModifier(JwtSecurityUtils.getUserId());
//        entity.setCreator(JwtSecurityUtils.getUserId());
//
//        //删除直连设备信息
////        deleteDevice();
//        return productDeviceMapper.updateByPrimaryKey(entity);
//
//    }
//
//
//    @Override
//    public long deleteByPrimaryKey(Long id) throws Exception {
//        deleteCache(ProductDevice.TABLE_NAME + id);
//
//        //向电信平台删除设备
//        ProductDeviceVo productDeviceVo = selectByPrimaryKey(id);
//        if (productDeviceVo.getThirdDeviceId() != null) {
//            telecomServiceImpl.deleteDevice(productDeviceVo.getThirdDeviceId());
//        }
//
//        long result = productDeviceMapper.deleteByPrimaryKey(id);
//
//
//        return result;
//    }
//
//    @Override
//    public ProductDeviceVo selectByPrimaryKey(Long id) throws Exception {
//        if (redisProxy.getCacheOpen()) {
//            ProductDeviceVo vo = readCache(ProductDevice.TABLE_NAME + id);
//            if (vo != null) {
//                //重置设备在线状态
//                resetDeviceIsOnline(vo);
//                return vo;
//            }
//        }
//
//        ProductDevice entity = productDeviceMapper.selectByPrimaryKey(id);
//
//        if (entity == null) {
//            return null;
//        }
//
//        ProductDeviceVo vo = assembleVo(entity);
//        saveCache(ProductDevice.TABLE_NAME + vo.getId(), vo);
//
//        return vo;
//    }
//
//    @Override
//    public PageBean findList4Map(Map<String, Object> params, PageBean pageBean) throws Exception {
//        //公租房系统用户查询此公租房可用设备信息
//        if (params.containsKey("BronzeCompanyID") && !"1".equals(params.get("BronzeCompanyID"))) {
//            params.put("companyId", JwtSecurityUtils.getCompanyId());
//        }
//        pageBean.setRows(productDeviceMapper.findCount(params));
//        setRowNum(params, pageBean);
//
//        if (redisProxy.getCacheOpen()) {
//            List<String> fieldList = new ArrayList<String>();
//            fieldList.add("id");
//            params.put("fieldList", fieldList);
//            List<AbstructBaseEntity> baseList = productDeviceMapper.findFieldList(params);
//            List<ProductDeviceVo> list = readCache(baseList);
//            //重置设备在线状态
//            resetDeviceIsOnline(list);
//            pageBean.setList(list);
//        } else {
//            //查询数据，将数据放入到分页对象中
//            pageBean.setList(assembleVoList(productDeviceMapper.findList(params)));
//        }
//
//        return pageBean;
//    }
//
//    @Override
//    public List<ProductDeviceVo> findList4Map(Map<String, Object> params) throws Exception {
//        if (redisProxy.getCacheOpen()) {
//            List<String> fieldList = new ArrayList<String>();
//            fieldList.add("id");
//            params.put("fieldList", fieldList);
//            List<AbstructBaseEntity> baseList = productDeviceMapper.findFieldList(params);
//            List<ProductDeviceVo> list = readCache(baseList);
//            //重置设备在线状态
//            resetDeviceIsOnline(list);
//            return list;
//        } else {
//
//            if (params.containsKey("deviceKeyList")) {
//                List deviceKeyList = (List) params.get("deviceKeyList");
//                params.put("deviceKeyList", deviceKeyList);
//            }
//
//            return assembleVoList(productDeviceMapper.findList(params));
//        }
//    }
//
//    @Override
//    public ProductDeviceVo findOne4Map(Map<String, Object> params) throws Exception {
//        List<ProductDeviceVo> list = findList4Map(params);
//        if (!list.isEmpty()) {
//            return list.get(0);
//        }
//
//        return null;
//    }
//
    @Override
    protected List<AbstructBaseEntity> findList4Ids(List<Object> ids) throws Exception {
        return productDeviceMapper.findListByIds(ids);
    }
//
//    @Override
//    public List<ProductDeviceVo> findFieldList(List<String> fieldList, Map<String, Object> params) throws Exception {
//        List<ProductDeviceVo> voList = new ArrayList<ProductDeviceVo>();
//
//        params.put("fieldList", fieldList);
//        List<AbstructBaseEntity> entityList = productDeviceMapper.findFieldList(params);
//        for (AbstructBaseEntity abstructBaseEntity : entityList) {
//            ProductDevice entity = (ProductDevice) abstructBaseEntity;
//            voList.add(assembleVo(entity));
//        }
//        return voList;
//    }
//
//    /**
//     * 通过houseId查询设备列表
//     *
//     * @param params
//     * @return
//     */
//    @Override
//    public List<ProductDeviceVo> findListByHouseId(Map<String, Object> params) throws Exception {
//        List<ProductDevice> deviceList = productDeviceMapper.findListByHouseId(params);
//        List<ProductDeviceVo> voList = new ArrayList<ProductDeviceVo>();
//
//        for (ProductDevice entity : deviceList) {
//            if (entity == null) {
//                return null;
//            }
//
//            ProductDeviceVo vo = new ProductDeviceVo();
//            ProductDeviceAssembler.assembleVo(vo, entity);
//            //重置设备在线状态
//            resetDeviceIsOnline(vo);
//            voList.add(vo);
//        }
//        return voList;
//    }
//
//    protected List<ProductDeviceVo> assembleVoList(List<ProductDevice> entityList) throws Exception {
//        List<ProductDeviceVo> voList = new ArrayList<ProductDeviceVo>();
//
//        for (ProductDevice entity : entityList) {
//            voList.add(assembleVo(entity));
//        }
//
//        return voList;
//    }
//
//    /**
//     * 拼装VO
//     *
//     * @param entity
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public ProductDeviceVo assembleVo(ProductDevice entity) throws Exception {
//        if (entity == null) {
//            return null;
//        }
//
//        ProductDeviceVo vo = new ProductDeviceVo();
//        ProductDeviceAssembler.assembleVo(vo, entity);
//
//        //产品
//        ProductVo productVo = productService.selectByPrimaryKey(vo.getProductId());
//        vo.setProductName(productVo == null ? "" : productVo.getName());
//        vo.setProductType(productVo == null ? "" : productVo.getType());
//        vo.setFrontImageUrl(productVo == null ? "" : productVo.getFrontImageUrl());
//        vo.setBackImageUrl(productVo == null ? "" : productVo.getBackImageUrl());
//        vo.setSideImageUrl(productVo == null ? "" : productVo.getSideImageUrl());
//        vo.setResetImageUrl(productVo == null ? "" : productVo.getResetImageUrl());
//
//        //分组 List
//        ProductDeviceGroupVo deviceGroupVo = productDeviceGroupService.selectByPrimaryKey(vo.getGroupId());
//        vo.setGroupName(deviceGroupVo == null ? "" : deviceGroupVo.getName());
//        vo.setIsSelect(false);
//
//        //重置设备在线状态
//        resetDeviceIsOnline(vo);
//
//        return vo;
//    }
//
//    /**
//     * 设备二维码
//     *
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public String createDeviceQrCode(Map<String, Object> params) throws Exception {
//        //二维码保存路径
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String imgPath = request.getSession().getServletContext().getRealPath("images");
//        //域名
//        String content = "https://www.baidu.com/?tn=02003390_2_hao_pg";
//        String imgName = "product-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".png";
//        String qrCodeImg = QRCodeUtil.getCreateQRCode(content, imgPath, imgName);
//
//        return "images" + qrCodeImg;
//    }
//
//
//    /**
//     * 批量删除
//     *
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public int batchDeleteProductDevice(Map<String, Object> params) throws Exception {
//        List<Map<String, Object>> maps = (List<Map<String, Object>>) params.get("data");
//        int result = 0;
//        for (Map<String, Object> map : maps) {
//            long id = Long.valueOf(map.get("id").toString());
//            result = productDeviceMapper.deleteByPrimaryKey(id);
//
//            deleteCache(ProductDevice.TABLE_NAME + id);
//        }
//        return result;
//    }
//
//    /**
//     * 批量注册
//     *
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public ImportRecord register(Map<String, Object> params) throws Exception {
//        //重复条数
//        List<ProductDevice> repeatList = null;
//        //异常条数 数据有问题
//        List<ProductDevice> exceptionList = null;
//        //成功条数
//        List<ProductDevice> successList = null;
//        //添加记录
//        ImportRecord importRecord = new ImportRecord();
//        repeatList = new ArrayList<>();
//        exceptionList = new ArrayList<>();
//        successList = new ArrayList<>();
//
//        List<String> imeiList = (ArrayList<String>) params.get("imeiList");
//
//        //根据产品id查询产品code
//        ProductVo productVo = productService.selectByPrimaryKey(Long.valueOf(params.get("productId").toString()));
//        if (productVo == null) {
//            logger.error("没有找到产品ID" + params.get("productId") + "的记录");
//            throw new RuntimeException("ID：" + Long.valueOf(params.get("productId").toString()) + "的产品不存在");
//        }
//        for (String imei : imeiList) {
//
//            params.put("code", productVo.getCode());
//            params.put("codePrefix", productVo.getCode());
//            params.put("number", 8);
//            ResultResponse resultResponse = sysCodeRuleFeignService.findList(params);//获取设备序号，设备序号不能为空
//
//            ProductDevice productDevice = new ProductDevice();
//            productDevice.setImei(imei);
//            productDevice.setProductId(Long.valueOf(params.get("productId").toString()));
//            //注册状态
//            productDevice.setStatus(ProductDeviceEnum.REGISTER.getStatus());
//            productDevice.setAlias(productVo.getName());
//            productDevice.setModel(productVo.getModel());
//            if ("0".equals(resultResponse.getCode())) {
//                if (StringUtils.isEmpty(resultResponse.getData())) {
//                    exceptionList.add(productDevice);
//                    continue;
//                }
//                productDevice.setSerial(resultResponse.getData().toString());
//            } else {
//                resultResponse = sysCodeRuleFeignService.findList(params);//重试机制，重新获取设备序号
//                if ("0".equals(resultResponse.getCode())) {
//                    if (StringUtils.isEmpty(resultResponse.getData())) {
//                        exceptionList.add(productDevice);
//                        continue;
//                    }
//                    productDevice.setSerial(resultResponse.getData().toString());
//                } else {
//                    logger.error("设备编码调用远程接口sysCodeRuleFeignService失败");
//                    exceptionList.add(productDevice);
//                    continue;
//                }
//            }
//
//            Map<String, Object> map = new HashMap<>();
//            map.put(ProductDeviceField.IMEI, imei);
//            ProductDeviceVo one4Map = findOne4Map(map);
//            if (one4Map == null) {
//                // nb 向电信物联平台注册设备
//                if (ProductTypeEnum.NB_IOT.getCode().equals(productVo.getType())) {
//                    RegDirectDeviceOutDTO regDirectDeviceOutDTO = telecomServiceImpl.registerDevice(imei);
//                    productDevice.setThirdDeviceId(regDirectDeviceOutDTO.getDeviceId());
//                    telecomServiceImpl.modifyDeviceInfo(productDevice.getThirdDeviceId(), productDevice);
//                }
//                save(productDevice);
//                //成功条数
//                successList.add(productDevice);
//            } else {
//                //重复条数
//                repeatList.add(productDevice);
//            }
//
//        }
//
//        //导出重复、异常数据EXCEL表格
//        if (repeatList.size() > 0 || exceptionList.size() > 0) {
//            String url = exportExceptionProductExcelService.exportExceptionBasisExcel(repeatList, exceptionList);
//            importRecord.setUrl(url);
//        }
//        importRecord.setRepeatNumber((long) repeatList.size());
//        importRecord.setExpectionNumber((long) exceptionList.size());
//        importRecord.setSuccessNumber((long) successList.size());
//        importRecord.setSuccessList(successList);
//
//        return importRecord;
//    }
//
//    /**
//     * 导入
//     *
//     * @param is
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public ImportRecord importDeviceKeys(InputStream is, String suffix, String productId) throws Exception {
//        //重复条数
//        List<ProductDevice> repeatList = null;
//        //异常条数 数据有问题
//        List<ProductDevice> exceptionList = null;
//        //成功条数
//        List<ProductDevice> successList = null;
//        //导入记录
//        ImportRecord importRecord = new ImportRecord();
//
//        repeatList = new ArrayList<>();
//        exceptionList = new ArrayList<>();
//        successList = new ArrayList<>();
//        //保存基础输入
//        productDeviceImport.getCustomerDepositItemList(is, repeatList, exceptionList, successList, suffix, productId);
//        if (repeatList.size() > 0 || exceptionList.size() > 0) {
//            String url = exportExceptionProductExcelService.exportExceptionBasisExcel(repeatList, exceptionList);
//            importRecord.setUrl(url);
//        }
//        importRecord.setRepeatNumber((long) repeatList.size());
//        importRecord.setExpectionNumber((long) exceptionList.size());
//        importRecord.setSuccessNumber((long) successList.size());
//        importRecord.setSuccessList(successList);
//        return importRecord;
//    }
//
//    /**
//     * 导入设备 （不做保存 仅 公租房房管理系统用）
//     *
//     * @param is
//     * @param category
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public List<ProductDeviceVo> importDevice(InputStream is, String category) throws Exception {
//        List<ProductDeviceVo> productDeviceList = new ArrayList<>();
//
//        List<ProductDeviceVo> productDeviceVos = productDeviceImport.geProductDeviceItemList(is, category, productDeviceList);
//        //重置设备在线状态
//        resetDeviceIsOnline(productDeviceVos);
//        return productDeviceVos;
//    }
//
//    /**
//     * 根据房间id 查询房间设备列表
//     */
//    @Override
//    public List<ProductDeviceVo> findProductDeviceListByRoomId(Map<String, Object> params) throws Exception {
//        List<ProductDevice> productDeviceList = productDeviceMapper.findProductDeviceListByRoomId(params);
//
//        return assembleVoList(productDeviceList);
//    }
//
//    /**
//     * 根据设备ID查询关联的设备列表
//     */
//    @Override
//    public List<ProductDeviceVo> findListByAssocDeviceId(Map<String, Object> params) throws Exception {
//        List<ProductDevice> productDeviceList = productDeviceMapper.findListByAssocDeviceId(params);
//        List<ProductDeviceVo> voList = new ArrayList<ProductDeviceVo>();
//
//        for (ProductDevice entity : productDeviceList) {
//            ProductDeviceVo vo = new ProductDeviceVo();
//            ProductDeviceAssembler.assembleVo(vo, entity);
//            //重置设备在线状态
//            resetDeviceIsOnline(vo);
//            //产品
//            ProductVo one4Map = productService.selectByPrimaryKey(vo.getProductId());
//            vo.setProductName(one4Map == null ? "" : one4Map.getName());
//            vo.setProductType(one4Map == null ? "" : one4Map.getType());
//            voList.add(vo);
//        }
////        return assembleVoList(productDeviceList);
//        return voList;
//    }
//
//    /**
//     * 根据设备ID查询关联的设备列表
//     */
//    @Override
//    public List<ProductDeviceVo> findListByUpgradeVersionId(Map<String, Object> params) throws Exception {
//        List<ProductDevice> productDeviceList = productDeviceMapper.findListByUpgradeVersionId(params);
//
//        return assembleVoList(productDeviceList);
//    }
//
//    /**
//     * 根据条件查询设备数量
//     */
//    @Override
//    public int deviceCountByToDay(Map<String, Object> params) throws Exception {
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        params.put(ProductDeviceField.MODIFY_TIME, formatter.format(date));
//        return productDeviceMapper.findCount(params);
//    }

    /**
     * 查询家庭所有列表
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public List<ProductDevice> findListOfCustomer(Map<String, Object> params) throws Exception {

        return productDeviceMapper.findListOfCustomer(params);
    }

//    /**
//     * 共享设备列表
//     *
//     * @param params
//     * @return
//     */
//    @Override
//    public List<ProductDeviceVo> findSharedDeviceList(Map<String, Object> params) throws Exception {
//        List<ProductDevice> deviceList = productDeviceMapper.findSharedDeviceList(params);
//        List<ProductDeviceVo> voList = new ArrayList<ProductDeviceVo>();
//
//        for (ProductDevice entity : deviceList) {
//            if (entity == null) {
//                return null;
//            }
//
//            ProductDeviceVo vo = new ProductDeviceVo();
//            ProductDeviceAssembler.assembleVo(vo, entity);
//            //重置设备在线状态
//            resetDeviceIsOnline(vo);
//            voList.add(vo);
//        }
//        return voList;
//    }
//
//
//    /**
//     * 根据imei创建猫眼设备
//     *
//     * @param imei
//     * @throws Exception
//     */
//    @Override
//    public long createDevice4Peephole(String imei) throws Exception {
//        String productCode = "peephole001";
//
//        //根据产品Code查询产品信息
//        Map<String, Object> params = new HashMap<>();
//        ProductVo productVo = null;
//        try {
//            params = new HashMap<>();
//            params.put("code", productCode);
//            productVo = productService.findOne4Map(params);
//            if (productVo == null) {
//                logger.error("没有code为\"peephole001\"的产品记录，请联系后台维护人员");
//                throw new Exception("没有code为\"peephole001\"的产品记录，请联系后台维护人员");
//            }
//        } catch (Exception e) {
//            logger.error("没有code为\"peephole001\"的产品记录，请联系后台维护人员", e);
//            throw new Exception("没有code为\"peephole001\"的产品记录，请联系后台维护人员");
//        }
//
//        //检查是否已经存在此设备
//        params = new HashMap<>();
//        params.put(ProductDeviceField.PRODUCT_ID, productVo.getId());
//        params.put(ProductDeviceField.IMEI, imei);
//        ProductDeviceVo deviceVo = findOne4Map(params);
//        if (deviceVo != null) {
//            logger.error("已经存在产品code为\"peephole001\"，IMEI码为\"" + imei + "\"的设备记录");
//            throw new Exception("已经存在产品code为\"peephole001\"，IMEI码为\"" + imei + "\"的设备记录");
//        }
//
//        //获取设备编码
//        params = new HashMap<>();
//        params.put("code", productVo.getCode());
//        params.put("codePrefix", productVo.getCode());
//        params.put("number", 8);
//        ResultResponse resultResponse = sysCodeRuleFeignService.findList(params);
//        if (!"0".equals(resultResponse.getCode())) {
//            logger.error("设备编码调用远程接口sysCodeRuleFeignService失败");
//            throw new Exception("设备编码调用远程接口sysCodeRuleFeignService失败");
//        }
//
//        //拼装设备信息
//        ProductDevice productDevice = new ProductDevice();
//        productDevice.setImei(imei);
//        productDevice.setProductId(productVo.getId());
//        //注册状态
//        productDevice.setStatus(ProductDeviceEnum.BIND_SUCCESS.getStatus());
//        productDevice.setAlias(productVo.getName());
//        productDevice.setSerial(resultResponse.getData().toString());
//        productDevice.setModel(productVo.getModel());
//        productDevice.setIsActived(1);
//        productDevice.setIsOnline(1);
//
//        return save(productDevice);
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description 根据imei创建锁设备
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/4/18 11:10
//     */
//    @Override
//    public long createDevice4Lock(String imei, String productCode) throws Exception {
//        //根据产品Code查询产品信息
//        Map<String, Object> params = new HashMap<>();
//        ProductVo productVo = null;
//        try {
//            params = new HashMap<>();
//            params.put("code", productCode);
//            productVo = productService.findOne4Map(params);
//            if (productVo == null) {
//                logger.error("没有code为\"zenjoy802\"的产品记录，请联系后台维护人员");
//                throw new RuntimeException("没有code为\"zenjoy802\"的产品记录，请联系后台维护人员");
//            }
//        } catch (Exception e) {
//            logger.error("没有code为\"zenjoy802\"的产品记录，请联系后台维护人员", e);
//            throw new RuntimeException("没有code为\"zenjoy802\"的产品记录，请联系后台维护人员");
//        }
//
//        //检查是否已经存在此设备
//        params = new HashMap<>();
//        params.put(ProductDeviceField.PRODUCT_ID, productVo.getId());
//        params.put(ProductDeviceField.IMEI, imei);
//        params.put(ProductDeviceField.IS_DELETED, 0);
//        ProductDeviceVo deviceVo = findOne4Map(params);
//        if (deviceVo != null) {
//            logger.error("已经存在产品code为\"zenjoy802\"，IMEI码为\"" + imei + "\"的设备记录");
//            throw new RuntimeException("已经存在产品code为\"zenjoy802\"，IMEI码为\"" + imei + "\"的设备记录");
//        }
//        //获取设备编码
//        params = new HashMap<>();
//        params.put("code", productVo.getCode());
//        params.put("codePrefix", productVo.getCode());
//        params.put("number", 8);
//        ResultResponse resultResponse = sysCodeRuleFeignService.findList(params);
//        if (!"0".equals(resultResponse.getCode())) {
//            logger.error("设备编码调用远程接口sysCodeRuleFeignService失败");
//            throw new Exception("设备编码调用远程接口sysCodeRuleFeignService失败");
//        }
//
//        //拼装设备信息
//        ProductDevice productDevice = new ProductDevice();
//        productDevice.setImei(imei);
//        productDevice.setProductId(productVo.getId());
//        //注册状态
//        productDevice.setStatus(ProductDeviceEnum.BIND_SUCCESS.getStatus());
//        productDevice.setAlias(productVo.getName());
//        productDevice.setSerial(resultResponse.getData().toString());
//        productDevice.setModel(productVo.getModel());
//        productDevice.setIsActived(1);
//        productDevice.setIsOnline(1);
//
//        return save(productDevice);
//    }
//
//    /**
//     * @param deviceVo NB设备
//     * @return
//     * @Description 创建线程控制NB在线睡眠
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/4/27 21:21
//     */
//    @Override
//    public void executeResetNBSleepTime(ProductDeviceVo deviceVo) throws Exception {
//        // 判断设备是否有猫眼或NB的管理设备
//        List<ProductDeviceVo> assocDeviceVoList = getAssocDeviceList(deviceVo.getId());
//        //查询锁板设备
//        ProductDeviceVo assocDeviceVo = getDeviceVoByType(assocDeviceVoList, ProductTypeEnum.LOCK.getCode());
//        if (assocDeviceVo == null) {
//            return;
//        }
//        //推送MQTT消息
//        publishMqttMessage(assocDeviceVo);
//
//        //更新设备状态
//        ProductDevice device = new ProductDevice();
//        device.setId(deviceVo.getId());
//        device.setIsOnline(1);
//        //设备是否异常（0-否 1-是）
//        device.setIsAbnormal(0);
//        device.setIsActived(1);
//        device.setLastestReportTime(DateUtils.getTimestamp());
//        updateByPrimaryKeySelective(device);
//    }
//
//    /**
//     * @param
//     * @return
//     * @description 推送MQTT消息
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/7/23 15:58
//     */
//    private void publishMqttMessage(ProductDeviceVo deviceVo) {
//        logger.info("------------------推送门铃唤醒MQTT消息开始------------------");
//        //推送到MQTT的消息主题
//        String topicName = "Phone/V0/DeviceWake/" + deviceVo.getDeviceKey();
//        //拼接EMQTT消息内容
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("deviceKey", deviceVo.getDeviceKey());
//        jsonObject.put("channel", "default");
//        // 数据data
//        JSONObject dataObject = new JSONObject();
//        jsonObject.put("data", dataObject);
//        PublishMqttMessage.getInstance().publish(topicName, jsonObject.toString());
//        logger.info("------------------推送门铃唤醒MQTT消息结束------------------");
//    }
//
//    /**
//     * @param
//     * @return
//     * @description 查询产品型号
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/6/25 15:24
//     */
//    @Override
//    public List<Map> findProductModel(Map<String, Object> params) throws Exception {
//        //默认只查询10行
//        params.put("startRowNum", 0);
//        params.put("endRowNum", 10);
//        params.put("type", "NB");
//        List<Map> deviceVoList = new ArrayList<>();
//        List<ProductDevice> deviceList = productDeviceMapper.findProductModel(params);
//        for (ProductDevice device : deviceList) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("value", device.getImei());
//            map.put("label", device.getImei());
//            map.put("deviceKey", device.getDeviceKey());
//            map.put("id", device.getId());
//            deviceVoList.add(map);
//        }
//        return deviceVoList;
//    }
//
//    /**
//     * 绑定
//     *
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public long updBindingStatus(Map<String, Object> params) throws Exception {
//        ProductDevice entity = new ProductDevice();
//        List<ProductDeviceVo> assocDeviceVoList = getAssocDeviceList(Long.valueOf(params.get("id").toString()));
//        //查询锁板设备
//        ProductDeviceVo assocDeviceVo = getDeviceVoByType(assocDeviceVoList, ProductTypeEnum.LOCK.getCode());
//        //获取NB设备
//        ProductDeviceVo assocDeviceVoNB = getDeviceVoByType(assocDeviceVoList, ProductTypeEnum.NB_IOT.getCode());
//        //锁版绑定
//        entity.setId(assocDeviceVo.getId());
//        entity.setStatus(ProductDeviceEnum.HOUSOE_BIND.getStatus());
//        updateByPrimaryKeySelective(entity);
//        //NB绑定
//        entity.setId(assocDeviceVoNB.getId());
//        return updateByPrimaryKeySelective(entity);
//    }
//
//    /**
//     * 解绑
//     *
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public long updUntyingStatus(Map<String, Object> params) throws Exception {
//        ProductDevice entity = new ProductDevice();
//        List<ProductDeviceVo> assocDeviceVoList = getAssocDeviceList(Long.valueOf(params.get("id").toString()));
//        //查询锁板设备
//        ProductDeviceVo assocDeviceVo = getDeviceVoByType(assocDeviceVoList, ProductTypeEnum.LOCK.getCode());
//        //获取NB设备
//        ProductDeviceVo assocDeviceVoNB = getDeviceVoByType(assocDeviceVoList, ProductTypeEnum.NB_IOT.getCode());
//        //锁版绑定
//        entity.setId(assocDeviceVo.getId());
//        entity.setStatus(ProductDeviceEnum.IMPORT.getStatus());
//        updateByPrimaryKeySelective(entity);
//        //NB绑定
//        entity.setId(assocDeviceVoNB.getId());
//        return updateByPrimaryKeySelective(entity);
//    }
//
//    /**
//     * 获取关联设备列表
//     *
//     * @return
//     */
//    private List<ProductDeviceVo> getAssocDeviceList(Long deviceId) {
//        List<ProductDeviceVo> deviceVoList = new ArrayList<>();
//        try {
//            Map<String, Object> params = new HashMap<>();
//            params.put("deviceId", deviceId);
//            List<ProductDeviceVo> voList = findListByAssocDeviceId(params);
//            //重置设备在线状态
//            deviceVoList.addAll(voList);
//        } catch (Exception e) {
//            logger.error("查询关联设备列表失败", e);
//        }
//        return deviceVoList;
//    }
//
//    /**
//     * 根据产品类型查询设备信息
//     *
//     * @param deviceVoList
//     * @return
//     */
//    private ProductDeviceVo getDeviceVoByType(List<ProductDeviceVo> deviceVoList, String type) {
//        for (ProductDeviceVo deviceVo : deviceVoList) {
//            if (type.equals(deviceVo.getProductType())) {
//                return deviceVo;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 重置设备在线状态
//     */
//    protected void resetDeviceIsOnline(List<ProductDeviceVo> deviceVoList) {
//        for (ProductDeviceVo deviceVo : deviceVoList) {
//            resetDeviceIsOnline(deviceVo);
//        }
//    }
//
//    /**
//     * 判断设备是否已离线
//     */
//    protected void resetDeviceIsOnline(ProductDeviceVo deviceVo) {
//        if (deviceVo.getIsOnline().intValue() == 0) {
//            return;
//        }
//
//        if (deviceVo.getLastestReportTime() != null
//                && DateUtils.getTimestamp().getTime() >= deviceVo.getLastestReportTime().getTime() + 20 * 1000) {
//            deviceVo.setIsOnline(0);
//
//            //更新数据库
//            ProductDevice entity = new ProductDevice();
//            entity.setId(deviceVo.getId());
//            entity.setIsOnline(0);
//            updateByPrimaryKeySelective(entity);
//        }
//    }
//
//    /**
//     * @param
//     * @return
//     * @Description 根据线程名称杀死线程名
//     * @author ZCZ
//     * @version 1.0
//     * @date 2019/4/27 23:04
//     */
//    private boolean killThreadByName(String name) {
//        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
//        int noThreads = currentGroup.activeCount();
//        Thread[] lstThreads = new Thread[noThreads];
//        currentGroup.enumerate(lstThreads);
//        //System.out.println("现有线程数" + noThreads);
//        for (int i = 0; i < noThreads; i++) {
//            String nm = lstThreads[i].getName();
//            //System.out.println("线程号：" + i + " = " + nm);
//            if (nm.equals(name)) {
//                lstThreads[i].interrupt();
//                return true;
//            }
//        }
//        return false;
//    }
}