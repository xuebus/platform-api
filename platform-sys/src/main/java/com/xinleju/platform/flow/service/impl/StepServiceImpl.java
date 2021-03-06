package com.xinleju.platform.flow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.StepDao;
import com.xinleju.platform.flow.entity.Step;
import com.xinleju.platform.flow.service.StepService;

/**
 * @author admin
 * 
 * 
 */
@Service
public class StepServiceImpl extends  BaseServiceImpl<String,Step> implements StepService{
	

	@Autowired
	private StepDao stepDao;

	@Override
	public List<String> bizVarBeUsedInFlow(String businessObjectId, String varCode) {
		return stepDao.bizVarBeUsedInFlow(businessObjectId, varCode);
	}

	@Override
	public List<Step> queryStepsBy(String flId) {
		// TODO Auto-generated method stub
		return stepDao.queryStepsBy(flId);
	}
	

}
