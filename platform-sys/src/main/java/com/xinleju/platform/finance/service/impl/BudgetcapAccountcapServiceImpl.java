package com.xinleju.platform.finance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.finance.dao.BudgetcapAccountcapDao;
import com.xinleju.platform.finance.entity.BudgetcapAccountcap;
import com.xinleju.platform.finance.service.BudgetcapAccountcapService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class BudgetcapAccountcapServiceImpl extends  BaseServiceImpl<String,BudgetcapAccountcap> implements BudgetcapAccountcapService{
	

	@Autowired
	private BudgetcapAccountcapDao budgetcapAccountcapDao;
	

}
