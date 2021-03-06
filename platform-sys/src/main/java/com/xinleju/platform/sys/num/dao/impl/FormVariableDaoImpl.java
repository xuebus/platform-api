package com.xinleju.platform.sys.num.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.num.dao.FormVariableDao;
import com.xinleju.platform.sys.num.entity.FormVariable;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class FormVariableDaoImpl extends BaseDaoImpl<String,FormVariable> implements FormVariableDao{

	public FormVariableDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getFormVariableData(Map<String, Object> map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.num.entity.FormVariable.getFormVariable", map);
	}

	@Override
	public Integer getFormVariableDataCount(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.num.entity.FormVariable.getFormVariableCount", map);
	}

	
	
}
