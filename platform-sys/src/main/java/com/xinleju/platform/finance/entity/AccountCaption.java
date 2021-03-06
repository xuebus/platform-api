package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_ACCOUNT_CAPTION",desc="会计科目")
public class AccountCaption extends BaseEntity{
	
		
	@Column(value="code",desc="科目编码")
	private String code;
    
  		
	@Column(value="name",desc="科目名称")
	private String name;
    
  		
	@Column(value="ass_ids",desc="辅助核算ids")
	private String assIds;
    
  		
	@Column(value="ass_names",desc="辅助核算名称")
	private String assNames;
    
  		
	@Column(value="biz_item_ids",desc="会计科目对照项id及值id")
	private String bizItemIds;
    
  		
	@Column(value="biz_item_names",desc="会计科目对照名称及值名称")
	private String bizItemNames;
    
  		
	@Column(value="account_set_id",desc="财务系统公司id")
	private String accountSetId;
    
  		
	@Column(value="parent_id",desc="父级id")
	private String parentId;
    
	
	@Column(value="prefix_id",desc="父级id")
	private String prefixId;
	
	@Column(value="sort",desc="排序")
	private String sort;
	
  		
		
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
    
  		
	public String getAssIds() {
		return assIds;
	}
	public void setAssIds(String assIds) {
		this.assIds = assIds;
	}
    
  		
	public String getAssNames() {
		return assNames;
	}
	public void setAssNames(String assNames) {
		this.assNames = assNames;
	}
    
  		
	public String getBizItemIds() {
		return bizItemIds;
	}
	public void setBizItemIds(String bizItemIds) {
		this.bizItemIds = bizItemIds;
	}
    
  		
	public String getBizItemNames() {
		return bizItemNames;
	}
	public void setBizItemNames(String bizItemNames) {
		this.bizItemNames = bizItemNames;
	}
    
  		
	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
    
  	
	
}
