package com.xinleju.platform.out.app.org.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.service.DataPermissionService;
import com.xinleju.platform.sys.res.service.FuncPermissionService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

public class AuthorizationOutServiceProducer implements AuthorizationOutServiceCustomer {
	private static Logger log = Logger.getLogger(AuthorizationOutServiceProducer.class);

	@Autowired
	private FuncPermissionService funcPermissionService;
	@Autowired
	private DataPermissionService dataPermissionService;
	/**
	 * 获取有权限的菜单
	 * @param userJson
	 * @param {userId:"用户Id",appId:"系统Id"}
	 * @return
	 */
	@Override
	public String getFuncMenuAuthByUserIdAndAppId(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userId = (String)param.get("userId");
			String appId = (String)param.get("appId");
			if(null == userId || userId.equals("")){
				throw new InvalidCustomException("传递的用户为空");
			}else if(null == appId || appId.equals("")){
				throw new InvalidCustomException("传递的系统为空");
			}else{
				List<ResourceDto> list=funcPermissionService.getFuncMenuAuthByUserIdAndAppId(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取有权限的菜单成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取有权限的菜单失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取有权限的菜单失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 获取有权限的按钮
	 * @param userJson
	 * @param paramJson:{userId:"用户Id",appId:"系统Id",menuId:"菜单Id"}
	 * @return
	 */
	@Override
	public String getFuncButtonAuthByUserIdAndAppIdAndMenuId(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userId = (String)param.get("userId");
			String appId = (String)param.get("appId");
			if(null == userId || userId.equals("")){
				throw new InvalidCustomException("传递的用户为空");
			}else if(null == appId || appId.equals("")){
				throw new InvalidCustomException("传递的系统为空");
			}else{
				List<ResourceDto> list=funcPermissionService.getFuncButtonAuthByUserIdAndAppIdAndMenuId(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取有权限的按钮成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取有权限的按钮失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取有权限的按钮失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 获取有权限的按钮
	 * @param userJson
	 * @param paramJson:{userLoginName:"用户账号",appCode:"系统CODE",menuCode:"菜单code"}
	 * @return
	 */
	@Override
	public String getFuncButtonAuthByUserLoginNameAndAppCodeAndMenuCode(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userId = (String)param.get("userLoginName");
			String appId = (String)param.get("appCode");
			if(null == userId || userId.equals("")){
				throw new InvalidCustomException("传递的用户为空");
			}else if(null == appId || appId.equals("")){
				throw new InvalidCustomException("传递的系统为空");
			}else{
				List<Map<String,Object>> list=funcPermissionService.getFuncButtonAuthByUserLoginNameAndAppCodeAndMenuCode(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取有权限的按钮成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取有权限的按钮失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取有权限的按钮失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 获取有权限的菜单(根据用户账号和系统code)
	 * @param userJson
	 * @param paramJson:{userLoginName:"用户账号",appCode:"系统CODE"}
	 * @return
	 */
	@Override
	public String getFuncMenuAuthByUserLoginNameAndAppCode(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userId = (String)param.get("userLoginName");
			String appId = (String)param.get("appCode");
			if(null == userId || userId.equals("")){
				throw new InvalidCustomException("传递的用户为空");
			}else{
				List<Map<String,Object>> list=funcPermissionService.getFuncMenuAuthByUserLoginNameAndAppCode(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取有权限的按钮成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取有权限的按钮失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取有权限的按钮失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 获取有权限的数据授权（根据用户账号和系统code和授权业务对象）
	 * @param userJson
	 * @param paramJson:{userLoginName:"用户账号",appCode:"系统CODE",itemCode:"授权业务对象code"} 不传递appCode默认查询所有系统的
	 * @return
	 */
	@Override
	public String getDataPointAuthByUserLoginNameAndAppCodeAndItemCode(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userId = (String)param.get("userLoginName");
			if(null == userId || userId.equals("")){
				throw new InvalidCustomException("传递的用户为空");
			}else{
				List<Map<String,Object>> list=dataPermissionService.getDataPointAuthByUserLoginNameAndAppCodeAndItemCode(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取有权限的按钮成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取有权限的按钮失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取有权限的按钮失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据数据授权ID查询值
	 * @param userJson
	 * @param paramJson:{dataPermissionId:"数据授权ID"}
	 * @return
	 */
	@Override
	public String getDataPointValAuthByDataPermissionId(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userId = (String)param.get("userLoginName");
			String appId = (String)param.get("appCode");
			if(null == userId || userId.equals("")){
				throw new InvalidCustomException("传递的用户为空");
			}else if(null == appId || appId.equals("")){
				throw new InvalidCustomException("传递的系统为空");
			}else{
				List<Map<String,Object>> list=dataPermissionService.getDataPointValAuthByDataPermissionId(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取有权限的按钮成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取有权限的按钮失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取有权限的按钮失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
