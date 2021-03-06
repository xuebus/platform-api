package com.xinleju.platform.flow.operation;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.model.InstanceUnit;

/**
 * 接受操作
 * 
 * @author daoqi
 *
 */
public class AcceptOperation extends DefaultOperation implements Operation{

	AcceptOperation() {
		super(OperationType.ACCEPT);
	}

	@Override
	protected void operate(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
		
		//下一步
		next(instanceUnit, approvalDto);
	}


}
