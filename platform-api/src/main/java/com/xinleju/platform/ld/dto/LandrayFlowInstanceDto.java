package com.xinleju.platform.ld.dto;

import com.xinleju.platform.base.dto.BaseDto;


/**
 * @author admin
 * 
 *
 */
public class LandrayFlowInstanceDto extends BaseDto{

		
	//���
	private String code;
    
  		
	//����
	private String name;
    
  		
	//������id
	private String startUserId;
    
  		
	//����������
	private String startDeptName;
    
  		
	//����˾����
	private String startCompanyName;
    
  		
	//�������̷���
	private String flTypeId;
    
  		
	//ģ������
	private String templetName;
    
  		
	//�ĵ�״̬
	private String status;
    
  		
	//����ʱ��
	private String startDate;
    
  		
	//��̬ҳ��url
	private String url;

	private String oaRealName;
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}
    
  		
	public String getStartDeptName() {
		return startDeptName;
	}
	public void setStartDeptName(String startDeptName) {
		this.startDeptName = startDeptName;
	}
    
  		
	public String getStartCompanyName() {
		return startCompanyName;
	}
	public void setStartCompanyName(String startCompanyName) {
		this.startCompanyName = startCompanyName;
	}
    
  		
	public String getFlTypeId() {
		return flTypeId;
	}
	public void setFlTypeId(String flTypeId) {
		this.flTypeId = flTypeId;
	}
    
  		
	public String getTempletName() {
		return templetName;
	}
	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
    
  		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


	public String getOaRealName() {
		return oaRealName;
	}

	public void setOaRealName(String oaRealName) {
		this.oaRealName = oaRealName;
	}
}
