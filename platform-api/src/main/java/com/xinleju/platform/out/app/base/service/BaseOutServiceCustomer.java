package com.xinleju.platform.out.app.base.service;

public interface BaseOutServiceCustomer {
	
	/**
	 * 获取所有产品类型
	 * @param userJson
	 * @param paramJson:{}
	 * @return
	 */
	public String getAllProductType(String userJson, String paramJson);
	
	/**
	 * 根据公司获取供方档案
	 * @param userJson
	 * @param paramJson:{companyId:"公司id"}
	 * @return
	 */
	public String getSupplierByCompanyId(String userJson, String paramJson);
	/**
	 * 根据主键获取供方档案
	 * @param userJson
	 * @param paramJson:{id:"id"}
	 * @return
	 */
	public String getSupplierById(String userJson, String paramJson);
}
