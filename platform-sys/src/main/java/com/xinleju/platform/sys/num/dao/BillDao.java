package com.xinleju.platform.sys.num.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.num.entity.Bill;

/**
 * @author ly
 *
 */

public interface BillDao extends BaseDao<String, Bill> {

	List<Map<String, Object>> getBillData(Map<String, Object> map);

	Integer getBillDataCount(Map<String, Object> map);

	Integer getCountByCode(Map<String, Object> map);
	
	

}
