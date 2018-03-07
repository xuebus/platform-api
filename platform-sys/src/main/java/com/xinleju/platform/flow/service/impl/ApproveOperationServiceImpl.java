package com.xinleju.platform.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.ApproveOperationDao;
import com.xinleju.platform.flow.dto.ApproveOperationDto;
import com.xinleju.platform.flow.entity.ApproveOperation;
import com.xinleju.platform.flow.service.ApproveOperationService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class ApproveOperationServiceImpl extends  BaseServiceImpl<String,ApproveOperation> implements ApproveOperationService{
	
	private static Map<String, String> nameMapper = new HashMap<String, String>();
	static {
		nameMapper.put("BJS", "non-acceptance");
		nameMapper.put("JS", "acceptance");
		nameMapper.put("WYY", "unanimity");
		nameMapper.put("GTFQR", "communicate initiator");
		nameMapper.put("HF", "reply");
		nameMapper.put("XB", "assist and transact");
		nameMapper.put("DH", "rebut");
		nameMapper.put("ZB", "return transact");
		nameMapper.put("TY", "agree");
	}

	@Autowired
	private ApproveOperationDao approveOperationDao;

	@Override
	public List<ApproveOperationDto> queryObjectListByApproveId(String approveTypeId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("approveId", approveTypeId);
		paramMap.put("delflag", 0);
		List<ApproveOperation> templist = approveOperationDao.queryList(paramMap);
		List<ApproveOperationDto> resultList = new ArrayList<ApproveOperationDto>();
		for(ApproveOperation obj : templist){
			ApproveOperationDto dto = new ApproveOperationDto();
			BeanUtils.copyProperties(obj, dto);
			resultList.add(dto);
		}
		return resultList;
	}

	@Override
	public int deletePseudoAllObjectByApproveTypeIds(String approveId) {
		return approveOperationDao.deletePseudoAllObjectByApproveTypeIds(approveId);
	}

	@Override
	public List<ApproveOperationDto> queryListByApproveRoleCode(String typeCode, String approveRole) {
		List<ApproveOperationDto> resultList = approveOperationDao.queryListByApproveRoleCode(typeCode, approveRole);
		
		//设置英文件名称
		for(ApproveOperationDto operate : resultList) {
			String eName = nameMapper.get(operate.getOperationCode());
			operate.seteName(eName);
		}
		
		return resultList;
	}
}