package com.xinleju.platform.base.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.flow.dto.service.ExpressionEvaluateServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

@RequestMapping("/expression")
@Controller
public class ExpressionEvaluateController {
	private static Logger log = Logger.getLogger(ExpressionEvaluateController.class);
	
	@Autowired
	public ExpressionEvaluateServiceCustomer expressionEvaluateService;

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public @ResponseBody MessageResult validate(String expression) {
		MessageResult result = new MessageResult();
		String dubboResultInfo = expressionEvaluateService.validate(null, expression);
		DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		result.setSuccess(dubboServiceResultInfo.isSucess());
		result.setMsg(dubboServiceResultInfo.getMsg());
		log.info(dubboServiceResultInfo.getMsg());
		return result;
	}
}
