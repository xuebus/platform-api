package com.xinleju.platform.sys.sync.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_SYNC_DATA",desc="组织数据同步配置")
public class SyncData extends BaseEntity{
	
		
	@Column(value="data_type",desc="数据类型")
	private String dataType;


	@Column(value="system_code",desc="系统编码")
	private String systemCode;
    
  		
	@Column(value="system",desc="系统名称")
	private String system;
    
  		
	@Column(value="url",desc="同步接口")
	private String url;
    
  		
	@Column(value="mode",desc="同步方式")
	private String mode;
    
  		
	@Column(value="num",desc="每次同步条数")
	private Integer num;

	@Column(value="status",desc="推送状态")
	private Boolean status;

	@Column(value="remark",desc="备注")
	private String remark;


	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
