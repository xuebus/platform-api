package com.xinleju.cloud.oa.bbs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.uitls.LoginUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.bbs.dto.BbsReplyDto;
import com.xinleju.cloud.oa.bbs.dto.service.BbsReplyDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;


/**
 * 回复（帖子回复）控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/oa/bbs/reply")
public class BbsReplyController {

	private static Logger log = Logger.getLogger(BbsReplyController.class);
	
	@Autowired
	private BbsReplyDtoServiceCustomer bbsReplyDtoServiceCustomer;
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.getBeanById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				Map resultMap = JacksonUtils.fromJson (dubboServiceResultInfo.getResult(),Map.class);
				result.setResult(resultMap);
				result.setSuccess(dubboServiceResultInfo.isSucess ());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}else{
				result.setSuccess(dubboServiceResultInfo.isSucess ());
				result.setMsg(dubboServiceResultInfo.getMsg ());
				result.setCode (ErrorInfoCode.SYSTEM_ERROR.getValue ());
			}
		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}

	/**
	 * 根据Id获取业务对象
	 *
	 * @param id  业务对象主键
	 *
	 * @return     业务对象
	 */
	@RequestMapping(value="/quote/{id}",method=RequestMethod.POST)
	public @ResponseBody MessageResult quote(@PathVariable("id")  String id,@RequestBody  Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.getBeanById(getUserJson(), JacksonUtils.toJson (map));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				Map resultMap = JacksonUtils.fromJson (dubboServiceResultInfo.getResult(),Map.class);
				result.setResult(resultMap);
				result.setSuccess(dubboServiceResultInfo.isSucess ());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}else{
				result.setSuccess(dubboServiceResultInfo.isSucess ());
				result.setMsg(dubboServiceResultInfo.getMsg ());
				result.setCode (ErrorInfoCode.SYSTEM_ERROR.getValue ());
			}
		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	/**
	 * 返回分页对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
		    String dubboResultInfo=bbsReplyDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<BbsReplyDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,BbsReplyDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody BbsReplyDto t){
		MessageResult result=new MessageResult();
		try {
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=bbsReplyDtoServiceCustomer.save(getUserJson(), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsReplyDto bbsReplyDto=JacksonUtils.fromJson(resultInfo, BbsReplyDto.class);
				result.setResult(bbsReplyDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(dubboServiceResultInfo.getMsg ());
				result.setCode (dubboServiceResultInfo.getCode ());
		    }
		} catch (Exception e) {
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsReplyDto bbsReplyDto=JacksonUtils.fromJson(resultInfo, BbsReplyDto.class);
				result.setResult(bbsReplyDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsReplyDto bbsReplyDto=JacksonUtils.fromJson(resultInfo, BbsReplyDto.class);
				result.setResult(bbsReplyDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		BbsReplyDto bbsReplyDto=null;
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=bbsReplyDtoServiceCustomer.update(getUserJson(), updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(updateDubboServiceResultInfo.isSucess ());
					 result.setMsg(updateDubboServiceResultInfo.getMsg());
					 result.setCode (updateDubboServiceResultInfo.getCode ());
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());

		}
		return result;
	}

	/**
	 * 伪删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.deletePseudoObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsReplyDto bbsReplyDto=JacksonUtils.fromJson(resultInfo, BbsReplyDto.class);
				result.setResult(bbsReplyDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}
	
	
	/**
	 * 伪删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.deletePseudoAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsReplyDto bbsReplyDto=JacksonUtils.fromJson(resultInfo, BbsReplyDto.class);
				result.setResult(bbsReplyDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}


	/**
	 * 修改修改实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/praise/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult praise(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
				String updateDubboResultInfo=bbsReplyDtoServiceCustomer.updatePraiseNum(getUserJson(),  "{\"id\":\""+id+"\"}");
				DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				if(updateDubboServiceResultInfo.isSucess()){
					Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					result.setResult(i);
					result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				}else{
					result.setSuccess(updateDubboServiceResultInfo.isSucess ());
					result.setMsg(updateDubboServiceResultInfo.getMsg ());
					result.setResult (updateDubboServiceResultInfo.getResult ());
				}
		} catch (Exception e) {
				result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());

		}
		return result;
	}

	/**
	 * 修改修改实体对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/updateStatus/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult updateStatus(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsReplyDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String updateJson= JacksonUtils.toJson(map);
				String updateDubboResultInfo=bbsReplyDtoServiceCustomer.updateStatus(getUserJson(), updateJson);
				DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
					result.setSuccess(updateDubboServiceResultInfo.isSucess ());
					result.setMsg(updateDubboServiceResultInfo.getMsg());
					result.setResult (updateDubboServiceResultInfo.getResult ());
			}else{
				result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg("不存在更新的对象");
				result.setCode (ErrorInfoCode.NULL_ERROR.getValue ());
			}
		} catch (Exception e) {
				result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	private String getUserJson() {
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();

		String userJson = JacksonUtils.toJson(userBeanInfo);
		return userJson;
	}
}
