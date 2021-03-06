package com.xinleju.platform.flow.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.FlDto;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.entity.Fl;
import com.xinleju.platform.flow.exception.FlowException;

/**
 * @author admin
 * 
 * 
 */

public interface FlService extends BaseService<String, Fl> {

	/**
	 * 根据条件获取流程模板列表
	 * 
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public Page queryFlList(Map<String, Object> paramater) throws Exception;
	/**
	 * 根据条件分组获取流程模板列表
	 * 
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public Page queryFlByGroupList(Map<String, Object> paramater) throws Exception;

	/**
	 * 保存流程模板及相关属性
	 * 
	 * @param userInfo
	 * @param flDto
	 * @throws Exception
	 */
	public void saveAll(String userInfo,FlDto flDto) throws Exception;
	
	/**
	 * 根据ID查询流程模板及相关属性
	 * 
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public FlDto getAll(String paramater) throws Exception;

	/**
	 * add by dgh
	 * 根据ID查询流程模板及相关属性 NEW
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public FlDto getAllForTemplate(String paramater) throws Exception;

	/**
	 * 流程发起
	 * 
	 * @param businessObjectCode
	 * @param flCode
	 * @param businessId
	 * @return
	 * @throws FlowException
	 */
	public String start(String businessObjectCode, String flCode, String businessId) throws FlowException;

	/**
	 * 流程仿真
	 * @param businessObjectId
	 * @param flId
	 * @param businessVariableJson
	 * @return
	 * @throws Exception
	 */
	public String emulation(String businessObjectId, String flId, String businessVariableJson) throws Exception;

	/**
	 * 流程仿真
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String emulation(Map<String,Object> paramMap) throws Exception;

	public List<FlDto> queryListByApprover(Map<String, String> map);
	/**
	 * 根据Participant查询各个环节
	 * @param map
	 * @return
	 */
	public List<FlDto> queryListByParticipant(Map<String, String> map);

	public List<FlDto> queryViewList(Map<String, Object> map)throws Exception;

	public List<FlDto> queryFlowBusiObjectList(Map<String, String> map);
	public Map<String, String>  getFlowRetractForInstance(Map<String, String> map);
	
	/**
	 * 根据用户ID查询其所在的流程模板（指定人、角色、岗位三种维度）
	 * 
	 * @param userId
	 * @return List<FlId>
	 */
	public List<String> queryFlByApprover(String userId);

	/**
	 * 根据用户ID查询岗位，调用权限服务
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> queryPostBy(String userId);
	
	/**
	 * 根据用户ID查询角色，调用权限服务
	 * 
	 * @param userId
	 * @return
	 */
	public List<String> queryRoleBy(String userId);

	/**
	 * 查询默认模板
	 * @param businessObjectCode
	 * @return
	 */
	public Fl queryDefaultFlow(String businessObjectCode);

	InstanceDto startForMobile(String businessObjectCode, String flCode, String businessId) throws FlowException;

	/**
	 * 根据流程模板编号字符串来逻辑删除对应的流程模板的所有版本
	 * @param codeText
	 * @return
	 */
	public int deleteFlowsByCodeText(String codeText) throws Exception;
	
	/**
	 * 根据业务对象编码查询流程模板列表
	 * 
	 * @param businessCode
	 */
	public List<Map<String, String>> queryFlowTemplateBy(String businessCode);

	/**
	 * 设置默认模板
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Boolean setDefaultFl(Map<String,Object> paramMap) throws Exception;
	public Integer updateFlowsByids(Map<String,Object> paramMap) throws Exception;
}
