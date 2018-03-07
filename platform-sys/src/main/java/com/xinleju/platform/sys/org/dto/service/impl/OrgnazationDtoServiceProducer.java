package com.xinleju.platform.sys.org.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinleju.platform.sys.org.dto.OrgnazationExcelDto;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.utils.StatusType;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.OrgnazationTreeNodeDto;
import com.xinleju.platform.sys.org.dto.OrgnazationUpdateDto;
import com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.Orgnazation;
import com.xinleju.platform.sys.org.entity.Post;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.sys.org.service.PostService;
import com.xinleju.platform.sys.org.service.StandardRoleService;
import com.xinleju.platform.sys.org.service.UserService;
import com.xinleju.platform.sys.res.service.AppSystemService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */

public class OrgnazationDtoServiceProducer implements OrgnazationDtoServiceCustomer{
	private static Logger log = Logger.getLogger(OrgnazationDtoServiceProducer.class);
	@Autowired
	private OrgnazationService orgnazationService;

	@Autowired
	private UserService userService;

	@Autowired
	private StandardRoleService standardRoleService;

	@Autowired
	private PostService postService;
	

	@Autowired
	private AppSystemService appSystemService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Orgnazation orgnazation=JacksonUtils.fromJson(saveJson, Orgnazation.class);
			if(orgnazation.getParentId()!=null&&StringUtils.isNotBlank(orgnazation.getParentId())){
				Orgnazation fa=orgnazationService.getObjectById(orgnazation.getParentId());
				String prefixName = fa.getPrefixName();
				prefixName=prefixName.replaceAll("\\\\", "\\\\\\\\");
				prefixName=prefixName.replaceAll("'", "\\\\\'");
				orgnazation.setPrefixId(fa.getPrefixId()+"/"+orgnazation.getId());
				orgnazation.setPrefixName(prefixName+"/"+orgnazation.getName());
			}else{
				orgnazation.setPrefixId(orgnazation.getId());
				orgnazation.setPrefixName(orgnazation.getPrefixName());
			}
			orgnazation=JacksonUtils.fromJson(saveJson, Orgnazation.class);
			
