package com.xinleju.platform.sys.notice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.utils.StatusType;
import com.xinleju.platform.sys.notice.dao.SysNoticePhoneServerDao;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer;
import com.xinleju.platform.sys.notice.service.SysNoticePhoneServerService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class SysNoticePhoneServerServiceImpl extends  BaseServiceImpl<String,SysNoticePhoneServer> implements SysNoticePhoneServerService{
	

	@Autowired
	private SysNoticePhoneServerDao sysNoticePhoneServerDao;

	@Override
	public int updateSetDefault(SysNoticePhoneServer serverBean) {
		int i=0;
		Map<String, Object> map = new HashMap<String,Object>();
		sysNoticePhoneServerDao.disableAllData(map);
		serverBean.setIsDefault(StatusType.StatusOpen.getCode());
	    i = sysNoticePhoneServerDao.update(serverBean);
		return i;
	}

	@Override
	public Page queryVaguePage(Map<String, Object> map) {
		Page page=new Page();
		List<Map<String,Object>> list = sysNoticePhoneServerDao.getPageData(map);
		Integer count = sysNoticePhoneServerDao.getPageDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

	@Override
	public void updateDisableAllData() throws Exception {
		sysNoticePhoneServerDao.disableAllData(new HashMap<String,Object>());
	}
	

}
