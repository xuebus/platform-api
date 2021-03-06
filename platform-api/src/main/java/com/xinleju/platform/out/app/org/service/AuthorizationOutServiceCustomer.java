package com.xinleju.platform.out.app.org.service;

public interface AuthorizationOutServiceCustomer {
	
	/**
	 * 获取有权限的菜单
	 * @param userJson
	 * @param paramJson:{userId:"用户Id",appId:"系统Id"}
	 * @return
	 */
	String getFuncMenuAuthByUserIdAndAppId(String userJson, String paramJson);
	
	/**
	 * 获取有权限的按钮
	 * @param userJson
	 * @param paramJson:{userId:"用户Id",appId:"系统Id",menuId:"菜单Id"}
	 * @return
	 */
	String getFuncButtonAuthByUserIdAndAppIdAndMenuId(String userJson, String paramJson);
	
	/**
	 * 获取有权限的按钮
	 * @param userJson
	 * @param paramJson:{userLoginName:"用户账号",appCode:"系统CODE",menuCode:"菜单code"}
	 * @return
	 */
	public String getFuncButtonAuthByUserLoginNameAndAppCodeAndMenuCode(String userJson, String paramJson) ;

	/**
	 * 获取有权限的菜单(根据用户账号和系统code)
	 * @param userJson
	 * @param paramJson:{userLoginName:"用户账号",appCode:"系统CODE"}
	 * @return
	 */
	public String getFuncMenuAuthByUserLoginNameAndAppCode(String userJson,String paramJson);
	/**
	 * 获取有权限的数据授权（根据用户账号和系统code和授权业务对象）
	 * @param userJson
	 * @param paramJson:{userLoginName:"用户账号",appCode:"系统CODE",itemCode:"授权业务对象code"}
	 * @return
	 */
	public String getDataPointAuthByUserLoginNameAndAppCodeAndItemCode(String userJson, String paramJson);
	/**
	 * 根据数据授权ID查询值
	 * @param userJson
	 * @param paramJson:{dataPermissionId:"数据授权ID"}
	 * @return
	 */
	public String getDataPointValAuthByDataPermissionId(String userJson,String paramJson);
}