			if(null == orgnazation.getSort()){
//				Map<String,Object> map=new HashMap<String,Object>();
//				map.put("tableName", "pt_sys_org_orgnazation");
//				Long maxSort=appSystemService.getMaxSort(map)+1L;//排序号自动加1
				orgnazation.setSort(99L);
			}
			//校验编码重复
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("code", orgnazation.getCode());
			map.put("parentId", orgnazation.getParentId());
			int c=orgnazationService.checkCode(map);
			if(c>0){
				throw new InvalidCustomException("同级编码已存在，不可重复");
			}
			orgnazationService.save(orgnazation);
			info.setResult(JacksonUtils.toJson(orgnazation));
			info.setSucess(true);
			info.setMsg("保存对象成功!");
		} catch (Exception e) {
			log.error("保存对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			int result= orgnazationService.updateNew(updateJson);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("更新对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 禁用启用组织结构
	 * @param paramater
	 * @return
	 */
	@Override
	public String updateOrgStatus(String userInfo, String updateJson)  {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map = JacksonUtils.fromJson(updateJson, HashMap.class);
			Integer status=(Integer)map.get("status");
			String id=(String)map.get("orgId");
			if(status == null){
				throw new InvalidCustomException("修改状态status不可为空");
			}
			if(id == null){
				throw new InvalidCustomException("组织主键orgId不可为空");
			}
			int result =0;
			if(status == 0){//禁用组织，并将其下级组织禁用
				Orgnazation org=orgnazationService.getObjectById(id);
				String prefixId=org.getPrefixId();
				map.put("orgId", prefixId);
				result=orgnazationService.lockOrg(map);
			}else if(status == 1){//启用组织，并将其上级组织启用
				result=orgnazationService.unLockOrg(map);
			}else{
				throw new InvalidCustomException("status状态不对");
			}

			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("更新对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson)
	{
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Orgnazation orgnazation=JacksonUtils.fromJson(deleteJson, Orgnazation.class);
			int result= orgnazationService.deleteObjectById(orgnazation.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("删除对象成功!");
		} catch (Exception e) {
			log.error("更新对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
	{
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(deleteJsonList)) {
				Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				List<String> list=Arrays.asList(map.get("id").toString().split(","));
				int result= orgnazationService.deleteAllObjectByIds(list);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("删除对象成功!");
			}
		} catch (Exception e) {
			log.error("删除对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("删除更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson)
	{
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Orgnazation orgnazation=JacksonUtils.fromJson(getJson, Orgnazation.class);
			//			Orgnazation	result = orgnazationService.getObjectById(orgnazation.getId());
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("id", orgnazation.getId());
			OrgnazationDto 	result = orgnazationService.getOrgById(map);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=orgnazationService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}else{
				Page page=orgnazationService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取分页对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取分页对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=orgnazationService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=orgnazationService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Orgnazation orgnazation=JacksonUtils.fromJson(deleteJson, Orgnazation.class);
			int result= orgnazationService.deleteOrgAllSon(orgnazation);
//			int result= orgnazationService.deleteObjectById(orgnazation.getId());
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("删除对象成功!");
		} catch (Exception e) {
			log.error("删除更新对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("删除对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateOrgSort(String userInfo, String paramater)  {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				OrgnazationUpdateDto orgnazationUpdateDto=JacksonUtils.fromJson(paramater, OrgnazationUpdateDto.class);

				//				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				//				String  type = (String)map.get("type");
				//				OrgnazationNodeDto  orgnazationDto_target = (OrgnazationNodeDto)map.get("target");
				//				OrgnazationNodeDto  orgnazationDto_source = (OrgnazationNodeDto)map.get("source");

				String  type = orgnazationUpdateDto.getType();
				OrgnazationTreeNodeDto  orgnazationDto_target = orgnazationUpdateDto.getTarget();
				OrgnazationTreeNodeDto  orgnazationDto_source = orgnazationUpdateDto.getSource();
				
				//查询出来所有组织机构
				List<Orgnazation> list_org  = orgnazationService.queryAllOrgListReturnOrg();

				//判断拖动位置是root情况
				if("inner".equals(type)){
					//拖动目标是目录
					if(orgnazationDto_target.getType().equals("cata")){
						//源拖动组织类型是公司或者集团的可以进行拖拽
						if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb")){
							updateOrgSortRootAndCata(orgnazationDto_target,orgnazationDto_source,list_org);
							info.setResult("更新成功");
							info.setSucess(true);
							info.setMsg("更新组织成功!");
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("目录下面只能放置集团和公司");
						}
						//拖动目标是集团或者公司
					}else if(orgnazationDto_target.getType().equals("zb") || orgnazationDto_target.getType().equals("company")){
						//源拖动组织类型是公司或者集团或者部门或者项目可以进行拖拽--项目分期不允许进行拖拽
						if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb") || orgnazationDto_source.getType().equals("dept") || orgnazationDto_source.getType().equals("group")){
							updateOrgSortRootNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
							info.setResult("更新成功");
							info.setSucess(true);
							info.setMsg("更新组织成功!");
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("集团或公司下面只能放置集团、公司、部门和项目");
						}
						//拖动目标是部门
					}else if(orgnazationDto_target.getType().equals("dept")){
						//源拖动组织类型是部门可以进行拖拽--其他不允许进行拖拽
						if(orgnazationDto_source.getType().equals("dept")){
							updateOrgSortRootNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
							info.setResult("更新成功");
							info.setSucess(true);
							info.setMsg("更新组织成功!");
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("部门下面只能放置部门");
						}
						//拖动目标是项目	
					}else if(orgnazationDto_target.getType().equals("group")){
						//源拖动组织类型是项目分期可以进行拖拽--其他不允许进行拖拽
						if(orgnazationDto_source.getType().equals("branch")){
							updateOrgSortRootNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
							info.setResult("更新成功");
							info.setSucess(true);
							info.setMsg("更新组织成功!");
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("项目团队下面只能放置项目分期");
						}
						//拖动目标是项目分期
					}else if(orgnazationDto_target.getType().equals("branch")){
						info.setResult("更新失败");
						info.setSucess(false);
						info.setMsg("分期下面不能放置任何组织");
					}
					//拖动位置是中间情况
				}else if("next".equals(type)){
					//拖动目标是目录
					if(orgnazationDto_target.getType().equals("cata")){
						info.setResult("更新失败");
						info.setSucess(false);
						info.setMsg("拖动不能和目录同级");
						//拖动目标是集团或者公司
					}else if(orgnazationDto_target.getType().equals("zb") || orgnazationDto_target.getType().equals("company")){
						//判断是否是一级公司或者集团，如果是一级公司或者集团(parentId等于空)，那么要调整位置的组织只能是集团或者公司
						if(null == orgnazationDto_target.getParentId() || "".equals(orgnazationDto_target.getParentId())){
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb")){
								updateOrgSortTopAndCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("目录下面只能放置集团和公司");
							}
						}else{
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb") || orgnazationDto_source.getType().equals("dept") || orgnazationDto_source.getType().equals("group")){
								updateOrgSortTopNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("集团或公司下面只能放置集团、公司、部门和项目");
							}
						}

						//拖动目标是部门
					}else if(orgnazationDto_target.getType().equals("dept")){
						//获取目标的上级组织（上级组织可以是集团、公司、部门）
						Orgnazation	result_target = orgnazationService.getObjectById(orgnazationDto_target.getParentId());
						//如果目标上级组织是公司
						if(result_target.getType().equals("company") || result_target.getType().equals("zb")){
							//源拖动组织类型是公司或者集团或者部门或者项目可以进行拖拽--项目分期不允许进行拖拽
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb") || orgnazationDto_source.getType().equals("dept") || orgnazationDto_source.getType().equals("group")){
								updateOrgSortTopNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("集团或公司下面只能放置集团、公司、部门和项目");
							}
						}else if(result_target.getType().equals("dept")){
							//源拖动组织类型是部门可以进行拖拽--其他不允许进行拖拽
							if(orgnazationDto_source.getType().equals("dept")){
								updateOrgSortTopNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("部门下面只能放置部门");
							}
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("组织机构树存在错误，部门只能在集团、公司、部门下面");
						}

						//拖动目标是项目	
					}else if(orgnazationDto_target.getType().equals("group")){
						//获取目标的上级组织（上级组织可以是集团、公司）
						Orgnazation	result_target = orgnazationService.getObjectById(orgnazationDto_target.getParentId());
						//如果目标上级组织是公司
						if(result_target.getType().equals("company") || result_target.getType().equals("zb")){
							//源拖动组织类型是公司或者集团或者部门或者项目可以进行拖拽--项目分期不允许进行拖拽
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb") || orgnazationDto_source.getType().equals("dept") || orgnazationDto_source.getType().equals("group")){
								updateOrgSortTopNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("集团或公司下面只能放置集团、公司、部门和项目");
							}
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("组织机构树存在错误，项目团队只能在集团或者公司下面");
						}
						//拖动目标是项目分期
					}else if(orgnazationDto_target.getType().equals("branch")){
						//获取目标的上级组织（上级组织可以是项目）
						Orgnazation	result_target = orgnazationService.getObjectById(orgnazationDto_target.getParentId());
						//如果目标上级组织是公司
						if(result_target.getType().equals("group")){
							//源拖动组织类型是公司或者集团或者部门或者项目可以进行拖拽--项目分期不允许进行拖拽
							if(orgnazationDto_source.getType().equals("branch")){
								updateOrgSortTopNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("项目团队下面只能放置项目分期");
							}
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("组织机构树存在错误，项目分期只能在项目团队下面");
						}
					}
				}else if("prev".equals(type)){

					//拖动目标是目录
					if(orgnazationDto_target.getType().equals("cata")){
						info.setResult("更新失败");
						info.setSucess(false);
						info.setMsg("拖动不能和目录同级");
						//拖动目标是集团或者公司
					}else if(orgnazationDto_target.getType().equals("zb") || orgnazationDto_target.getType().equals("company")){
						//判断是否是一级公司或者集团，如果是一级公司或者集团(parentId等于空)，那么要调整位置的组织只能是集团或者公司
						if(null == orgnazationDto_target.getParentId() || "".equals(orgnazationDto_target.getParentId())){
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb")){
								updateOrgSortPrevAndCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("目录下面只能放置集团和公司");
							}
						}else{
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb") || orgnazationDto_source.getType().equals("dept") || orgnazationDto_source.getType().equals("group")){
								updateOrgSortPrevNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("集团或公司下面只能放置集团、公司、部门和项目");
							}
						}

						//拖动目标是部门
					}else if(orgnazationDto_target.getType().equals("dept")){
						//获取目标的上级组织（上级组织可以是集团、公司、部门）
						Orgnazation	result_target = orgnazationService.getObjectById(orgnazationDto_target.getParentId());
						//如果目标上级组织是公司
						if(result_target.getType().equals("company") || result_target.getType().equals("zb")){
							//源拖动组织类型是公司或者集团或者部门或者项目可以进行拖拽--项目分期不允许进行拖拽
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb") || orgnazationDto_source.getType().equals("dept") || orgnazationDto_source.getType().equals("group")){
								updateOrgSortPrevNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("集团或公司下面只能放置集团、公司、部门和项目");
							}
						}else if(result_target.getType().equals("dept")){
							//源拖动组织类型是部门可以进行拖拽--其他不允许进行拖拽
							if(orgnazationDto_source.getType().equals("dept")){
								updateOrgSortPrevNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("部门下面只能放置部门");
							}
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("组织机构树存在错误，部门只能在集团、公司、部门下面");
						}

						//拖动目标是项目	
					}else if(orgnazationDto_target.getType().equals("group")){
						//获取目标的上级组织（上级组织可以是集团、公司）
						Orgnazation	result_target = orgnazationService.getObjectById(orgnazationDto_target.getParentId());
						//如果目标上级组织是公司
						if(result_target.getType().equals("company") || result_target.getType().equals("zb")){
							//源拖动组织类型是公司或者集团或者部门或者项目可以进行拖拽--项目分期不允许进行拖拽
							if(orgnazationDto_source.getType().equals("company") || orgnazationDto_source.getType().equals("zb") || orgnazationDto_source.getType().equals("dept") || orgnazationDto_source.getType().equals("group")){
								updateOrgSortPrevNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("集团或公司下面只能放置集团、公司、部门和项目");
							}
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("组织机构树存在错误，项目团队只能在集团或者公司下面");
						}
						//拖动目标是项目分期
					}else if(orgnazationDto_target.getType().equals("branch")){
						//获取目标的上级组织（上级组织可以是项目）
						Orgnazation	result_target = orgnazationService.getObjectById(orgnazationDto_target.getParentId());
						//如果目标上级组织是公司
						if(result_target.getType().equals("group")){
							//源拖动组织类型是公司或者集团或者部门或者项目可以进行拖拽--项目分期不允许进行拖拽
							if(orgnazationDto_source.getType().equals("branch")){
								updateOrgSortPrevNotCata(orgnazationDto_target,orgnazationDto_source,list_org);
								info.setResult("更新成功");
								info.setSucess(true);
								info.setMsg("更新组织成功!");
							}else{
								info.setResult("更新失败");
								info.setSucess(false);
								info.setMsg("项目团队下面只能放置项目分期");
							}
						}else{
							info.setResult("更新失败");
							info.setSucess(false);
							info.setMsg("组织机构树存在错误，项目分期只能在项目团队下面");
						}
					}

				}
			}else{
				info.setResult("更新失败");
				info.setSucess(true);
				info.setMsg("要更新的组织为空!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("更新失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("更新失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}



	//type是root、target目标是目录的情况下，进行查询下级组织（一级集团或者公司）并修改sort，以及修改要进行调整位置的组织机构sort
	public void updateOrgSortRootAndCata(OrgnazationTreeNodeDto orgnazationDto_target,OrgnazationTreeNodeDto orgnazationDto_source,List<Orgnazation> list_org){
		try {
			//查询出来要调整位置的组织
			Orgnazation	result_source = orgnazationService.getObjectById(orgnazationDto_source.getId());
			
			//查询出来目标组织
			Orgnazation result_target = orgnazationService.getObjectById(orgnazationDto_target.getId());
			
			//判断源拖动组织rootId是否目录的id相同---如果相同不用替换下属组织的所有rootId
			if(orgnazationDto_source.getRootId().equals(result_target.getId())){

			}else{
				//如果不相同，替换下属所有组织的rootId
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****start*****/
				List<Orgnazation> list_return = getOrgList(result_source,list_org);
				for(Orgnazation orgnazation:list_return){
					orgnazation.setRootId(result_target.getId());
					//修改根目录Id
					int result = orgnazationService.update(orgnazation);
				}
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****end*******/
			}

			//获取目录下的一级集团和公司（准备更改sort值）
			List<Orgnazation> list  = orgnazationService.queryOrgListRootReturnOrg(result_target.getId());
			Long newsort = 1L;
			if(null!=list &&list.size()>0){
				Orgnazation orgnazation = list.get(list.size()-1);
				newsort = orgnazation.getSort()+1;
			}
			//			for(Orgnazation orgnazation:list){
			//				//如果组织ID匹配与要调整位置的源组织相同，则跳出循环，以下的组织不用进行更改sort
			//				if(orgnazation.getId().equals(orgnazationDto_source.getId())){
			//					break;
			//				}else{
			//					orgnazation.setSort(orgnazation.getSort()+1L);
			//					//修改sort排序号
			//					int result=   orgnazationService.update(orgnazation);
			//				}
			//			}

			//更改之前获取原先的全路径
			String prefixIdold = result_source.getPrefixId();
			String prefixNameold = result_source.getPrefixName();
			//更改组织的目录ID，父节点ID，sort
			result_source.setRootId(result_target.getId());//修改目录rootId，如果目录进行调整，那么这个是不一致的，进行更改
			result_source.setParentId(null);//因为调整到一级集团或者公司了，所以parentId设置null
			result_source.setSort(newsort);//因为调整到根节点，所以排序号设置为1
			//父级是根目录，所以修改全路径名称
			result_source.setPrefixId(result_source.getId());
			result_source.setPrefixName(result_source.getName());
			//更改之后获取新的全路径
			String prefixId = result_source.getPrefixId();
			String prefixName = result_source.getPrefixName();
			//调用数据库进行修改
			int result=   orgnazationService.update(result_source);

			//如果原先和现在的全路径进行了更改，那么更改所有下级的全路径
			if(!prefixIdold.equals(prefixId) || !prefixNameold.equals(prefixName)){
				Map<String,String> map = new HashMap<String,String>();
				map.put("prefixIdold", prefixIdold);
				map.put("prefixId", prefixId);
				map.put("prefixNameold", prefixNameold);
				map.put("prefixName", prefixName);
				orgnazationService.updatePrefix(map);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}


	//type是root、target目标是集团、公司、部门、项目的情况下，进行查询下级组织并修改sort，以及修改要进行调整位置的组织机构sort
	public void updateOrgSortRootNotCata(OrgnazationTreeNodeDto orgnazationDto_target,OrgnazationTreeNodeDto orgnazationDto_source,List<Orgnazation> list_org){
		try {
			//查询出来要调整位置的组织
			Orgnazation	result_source = orgnazationService.getObjectById(orgnazationDto_source.getId());
			
			//查询出来目标组织
			Orgnazation result_target = orgnazationService.getObjectById(orgnazationDto_target.getId());
			
			//判断源拖动组织rootId是否和目标的目录的id相同---如果相同不用替换下属组织的所有rootId
			if(orgnazationDto_source.getRootId().equals(result_target.getRootId())){

			}else{
				//如果不相同，替换下属所有组织的rootId
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****start*****/
				List<Orgnazation> list_return = getOrgList(result_source,list_org);
				for(Orgnazation orgnazation:list_return){
					orgnazation.setRootId(result_target.getRootId());
					//修改根目录Id
					int result = orgnazationService.update(orgnazation);
				}
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****end*******/
			}
			//获取集团、公司、部门、项目组织结构下一级子节点（准备更改sort值）
			List<Orgnazation> list  = orgnazationService.queryOrgListReturnOrg(result_target.getId());
			Long newsort = 1L;
			if(null!=list &&list.size()>0){
				Orgnazation orgnazation = list.get(list.size()-1);
				newsort = orgnazation.getSort()+1;
			}
			//			for(Orgnazation orgnazation:list){
			//				//如果组织ID匹配与要调整位置的源组织相同，则跳出循环，以下的组织不用进行更改sort
			//				if(orgnazation.getId().equals(orgnazationDto_source.getId())){
			//					break;
			//				}else{
			//					orgnazation.setSort(orgnazation.getSort()+1L);
			//					//修改sort排序号
			//					int result=   orgnazationService.update(orgnazation);
			//				}
			//			}
			//更改之前获取原先的全路径
			String prefixIdold = result_source.getPrefixId();
			String prefixNameold = result_source.getPrefixName();
			//更改组织的目录ID，父节点ID，sort
			result_source.setRootId(result_target.getRootId());//修改目录rootId，如果目录进行调整，那么这个是不一致的，进行更改
			result_source.setParentId(result_target.getId());//目标的ID，即为要更改的组织位置的父ID
			result_source.setSort(newsort);//因为调整到根节点，所以排序号设置为1

			//设置新的全路径名称
			result_source.setPrefixId(result_target.getPrefixId()+"/"+result_source.getId());
			result_source.setPrefixName(result_target.getPrefixName()+"/"+result_source.getName());
			//更改之后获取新的全路径
			String prefixId = result_source.getPrefixId();
			String prefixName = result_source.getPrefixName();
			//调用数据库进行修改
			int result=   orgnazationService.update(result_source);
			//如果原先和现在的全路径进行了更改，那么更改所有下级的全路径
			if(!prefixIdold.equals(prefixId) || !prefixNameold.equals(prefixName)){
				Map<String,String> map = new HashMap<String,String>();
				map.put("prefixIdold", prefixIdold);
				map.put("prefixId", prefixId);
				map.put("prefixNameold", prefixNameold);
				map.put("prefixName", prefixName);
				orgnazationService.updatePrefix(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	//type是top、target目标是一级集团或者公司的情况下，那么上级就是目录，进行查询下级组织（一级集团或者公司）并修改sort，以及修改要进行调整位置的组织机构sort
	public void updateOrgSortTopAndCata(OrgnazationTreeNodeDto orgnazationDto_target,OrgnazationTreeNodeDto orgnazationDto_source,List<Orgnazation> list_org){
		try {
			//查询出来要调整位置的组织
			Orgnazation	result_source = orgnazationService.getObjectById(orgnazationDto_source.getId());
			
			//查询出来目标组织
			Orgnazation result_target = orgnazationService.getObjectById(orgnazationDto_target.getId());
			//判断源拖动组织rootId是否目录的id相同---如果相同不用替换下属组织的所有rootId
			if(orgnazationDto_source.getRootId().equals(result_target.getRootId())){

			}else{
				//如果不相同，替换下属所有组织的rootId
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****start*****/
				List<Orgnazation> list_return = getOrgList(result_source,list_org);
				for(Orgnazation orgnazation:list_return){
					orgnazation.setRootId(result_target.getRootId());
					//修改根目录Id
					int result = orgnazationService.update(orgnazation);
				}
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****end*******/
			}

			//获取目录下的一级集团和公司，排序在目标之后的组织（准备更改sort值）
			List<Orgnazation> list  = orgnazationService.queryOrgListRootReturnOrg(result_target.getRootId(),result_target.getSort());

			for(Orgnazation orgnazation:list){
				//如果组织ID匹配与要调整位置的源组织相同，则跳出循环，以下的组织不用进行更改sort
				if(orgnazation.getId().equals(orgnazationDto_source.getId())){
					break;
				}else{
					orgnazation.setSort(orgnazation.getSort()+1L);
					//修改sort排序号
					int result=   orgnazationService.update(orgnazation);
				}
			}
			//更改之前获取原先的全路径
			String prefixIdold = result_source.getPrefixId();
			String prefixNameold = result_source.getPrefixName();
			//更改组织的目录ID，父节点ID，sort
			result_source.setRootId(result_target.getRootId());//因为是top类型，所以rootId和目标的一致
			result_source.setParentId(result_target.getParentId());//因为是top类型，所以parentId和目标的一致
			result_source.setSort(result_target.getSort()+1L);//因为是top类型，所以排序为目标的下一个排序，设置为+1
			//判断上级是否为空，如果为空全路径为自己，不为空加上上级的全路径
			if(null != result_target.getParentId() && !"".equals(result_target.getParentId())){
				//查询出来父级的组织
				Orgnazation	result_parent = orgnazationService.getObjectById(result_target.getParentId());
				//设置新的全路径名称
				result_source.setPrefixId(result_parent.getPrefixId()+"/"+result_source.getId());
				result_source.setPrefixName(result_parent.getPrefixName()+"/"+result_source.getName());
			}else{
				//设置新的全路径名称
				result_source.setPrefixId(result_source.getId());
				result_source.setPrefixName(result_source.getName());
			}
			//更改之后获取新的全路径
			String prefixId = result_source.getPrefixId();
			String prefixName = result_source.getPrefixName();
			//调用数据库进行修改
			int result=   orgnazationService.update(result_source);
			//如果原先和现在的全路径进行了更改，那么更改所有下级的全路径
			if(!prefixIdold.equals(prefixId) || !prefixNameold.equals(prefixName)){
				Map<String,String> map = new HashMap<String,String>();
				map.put("prefixIdold", prefixIdold);
				map.put("prefixId", prefixId);
				map.put("prefixNameold", prefixNameold);
				map.put("prefixName", prefixName);
				orgnazationService.updatePrefix(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	//type是top、target目标是集团、公司、部门、项目的情况下，进行查询下级组织并修改sort，以及修改要进行调整位置的组织机构sort
	public void updateOrgSortTopNotCata(OrgnazationTreeNodeDto orgnazationDto_target,OrgnazationTreeNodeDto orgnazationDto_source,List<Orgnazation> list_org){
		try {
			//查询出来要调整位置的组织
			Orgnazation	result_source = orgnazationService.getObjectById(orgnazationDto_source.getId());
			//查询出来目标组织
			Orgnazation result_target = orgnazationService.getObjectById(orgnazationDto_target.getId());
			//判断源拖动组织rootId是否目录的id相同---如果相同不用替换下属组织的所有rootId
			if(orgnazationDto_source.getRootId().equals(result_target.getRootId())){

			}else{
				//如果不相同，替换下属所有组织的rootId
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****start*****/
				List<Orgnazation> list_return = getOrgList(result_source,list_org);
				for(Orgnazation orgnazation:list_return){
					orgnazation.setRootId(result_target.getRootId());
					//修改根目录Id
					int result = orgnazationService.update(orgnazation);
				}
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****end*******/
			}

			//获取集团、公司、部门、项目组织结构同一级节点，排序在目标之后的组织（准备更改sort值）
			List<Orgnazation> list  = orgnazationService.queryOrgListReturnOrg(result_target.getParentId(),result_target.getSort());

			for(Orgnazation orgnazation:list){
				//如果组织ID匹配与要调整位置的源组织相同，则跳出循环，以下的组织不用进行更改sort
				if(orgnazation.getId().equals(orgnazationDto_source.getId())){
					break;
				}else{
					orgnazation.setSort(orgnazation.getSort()+1L);
					//修改sort排序号
					int result=   orgnazationService.update(orgnazation);
				}
			}
			//更改之前获取原先的全路径
			String prefixIdold = result_source.getPrefixId();
			String prefixNameold = result_source.getPrefixName();

			//更改组织的目录ID，父节点ID，sort
			result_source.setRootId(result_target.getRootId());//因为是top类型，所以rootId和目标的一致
			result_source.setParentId(result_target.getParentId());//因为是top类型，所以parentId和目标的一致
			result_source.setSort(result_target.getSort()+1L);//因为是top类型，所以排序为目标的下一个排序，设置为+1
			//判断上级是否为空，如果为空全路径为自己，不为空加上上级的全路径
			if(null != result_target.getParentId() && !"".equals(result_target.getParentId())){
				//查询出来父级的组织
				Orgnazation	result_parent = orgnazationService.getObjectById(result_target.getParentId());
				//设置新的全路径名称
				result_source.setPrefixId(result_parent.getPrefixId()+"/"+result_source.getId());
				result_source.setPrefixName(result_parent.getPrefixName()+"/"+result_source.getName());
			}else{
				//设置新的全路径名称
				result_source.setPrefixId(result_source.getId());
				result_source.setPrefixName(result_source.getName());
			}
			//更改之后获取新的全路径
			String prefixId = result_source.getPrefixId();
			String prefixName = result_source.getPrefixName();
			//调用数据库进行修改
			int result=   orgnazationService.update(result_source);
			//如果原先和现在的全路径进行了更改，那么更改所有下级的全路径
			if(!prefixIdold.equals(prefixId) || !prefixNameold.equals(prefixName)){
				Map<String,String> map = new HashMap<String,String>();
				map.put("prefixIdold", prefixIdold);
				map.put("prefixId", prefixId);
				map.put("prefixNameold", prefixNameold);
				map.put("prefixName", prefixName);
				orgnazationService.updatePrefix(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}


	//type是top、target目标是一级集团或者公司的情况下，那么上级就是目录，进行查询下级组织（一级集团或者公司）并修改sort，以及修改要进行调整位置的组织机构sort
	public void updateOrgSortPrevAndCata(OrgnazationTreeNodeDto orgnazationDto_target,OrgnazationTreeNodeDto orgnazationDto_source,List<Orgnazation> list_org){
		try {
			//查询出来要调整位置的组织
			Orgnazation	result_source = orgnazationService.getObjectById(orgnazationDto_source.getId());
			//查询出来目标位置的组织
			Orgnazation	result_taget = orgnazationService.getObjectById(orgnazationDto_target.getId());
			//判断源拖动组织rootId是否目录的id相同---如果相同不用替换下属组织的所有rootId
			if(orgnazationDto_source.getRootId().equals(result_taget.getRootId())){

			}else{
				//如果不相同，替换下属所有组织的rootId
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****start*****/
				List<Orgnazation> list_return = getOrgList(result_source,list_org);
				for(Orgnazation orgnazation:list_return){
					orgnazation.setRootId(result_taget.getRootId());
					//修改根目录Id
					int result = orgnazationService.update(orgnazation);
				}
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****end*******/
			}

			//获取目录下的一级集团和公司，排序在目标之后的组织（准备更改sort值）
			List<Orgnazation> list  = orgnazationService.queryOrgListRootReturnOrg(result_taget.getRootId(),result_taget.getSort());

			for(Orgnazation orgnazation:list){
				//如果组织ID匹配与要调整位置的源组织相同，则跳出循环，以下的组织不用进行更改sort
				if(orgnazation.getId().equals(orgnazationDto_source.getId())){
					break;
				}else{
					orgnazation.setSort(orgnazation.getSort()+1L);
					//修改sort排序号
					int result=   orgnazationService.update(orgnazation);
				}
			}
			//更改之前获取原先的全路径
			String prefixIdold = result_source.getPrefixId();
			String prefixNameold = result_source.getPrefixName();

			//更改组织的目录ID，父节点ID，sort
			result_source.setRootId(result_taget.getRootId());//因为是top类型，所以rootId和目标的一致
			result_source.setParentId(result_taget.getParentId());//因为是top类型，所以parentId和目标的一致
			result_source.setSort(result_taget.getSort());//调整为目标的顺序

			//更改目标顺序
			result_taget.setSort(result_taget.getSort()+1L);//目标sort+1

			//判断上级是否为空，如果为空全路径为自己，不为空加上上级的全路径
			if(null != result_taget.getParentId() && !"".equals(result_taget.getParentId())){
				//查询出来父级的组织
				Orgnazation	result_parent = orgnazationService.getObjectById(result_taget.getParentId());
				//设置新的全路径名称
				result_source.setPrefixId(result_parent.getPrefixId()+"/"+result_source.getId());
				result_source.setPrefixName(result_parent.getPrefixName()+"/"+result_source.getName());
			}else{
				//设置新的全路径名称
				result_source.setPrefixId(result_source.getId());
				result_source.setPrefixName(result_source.getName());
			}
			//更改之后获取新的全路径
			String prefixId = result_source.getPrefixId();
			String prefixName = result_source.getPrefixName();

			//调用数据库进行修改
			int result_t=   orgnazationService.update(result_taget);
			int result_s=   orgnazationService.update(result_source);

			//如果原先和现在的全路径进行了更改，那么更改所有下级的全路径
			if(!prefixIdold.equals(prefixId) || !prefixNameold.equals(prefixName)){
				Map<String,String> map = new HashMap<String,String>();
				map.put("prefixIdold", prefixIdold);
				map.put("prefixId", prefixId);
				map.put("prefixNameold", prefixNameold);
				map.put("prefixName", prefixName);
				orgnazationService.updatePrefix(map);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	//type是top、target目标是集团、公司、部门、项目的情况下，进行查询下级组织并修改sort，以及修改要进行调整位置的组织机构sort
	public void updateOrgSortPrevNotCata(OrgnazationTreeNodeDto orgnazationDto_target,OrgnazationTreeNodeDto orgnazationDto_source,List<Orgnazation> list_org){
		try {
			//查询出来要调整位置的组织
			Orgnazation	result_source = orgnazationService.getObjectById(orgnazationDto_source.getId());
			//查询出来目标位置的组织
			Orgnazation	result_taget = orgnazationService.getObjectById(orgnazationDto_target.getId());
			//判断源拖动组织rootId是否目录的id相同---如果相同不用替换下属组织的所有rootId
			if(orgnazationDto_source.getRootId().equals(result_taget.getRootId())){

			}else{
				//如果不相同，替换下属所有组织的rootId
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****start*****/
				List<Orgnazation> list_return = getOrgList(result_source,list_org);
				for(Orgnazation orgnazation:list_return){
					orgnazation.setRootId(result_taget.getRootId());
					//修改根目录Id
					int result = orgnazationService.update(orgnazation);
				}
				/***********查询出要调整位置的公司或者集团下所有组织（准备进行修改rootId）*****end*******/
			}

			//获取集团、公司、部门、项目组织结构同一级节点，排序在目标之后的组织（准备更改sort值）
			List<Orgnazation> list  = orgnazationService.queryOrgListReturnOrg(result_taget.getParentId(),result_taget.getSort());

			for(Orgnazation orgnazation:list){
				//如果组织ID匹配与要调整位置的源组织相同，则跳出循环，以下的组织不用进行更改sort
				if(orgnazation.getId().equals(orgnazationDto_source.getId())){
					break;
				}else{
					orgnazation.setSort(orgnazation.getSort()+1L);
					//修改sort排序号
					int result=   orgnazationService.update(orgnazation);
				}
			}
			//更改之前获取原先的全路径
			String prefixIdold = result_source.getPrefixId();
			String prefixNameold = result_source.getPrefixName();

			//更改组织的目录ID，父节点ID，sort
			result_source.setRootId(result_taget.getRootId());//因为是top类型，所以rootId和目标的一致
			result_source.setParentId(result_taget.getParentId());//因为是top类型，所以parentId和目标的一致
			result_source.setSort(result_taget.getSort());//调整为目标的顺序
			//更改目标顺序
			result_taget.setSort(result_taget.getSort()+1L);//目标sort+1

			//判断上级是否为空，如果为空全路径为自己，不为空加上上级的全路径
			if(null != result_taget.getParentId() && !"".equals(result_taget.getParentId())){
				//查询出来父级的组织
				Orgnazation	result_parent = orgnazationService.getObjectById(result_taget.getParentId());
				//设置新的全路径名称
				result_source.setPrefixId(result_parent.getPrefixId()+"/"+result_source.getId());
				result_source.setPrefixName(result_parent.getPrefixName()+"/"+result_source.getName());
			}else{
				//设置新的全路径名称
				result_source.setPrefixId(result_source.getId());
				result_source.setPrefixName(result_source.getName());
			}
			//更改之后获取新的全路径
			String prefixId = result_source.getPrefixId();
			String prefixName = result_source.getPrefixName();

			//调用数据库进行修改
			int result_t=   orgnazationService.update(result_taget);
			int result_s=   orgnazationService.update(result_source);

			//如果原先和现在的全路径进行了更改，那么更改所有下级的全路径
			if(!prefixIdold.equals(prefixId) || !prefixNameold.equals(prefixName)){
				Map<String,String> map = new HashMap<String,String>();
				map.put("prefixIdold", prefixIdold);
				map.put("prefixId", prefixId);
				map.put("prefixNameold", prefixNameold);
				map.put("prefixName", prefixName);
				orgnazationService.updatePrefix(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}


	/**
	 * 递归获取组织结构所有子节点
	 * @param OrgnazationTreeNodeDto
	 * @return
	 */
	public List<Orgnazation> getOrgList(Orgnazation orgnazation,List<Orgnazation> list_org) throws Exception{
		List<Orgnazation> list_return = new ArrayList<Orgnazation>();
		List<Orgnazation> list1  = queryOrgChildNode(orgnazation.getId(),list_org);
		list_return.addAll(list1);
		if(list1!=null && list1.size()>0){
			for(Orgnazation OrgnazationTreeNodeDto1:list1){
				getOrgList(OrgnazationTreeNodeDto1,list_org);
			}
		}else{
			return list_return;
		}
		return list_return;
	}

	//查询组织结构子节点（代替从数据库中进行查询）
	public List<Orgnazation> queryOrgChildNode(String parentId,List<Orgnazation> list_org){
		List<Orgnazation> listOrgChildNode = new ArrayList<Orgnazation>();
		for(Orgnazation orgNodeDto:list_org){
			if(parentId.equals(orgNodeDto.getParentId())){
				listOrgChildNode.add(orgNodeDto);
			}
		}
		return listOrgChildNode;
	}

	@Override
	public String queryListCompany(String userinfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Map<String,Object> param=new HashMap<String, Object>();
				if (map != null) {
					Set<String> keySet = map.keySet();
					for (String  key : keySet) {
						param.put(key, map.get(key));
					}
				}
				param.put("type", "company");
				List<Map<String,Object>> list=orgnazationService.queryListCompany(param);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=orgnazationService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);	
	}

	@Override
	public String updateStatus(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map = JacksonUtils.fromJson(updateJson, HashMap.class);
			//获取组织机构ID，以及禁用/启动标记
			String id = map.get("id").toString();
			String statusSign = map.get("statusSign").toString();

			Orgnazation orgnazation = orgnazationService.getObjectById(id);
			//禁用组织，更改组织结构下属所有机构状态为禁用
			if("0".equals(statusSign)){

			}else if("1".equals(statusSign)){
				//启用组织，启用上级所有组织机构
			}
			//设置当前组织状态进行更改
			orgnazation.setStatus(statusSign);
			int result=   orgnazationService.update(orgnazation);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("更新组织机构状态成功!");
		} catch (Exception e) {
			log.error("更新组织机构状态失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("更新组织机构状态失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryResListByIds(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				List<Map<String,String>> listReturn = new ArrayList<Map<String,String>>();
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list = (List<Map<String,Object>>)map.get("paramData");
				//					Map<String,Object> param=new HashMap<String, Object>();
				for(Map m: list ){
					String type = m.get("type").toString();
					List<String> listIds = (List<String>)m.get("ids");
					//类型user-人员 post-岗位 role-角色 org-组织
					if(type.equals("user")){
						List<Map<String,String>> listUser=userService.queryUsersByIds(m);
						listReturn.addAll(listUser);
					}else if(type.equals("post")){
						List<Map<String,String>> listPost=postService.queryPostsByIds(m);
						listReturn.addAll(listPost);
					}else if(type.equals("role")){
						List<Map<String,String>> listRole=standardRoleService.queryRolesByIds(m);
						listReturn.addAll(listRole);
					}else if(type.equals("org")){
						List<Map<String,String>> listOrg=orgnazationService.queryOrgsByIds(m);
						listReturn.addAll(listOrg);
					} else if(type.equals("postUser")){
						String puArr[]=new String[2];
						List<Map<String, String>> pus=new ArrayList<Map<String, String>>();//带岗人员
						List<String> pus_no=new ArrayList<String>();//不带岗人员
						for (int i = 0; i < listIds.size(); i++) {
							Map<String, String> pu=new HashMap<String, String>();
							puArr=listIds.get(i).split("&&");
							//获取指定组织用户  add by gyh 20171106
							if(puArr.length==2 && StringUtils.isNotBlank(puArr[1]) && !"null".equals(puArr[1])){
								pu.put("userId", puArr[0]);
								pu.put("postId", puArr[1]);
								pus.add(pu);
							}else {
								pus_no.add(puArr[0]);
							}
						}
						if(pus.size()>0){
							m.put("pus", pus);
							List<String> puIds=userService.selectPuIds(m);
							m.put("puIds", puIds);
							List<Map<String,String>> user1=userService.queryPostUsersByIds(m);
							listReturn.addAll(user1);
						}
						if(pus_no.size()>0){
							m.put("ids", pus_no);
							List<Map<String,String>> user2=userService.queryUsersByIds(m);
							listReturn.addAll(user2);
						}
					}
				}
				info.setResult(JacksonUtils.toJson(listReturn));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=orgnazationService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	//根据用户id，获取其角色/岗位/组织/菜单  等信息
	@Override
	public String getUserRPOMInfoByUserId(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			AuthenticationDto dto=null;
			Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
			/*if(!map.containsKey("userId")){
					throw new InvalidCustomException("userId不可为空");
				}*/
			dto = orgnazationService.getUserRPOMInfoByUserId(map);
			info.setResult(JacksonUtils.toJson(dto));
			info.setSucess(true);
			info.setMsg("获取列表对象成功!");
		} catch (Exception e) {
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}


	/**
	 *  查询用户所有组织信息：所属组织U岗位组织
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getUserAllOrgs(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
			if(!map.containsKey("userId")){
				throw new InvalidCustomException("userId不可为空");
			}
			List<OrgnazationDto> list = orgnazationService.getUserAllOrgs(map);
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取列表对象成功!");
		} catch (Exception e) {
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/**
	 *  查询部门 或项目分期 （包含集团和公司）
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getDeptOrBranch(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
			List<Map<String,Object>> list = orgnazationService.getDeptOrBranch(map);
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取列表对象成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer#queryListCompanyTree(java.lang.String, java.lang.String)
	 */
	@Override
	public String queryListCompanyTree(String userJson, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		List<ContentChildTreeData> resultList=new ArrayList<ContentChildTreeData>();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Map<String,Object> param=new HashMap<String, Object>();
				if (map != null) {
					Set<String> keySet = map.keySet();
					for (String  key : keySet) {
						param.put(key, map.get(key));
					}
				}
				param.put("type", "company");
				List<Map<String,Object>> list=orgnazationService.queryListCompany(param);
				  if(list.size() > 0){
					   for (Map  treeNode : list) {
						   if(treeNode.get("status").equals(StatusType.StatusOpen.getCode())){
							   ContentChildTreeData contentChildTreeData = new ContentChildTreeData();
							   contentChildTreeData.setpId("1");
							   contentChildTreeData.setName(String.valueOf(treeNode.get("label")));
							   contentChildTreeData.setId(String.valueOf(treeNode.get("value")));
							   contentChildTreeData.setParentId("1");
							   resultList.add(contentChildTreeData);
						   }
					   }
				   }
				info.setResult(JacksonUtils.toJson(resultList));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=orgnazationService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);	
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer#queryListCompanyAndZb(java.lang.String, java.lang.String)
	 */
	@Override
	public String queryListCompanyAndZb(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		List<ContentChildTreeData> resultList=new ArrayList<ContentChildTreeData>();
		try {
			if(StringUtils.isNotBlank(paramater)){
				List<Orgnazation> list=orgnazationService.queryListCompanyAndZb();
				  if(list.size() > 0){
					  for (Orgnazation orgnazation : list) {
						  ContentChildTreeData contentChildTreeData = new ContentChildTreeData();
						  String parentId = orgnazation.getParentId();
						  if(parentId!=null&&(!"".equals(parentId))){
							  contentChildTreeData.setpId(parentId);
							  contentChildTreeData.setName(orgnazation.getName());
							  contentChildTreeData.setId(orgnazation.getId());
							  contentChildTreeData.setParentId(parentId);
						  }else{
							  contentChildTreeData.setpId("1");
							  contentChildTreeData.setName(orgnazation.getName());
							  contentChildTreeData.setId(orgnazation.getId());
							  contentChildTreeData.setParentId("1");
							  
						  }
						  resultList.add(contentChildTreeData);
					  
					  }
				   }
				info.setResult(JacksonUtils.toJson(resultList));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=orgnazationService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);	
	}
	
	/**
	 * 公司岗：公司
	 * 部门岗：公司+部门
	 * 项目岗：公司+项目
	 * 分期岗：公司+项目+分期
	 */
	@Override
	public String getOrgsByPostId(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		Map map=JacksonUtils.fromJson(paramater, HashMap.class);
		try {
			if((!map.containsKey("postId"))||map.get("postId")==null||StringUtils.isBlank(map.get("postId").toString())){
				throw new InvalidCustomException("postId不可为空");
			}
			List<OrgnazationDto> res = orgnazationService.getOrgsByPostId(map);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取列表对象成功!");
		} catch (Exception e) {
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);	
	}
	/**
	 * 复制粘贴组织结构
	 */
	@Override
	public String copyAndPasteOrg(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
		try {
			if((!map.containsKey("copyId"))||map.get("copyId")==null||StringUtils.isBlank(map.get("copyId").toString())){
				throw new InvalidCustomException("copyId不可为空");
			}
			/*if((!map.containsKey("pasteId"))||map.get("pasteId")==null||StringUtils.isBlank(map.get("pasteId").toString())){
				throw new InvalidCustomException("pasteId不可为空");
			}*/
			Map<String, Orgnazation> c= orgnazationService.saveCopyAndPasteOrg(map);
			info.setResult(JacksonUtils.toJson(c));
			info.setSucess(true);
			info.setMsg("粘贴组织机构成功!");
		} catch (Exception e) {
			log.error("粘贴组织机构失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("粘贴组织机构失败");
			info.setExceptionMsg(e.getMessage().replace(e.getClass().getName(), ""));
		}
		return JacksonUtils.toJson(info);	
	}
	/**
	 * 读excel并插入db，校验数据的合法性，插入数据库，并返回结果
	 */
	@Override
	public String readExcelAndInsert(String userJson, List<OrgnazationExcelDto> orgList, String parentId) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String, Object> c = orgnazationService.readExcelAndInsert(orgList,parentId);
			if(c != null){
				info.setResult(JacksonUtils.toJson(c));
				info.setSucess(true);
				info.setMsg("导入成功!");
			}else{
				info.setSucess(false);
				info.setMsg("导入失败：导入节点不存在");
			}

		} catch (Exception e) {
			log.error("导入失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("导入失败");
			info.setExceptionMsg(e.getMessage().replace(e.getClass().getName(), ""));
		}
		return JacksonUtils.toJson(info);
	}

}