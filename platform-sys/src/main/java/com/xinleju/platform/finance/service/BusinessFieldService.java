package com.xinleju.platform.finance.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.finance.dto.BusinessFieldDto;
import com.xinleju.platform.finance.entity.BusinessField;

/**
 * @author admin
 * 
 * 
 */

public interface BusinessFieldService extends  BaseService <String,BusinessField>{

	public List<Map<String, Object>> getMapListByObjId(String paramater) throws Exception;

	public List<BusinessFieldDto> queryTreeList(Map<String, Object> map) throws Exception;

	
}
