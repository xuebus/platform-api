package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_VOUCHER_BILL_ENTRY",desc="单据凭证分录")
public class VoucherBillEntry extends BaseEntity{
	
		
	@Column(value="voucher_bill_id",desc="单据凭证id")
	private String voucherBillId;
    
  		
	@Column(value="summary",desc="摘要")
	private String summary;
    
  		
	@Column(value="caption_code",desc="会计科目编码")
	private String captionCode;
    
  		
	@Column(value="caption_id",desc="会计科目id")
	private String captionId;
    
  		
	@Column(value="caption_name",desc="会计科目名称")
	private String captionName;
    
  		
	@Column(value="ass_code",desc="辅助核算业务对象代码")
	private String assCode;
    
  		
	@Column(value="ass_name",desc="辅助核算业务对象名称")
	private String assName;
    
  		
	@Column(value="ass_compent",desc="辅助核算明细拼接串")
	private String assCompent;
    
  		
	@Column(value="cash_flow_code",desc="现金流量编码")
	private String cashFlowCode;
    
  		
	@Column(value="cash_flow_name",desc="现金流量名称")
	private String cashFlowName;
    
  		
	@Column(value="crmny",desc="贷方金额")
	private String crmny;
    
  		
	@Column(value="drmny",desc="借方金额")
	private String drmny;
    
  		
	@Column(value="remark",desc="备注")
	private String remark;
	@Column(value="real_ass_name",desc="实际使用该辅助核算的部门")
	private String realAssName;
	@Column(value="ass_id",desc="辅助核算id")
	private String assId;
		
	public String getAssId() {
		return assId;
	}
	public void setAssId(String assId) {
		this.assId = assId;
	}
	public String getVoucherBillId() {
		return voucherBillId;
	}
	public void setVoucherBillId(String voucherBillId) {
		this.voucherBillId = voucherBillId;
	}
    
  		
	public String getRealAssName() {
		return realAssName;
	}
	public void setRealAssName(String realAssName) {
		this.realAssName = realAssName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
    
  		
	public String getCaptionCode() {
		return captionCode;
	}
	public void setCaptionCode(String captionCode) {
		this.captionCode = captionCode;
	}
    
  		
	public String getCaptionId() {
		return captionId;
	}
	public void setCaptionId(String captionId) {
		this.captionId = captionId;
	}
    
  		
	public String getCaptionName() {
		return captionName;
	}
	public void setCaptionName(String captionName) {
		this.captionName = captionName;
	}
    
  		
	public String getAssCode() {
		return assCode;
	}
	public void setAssCode(String assCode) {
		this.assCode = assCode;
	}
    
  		
	public String getAssName() {
		return assName;
	}
	public void setAssName(String assName) {
		this.assName = assName;
	}
    
  		
	public String getAssCompent() {
		return assCompent;
	}
	public void setAssCompent(String assCompent) {
		this.assCompent = assCompent;
	}
    
  		
	public String getCashFlowCode() {
		return cashFlowCode;
	}
	public void setCashFlowCode(String cashFlowCode) {
		this.cashFlowCode = cashFlowCode;
	}
    
  		
	public String getCashFlowName() {
		return cashFlowName;
	}
	public void setCashFlowName(String cashFlowName) {
		this.cashFlowName = cashFlowName;
	}
    
  		
	public String getCrmny() {
		return crmny;
	}
	public void setCrmny(String crmny) {
		this.crmny = crmny;
	}
    
  		
	public String getDrmny() {
		return drmny;
	}
	public void setDrmny(String drmny) {
		this.drmny = drmny;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	
}