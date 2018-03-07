package com.xinleju.platform.univ.search.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.search.dao.SearchIndexDao;
import com.xinleju.platform.univ.search.entity.SearchIndex;

/**
 * @author haoqp
 * 
 * 
 */

@Repository
public class SearchIndexDaoImpl extends BaseDaoImpl<String,SearchIndex> implements SearchIndexDao{

	public SearchIndexDaoImpl() {
		super();
	}

	
	
}
