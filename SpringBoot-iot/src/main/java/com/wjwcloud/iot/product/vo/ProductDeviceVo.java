package com.wjwcloud.iot.product.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.iot.model.AbstructBaseVo;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public class ProductDeviceVo extends AbstructBaseVo<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

    public ProductDeviceVo() {
		super();
    }

    
	/**
	 * 唯一标识ID
	 */
	private Long id;

	/**
	 * 企业ID
	 */
	private Long companyId;

	/**
	 * 产品ID
	 */
	private Long productId;

	/**
	 * 分组ID
	 */
	private Long groupId;

	/**
	 * 别名
	 */
	private String alias;

	/**
	 * 设备key
	 */
	private String deviceKey;

	/**
	 * 序号
	 */
	private String serial;

	/**
	 * 型号
	 */
	private String model;

	/**
	 * mac地址
	 */
	private String mac;

	/**
	 * ip地址
	 */
	private String ip;

	/**
	 * 服务集标识
	 */
	private String ssid;

	/**
	 * 是否激活
	 */
	private Integer isActived;

	/**
	 * 激活时间
	 */
	private Timestamp activeTime;

	/**
	 * 是否在线
	 */
	private Integer isOnline;

	/**
	 * 设备二维码
	 */
	private String qrcode;

	/**
	 * 当前电量
	 */
	private String power;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 0-无效1-有效
	 */
	private Integer status;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 删除标记（0：正常，1：删除）
	 */
	private Integer isDeleted;

	/**
	 * 版本（乐观锁）
	 */
	private Integer version;

	/**
	 * 修改人
	 */
	private Long modifier;

	/**
	 * 修改时间
	 */
	private Timestamp modifyTime;

	/**
	 * 创建人
	 */
	private Long creator;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	/**
	 * 第三方设备ID
	 */
	private String thirdDeviceId;

	/**
	 * 第三方设备秘钥
	 */
	private String thirdDeviceSecret;

	/**
	 * 共享秘钥psk
	 */
	private String psk;

	/**
	 * 设备用户序号二进制
	 */
	private String deviceUserIndexBinary;

	/**
	 * 设备秘钥序号二进制
	 */
	private String deviceSecretIndexBinary;

	/**
	 * 设备业务状态
	 */
	private String deviceStatus;

	/**
	 * SIM流量卡
	 */
	private String sim;

	/**
	 * 国际移动设备识别码
	 */
	private String imei;

	/**
	 * 蓝牙MAC地址
	 */
	private String bluetoothMac;

	/**
	 * 蓝牙强度
	 */
	private String bluetoothSignal;

	/**
	 * 设备信号强度
	 */
	private Integer rssi;

	/**
	 * 设备是否异常
	 */
	private Integer isAbnormal;

	/**
	 * 最后一次上报时间
	 */
	private Timestamp lastestReportTime;


	/**********************  扩展字段 START ************************************************/

	/**
	 * 产品名称
	 */
	private String productName;

	/**
	 * 产品类型
	 */
	private String productType;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 家庭设备权限列表
	 */
