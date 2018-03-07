package com.xinleju.platform.univ.task.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.task.dao.TaskParametersDao;
import com.xinleju.platform.univ.task.entity.TaskParameters;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class TaskParametersDaoImpl extends BaseDaoImpl<String,TaskParameters> implements TaskParametersDao{

	public TaskParametersDaoImpl() {
		super();
	}

	
	
}