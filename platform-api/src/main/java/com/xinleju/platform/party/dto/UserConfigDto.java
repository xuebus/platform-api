package com.xinleju.platform.party.dto;

import java.util.Date;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class UserConfigDto extends BaseDto{

		
	//第三方应用id
	private String appId;
    
  		
	//用户ID
	private String userId;
    
  		
	//token
	private String token;
    
  		
	//token开始时间
	private Date startTime;
    
  		
	//token结束时间
	private Date endTime;
    
  		
	//状态
	private String status;
	
	
	//状态
	private String remark;
    
  		
		
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    
  		
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    
  		
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
    
  		
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
}