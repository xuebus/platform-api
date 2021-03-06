package com.xinleju.platform.flow.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.BusinessObjectDao;
import com.xinleju.platform.flow.dto.BusinessObjectDto;
import com.xinleju.platform.flow.entity.BusinessObject;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BusinessObjectDaoImpl extends BaseDaoImpl<String, BusinessObject> implements BusinessObjectDao {

	public BusinessObjectDaoImpl() {
		super();
	}

	@Override
	public List<BusinessObjectDto> getTree(Map<String, Object> paramater) throws Exception {
		long time1 = System.currentTimeMillis();
		List<BusinessObjectDto> dataList = getSqlSession().selectList("com.xinleju.platform.flow.entity.BusinessObject.getTree");
	/*	long time2 = System.currentTimeMillis();
		System.out.println("\nBusinessObjectDaoImpl getTree()所花费的时间是 ： "+(time2-time1));*/
		return dataList;
	}

	@Override
	public List<BusinessObjectDto> seachKeyword(Map<String, String> paramater) {
		String methodId = "com.xinleju.platform.flow.entity.BusinessObject.seachKeyword";
		return getSqlSession().selectList(methodId, paramater);//传递多个查询条件参数
	}

	@Override
	public List<BusinessObjectDto> queryListByCondition(Map<String, Object> map) {
		return getSqlSession().selectList(BusinessObject.class.getName()+".queryListByCondition", map);
	}

	@Override
	public List<String> selectAllParentId(Map<String, Object> map) {
		return getSqlSession().selectList(BusinessObject.class.getName()+".selectAllParentId", map);
	}

	@Override
	public List<BusinessObjectDto> getTreeBySystemApp(Map<String, String> paramMap) {
		String method = BusinessObject.class.getName()+".getTreeBySystemApp";
		return getSqlSession().selectList(method, paramMap);
		
	}

	@Override
	public BusinessObjectDto getObjectByCode(String businessObjectCode) {
		return getSqlSession().selectOne(BusinessObject.class.getName()+".getObjectByCode", businessObjectCode);
	}

	@Override
	public BusinessObjectDto getObjectByFlCode(String flCode) {
		return getSqlSession().selectOne(BusinessObject.class.getName()+".getObjectByFlCode", flCode);
	}
	
	@Override
	public int updateObjectPrefixIdByParamMap(Map<String, String> paramMap) {
		String method = BusinessObject.class.getName()+".updateObjectPrefixIdByParamMap";
		return getSqlSession().update(method, paramMap);
	}

	@Override
	public Integer queryCountLikePrefixMap(Map paramMap) {
		String method = BusinessObject.class.getName()+".queryCountLikePrefixMap";
		return getSqlSession().selectOne(method, paramMap);
	}

	@Override
	public List<BusinessObjectDto> getCategoryTreeBySystemApp(Map<String, String> paramMap) {
		String method = BusinessObject.class.getName()+".getCategoryTreeBySystemApp";
		return getSqlSession().selectList(method, paramMap);
	}

	@Override
	public Integer queryRelatedCountByPrefixMap(Map paramMap) {
		String method = BusinessObject.class.getName()+".queryRelatedCountByPrefixMap";
		return getSqlSession().selectOne(method, paramMap);
	}

	@Override
	public int deleteObjectAndChileren(Map<String, String> paramMap) {
		String method = BusinessObject.class.getName()+".deleteObjectAndChileren";
		return getSqlSession().update(method, paramMap);
	}

	@Override
	public List<BusinessObjectDto> queryBusiObjectTypeByParamMap(Map<String, Object> paramMap) {
		String method = BusinessObject.class.getName()+".queryBusiObjectTypeByParamMap";
		return getSqlSession().selectList(method, paramMap);
	}

	//更新业务对象分类本身的name和prefixSort updateNameAndPrefixSort
	@Override
	public void updateNameAndPrefixSort(Map<String, Object> updateMap) {
		String method = BusinessObject.class.getName()+".updateNameAndPrefixSort";
		getSqlSession().update(method, updateMap);
	}
	//更新业务对象下面的子对象的的prefixSort  updateSubObjectsPrefixSortByParamMap
	@Override
	public void updateSubObjectsPrefixSortByParamMap(Map<String, Object> updateMap) {
		String method = BusinessObject.class.getName()+".updateSubObjectsPrefixSortByParamMap";
		getSqlSession().update(method, updateMap);
	}

	@Override
	public List<BusinessObject> queryBusiObjectListByParam(Map<String, Object> paramMap) {
		String method = BusinessObject.class.getName()+".queryBusiObjectListByParam";
		return getSqlSession().selectList(method, paramMap);
	}

	@Override
	public void updateAllNodes(Map<String, Object> paramMap) {
		String method = BusinessObject.class.getName()+".updateAllNodes";
		getSqlSession().update(method, paramMap);
	}

	@Override
	public void updateAllNodesSortAndPrefix(Map<String, Object> updateMap) {
		String method = BusinessObject.class.getName()+".updateAllNodesSortAndPrefix";
		getSqlSession().update(method, updateMap);
	}
	
	/**
	 * 根据流程实例查询业务对象callbackUrl 
	 */
	public String selectCallBackUrlByInstanceId(Map<String, Object> param)throws Exception{
		return getSqlSession().selectOne(BusinessObject.class.getName()+".selectCallBackUrlByInstanceId", param);
	}
}
