package com.xinleju.platform.flow.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis.encoding.ser.ArrayDeserializer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.FlDto;
import com.xinleju.platform.flow.dto.service.FlDtoServiceCustomer;
import com.xinleju.platform.flow.entity.BusinessObject;
import com.xinleju.platform.flow.entity.Fl;
import com.xinleju.platform.flow.enumeration.FlStatus;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.service.BusinessObjectService;
import com.xinleju.platform.flow.service.FlService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class FlDtoServiceProducer implements FlDtoServiceCustomer{
	private static Logger log = Logger.getLogger(FlDtoServiceProducer.class);
	@Autowired
	private FlService flService;
	@Autowired
	private BusinessObjectService businessObjectService;
	
	@Override
	public String saveAll(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
	    try {
	    	FlDto flDto=JacksonUtils.fromJson(saveJson, FlDto.class);
			flService.saveAll(userInfo,flDto);
		    //info.setResult(JacksonUtils.toJson(fl));
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
	
	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   Fl fl=JacksonUtils.fromJson(saveJson, Fl.class);
		   flService.save(fl);
		   info.setResult(JacksonUtils.toJson(fl));
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
	public String updateBatch(String userInfo, String updateJsonList) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(updateJsonList)) {
				List<Fl> listFl = JacksonUtils.fromJson(updateJsonList, ArrayList.class, Fl.class);
				int result = flService.updateBatch(listFl);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("批量更新对象成功!");
			}
		} catch (Exception e) {
			log.error("批量更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("批量更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Fl fl = JacksonUtils.fromJson(updateJson, Fl.class);
			   int result=   flService.update(fl);
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
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Fl fl=JacksonUtils.fromJson(deleteJson, Fl.class);
			   int result= flService.deleteObjectById(fl.getId());
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
				   int result= flService.deleteAllObjectByIds(list);
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
			Fl fl=JacksonUtils.fromJson(getJson, Fl.class);
			Fl	result = flService.getObjectById(fl.getId());
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
				Page page=flService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				List<FlDto> flDtos = JacksonUtils.fromJson(JacksonUtils.toJson(page.getList()), ArrayList.class,FlDto.class);
				if (flDtos != null && flDtos.size() > 0) {
					Map<String, FlDto> flDtoMap = new HashMap<String, FlDto>();
					Map<String,	String> idMap = new HashMap<String, String>();
					Map<String, Map<String, String>> vasMap = new HashMap<String, Map<String, String>>();
					for (FlDto flDto : flDtos) {
						String codeStr = flDto.getCode();
						Map<String, String> tmpMap = new HashMap<String, String>();
						for (FlDto flDto2 : flDtos) {
							if (codeStr.equals(flDto2.getCode())) {
								String idStr = flDto2.getId();
								String vas = flDto2.getVersion()+"-";
								if ("0".endsWith(flDto2.getStatus())) {
									vas += FlStatus.DRAFT.getName();
								}else if("1".endsWith(flDto2.getStatus())){
									idMap.put(codeStr, idStr);
									vas += FlStatus.PUBLISH.getName();
								}else if("2".endsWith(flDto2.getStatus())){
									vas += FlStatus.DISABLED.getName();
								}							
								tmpMap.put(idStr, vas);
							}
						}
						vasMap.put(codeStr, tmpMap);						
						flDto.setVersionAndStatus(JacksonUtils.toJson(vasMap.get(codeStr)));
						BusinessObject businessObject = businessObjectService.getObjectById(flDto.getBusinessObjectId());
						flDto.setBusinessObjectName(businessObject.getName());
						flDtoMap.put(codeStr, flDto);
					}
					
					Set<String> set = flDtoMap.keySet();
					for (String str : set) {
						flDtoMap.get(str).setId(idMap.get(str));
					}
					page.setList(new ArrayList<FlDto>(flDtoMap.values()));
				}
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=flService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			 e.printStackTrace();
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
				List list=flService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=flService.queryList(null);
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
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   Fl fl=JacksonUtils.fromJson(deleteJson, Fl.class);
			   int result= flService.deletePseudoObjectById(fl.getId());
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
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= flService.deletePseudoAllObjectByIds(list);
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
	public String queryFlList(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				Page page = flService.queryFlList(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				 info.setSucess(false);
				 info.setMsg("获取列表对象失败!");
				 info.setExceptionMsg("参数为空！");
			}
		} catch (Exception e) {
//			 e.printStackTrace();
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	@Override
	public String queryFlByGroupList(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				Page page = flService.queryFlByGroupList(map);
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				info.setSucess(false);
				info.setMsg("获取列表对象失败!");
				info.setExceptionMsg("参数为空！");
			}
		} catch (Exception e) {
//			 e.printStackTrace();
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
/*	@Override
	public String queryFlList(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				//按编码及版本号降序排列
				System.out.println("start =============" + new Timestamp(new Date().getTime()));
				Page page = flService.queryFlList(map);
				List<FlDto> flDtos = JacksonUtils.fromJson(JacksonUtils.toJson(page.getList()), ArrayList.class,FlDto.class);
				if (flDtos != null && flDtos.size() > 0) {
					Map<String, FlDto> flDtoMap = new LinkedHashMap<String, FlDto>();
					String oldCodestr = "";
					for (FlDto flDto : flDtos) {
						BusinessObject businessObject = businessObjectService.getObjectById(flDto.getBusinessObjectId());
						if (businessObject == null) {
							continue;
						}else {
							flDto.setBusinessObjectName(businessObject.getName());
							String codeStr = flDto.getCode();
							if (StringUtils.isNotBlank(codeStr)) {
								if (!oldCodestr.equals(codeStr)) {
									oldCodestr = codeStr;
								} else {
									continue;
								}

								Map<String, Object> queryCond = new HashMap<String, Object>();
								queryCond.put("code", codeStr);

								// 组装模板版本列表
								Page page2 = flService.queryFlList(queryCond);
								List<FlDto> flDtos2 = JacksonUtils.fromJson(JacksonUtils.toJson(page2.getList()),
										ArrayList.class, FlDto.class);
								Map<String, String> vasMap = new LinkedHashMap<String, String>();
								for (FlDto flDto2 : flDtos2) {
									String idStr = flDto2.getId();
									String vas = flDto2.getVersion() + "-";
									if ("0".endsWith(flDto2.getStatus())) {
										vas += FlStatus.DRAFT.getName();
									} else if ("1".endsWith(flDto2.getStatus())) {
										vas += FlStatus.PUBLISH.getName();
									} else if ("2".endsWith(flDto2.getStatus())) {
										vas += FlStatus.DISABLED.getName();
									}
									vasMap.put(idStr, vas);
								}

								flDto.setVersionAndStatus(JacksonUtils.toJson(vasMap));
								flDtoMap.put(codeStr, flDto);
							}
						}
					}
					
					page.setList(new ArrayList<FlDto>(flDtoMap.values()));
				}
				System.out.println("end =============" + new Timestamp(new Date().getTime()));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				 info.setSucess(false);
				 info.setMsg("获取列表对象失败!");
				 info.setExceptionMsg("参数为空！");
			}
		} catch (Exception e) {
			 e.printStackTrace();
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}*/
	
	@Override
	public String getAll(String userInfo, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			//Fl fl = JacksonUtils.fromJson(paramater, Fl.class);
			Map<String,Object> paramMap = JacksonUtils.fromJson(paramater,Map.class);
			String flId = (String) paramMap.get("id");
			Boolean entryType = (Boolean) paramMap.get("entryType");
			FlDto result = entryType!=null&&entryType?flService.getAllForTemplate(flId):flService.getAll(flId);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("获取对象及相关属性成功!");
		} catch (Exception e) {
			log.error("获取对象及相关属性失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取对象及相关属性失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String start(String userInfo, String parameter) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		log.info("流程启动时参数：" +  parameter);
		try {
			Map<String, Object> parameterMap = JacksonUtils.fromJson(parameter, HashMap.class);
			String businessId = (String) parameterMap.get("businessId");
			String flCode = (String) parameterMap.get("flCode");
			String businessObjectCode = (String) parameterMap.get("businessObjectCode");
			if(!StringUtils.isNotBlank(businessId) || (!StringUtils.isNotBlank(flCode) && !StringUtils.isNotBlank(businessObjectCode))) {
				info.setSucess(false);
				info.setMsg("请求参数错误:" + parameterMap);
			
			} else {
				String flowInstanceId = flService.start(businessObjectCode, flCode, businessId);
				info.setResult(flowInstanceId);
				info.setSucess(true);
				info.setMsg("流程发起成功!");
			}
		} catch (Exception e) {
			log.error("流程发起失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg(e.getMessage());
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String emulation(String userInfo,String parameter){
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		log.info("流程启动时参数：" +  parameter);
		try {
			Map<String, Object> parameterMap = JacksonUtils.fromJson(parameter, HashMap.class);
			String flowInstanceId = flService.emulation(parameterMap);
			info.setResult(flowInstanceId);
			info.setSucess(true);
			info.setMsg("流程仿真成功!");
			/*String businessVariableJson = (String) parameterMap.get("businessVariableJson");
			String flId = (String) parameterMap.get("flId");
			String businessObjectId = (String) parameterMap.get("businessObjectId");
			if(!StringUtils.isNotBlank(businessVariableJson) || (!StringUtils.isNotBlank(flId) && !StringUtils.isNotBlank(businessObjectId))) {
				info.setSucess(false);
				info.setMsg("请求参数错误:" + parameterMap);

			} else {
				String flowInstanceId = flService.emulation(businessObjectId, flId, businessVariableJson);
				info.setResult(flowInstanceId);
				info.setSucess(true);
				info.setMsg("流程仿真成功!");
			}*/
		} catch (Exception e) {
			log.error("流程仿真失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg(e.getMessage());
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryListByApprover(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=flService.queryListByApprover(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=flService.queryList(null);
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
	public String queryListByParticipant(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=flService.queryListByParticipant(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=flService.queryList(null);
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
	public String queryViewList(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<FlDto> list=flService.queryViewList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=flService.queryList(null);
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
	public String queryFlowBusiObjectList(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String, String> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<FlDto> list=flService.queryFlowBusiObjectList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=flService.queryList(null);
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
	public String getFlowRetractForInstance(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String, String> map=JacksonUtils.fromJson(paramater, HashMap.class);
				Map<String, String> list=flService.getFlowRetractForInstance(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=flService.queryList(null);
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
	public String queryDefaultFl(String userJson, String paramaterJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> paramMap = JacksonUtils.fromJson(paramaterJson, Map.class);
			Fl	result = flService.queryDefaultFlow((String) paramMap.get("businessObjectCode"));
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

	/**
	 * 根据流程模板编号字符串来逻辑删除对应的流程模板的所有版本
	 * @param codeText
	 * @return
	 */
	@Override
	public String deleteFlowsByCodeText(String userJson, String deleteJsonList) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
	    try {
		   if (StringUtils.isNotBlank(deleteJsonList)) {
			   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
			   String codeText =(String) map.get("codeText");
			   int result= flService.deleteFlowsByCodeText(codeText);
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
	public String setDefaultFl(String userJson, String parameterJson) {

		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> paramMap = JacksonUtils.fromJson(parameterJson, Map.class);
			Boolean	result = flService.setDefaultFl(paramMap);
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
	public String updateFlowsByids(String userInfo, String param) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(param)) {
				Map<String,Object> map = JacksonUtils.fromJson(param, HashMap.class);
				List<String> ids =(ArrayList) map.get("ids");
				/*List<Fl> fls = new ArrayList<>();
				for (int i = 0; i <ids.size() ; i++) {
					Fl fl = new Fl();
					fl.setId(ids.get(i));

				}*/
				int result = flService.updateFlowsByids(map);
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("批量更新对象成功!");
			}
		} catch (Exception e) {
			log.error("批量更新对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("批量更新对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
