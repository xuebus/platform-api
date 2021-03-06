package com.xinleju.platform.sys.org.dto;

import java.sql.Timestamp;



/**
 * @author sy
 * 
 *
 */
public class SyncUserDto{

	//ID
	private String thirdId;

	//登陆账号
	private String loginId;
	//工号
	private String jobNumber;
	
	//状态
	private String state;
    
  	//密码
	private String password;
	
	//企业ID
	private String thirdEnterpriseId;
    
  		
	//姓名
	private String name;
    
    
  		
	//手机号
	private String mobile;
    
  		
	//企业邮箱
	private String orgEmail;
    
  		
	//个人邮箱
	private String email;
    
  		
	//办公电话
	private String officePhone;
    
  		
	//出生日期
	private Timestamp birthday;
  		
	//性别
	private Integer gender;
    
  		
	//说明
	private String remark;
    
  		
	
    
  	//qq号
	private String qq;
	
	//微信号
	private String wechat;
	
	//家庭住址
	private String homeAddress;
	
	//身份证号
	private String identityCard;

	public String getThirdId() {
		return thirdId;
	}

	public void setThirdId(String thirdId) {
		this.thirdId = thirdId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getThirdEnterpriseId() {
		return thirdEnterpriseId;
	}

	public void setThirdEnterpriseId(String thirdEnterpriseId) {
		this.thirdEnterpriseId = thirdEnterpriseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrgEmail() {
		return orgEmail;
	}

	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
