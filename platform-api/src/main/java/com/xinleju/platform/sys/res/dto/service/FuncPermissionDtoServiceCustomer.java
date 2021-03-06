package com.xinleju.platform.sys.res.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface FuncPermissionDtoServiceCustomer extends BaseDtoServiceCustomer{
	
	/**
	 * 查询根据角色查询系统权限
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String querySystemListByRole(String userinfo, String paramaterJson);
	/**
	 * 根据系统查询按钮树
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryAuthorizationListByAppIds(String userinfo, String paramater);
	
	/**
	 * 根据系统和角色查询已授权数据
	 * @param map
	 * @return
	 */
	public String queryAuthDataByappIdsAndroleIds(String userinfo, String paramater);
	
	/**
	 * 根据类型和ID查询权限范围（自有的，继承的）
	 * @param map
	 * @return
	 */
	public String queryListByObjectType(String userinfo, String paramater);
	
	/**
	 *查询角色树（标准岗位）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryAuthorizationListAllRoles(String userinfo, String paramater);
	
	/**
	 *查询对象树（通用角色）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryAuthorizationListAllCurrencyRoles(String userinfo, String paramater);
	
	/**
	 *查询对象树（岗位）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryAuthorizationListAllPost(String userinfo, String paramater);
	
	/**
	 *查询对象树（人员）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryAuthorizationListAllUser(String userinfo, String paramater);
	
	/**
	 * 根据动作点查询已授权数据
	 * @param map
	 * @return
	 */
	public String queryAuthDataByOperationIds(String userinfo, String paramater);
	
	/**
	 * 保存授权数据（动作点到角色）
	 * @param map
	 * @return
	 */
	public String saveBatchFunToRole(String userInfo, String saveJsonList);

	/**
	 *
	 * @param userInfo
	 * @param menuIdmap
	 * @return
	 */
	public String queryUsersByMenuId(String userInfo, String menuIdmap);

	/**
	 * 根据系统和角色查询已授权数据
	 * @param map
	 * @return
	 */
    String queryAuthorizationListByAppId(String userJson, String paramaterJson);
    /**
	 * 保存引入数据
	 * @param t
	 * @return
	 */
	String saveBatchFunImport(String userInfo, String saveJsonList);
}
