package com.xinleju.platform.portal.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.portal.dao.ComponentCategoryDao;
import com.xinleju.platform.portal.entity.ComponentCategory;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class ComponentCategoryDaoImpl extends BaseDaoImpl<String,ComponentCategory> implements ComponentCategoryDao{

	public ComponentCategoryDaoImpl() {
		super();
	}

	@Override
	public ComponentCategory getComponentCategoryBySerialNo(
			ComponentCategory componentCategory) {
		
		return this.getSqlSession().selectOne("com.xinleju.platform.portal.entity.ComponentCategory.getComponentCategoryBySerialNo", componentCategory);
	}

	
	
}
