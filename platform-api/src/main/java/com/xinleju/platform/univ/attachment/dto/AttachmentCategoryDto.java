package com.xinleju.platform.univ.attachment.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author haoqp
 * 
 *
 */
public class AttachmentCategoryDto extends BaseDto{

		
	//系统id
	private String appId;
    
  		
	//分类名称
	private String name;
    
  		
	//分类编码
	private String code;
    
  		
	//父分类id
	private String parentId;
    
  		
		
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
}
