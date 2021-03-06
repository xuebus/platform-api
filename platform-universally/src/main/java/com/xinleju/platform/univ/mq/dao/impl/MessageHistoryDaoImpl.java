package com.xinleju.platform.univ.mq.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.mq.dao.MessageHistoryDao;
import com.xinleju.platform.univ.mq.entity.MessageHistory;

/**
 * @author xubaoyong
 * 
 * 
 */

@Repository
public class MessageHistoryDaoImpl extends BaseDaoImpl<String,MessageHistory> implements MessageHistoryDao{

	public MessageHistoryDaoImpl() {
		super();
	}

	
	
}
