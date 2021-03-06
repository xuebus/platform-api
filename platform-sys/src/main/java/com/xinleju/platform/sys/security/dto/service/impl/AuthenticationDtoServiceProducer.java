package com.xinleju.platform.sys.security.dto.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.xinleju.platform.sys.org.dao.OrgnazationDao;
import com.xinleju.platform.sys.org.entity.Orgnazation;
import com.xinleju.platform.sys.org.entity.UserTrial;
import com.xinleju.platform.sys.org.service.*;
import com.xinleju.platform.utils.RandomString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.service.DataSourceBeanService;
import com.xinleju.platform.base.utils.AdAuthenticationUtil;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.SecurityOrganizationDto;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.sys.log.entity.LogDubbo;
import com.xinleju.platform.sys.log.entity.LogOperation;
import com.xinleju.platform.sys.log.service.LogDubboService;
import com.xinleju.platform.sys.log.service.LogOperationService;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.PostDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.utils.EncryptionUtils;
import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.service.FuncPermissionService;
import com.xinleju.platform.sys.res.service.ResourceService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.security.dto.service.AuthenticationDtoServiceCustomer;
import com.xinleju.platform.sys.security.service.AuthenticationService;
import com.xinleju.platform.sys.sync.service.SyncDataService;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.utils.BrowseTool;
import com.xinleju.platform.utils.SyncDataThread;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author admin
 * 
 *
 */
 
public class AuthenticationDtoServiceProducer implements AuthenticationDtoServiceCustomer{
	private static Logger log = Logger.getLogger(AuthenticationDtoServiceProducer.class);
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private StandardRoleService standardRoleService;
	
	@Autowired
	private PostService postService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrgnazationService orgnazationService;
	@Autowired
	private LogOperationService logOperationService;
	@Autowired
	private SyncDataService syncDataService;

	@Autowired
	private DataSourceBeanService dataSourceBeanService;

	@Autowired
	private LogDubboService logDubboService;


	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private FuncPermissionService funcPermissionService;
	@Autowired
	private OrgnazationDao orgnazationDao;
//	
//	@Autowired  shiyong@xinyuan
//	private StandardRoleService standardRoleService;

