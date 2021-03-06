package com.xinleju.cloud.oa.content.controller;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.content.dto.ContentTypeAndAuthorDto;
import com.xinleju.cloud.oa.content.dto.ContentTypeDto;
import com.xinleju.cloud.oa.content.dto.ContentTypeTreeData;
import com.xinleju.cloud.oa.content.dto.service.ContentTypeDtoServiceCustomer;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;
import com.xinleju.platform.univ.search.dto.SearchIndexDto;
import com.xinleju.platform.univ.search.dto.service.SearchIndexDtoServiceCustomer;

import javax.servlet.http.HttpServletRequest;


/**
 * 知识大类控制层
 *
 * @author admin
 */
@Controller
@RequestMapping("/oa/content/contentType")
public class ContentTypeController {

    private static Logger log = Logger.getLogger(ContentTypeController.class);

    @Autowired
    private ContentTypeDtoServiceCustomer contentTypeDtoServiceCustomer;
    @Autowired
    private SearchIndexDtoServiceCustomer searchIndexDtoServiceCustomer;

    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult get(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeDto contentTypeDto = JacksonUtils.fromJson(resultInfo, ContentTypeDto.class);
                result.setResult(contentTypeDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 返回分页对象
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/page", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult page(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentTypeDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                PageBeanInfo pageInfo = JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
                result.setResult(pageInfo);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用page方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 返回符合条件的列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/queryList", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult queryList(@RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        String paramaterJson = JacksonUtils.toJson(map);
        try {
            String dubboResultInfo = contentTypeDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<ContentTypeDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentTypeDto.class);
                result.setResult(list);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }

    /**
     * 保存实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult save(@RequestBody ContentTypeDto t) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = contentTypeDtoServiceCustomer.save(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {

                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeDto contentTypeDto = JacksonUtils.fromJson(resultInfo, ContentTypeDto.class);

                //处理全文检索
//		    	SearchIndexDto SearchIndexDto = new SearchIndexDto();
//		    	SearchIndexDto.setContent(content);
//		    	SearchIndexDto.setEsDocId(contentTypeDto.getId());;
//		    	searchIndexDtoServiceCustomer.save(arg0, arg1);

                result.setResult(contentTypeDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 删除实体对象
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult delete(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentTypeDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeDto contentTypeDto = JacksonUtils.fromJson(resultInfo, ContentTypeDto.class);
                result.setResult(contentTypeDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }


    /**
     * 删除实体对象
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    MessageResult deleteBatch(@PathVariable("ids") String ids) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentTypeDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeDto contentTypeDto = JacksonUtils.fromJson(resultInfo, ContentTypeDto.class);
                result.setResult(contentTypeDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用delete方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }

    /**
     * 伪删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deletePseudoAllObjectByIds/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public MessageResult deletePseudoAllObjectByIds(@PathVariable("ids") String ids){
        MessageResult result = new MessageResult();
        try {
            SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
            String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

            String dubboResultInfo = contentTypeDtoServiceCustomer.deletePseudoAllObjectByIds(userInfo, "{\"id\":\"" + ids + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeDto contentTypeDto = JacksonUtils.fromJson(resultInfo, ContentTypeDto.class);
                result.setResult(contentTypeDto);
                result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
                result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.DELETEERROR.isResult());
                result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用deletePseudoBatch方法:  【参数" + ids + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.DELETEERROR.isResult());
            result.setMsg(MessageInfo.DELETEERROR.getMsg() + "【" + e.getMessage() + "】");
        }

        return result;
    }

    /**
     * 修改修改实体对象
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public
    @ResponseBody
    MessageResult update(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        MessageResult result = new MessageResult();
        ContentTypeDto contentTypeDto = null;
        try {

            String dubboResultInfo = contentTypeDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                Map<String, Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
                map.put("id", id);
                oldMap.putAll(map);
                String updateJson = JacksonUtils.toJson(oldMap);
                String updateDubboResultInfo = contentTypeDtoServiceCustomer.update(getUserJson(), updateJson);
                DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
                if (updateDubboServiceResultInfo.isSucess()) {
                    Integer i = JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
                    result.setResult(i);
                    result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
                    result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
                } else {
                    result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                    result.setMsg(updateDubboServiceResultInfo.getMsg() + "【" + updateDubboServiceResultInfo.getExceptionMsg() + "】");
                }
            } else {
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg("不存在更新的对象");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(contentTypeDto);
                log.error("调用update方法:  【参数" + id + "," + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.UPDATEERROR.isResult());
                result.setMsg(MessageInfo.UPDATEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 查询知识管理大类总数
     *
     * @return
     */

    @RequestMapping(value = "/getCountFolders", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult getCountFolders(@RequestBody String paramaterJson) {
        MessageResult result = new MessageResult();
        String countFolder = contentTypeDtoServiceCustomer.countFolders(getUserJson(), null);
        DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(countFolder, DubboServiceResultInfo.class);
        if (dubboServiceResultInfo.isSucess()) {
            String resultInfo = dubboServiceResultInfo.getResult();
            result.setResult(resultInfo);
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } else {
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
        }
        return result;

    }

    /**
     * 热度查询统计
     *
     * @param paramaterJson
     * @return
     */
    @RequestMapping(value = "/getTopHeatDegree", method = {RequestMethod.POST}, consumes = "application/json")
    public
    @ResponseBody
    MessageResult getTopHeatDegree(@RequestBody String paramaterJson) {
        MessageResult result = new MessageResult();
        String countFolder = contentTypeDtoServiceCustomer.countFolders(getUserJson(), null);
        DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(countFolder, DubboServiceResultInfo.class);
        if (dubboServiceResultInfo.isSucess()) {
            String resultInfo = dubboServiceResultInfo.getResult();
            result.setResult(resultInfo);
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } else {
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
        }
        return result;

    }

    /**
     * 查询知识大类树
     *
     * @return
     */

    @RequestMapping(value = "/getContentTypeTree", method = {RequestMethod.GET})
    public
    @ResponseBody
    MessageResult getContentTypeTree() {
        MessageResult result = new MessageResult();
        String contentTypeTree = contentTypeDtoServiceCustomer.getContentTypeTree(getUserJson(), null);
        DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(contentTypeTree, DubboServiceResultInfo.class);
        if (dubboServiceResultInfo.isSucess()) {
            String resultInfo = dubboServiceResultInfo.getResult();
            List<ContentTypeTreeData> contentList = JacksonUtils.fromJson(resultInfo, ArrayList.class, ContentTypeTreeData.class);
            result.setResult(contentList);
            result.setSuccess(MessageInfo.GETSUCCESS.isResult());
            result.setMsg(MessageInfo.GETSUCCESS.getMsg());
        } else {
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
        }
        return result;

    }

    /**
     * B方法追加文件：使用FileWriter
     */
    private void appendMethodB(String fileName, String content) throws Exception {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.flush();
            writer.close();
    }

    /**
     * 保存实体对象
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/saveContentAndAuthor", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult saveContentAndAuthor(@RequestBody ContentTypeAndAuthorDto t, HttpServletRequest request) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = contentTypeDtoServiceCustomer.saveContentTypeAndAuthor(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeAndAuthorDto contentTypeAndAuthorDto = JacksonUtils.fromJson(resultInfo, ContentTypeAndAuthorDto.class);
                result.setResult(contentTypeAndAuthorDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 根据Id获取业务对象
     *
     * @param id 业务对象主键
     * @return 业务对象
     */
    @RequestMapping(value = "/getContentTypeAndAuthor/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    MessageResult getContentTypeAndAuthor(@PathVariable("id") String id) {
        MessageResult result = new MessageResult();
        try {
            String dubboResultInfo = contentTypeDtoServiceCustomer.getContentTypeAndAuthor(getUserJson(), "{\"id\":\"" + id + "\"}");
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeAndAuthorDto contentTypeAndAuthorDto = JacksonUtils.fromJson(resultInfo, ContentTypeAndAuthorDto.class);
                result.setResult(contentTypeAndAuthorDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("调用get方法:  【参数" + id + "】======" + "【" + e.getMessage() + "】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
        }
        return result;
    }


    /**
     * 附件上传时验证附件大小是否满足要求
     *
     * @param
     * @return 业务对象
     */
    @RequestMapping(value = "/validateFuJianSize", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult validateFuJianSize(@RequestBody ContentTypeDto t) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = contentTypeDtoServiceCustomer.validateFuJianSize(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeDto contentTypeDto = JacksonUtils.fromJson(resultInfo, ContentTypeDto.class);
                result.setResult(contentTypeDto);
                result.setSuccess(MessageInfo.GETSUCCESS.isResult());
                result.setMsg(MessageInfo.GETSUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.GETERROR.isResult());
                result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 新建一条知识大类的时候，往数据库属性表中，添加该知识大类的相应21条基本属性
     *
     * @param t
     * @return
     */
    @RequestMapping(value = "/saveContentAndAattribute", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    MessageResult saveContentAndAattribute(@RequestBody ContentTypeAndAuthorDto t) {
        MessageResult result = new MessageResult();
        try {
            String saveJson = JacksonUtils.toJson(t);
            String dubboResultInfo = contentTypeDtoServiceCustomer.saveContentTypeAndAuthor(getUserJson(), saveJson);
            DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                ContentTypeAndAuthorDto contentTypeAndAuthorDto = JacksonUtils.fromJson(resultInfo, ContentTypeAndAuthorDto.class);
                result.setResult(contentTypeAndAuthorDto);
                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
            } else {
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + dubboServiceResultInfo.getExceptionMsg() + "】");
            }
        } catch (Exception e) {
            try {
                //e.printStackTrace();
                ObjectMapper mapper = new ObjectMapper();
                String paramJson = mapper.writeValueAsString(t);
                log.error("调用save方法:  【参数" + paramJson + "】======" + "【" + e.getMessage() + "】");
                result.setSuccess(MessageInfo.SAVEERROR.isResult());
                result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
            } catch (JsonProcessingException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 保存知识大类
     *
     * @param t
     * @param oper
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateType", method = RequestMethod.POST)
    public
    @ResponseBody
    MessageResult saveOrUpdateType(ContentTypeDto t, String oper) {
        MessageResult result = new MessageResult();

        return result;

    }

    private String getUserJson() {
        SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
        String userJson = JacksonUtils.toJson(userBeanInfo);
        return userJson;
    }

    public static void main(String[] args) {

    }
}