//	private List<ProductDeviceRoleVo> deviceRoleVoList;

	/**
	 * 设备秘钥集合
	 */
	private Map<String,Object> secretTypeMap;

	/**
	 * 是否选中
	 */
	private boolean isSelect;

	/**
	 * 锁产品版本
	 */
	private String lockVersion;

	/**
	 * 锁设备版本
	 */
	private String lockDeviceVersion;

	/**
	 * 主板产品版本
	 */
	private String masterControlVersion;
	/**
	 * 主板设备版本
	 */
	private String masterControlDeviceVersion;

	/**
	 * 芯片产品版本
	 */
	private String encryptChipVersion;

	/**
	 * 芯片设备版本
	 */
	private String encryptChipDeviceVersion;

	/**
	 * 正面图片地址
	 */
	private String frontImageUrl;

	/**
	 * 反面图片地址
	 */
	private String backImageUrl;

	/**
	 * 侧面图片地址
	 */
	private String sideImageUrl;

	/**
	 * 复位示意图片
	 */
	private String resetImageUrl;

	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	/**********************  扩展字段 END ************************************************/




	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getDeviceUserIndexBinary() {
		return deviceUserIndexBinary;
	}

	public void setDeviceUserIndexBinary(String deviceUserIndexBinary) {
		this.deviceUserIndexBinary = deviceUserIndexBinary;
	}

	public String getDeviceSecretIndexBinary() {
		return deviceSecretIndexBinary;
	}

	public void setDeviceSecretIndexBinary(String deviceSecretIndexBinary) {
		this.deviceSecretIndexBinary = deviceSecretIndexBinary;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
    public void setId(Long id){
		this.id = id;
	}

	@Override
    public Long getId(){
		return this.id;
	}

	public void setCompanyId(Long companyId){
		this.companyId = companyId;
	}

	public Long getCompanyId(){
		return this.companyId;
	}

	public void setProductId(Long productId){
		this.productId = productId;
	}

	public Long getProductId(){
		return this.productId;
	}

	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}

	public Long getGroupId(){
		return this.groupId;
	}

	public void setAlias(String alias){
		this.alias = alias;
	}

	public String getAlias(){
		return this.alias;
	}

	public void setDeviceKey(String deviceKey){
		this.deviceKey = deviceKey;
	}

	public String getDeviceKey(){
		return this.deviceKey;
	}

	public void setSerial(String serial){
		this.serial = serial;
	}

	public String getSerial(){
		return this.serial;
	}

	public void setModel(String model){
		this.model = model;
	}

	public String getModel(){
		return this.model;
	}

	public void setMac(String mac){
		this.mac = mac;
	}

	public String getMac(){
		return this.mac;
	}

	public void setIp(String ip){
		this.ip = ip;
	}

	public String getIp(){
		return this.ip;
	}

	public void setSsid(String ssid){
		this.ssid = ssid;
	}

	public String getSsid(){
		return this.ssid;
	}

	public void setIsActived(Integer isActived){
		this.isActived = isActived;
	}

	public Integer getIsActived(){
		return this.isActived;
	}

	public void setIsOnline(Integer isOnline){
		this.isOnline = isOnline;
	}

	public Integer getIsOnline(){
		return this.isOnline;
	}

	public void setQrcode(String qrcode){
		this.qrcode = qrcode;
	}

	public String getQrcode(){
		return this.qrcode;
	}

	public void setPower(String power){
		this.power = power;
	}

	public String getPower(){
		return this.power;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	public void setIsDeleted(Integer isDeleted){
		this.isDeleted = isDeleted;
	}

	public Integer getIsDeleted(){
		return this.isDeleted;
	}

	public void setVersion(Integer version){
		this.version = version;
	}

	public Integer getVersion(){
		return this.version;
	}

	public void setModifier(Long modifier){
		this.modifier = modifier;
	}

	public Long getModifier(){
		return this.modifier;
	}

	public void setModifyTime(Timestamp modifyTime){
		this.modifyTime = modifyTime;
	}

	public Timestamp getModifyTime(){
		return this.modifyTime;
	}

	public void setCreator(Long creator){
		this.creator = creator;
	}

	public Long getCreator(){
		return this.creator;
	}

	public void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Timestamp getCreateTime(){
		return this.createTime;
	}

	public String getThirdDeviceId() {
		return thirdDeviceId;
	}

	public void setThirdDeviceId(String thirdDeviceId) {
		this.thirdDeviceId = thirdDeviceId;
	}

	public String getPsk() {
		return psk;
	}

	public void setPsk(String psk) {
		this.psk = psk;
	}

//	public List<ProductDeviceRoleVo> getDeviceRoleVoList() {
//		return deviceRoleVoList;
//	}
//
//	public void setDeviceRoleVoList(List<ProductDeviceRoleVo> deviceRoleVoList) {
//		this.deviceRoleVoList = deviceRoleVoList;
//	}

	public Map<String, Object> getSecretTypeMap() {
		return secretTypeMap;
	}

	public void setSecretTypeMap(Map<String, Object> secretTypeMap) {
		this.secretTypeMap = secretTypeMap;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getBluetoothMac() {
		return bluetoothMac;
	}

	public void setBluetoothMac(String bluetoothMac) {
		this.bluetoothMac = bluetoothMac;
	}

	public String getBluetoothSignal() {
		return bluetoothSignal;
	}

	public void setBluetoothSignal(String bluetoothSignal) {
		this.bluetoothSignal = bluetoothSignal;
	}

	public String getThirdDeviceSecret() {
		return thirdDeviceSecret;
	}

	public void setThirdDeviceSecret(String thirdDeviceSecret) {
		this.thirdDeviceSecret = thirdDeviceSecret;
	}

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean select) {
		isSelect = select;
	}

	public Timestamp getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Timestamp activeTime) {
		this.activeTime = activeTime;
	}

	public Integer getRssi() {
		return rssi;
	}

	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	public Integer getIsAbnormal() {
		return isAbnormal;
	}

	public void setIsAbnormal(Integer isAbnormal) {
		this.isAbnormal = isAbnormal;
	}

	public Timestamp getLastestReportTime() {
		return lastestReportTime;
	}

	public void setLastestReportTime(Timestamp lastestReportTime) {
		this.lastestReportTime = lastestReportTime;
	}


	public String getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(String lockVersion) {
		this.lockVersion = lockVersion;
	}

	public String getLockDeviceVersion() {
		return lockDeviceVersion;
	}

	public void setLockDeviceVersion(String lockDeviceVersion) {
		this.lockDeviceVersion = lockDeviceVersion;
	}

	public String getMasterControlVersion() {
		return masterControlVersion;
	}

	public void setMasterControlVersion(String masterControlVersion) {
		this.masterControlVersion = masterControlVersion;
	}

	public String getMasterControlDeviceVersion() {
		return masterControlDeviceVersion;
	}

	public void setMasterControlDeviceVersion(String masterControlDeviceVersion) {
		this.masterControlDeviceVersion = masterControlDeviceVersion;
	}

	public String getEncryptChipVersion() {
		return encryptChipVersion;
	}

	public void setEncryptChipVersion(String encryptChipVersion) {
		this.encryptChipVersion = encryptChipVersion;
	}

	public String getEncryptChipDeviceVersion() {
		return encryptChipDeviceVersion;
	}

	public void setEncryptChipDeviceVersion(String encryptChipDeviceVersion) {
		this.encryptChipDeviceVersion = encryptChipDeviceVersion;
	}

	public String getFrontImageUrl() {
		return frontImageUrl;
	}

	public void setFrontImageUrl(String frontImageUrl) {
		this.frontImageUrl = frontImageUrl;
	}

	public String getBackImageUrl() {
		return backImageUrl;
	}

	public void setBackImageUrl(String backImageUrl) {
		this.backImageUrl = backImageUrl;
	}

	public String getSideImageUrl() {
		return sideImageUrl;
	}

	public void setSideImageUrl(String sideImageUrl) {
		this.sideImageUrl = sideImageUrl;
	}

	public String getResetImageUrl() {
		return resetImageUrl;
	}

	public void setResetImageUrl(String resetImageUrl) {
		this.resetImageUrl = resetImageUrl;
	}

	public static ProductDeviceVo fromJson(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        return (ProductDeviceVo) JSONObject.toJavaObject(jsonObject, ProductDeviceVo.class);
    }

}
