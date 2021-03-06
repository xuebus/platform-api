package com.xinleju.platform.sys.res.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_RES_DATA_ITEM",desc="数据对象控制项")
public class DataItem extends BaseEntity{
	
		
	@Column(value="ctrl_code",desc="作用域编号")
	private String ctrlCode;
    
  		
	@Column(value="ctrl_name",desc="作用域名称")
	private String ctrlName;
	
	@Column(value="item_code",desc="控制对象编号")
	private String itemCode;
	
	
	@Column(value="item_name",desc="控制对象名称")
	private String itemName;
	
	@Column(value="app_id",desc="系统ID")
	private String appId;
  		
	@Column(value="ctrl_id",desc="数据控制对象id")
	private String ctrlId;
    
  		
	@Column(value="icon",desc="图标")
	private String icon;
    
  		
	@Column(value="type",desc="类型")
	private String type;
    
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
	@Column(value="remark",desc="备注")
	private String remark;
    
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCtrlCode() {
		return ctrlCode;
	}
	public void setCtrlCode(String ctrlCode) {
		this.ctrlCode = ctrlCode;
	}
	public String getCtrlName() {
		return ctrlName;
	}
	public void setCtrlName(String ctrlName) {
		this.ctrlName = ctrlName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
  		
	public String getCtrlId() {
		return ctrlId;
	}
	public void setCtrlId(String ctrlId) {
		this.ctrlId = ctrlId;
	}
    
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
	
}
