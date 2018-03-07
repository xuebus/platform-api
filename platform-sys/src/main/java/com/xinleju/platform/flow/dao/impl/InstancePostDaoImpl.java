package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstancePostDao;
import com.xinleju.platform.flow.entity.InstancePost;

/**
 * @author admin
 * 
 * 
 */
@Repository
public class InstancePostDaoImpl extends BaseDaoImpl<String,InstancePost> implements InstancePostDao{

	public InstancePostDaoImpl() {
		super();
	}
}