	@Override
	public String login(String userInfo, String getJson)
	 {
		
		System.out.println("进入dubbo   login  方法");
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			User user=JacksonUtils.fromJson(getJson, User.class);
			List<User> userList = authenticationService.login(user);
			String passwordNew = EncryptionUtils.getEncryptInfo(user.getLoginName(), user.getPassword());
			
			if(null==userList || userList.size() ==0){
				info.setResult("");
			    info.setSucess(false);
			    info.setCode(ErrorInfoCode.LOGIN_LOGINNAMEERROR.getValue());
			    info.setMsg(ErrorInfoCode.LOGIN_LOGINNAMEERROR.getName());
			}else if(userList.size()>1){
				info.setResult("");
			    info.setSucess(false);
			    info.setCode(ErrorInfoCode.LOGIN_MORE.getValue());
			    info.setMsg(ErrorInfoCode.LOGIN_MORE.getName());
			}else{
				User userNew = userList.get(0);
				if(userNew.getPassword().equals(passwordNew)){
					userNew.setPassword("");
					info.setResult(JacksonUtils.toJson(userNew));
				    info.setSucess(true);
				    info.setCode(ErrorInfoCode.LOGIN_SUCCESS.getValue());
				    info.setMsg(ErrorInfoCode.LOGIN_SUCCESS.getName());
				}else{
					info.setResult("");
				    info.setSucess(false);
				    info.setCode(ErrorInfoCode.LOGIN_PASSWORDERROR.getValue());
				    info.setMsg(ErrorInfoCode.LOGIN_PASSWORDERROR.getName());
				}
			}
		} catch (Exception e) {
			 log.error("登录失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setCode(ErrorInfoCode.LOGIN_ERROR.getValue());
			 info.setMsg(ErrorInfoCode.LOGIN_ERROR.getName());
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	@Override
	public String preCheck(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			User user=JacksonUtils.fromJson(paramater, User.class);
			
			List<User> userList = authenticationService.login(user);
			
			if(null==userList || userList.size() ==0){
				info.setResult("");
			    info.setSucess(false);
			    info.setCode(ErrorInfoCode.LOGIN_LOGINNAMEERROR.getValue());
			    info.setMsg(ErrorInfoCode.LOGIN_LOGINNAMEERROR.getName());
			}else if(userList.size()>1){
				info.setResult("");
			    info.setSucess(false);
			    info.setCode(ErrorInfoCode.LOGIN_MORE.getValue());
			    info.setMsg(ErrorInfoCode.LOGIN_MORE.getName());
			}else{
				User userNew = userList.get(0);
				userNew.setPassword("");
				info.setResult(JacksonUtils.toJson(userNew));
			    info.setSucess(true);
			    info.setCode(ErrorInfoCode.LOGIN_SUCCESS.getValue());
			    info.setMsg(ErrorInfoCode.LOGIN_SUCCESS.getName());
			}
		} catch (Exception e) {
			 log.error("登录失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setCode(ErrorInfoCode.LOGIN_ERROR.getValue());
			 info.setMsg(ErrorInfoCode.LOGIN_ERROR.getName());
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getUserAuthenticationInfo(String userInfo, String paramater)
			throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			User user=JacksonUtils.fromJson(paramater, User.class);
			Map<String,Object> map = new HashMap<String,Object>();
			AuthenticationDto authenticationDto = new AuthenticationDto();
			map.put("userId", user.getId());
			//获取用户的角色
			Date t0 = new Date();
			List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(map);
			
			//获取用户的通用角色
			List<StandardRoleDto> currencyRoleDtoList =  standardRoleService.queryCurrencyRoleListByUserId(map);
			
			Date t1 = new Date();
//			System.out.println("获取用户角色用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
			//获取用户的岗位
			List<PostDto> postDtoList = postService.queryAuthPostListByUserId(map);
			Date t2 = new Date();
//			System.out.println("获取用户岗位用时：：：：：：：：："+(t2.getTime()-t1.getTime()));
			//当前用户的所属组织
			map.put("orgId", user.getBelongOrgId());
			//获取当前用户的组织机构
			List<OrgnazationDto> orgnazationDtoList = orgnazationService.queryAuthOrgListByOrgId(map);
			
			if(null != orgnazationDtoList && orgnazationDtoList.size()>0){
				
				//直属组织
				OrgnazationDto orgnazationDto= orgnazationDtoList.get(orgnazationDtoList.size()-1);
				//顶级组织
				OrgnazationDto topOrgnazationDto= orgnazationDtoList.get(0);
				//公司
				List<OrgnazationDto> companyList = new ArrayList<OrgnazationDto>();
				//部门
				List<OrgnazationDto> deptList = new ArrayList<OrgnazationDto>();
				for(OrgnazationDto orgDto:orgnazationDtoList){
					if(orgDto.getType().equals("company") || orgDto.getType().equals("zb")){
						companyList.add(orgDto);
					}
					if(orgDto.getType().equals("dept")){
						deptList.add(orgDto);
					}
				}
				//设置当前用户所在组织的类型
				authenticationDto.setOrganizationType(orgnazationDto.getType());
				//设置当前用户所在的组织，分期、项目、部门、顶级部门、公司、顶级公司
				if(orgnazationDto.getType().equals("branch")){
					//设置分期
					authenticationDto.setBranchDto(orgnazationDto);
					//设置项目
					authenticationDto.setGroupDto(orgnazationDtoList.get(orgnazationDtoList.size()-2));
					//设置直属公司
					authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
					//设置顶级公司
					authenticationDto.setTopCompanyDto(companyList.get(0));
					//顶级公司也可以如下设置
					//authenticationDto.setTopCompanyDto(topOrgnazationDto);
				}else if(orgnazationDto.getType().equals("group")){
					//设置项目
					authenticationDto.setGroupDto(orgnazationDto);
					//设置直属公司
					authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
					//设置顶级公司
					authenticationDto.setTopCompanyDto(companyList.get(0));
				}else if(orgnazationDto.getType().equals("dept")){
					//设置直属部门
					authenticationDto.setDirectDeptDto(orgnazationDto);
					//设置顶级部门
					authenticationDto.setTopDeptDto(deptList.get(0));
					//设置直属公司
					authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
					//设置顶级公司
					authenticationDto.setTopCompanyDto(companyList.get(0));
				}else if(orgnazationDto.getType().equals("company")){
					//设置直属公司
					authenticationDto.setDirectCompanyDto(orgnazationDto);
					//设置顶级公司
					authenticationDto.setTopCompanyDto(topOrgnazationDto);
				}else if(orgnazationDto.getType().equals("zb")){
					//设置直属公司
					authenticationDto.setDirectCompanyDto(orgnazationDto);
					//设置顶级公司
					authenticationDto.setTopCompanyDto(topOrgnazationDto);
				}
			}
			//用户的ID，用户标准岗位ID，用户角色ID，用户的岗位ID，用户的所有组织id及上级组织id
			Set<String> list = new HashSet<>();
			/*if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
				for(StandardRoleDto srDto:standardRoleDtoList){
					list.add(srDto.getId());
				}
				map.put("roleIds", list);
				//获取用户授权的菜单
				List<ResourceDto> resourceDtoList =  resourceService.queryAuthMenu(map);
				authenticationDto.setResourceDtoList(resourceDtoList);
			}*/
			
			//用户的标准岗位ID
			if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
				for(StandardRoleDto srDto:standardRoleDtoList){
					list.add(srDto.getId());
				}
			}
			
			//用户的岗位ID
			if(null != postDtoList && postDtoList.size()>0){
				for(PostDto srDto:postDtoList){
					list.add(srDto.getId());
				}
			}
			//用户的组织ID
			List<OrgnazationDto> userOrgs=orgnazationDao.selectUserAllOrgs(map);
			Set<String> orgIds = new HashSet<>();
			if(CollectionUtils.isNotEmpty(userOrgs)) {
				for (int i = 0; i < userOrgs.size(); i++) {
					orgIds.add(userOrgs.get(i).getId());
				}
			}
			map.put("orgIds",orgIds);
			List<Map<String,Object>> orgs = orgnazationDao.selectAllUpOrgs(map);
			if(CollectionUtils.isNotEmpty(orgs)){
				for(Map<String,Object> org : orgs){
					list.add((String)org.get("parentId"));
				}
			}
			//当前用户的ID
			list.add(user.getId());
			
			//用户的通用角色ID
			if(null != currencyRoleDtoList && currencyRoleDtoList.size()>0){
				for(StandardRoleDto srDto:currencyRoleDtoList){
					list.add(srDto.getId());
				}
			}
			
			Date t3 = new Date();
//			System.out.println("获取用户岗位用时：：：：：：：：："+(t3.getTime()-t2.getTime()));
			map.put("roleIds", list);
			//获取用户的菜单清单（已授权的未授权的）
			List<ResourceDto> resourceDtoList =  funcPermissionService.queryAuthorizationAllList(map);
			Date t4 = new Date();
//			System.out.println("获取用户菜单用时：：：：：：：：："+(t4.getTime()-t3.getTime()));
			authenticationDto.setResourceDtoList(resourceDtoList);
			
			//标准岗位
			authenticationDto.setStandardRoleDtoList(standardRoleDtoList);
			//通用角色
			authenticationDto.setCurrencyRoleDtoList(currencyRoleDtoList);
			
			authenticationDto.setPostDtoList(postDtoList);
			info.setResult(JacksonUtils.toJson(authenticationDto));
		    info.setSucess(true);
		    info.setMsg("获取认证信息成功");
		} catch (Exception e) {
			 e.printStackTrace();
			 log.error("获取认证信息失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg(e.getMessage());
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 用户重置密码
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	@Override
	public String resetPwd(String userInfo, String paramater)throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
			String password=(String)map.get("password");
			String id=(String)map.get("id");
			User user=userService.getObjectById(id);
			user.setPassword(EncryptionUtils.getEncryptInfo(user.getLoginName(),password));
			int result = userService.update(user);
			//TODO(gyh--AD)
//			int result = userService.updateAndAd(user,password);
			SecurityUserBeanInfo securityUserBeanInfo=JacksonUtils.fromJson(userInfo, SecurityUserBeanInfo.class);
			try{
				//同步数据到第三方系统
				SyncDataThread sdt = new SyncDataThread(user.getId(),syncDataService,securityUserBeanInfo.getTendCode());
				sdt.start();
			}catch(Exception e){
			}
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("设置密码成功!");
		} catch (Exception e) {
			log.error("设置密码失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg(e.getMessage());
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	//登录记录日志
	@Override
	public String loginInLog(String userInfo, String paramater) throws Exception {
		Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		info.setSucess(true);
		//记录退出日志
		try {
			SecurityUserBeanInfo beanInfo=LoginUtils.getSecurityUserBeanInfo();
			LogOperation ope=new LogOperation();
			ope.setId(IDGenerator.getUUID());
			if(beanInfo!=null && beanInfo.getSecurityUserDto() != null){
				SecurityUserDto user=beanInfo.getSecurityUserDto();
				ope.setLoginName(user.getLoginName());
				ope.setName(user.getRealName());
				ope.setComId(user.getBelongOrgId());
				if (beanInfo.getSecurityDirectCompanyDto()!=null) {
					ope.setComName(beanInfo.getSecurityDirectCompanyDto().getName());
				}
			}else{
				ope.setLoginName(null);
				ope.setName(null);
				ope.setComId(null);				
			}
			if(map.containsKey("ip")){
				ope.setLoginIp(map.get("ip").toString());
			}
			if(map.containsKey("loginBrowser")){
				ope.setLoginBrowser(BrowseTool.checkBrowse(map.get("loginBrowser").toString()));
			}
			ope.setOperationTime(new Timestamp(System.currentTimeMillis()));
			ope.setOperationTypeId("1");//登录退出，由平台记录，记录用户登录和退出情况
			ope.setSysCode("6fbd2eb96cde4bb699e4e481b3bf8ce7");//平台系统
			ope.setNode("登录");
			if(map.containsKey("msg")){
				ope.setNote(map.get("msg").toString());
			}
			if(ope.getName()==null && map.containsKey("loginName")){
				ope.setLoginName(map.get("loginName").toString());
			}
			logOperationService.save(ope);
		} catch (Exception e) {
		}
		return JacksonUtils.toJson(info);
	}
	

	//退出记录日志
	@Override
	public String loginOutLog(String userInfo, String paramater) throws Exception {
		Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		info.setSucess(true);
		//记录退出日志
		try {
			SecurityUserBeanInfo beanInfo=LoginUtils.getSecurityUserBeanInfo();
			LogOperation ope=new LogOperation();
			ope.setId(IDGenerator.getUUID());
			if(beanInfo!=null && beanInfo.getSecurityUserDto() != null){
				SecurityUserDto user=beanInfo.getSecurityUserDto();
				ope.setLoginName(user.getLoginName());
				ope.setName(user.getRealName());
				ope.setComId(user.getBelongOrgId());
				if (beanInfo.getSecurityDirectCompanyDto()!=null) {
					ope.setComName(beanInfo.getSecurityDirectCompanyDto().getName());
				}
			}else{
				ope.setLoginName(null);
				ope.setName(null);
				ope.setComId(null);				
			}
			if(map.containsKey("ip")){
				ope.setLoginIp(map.get("ip").toString());
			}
			if(map.containsKey("loginBrowser")){
				ope.setLoginBrowser(BrowseTool.checkBrowse(map.get("loginBrowser").toString()));
			}
			ope.setOperationTime(new Timestamp(System.currentTimeMillis()));
			ope.setOperationTypeId("1");//登录退出，由平台记录，记录用户登录和退出情况
			ope.setSysCode("6fbd2eb96cde4bb699e4e481b3bf8ce7");//平台系统
			ope.setNode("退出");
			if(map.containsKey("msg")){
				ope.setNote(map.get("msg").toString());
			}
			logOperationService.save(ope);
		} catch (Exception e) {
		}
		return JacksonUtils.toJson(info);
	}
	//注册记录日志 
	@Override
	public String registerLog(String userInfo, String paramater) throws Exception {
		Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		info.setSucess(true);
		//记录退出日志
		try {
			LogOperation ope=new LogOperation();
			ope.setId(IDGenerator.getUUID());
			if(map.containsKey("loginName")){
				ope.setLoginName(map.get("loginName").toString());
			}
			if(map.containsKey("name")){
				ope.setName(map.get("name").toString());
			}
			if(map.containsKey("companyName")){
				ope.setComName(map.get("companyName").toString());
			}
			ope.setComId(null);				
			if(map.containsKey("ip")){
				ope.setLoginIp(map.get("ip").toString());
			}
			if(map.containsKey("loginBrowser")){
				ope.setLoginBrowser(BrowseTool.checkBrowse(map.get("loginBrowser").toString()));
			}
			ope.setOperationTime(new Timestamp(System.currentTimeMillis()));
			ope.setOperationTypeId("2");//租户注册
			ope.setSysCode("6fbd2eb96cde4bb699e4e481b3bf8ce7");//平台系统
			ope.setNode("租户注册");
			if(map.containsKey("msg")){
				ope.setNote(map.get("msg").toString());
			}
			logOperationService.save(ope);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return JacksonUtils.toJson(info);
	}
	@Override
	public String getDomain(String userInfo, String paramater) throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		
		// TODO Auto-generated method stub
		Map map=JacksonUtils.fromJson(paramater, HashMap.class);
		Map<String,Object> demain=dataSourceBeanService.queryTendDemainInfo(map);
		if(null == demain){
			info.setSucess(false);
			info.setCode(ErrorInfoCode.LOGIN_DOMAINNULLERROR.getValue());
			info.setMsg(ErrorInfoCode.LOGIN_DOMAINNULLERROR.getName());
		}else{
			String status = demain.get("status").toString();
			String dueTime = demain.get("dueTime").toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dt1 = df.parse(dueTime);
			Date dt2 = new Date();
			//判断租户是否过期
			if(dt1.getTime() < dt2.getTime()){
				info.setSucess(false);
				info.setCode(ErrorInfoCode.LOGIN_DOMAINDUETIMEERROR.getValue());
				info.setMsg(ErrorInfoCode.LOGIN_DOMAINDUETIMEERROR.getName());
			}else{
				//判断租户是否禁用
				if(status.equals("1")){
					info.setSucess(true);
					info.setCode(ErrorInfoCode.LOGIN_SUCCESS.getValue());
					info.setMsg(ErrorInfoCode.LOGIN_SUCCESS.getName());
				}else{
					info.setSucess(false);
					info.setCode(ErrorInfoCode.LOGIN_DOMAINSTATUSERROR.getValue());
					info.setMsg(ErrorInfoCode.LOGIN_DOMAINSTATUSERROR.getName());
				}
			}
		}
		info.setResult(JacksonUtils.toJson(demain));
	    return JacksonUtils.toJson(info);
	}
	//打开菜单记录日志 
	@Override
	public String menuLog(String userInfo, String paramater) throws Exception {
		LogOperation ope=JacksonUtils.fromJson(paramater, LogOperation.class);
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		info.setSucess(true);
		try {
			SecurityUserBeanInfo beanInfo=LoginUtils.getSecurityUserBeanInfo();
			ope.setId(IDGenerator.getUUID());
			if(beanInfo!=null && beanInfo.getSecurityUserDto() !=null){
				SecurityUserDto u=beanInfo.getSecurityUserDto();
				ope.setLoginName(u.getLoginName());
				ope.setName(u.getRealName());
				if (beanInfo.getSecurityDirectCompanyDto()!=null) {
					SecurityOrganizationDto c=beanInfo.getSecurityDirectCompanyDto();
					ope.setComName(c.getPrefixName());
					ope.setComId(c.getId());				
				}
			}
			if (ope.getLoginBrowser()!=null) {
				ope.setLoginBrowser(BrowseTool.checkBrowse(ope.getLoginBrowser()));
			}
			ope.setOperationTime(new Timestamp(System.currentTimeMillis()));
			ope.setOperationTypeId("3");//打开菜单
//			ope.setSysCode("PT");//平台系统
			logOperationService.save(ope);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return JacksonUtils.toJson(info);
	}
	//操作业务日志 
	@Override
	public String opeLog(String userInfo, String paramater) throws Exception {
		LogOperation ope=JacksonUtils.fromJson(paramater, LogOperation.class);
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		info.setSucess(true);
		try {
			SecurityUserBeanInfo beanInfo=LoginUtils.getSecurityUserBeanInfo();
			ope.setId(IDGenerator.getUUID());
			if(beanInfo!=null && beanInfo.getSecurityUserDto() !=null){
				SecurityUserDto u=beanInfo.getSecurityUserDto();
				ope.setLoginName(u.getLoginName());
				ope.setName(u.getRealName());
				if (beanInfo.getSecurityDirectCompanyDto()!=null) {
					SecurityOrganizationDto c=beanInfo.getSecurityDirectCompanyDto();
					ope.setComName(c.getName());
					ope.setComId(c.getId());				
				}
			}
			if (ope.getLoginBrowser()!=null) {
				ope.setLoginBrowser(BrowseTool.checkBrowse(ope.getLoginBrowser()));
			}
			ope.setOperationTime(new Timestamp(System.currentTimeMillis()));
			ope.setOperationTypeId("4");//操作业务
			logOperationService.save(ope);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return JacksonUtils.toJson(info);
	}
	//dubbo服务记录日志 
	@Override
	public String dubboLog(String userInfo, String paramater) throws Exception {
		LogDubbo logDubbo=JacksonUtils.fromJson(paramater, LogDubbo.class);
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		info.setSucess(true);
		try {
			logDubbo.setId(IDGenerator.getUUID());
			logDubboService.save(logDubbo);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getUserAuthenticationInfoWithoutResource(String userInfo, String paramater)
			throws Exception {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			User user=JacksonUtils.fromJson(paramater, User.class);
			Map<String,Object> map = new HashMap<String,Object>();
			AuthenticationDto authenticationDto = new AuthenticationDto();
			map.put("userId", user.getId());
			//获取用户的角色
			Date t0 = new Date();
			List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(map);

			//获取用户的通用角色
			List<StandardRoleDto> currencyRoleDtoList =  standardRoleService.queryCurrencyRoleListByUserId(map);

			Date t1 = new Date();
//			System.out.println("获取用户角色用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
			//获取用户的岗位
			List<PostDto> postDtoList = postService.queryAuthPostListByUserId(map);
			Date t2 = new Date();
//			System.out.println("获取用户岗位用时：：：：：：：：："+(t2.getTime()-t1.getTime()));
			//当前用户的所属组织
			map.put("orgId", user.getBelongOrgId());
			//获取当前用户的组织机构
			List<OrgnazationDto> orgnazationDtoList = orgnazationService.queryAuthOrgListByOrgId(map);

			if(null != orgnazationDtoList && orgnazationDtoList.size()>0){

				//直属组织
				OrgnazationDto orgnazationDto= orgnazationDtoList.get(orgnazationDtoList.size()-1);
				//顶级组织
				OrgnazationDto topOrgnazationDto= orgnazationDtoList.get(0);
				//公司
				List<OrgnazationDto> companyList = new ArrayList<OrgnazationDto>();
				//部门
				List<OrgnazationDto> deptList = new ArrayList<OrgnazationDto>();
				for(OrgnazationDto orgDto:orgnazationDtoList){
					if(orgDto.getType().equals("company") || orgDto.getType().equals("zb")){
						companyList.add(orgDto);
					}
					if(orgDto.getType().equals("dept")){
						deptList.add(orgDto);
					}
				}
				//设置当前用户所在组织的类型
				authenticationDto.setOrganizationType(orgnazationDto.getType());
				//设置当前用户所在的组织，分期、项目、部门、顶级部门、公司、顶级公司
				if(orgnazationDto.getType().equals("branch")){
					//设置分期
					authenticationDto.setBranchDto(orgnazationDto);
					//设置项目
					authenticationDto.setGroupDto(orgnazationDtoList.get(orgnazationDtoList.size()-2));
					//设置直属公司
					authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
					//设置顶级公司
					authenticationDto.setTopCompanyDto(companyList.get(0));
					//顶级公司也可以如下设置
					//authenticationDto.setTopCompanyDto(topOrgnazationDto);
				}else if(orgnazationDto.getType().equals("group")){
					//设置项目
					authenticationDto.setGroupDto(orgnazationDto);
					//设置直属公司
					authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
					//设置顶级公司
					authenticationDto.setTopCompanyDto(companyList.get(0));
				}else if(orgnazationDto.getType().equals("dept")){
					//设置直属部门
					authenticationDto.setDirectDeptDto(orgnazationDto);
					//设置顶级部门
					authenticationDto.setTopDeptDto(deptList.get(0));
					//设置直属公司
					authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
					//设置顶级公司
					authenticationDto.setTopCompanyDto(companyList.get(0));
				}else if(orgnazationDto.getType().equals("company")){
					//设置直属公司
					authenticationDto.setDirectCompanyDto(orgnazationDto);
					//设置顶级公司
					authenticationDto.setTopCompanyDto(topOrgnazationDto);
				}else if(orgnazationDto.getType().equals("zb")){
					//设置直属公司
					authenticationDto.setDirectCompanyDto(orgnazationDto);
					//设置顶级公司
					authenticationDto.setTopCompanyDto(topOrgnazationDto);
				}
			}
			//用户的ID，用户标准岗位ID，用户角色ID，用户的岗位ID
			Set<String> list = new HashSet<>();
			/*if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
				for(StandardRoleDto srDto:standardRoleDtoList){
					list.add(srDto.getId());
				}
				map.put("roleIds", list);
				//获取用户授权的菜单
				List<ResourceDto> resourceDtoList =  resourceService.queryAuthMenu(map);
				authenticationDto.setResourceDtoList(resourceDtoList);
			}*/

			//用户的标准岗位ID
			if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
				for(StandardRoleDto srDto:standardRoleDtoList){
					list.add(srDto.getId());
				}
			}

			//用户的岗位ID
			if(null != postDtoList && postDtoList.size()>0){
				for(PostDto srDto:postDtoList){
					list.add(srDto.getId());
				}
			}

			//当前用户的ID
			list.add(user.getId());

			//用户的通用角色ID
			if(null != currencyRoleDtoList && currencyRoleDtoList.size()>0){
				for(StandardRoleDto srDto:currencyRoleDtoList){
					list.add(srDto.getId());
				}
			}

			Date t3 = new Date();
//			System.out.println("获取用户岗位用时：：：：：：：：："+(t3.getTime()-t2.getTime()));
			map.put("roleIds", list);

			//标准岗位
			authenticationDto.setStandardRoleDtoList(standardRoleDtoList);
			//通用角色
			authenticationDto.setCurrencyRoleDtoList(currencyRoleDtoList);

			authenticationDto.setPostDtoList(postDtoList);
			info.setResult(JacksonUtils.toJson(authenticationDto));
			info.setSucess(true);
			info.setMsg("获取认证信息成功");
		} catch (Exception e) {
			log.error("获取认证信息失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg(e.getMessage());
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryUserPostRoleList(String userInfo, String parameter) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> userMap =JacksonUtils.fromJson(parameter, Map.class);
			Map<String,Object> map = new HashMap<String,Object>();
			AuthenticationDto authenticationDto = new AuthenticationDto();
			String userId = (String) userMap.get("userId");
			map.put("userId", userId);

			/*User user = userService.getObjectById(userId);
			String orgId = user.getBelongOrgId();*/
			//获取用户的角色
			Date t0 = new Date();
			List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(map);

			//获取用户的通用角色
			List<StandardRoleDto> currencyRoleDtoList =  standardRoleService.queryCurrencyRoleListByUserId(map);

			Date t1 = new Date();
			//获取用户的岗位
			List<PostDto> postDtoList = postService.queryAuthPostListByUserId(map);
			//标准岗位
			//authenticationDto.setStandardRoleDtoList(standardRoleDtoList);
			//岗位
			//authenticationDto.setPostDtoList(postDtoList);
			//通用角色
			//authenticationDto.setCurrencyRoleDtoList(currencyRoleDtoList);

			List<Map<String,Object>> treeMapList = new ArrayList<Map<String,Object>>();
			Map<String,Object> standarRoleMap = new HashMap<String,Object>();
			standarRoleMap.put("id","1");
			standarRoleMap.put("name","标准岗位");
			standarRoleMap.put("pId",null);

			Map<String,Object> postMap = new HashMap<String,Object>();
			postMap.put("id","2");
			postMap.put("name","岗位");
			postMap.put("pId",null);

			Map<String,Object> roleMap = new HashMap<String,Object>();
			roleMap.put("id","3");
			roleMap.put("name","角色");
			roleMap.put("pId",null);

			treeMapList.add(standarRoleMap);
			treeMapList.add(postMap);
			treeMapList.add(roleMap);

			//标准岗位
			for (StandardRoleDto standardRoleDto:standardRoleDtoList) {
				Map<String,Object> newMap = new HashMap<String,Object>();
				newMap.put("pId","1");
				newMap.put("id",standardRoleDto.getId());
				newMap.put("name",standardRoleDto.getPrefixName());
				treeMapList.add(newMap);
			}

			//岗位
			for (PostDto postDto:postDtoList) {
				Map<String,Object> newMap = new HashMap<String,Object>();
				newMap.put("pId","2");
				newMap.put("id",postDto.getId());
				newMap.put("name",postDto.getOrgPrefixName()+"/"+postDto.getName());
				treeMapList.add(newMap);
			}

			//角色
			for (StandardRoleDto currencyRoleDto:currencyRoleDtoList) {
				Map<String,Object> newMap = new HashMap<String,Object>();
				newMap.put("pId","3");
				newMap.put("id",currencyRoleDto.getId());
				newMap.put("name",currencyRoleDto.getPrefixName());
				treeMapList.add(newMap);
			}

			info.setResult(JacksonUtils.toJson(treeMapList));
			info.setSucess(true);
			info.setMsg("获取用户岗位/角色/标准岗位成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户岗位/角色/标准岗位失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg(e.getMessage());
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
