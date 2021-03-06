package com.xinleju.platform.sys.res.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class DataCtrlDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
  		
	//应用id
	private String appId;
    
  		
	//图标
	private String icon;
    
  		
	//排序
	private Long sort;
    
  	//对象控制项
	private List<DataItemDto> list;
		
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
  		
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public List<DataItemDto> getList() {
		return list;
	}
	public void setList(List<DataItemDto> list) {
		this.list = list;
	}
    
  		
}
