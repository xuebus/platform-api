package com.xinleju.platform.univ.search.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author haoqp
 * 
 *
 */
public class SearchCategoryDto extends BaseDto{

		
	//分类编码
	private String code;
    
  		
	//分类名称
	private String name;

	public String getHostUrl() {
		return hostUrl;
	}
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}
	//分类url
	private String hostUrl;
	
	// 状态：0-启用;1-禁用
	private Boolean status;
    
	// 属性列表
	private List<SearchCategoryPropertyDto> propertyList;	
		
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
	public List<SearchCategoryPropertyDto> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<SearchCategoryPropertyDto> propertyList) {
		this.propertyList = propertyList;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
    
  		
}
