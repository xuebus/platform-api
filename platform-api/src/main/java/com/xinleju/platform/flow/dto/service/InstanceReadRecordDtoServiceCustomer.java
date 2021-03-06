package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface InstanceReadRecordDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 查询指定流程实例的阅读记录
	 * 
	 * @param instanceId
	 * @return
	 */
	String queryRecord(String userInfo, String instanceId);

}
