package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface MobileMeetingDtoServiceCustomer extends BaseDtoServiceCustomer {

	String pageQueryByParamMap(String userJson, String paramaterJson);

	String queryMeetingDetail(String userJson, String paramaterJson);

	String querySummaryDetail(String userJson, String paramaterJson);

	
}
