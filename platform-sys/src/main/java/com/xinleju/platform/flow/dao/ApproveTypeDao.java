package com.xinleju.platform.flow.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.entity.ApproveType;

/**
 * @author admin
 *
 */

public interface ApproveTypeDao extends BaseDao<String, ApproveType> {

	int resetApproveTypeData();

	int resetOperationTypeData();

	int deleteAllMapDataByDelflag();
	
	

}
