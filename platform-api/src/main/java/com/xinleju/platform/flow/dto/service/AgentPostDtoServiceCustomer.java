package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface AgentPostDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 获取代理授权岗位列表
	 * @param parentId
	 * @return
	 */
	public String queryAgentPostList(String userInfo, String paramater);
}
