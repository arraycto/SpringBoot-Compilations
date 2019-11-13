package com.wjwcloud.iot.customer.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjwcloud.iot.model.AbstructBaseVo;

import java.io.Serializable;
import java.sql.Timestamp;


public class CustomerVo extends AbstructBaseVo<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

    public CustomerVo() {
		super();
    }

    
	/**
	 * 唯一标识ID
	 */
	private Long id;

	/**
	 * 公司ID
	 */
	private Long companyId;

	/**
	 * 会员编码
	 */
	private String code;

	/**
	 * 会员名称
	 */
	private String name;

	/**
	 * 简称
	 */
	private String shortName;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 会员类型（公司、个人）
	 */
	private String type;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机
	 */
	private String mobilePhone;

	/**
	 * 固话
	 */
	private String telphone;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 生日
	 */
	private String birthday;

	/**
	 * 身份证
	 */
	private String idCard;

	/**
	 * 微信号
	 */
	private String weixin;

	/**
	 * QQ号
	 */
	private String qq;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 等级
	 */
	private String grade;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 随机码
	 */
	private String encrypt;

	/**
	 * 加密盐值
	 */
	private String saltValue;

	/**
	 * 登录错误次数
	 */
	private Integer loginErrorCount;

	/**
	 * 锁定时间
	 */
	private Timestamp loginLockTime;

	/**
	 * 会员来源
	 */
	private String origin;

	/**
	 * 手机UA
	 */
	private String phoneUa;

	/**
	 * 手机imei
	 */
	private String phoneImei;

	/**
	 * 会员描述
	 */
	private String description;

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
	 * 扩展字段
	 * 省名称
	 */
	private String provinceName;
	/**
	 * 市名称
	 */
	private String cityName;
	/**
	 * 区名称
	 */
	private String districtName;
	/**
	 * 街道名称
	 */
	private String streetName;

	/**
	 * 详细地址
	 */
	private String address;


	/**
	 * 手机端app唯一标识
	 */
	private String appClientId;

	/**
	 * 手机app平台（android，ios）
	 */
	private String appPlatform;

	/**
	 * 手机端app接入时间
	 */
	private Timestamp appJoinTime;

	/**
	 * 会员地址
	 */
//	private CustomerAddressVo customerAddressVo;

	/**
	 * 角色名称
	 */
	private String roleNames;
	/**
	 * 角色ID
	 */
	private String roleIds;

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getAppClientId() {
		return appClientId;
	}

	public void setAppClientId(String appClientId) {
		this.appClientId = appClientId;
	}

	public String getAppPlatform() {
		return appPlatform;
	}

	public void setAppPlatform(String appPlatform) {
		this.appPlatform = appPlatform;
	}

	public Timestamp getAppJoinTime() {
		return appJoinTime;
	}

	public void setAppJoinTime(Timestamp appJoinTime) {
		this.appJoinTime = appJoinTime;
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

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}

	public void setShortName(String shortName){
		this.shortName = shortName;
	}

	public String getShortName(){
		return this.shortName;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getNickname(){
		return this.nickname;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return this.type;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setMobilePhone(String mobilePhone){
		this.mobilePhone = mobilePhone;
	}

	public String getMobilePhone(){
		return this.mobilePhone;
	}

	public void setTelphone(String telphone){
		this.telphone = telphone;
	}

	public String getTelphone(){
		return this.telphone;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return this.gender;
	}

	public void setBirthday(String birthday){
		this.birthday = birthday;
	}

	public String getBirthday(){
		return this.birthday;
	}

	public void setIdCard(String idCard){
		this.idCard = idCard;
	}

	public String getIdCard(){
		return this.idCard;
	}

	public void setWeixin(String weixin){
		this.weixin = weixin;
	}

	public String getWeixin(){
		return this.weixin;
	}

	public void setQq(String qq){
		this.qq = qq;
	}

	public String getQq(){
		return this.qq;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return this.avatar;
	}

	public void setGrade(String grade){
		this.grade = grade;
	}

	public String getGrade(){
		return this.grade;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setEncrypt(String encrypt){
		this.encrypt = encrypt;
	}

	public String getEncrypt(){
		return this.encrypt;
	}

	public void setSaltValue(String saltValue){
		this.saltValue = saltValue;
	}

	public String getSaltValue(){
		return this.saltValue;
	}

	public void setLoginErrorCount(Integer loginErrorCount){
		this.loginErrorCount = loginErrorCount;
	}

	public Integer getLoginErrorCount(){
		return this.loginErrorCount;
	}

	public void setLoginLockTime(Timestamp loginLockTime){
		this.loginLockTime = loginLockTime;
	}

	public Timestamp getLoginLockTime(){
		return this.loginLockTime;
	}

	public void setOrigin(String origin){
		this.origin = origin;
	}

	public String getOrigin(){
		return this.origin;
	}

	public void setPhoneUa(String phoneUa){
		this.phoneUa = phoneUa;
	}

	public String getPhoneUa(){
		return this.phoneUa;
	}

	public void setPhoneImei(String phoneImei){
		this.phoneImei = phoneImei;
	}

	public String getPhoneImei(){
		return this.phoneImei;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return this.description;
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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public CustomerAddressVo getCustomerAddressVo() {
//		return customerAddressVo;
//	}
//
//	public void setCustomerAddressVo(CustomerAddressVo customerAddressVo) {
//		this.customerAddressVo = customerAddressVo;
//	}

	public static CustomerVo fromJson(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        return (CustomerVo) JSONObject.toJavaObject(jsonObject, CustomerVo.class);
    }
}
