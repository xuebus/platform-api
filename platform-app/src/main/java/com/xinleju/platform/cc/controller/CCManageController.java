package com.xinleju.platform.cc.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.AttachmentDto;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentDtoServiceCustomer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/10/20.
 */
@Controller
@RequestMapping("/cc/ccManage")
public class CCManageController {
    private static Logger log = Logger.getLogger(CCManageController.class);

    @Autowired
    private AttachmentDtoServiceCustomer attachmentDtoServiceCustomer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value="/save",method= RequestMethod.POST, consumes="application/json")
    public @ResponseBody
    MessageResult save(@RequestBody Map<String,Object> map){
        MessageResult result=new MessageResult();
        try {
            String ccName = (String) map.get("ccName");
            Map<String, Object> postMap = new HashMap();
            postMap.put ("businessId", Arrays.asList (new String[]{"ccmanage"}));
            postMap.put ("appId", "1");
            postMap.put ("categoryId", "1");
            String resultJson = attachmentDtoServiceCustomer.queryListByObject (JacksonUtils.toJson (LoginUtils.getSecurityUserBeanInfo ()), JacksonUtils.toJson (postMap));
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultJson, DubboServiceResultInfo.class);
            List<AttachmentDto> attachmentDtos = JacksonUtils.fromJson (dubboServiceResultInfo.getResult (), List.class, AttachmentDto.class);
            String downloadUrl = null;
            if(attachmentDtos!=null&&!attachmentDtos.isEmpty ()){
                AttachmentDto attachmentDto = attachmentDtos.get (0);
                String attachName = attachmentDto.getFullName();
                downloadUrl =  attachmentDto.getUrl ();
                downloadUrl += "?attname="+attachName;
            }

            ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
            //String cacheCompanyId = valueOperations.get("");

            valueOperations.set("ccExeFileUrl",downloadUrl);

            Map<String, Object> postMap1 = new HashMap();
            postMap1.put ("businessId", Arrays.asList (new String[]{"xpccmanage"}));
            postMap1.put ("appId", "1");
            postMap1.put ("categoryId", "1");
            String resultJson1 = attachmentDtoServiceCustomer.queryListByObject (JacksonUtils.toJson (LoginUtils.getSecurityUserBeanInfo ()), JacksonUtils.toJson (postMap1));
            DubboServiceResultInfo dubboServiceResultInfo1 = JacksonUtils.fromJson (resultJson1, DubboServiceResultInfo.class);
            List<AttachmentDto> attachmentDtos1 = JacksonUtils.fromJson (dubboServiceResultInfo1.getResult (), List.class, AttachmentDto.class);
            String downloadUrl1 = null;
            if(attachmentDtos1!=null&&!attachmentDtos1.isEmpty ()){
                AttachmentDto attachmentDto1 = attachmentDtos1.get (0);
                String attachName = attachmentDto1.getFullName();
                downloadUrl1 =  attachmentDto1.getUrl ();
                downloadUrl1 += "?attname="+attachName;
            }

            valueOperations.set("xpCcExeFileUrl",downloadUrl1);
            result.setResult(true);
            result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
            result.setMsg(MessageInfo.SAVESUCCESS.getMsg());

        } catch (Exception e) {
            try {
                ////e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String  paramJson = mapper.writeValueAsString(map);
                log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    @RequestMapping(value = "/getExeFileUrl", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult getExeFileUrl() {
        MessageResult result = new MessageResult();
        try {


            /*ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
            //Win7~Win10通用版
            String ccExeFileUrl = valueOperations.get("ccExeFileUrl");
            //WinXP专用版
            String xpCcExeFileUrl = valueOperations.get("xpCcExeFileUrl");
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("ccExeFileUrl",ccExeFileUrl);
            map.put("xpCcExeFileUrl",xpCcExeFileUrl);
            result.setResult(map);
            result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
            result.setMsg(MessageInfo.SAVESUCCESS.getMsg());*/
            Map<String,Object> resultMap = new HashMap<String,Object>();
            Map<String, Object> postMap = new HashMap();
            postMap.put ("businessId", Arrays.asList (new String[]{"ccmanage"}));
            postMap.put ("appId", "1");
            postMap.put ("categoryId", "1");
            String resultJson = attachmentDtoServiceCustomer.queryListByObject (JacksonUtils.toJson (LoginUtils.getSecurityUserBeanInfo ()), JacksonUtils.toJson (postMap));
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultJson, DubboServiceResultInfo.class);
            List<AttachmentDto> attachmentDtos = JacksonUtils.fromJson (dubboServiceResultInfo.getResult (), List.class, AttachmentDto.class);
            String downloadUrl = null;
            if(attachmentDtos!=null&&!attachmentDtos.isEmpty ()){
                AttachmentDto attachmentDto = attachmentDtos.get (0);
                String attachName = attachmentDto.getFullName();
                downloadUrl =  attachmentDto.getUrl ();
                downloadUrl += "?attname="+attachName;
            }
            resultMap.put("ccExeFileUrl",downloadUrl);

            //ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
            //String cacheCompanyId = valueOperations.get("");

            //valueOperations.set("ccExeFileUrl",downloadUrl);

            Map<String, Object> postMap1 = new HashMap();
            postMap1.put ("businessId", Arrays.asList (new String[]{"xpccmanage"}));
            postMap1.put ("appId", "1");
            postMap1.put ("categoryId", "1");
            String resultJson1 = attachmentDtoServiceCustomer.queryListByObject (JacksonUtils.toJson (LoginUtils.getSecurityUserBeanInfo ()), JacksonUtils.toJson (postMap1));
            DubboServiceResultInfo dubboServiceResultInfo1 = JacksonUtils.fromJson (resultJson1, DubboServiceResultInfo.class);
            List<AttachmentDto> attachmentDtos1 = JacksonUtils.fromJson (dubboServiceResultInfo1.getResult (), List.class, AttachmentDto.class);
            String downloadUrl1 = null;
            if(attachmentDtos1!=null&&!attachmentDtos1.isEmpty ()){
                AttachmentDto attachmentDto1 = attachmentDtos1.get (0);
                String attachName = attachmentDto1.getFullName();
                downloadUrl1 =  attachmentDto1.getUrl ();
                downloadUrl1 += "?attname="+attachName;
            }
            resultMap.put("xpCcExeFileUrl",downloadUrl1);

            //valueOperations.set("xpCcExeFileUrl",downloadUrl1);
            result.setResult(resultMap);
            result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
            result.setMsg(MessageInfo.SAVESUCCESS.getMsg());

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用getExeFileUrl方法:  【参数 】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }
}
