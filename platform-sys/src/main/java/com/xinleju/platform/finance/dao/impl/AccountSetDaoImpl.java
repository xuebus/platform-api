package com.xinleju.platform.finance.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.AccountSetDao;
import com.xinleju.platform.finance.entity.AccountSet;
import com.xinleju.platform.finance.entity.SysRegister;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class AccountSetDaoImpl extends BaseDaoImpl<String,AccountSet> implements AccountSetDao{

	public AccountSetDaoImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.SysRegisterDao#getpageList(java.util.Map)
	 */
	@Override
	public List<SysRegister> getpageList(Map map) {
	  return getSqlSession().selectList("com.xinleju.platform.finance.entity.AccountSet.getpageList", map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.SysRegisterDao#getpageListCount(java.util.Map)
	 */
	@Override
	public Integer getpageListCount(Map map) {
		  return getSqlSession().selectOne("com.xinleju.platform.finance.entity.AccountSet.getpageListCount", map);
	}
	
}
