package com.xinleju.platform.sys.org.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.DeptDao;
import com.xinleju.platform.sys.org.entity.Dept;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class DeptDaoImpl extends BaseDaoImpl<String,Dept> implements DeptDao{

	public DeptDaoImpl() {
		super();
	}

	
	
}