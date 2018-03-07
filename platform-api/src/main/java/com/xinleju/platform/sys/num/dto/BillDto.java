package com.xinleju.platform.sys.num.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BillDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//连接符
	private String connector;
    
  		
	//备注
	private String remark;
    
  		
	//状态
	private String status;
	
	//规则对象
	private List<RulerDto> rulerList;
    
  		
		
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
    
  		
	public String getConnector() {
		return connector;
	}
	public void setConnector(String connector) {
		this.connector = connector;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<RulerDto> getRulerList() {
		return rulerList;
	}
	public void setRulerList(List<RulerDto> rulerList) {
		this.rulerList = rulerList;
	}
    
  		
}