package com.xinleju.cloud.oa.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.cloud.oa.sys.dto.SysFloatWindowDto;
import com.xinleju.cloud.oa.sys.dto.service.SysFloatWindowDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 浮动窗口表控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/oa/sys/sysFloatWindow")
public class SysFloatWindowController {

	private static Logger log = Logger.getLogger(SysFloatWindowController.class);
	@Autowired
	private SysFloatWindowDtoServiceCustomer sysFloatWindowDtoServiceCustomer;
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysFloatWindowDto sysFloatWindowDto=JacksonUtils.fromJson(resultInfo, SysFloatWindowDto.class);
				result.setResult(sysFloatWindowDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				 result.setCode(dubboServiceResultInfo.getCode());
			     result.setSuccess(dubboServiceResultInfo.isSucess());
			     result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	
	
	/**
	 * 返回分页对象
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
		    String dubboResultInfo=sysFloatWindowDtoServiceCustomer.getPage(JacksonUtils.toJson(userInfo), paramaterJson);
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
			//e.printStackTrace();
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
		String paramaterJson =  JacksonUtils.toJson(map);
		DubboServiceResultInfo dubboServiceResultInfo = new DubboServiceResultInfo();
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.queryList(JacksonUtils.toJson(userInfo), paramaterJson);
		     dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysFloatWindowDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysFloatWindowDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getCode());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		   }catch (Exception e) {
			//e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody SysFloatWindowDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.save(JacksonUtils.toJson(userInfo), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysFloatWindowDto sysFloatWindowDto=JacksonUtils.fromJson(resultInfo, SysFloatWindowDto.class);
				result.setResult(sysFloatWindowDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getCode());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			try {
				//e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysFloatWindowDto sysFloatWindowDto=JacksonUtils.fromJson(resultInfo, SysFloatWindowDto.class);
				result.setResult(sysFloatWindowDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getCode());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.deleteAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysFloatWindowDto sysFloatWindowDto=JacksonUtils.fromJson(resultInfo, SysFloatWindowDto.class);
				result.setResult(sysFloatWindowDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getCode());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		SysFloatWindowDto sysFloatWindowDto=null;
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.getObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=sysFloatWindowDtoServiceCustomer.update(JacksonUtils.toJson(userInfo), updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setCode(updateDubboServiceResultInfo.getCode());
				     result.setResult(updateDubboServiceResultInfo.getMsg());
				     result.setSuccess(updateDubboServiceResultInfo.isSucess());
				     result.setMsg(updateDubboServiceResultInfo.getMsg());
				 }
			}else{
				 result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 //e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(sysFloatWindowDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.deletePseudoObjectById(JacksonUtils.toJson(userInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysFloatWindowDto sysFloatWindowDto=JacksonUtils.fromJson(resultInfo, SysFloatWindowDto.class);
				result.setResult(sysFloatWindowDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
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
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.deletePseudoAllObjectByIds(JacksonUtils.toJson(userInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysFloatWindowDto sysFloatWindowDto=JacksonUtils.fromJson(resultInfo, SysFloatWindowDto.class);
				result.setResult(sysFloatWindowDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setResult(dubboServiceResultInfo.getResult());
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		
		return result;
	}
	/**
	 * 模糊查询： 名称或编码
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/fuzzySearch",method=RequestMethod.POST,consumes="application/json")
	public @ResponseBody MessageResult fuzzySearch(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String  paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
			String dubboResultInfo=sysFloatWindowDtoServiceCustomer.fuzzySearch(JacksonUtils.toJson(userInfo),paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysFloatWindowDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysFloatWindowDto.class);
					result.setResult(list);
					result.setSuccess(MessageInfo.QUERYSUCCESS.isResult());
					result.setMsg(MessageInfo.QUERYSUCCESS.getMsg());
			}else{
				 result.setCode(dubboServiceResultInfo.getCode());
			     result.setResult(null);
			     result.setSuccess(dubboServiceResultInfo.isSucess());
			     result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		    log.error("调用fuzzySearch方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.QUERYERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
}
