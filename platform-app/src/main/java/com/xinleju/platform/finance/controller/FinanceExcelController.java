package com.xinleju.platform.finance.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.finance.dto.AccountCaptionDto;
import com.xinleju.platform.finance.dto.AccountSetCompanyDto;
import com.xinleju.platform.finance.dto.AssMappingDto;
import com.xinleju.platform.finance.dto.AssTypeDto;
import com.xinleju.platform.finance.dto.CashFlowItemDto;
import com.xinleju.platform.finance.dto.VoucherTemplateDto;
import com.xinleju.platform.finance.dto.VoucherTemplateEntryDto;
import com.xinleju.platform.finance.dto.VoucherTemplateTypeDto;
import com.xinleju.platform.finance.dto.service.AccountCaptionDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.AccountSetCompanyDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.AssMappingDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.AssTypeDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.CashFlowItemDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.VoucherTemplateDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.VoucherTemplateEntryDtoServiceCustomer;
import com.xinleju.platform.finance.dto.service.VoucherTemplateTypeDtoServiceCustomer;
import com.xinleju.platform.finance.utils.ExcelSheet;
import com.xinleju.platform.finance.utils.VoucherTitleFileArr;
import com.xinleju.platform.finance.utils.excel.DateUtil;
import com.xinleju.platform.finance.utils.excel.ExcelHelper;
import com.xinleju.platform.finance.utils.excel.HssfExcelHelper;
import com.xinleju.platform.finance.utils.excel.JxlExcelHelper;
import com.xinleju.platform.finance.utils.excel.StringUtil;
import com.xinleju.platform.finance.utils.excel.XssfExcelHelper;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 财务接口导入导出
 * @author admin
 *
 */
@Controller
@RequestMapping("/finance/excel")
public class FinanceExcelController {
    private static Logger log = Logger.getLogger(CashFlowItemController.class);

    @Autowired
    private AccountCaptionDtoServiceCustomer accountCaptionDtoServiceCustomer;
    @Autowired
    private CashFlowItemDtoServiceCustomer cashFlowItemDtoServiceCustomer;
    @Autowired
    private AssTypeDtoServiceCustomer assTypeDtoServiceCustomer;
    @Autowired
    private AssMappingDtoServiceCustomer assMappingDtoServiceCustomer;
    @Autowired
    private VoucherTemplateDtoServiceCustomer voucherTemplateDtoServiceCustomer;
    @Autowired
    private VoucherTemplateTypeDtoServiceCustomer voucherTemplateTypeDtoServiceCustomer;
    @Autowired
    private VoucherTemplateEntryDtoServiceCustomer voucherTemplateEntryDtoServiceCustomer;
    @Autowired
	private AccountSetCompanyDtoServiceCustomer accountSetCompanyDtoServiceCustomer;
    /**
     * 导入excel
     * @return
     */
    @RequestMapping(value="/excelImportHelper")
    public @ResponseBody
    MessageResult excelImport(@RequestParam(value="excelFile") MultipartFile excelFile,HttpServletRequest request, HttpServletResponse response )throws ServletException, IOException {
        MessageResult result=new MessageResult();
        String excelCompanyId = request.getParameter("excelCompanyId");
        try {
            //获取用户对象
            SecurityUserBeanInfo user= LoginUtils.getSecurityUserBeanInfo();
            //用户对象转json
            String userJson = JacksonUtils.toJson(user);
            Map<String,Object> companyMap = new HashMap<String,Object>();
            companyMap.put("accountSetId", excelCompanyId);
            companyMap.put("delflag", "0");
            String dubboResultInfoCompany=accountSetCompanyDtoServiceCustomer.queryList(userJson, JacksonUtils.toJson(companyMap));
            DubboServiceResultInfo dubboServiceResultInfoCompany= JacksonUtils.fromJson(dubboResultInfoCompany, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfoCompany.isSucess()){
				String resultInfo= dubboServiceResultInfoCompany.getResult();
				List<AccountSetCompanyDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,AccountSetCompanyDto.class);
				if(null != list && list.size() > 0){
					CommonsMultipartFile cf= (CommonsMultipartFile)excelFile;
		            DiskFileItem fi = (DiskFileItem)cf.getFileItem();
		            File file = fi.getStoreLocation();

		            int maxSize=2*1024*1024;        //单个上传文件大小的上限
		            //检查输入请求是否为multipart表单数据。
		            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		            if (isMultipart == true) {
		                //为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。
		                FileItemFactory factory = new DiskFileItemFactory();
		                ServletFileUpload upload = new ServletFileUpload(factory);
		                List<FileItem> items = upload.parseRequest(request);
		                Iterator<FileItem> itr = items.iterator();
		                while (itr.hasNext()) {
		                    FileItem item = (FileItem) itr.next();
		                    //检查当前项目是普通表单项目还是上传文件。
		                    if (!item.isFormField()) {
		                        //如果是上传文件
		                        String allName = item.getName();
		                        String fileName = allName.substring(0,allName.lastIndexOf("."));
		                        double fileSize = (item.getSize())/1024.0;
		                        String fileType = allName.substring(allName.lastIndexOf("."),allName.length());
		                        if(!"".equals(allName)){
		                            byte[] by = item.get();

		                        }
		                    }
		                }
		            }

		            String name = excelFile.getOriginalFilename();
		            String suffix = StringUtil.getSuffix(name);//获取后缀
		            long size = excelFile.getSize();
		            Map<String,Object> map = new HashMap<String,Object>();
		            if(size<=maxSize){//2M以内用JXL解析excel
		                if("xlsx".equals(suffix)){//07版本以上excel用poi XssfExcelHelper解析
		                    ExcelHelper eh = XssfExcelHelper.getInstance(file);//jxl解析
		                    map = readExcelToMap(eh);
		                }else{
		                    ExcelHelper eh = JxlExcelHelper.getInstance(file);//jxl解析
		                    map = readExcelToMap(eh);
		                }
		            }else{
		                if("xlsx".equals(suffix)){//07版本以上excel用poi  XssfExcelHelper解析
		                    ExcelHelper eh = XssfExcelHelper.getInstance(file);
		                    map = readExcelToMap(eh);
		                }else{//07版本以下excel用poi  ssfExcelHelper解析
		                    ExcelHelper eh = HssfExcelHelper.getInstance(file);
		                    map = readExcelToMap(eh);
		                }
		            }
		            //调用保存方法
		            String dubboResultInfo = voucherTemplateDtoServiceCustomer.saveVoucher(userJson,JacksonUtils.toJson(map),excelCompanyId,list.get(0).getCompanyId());
		            DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		            if(dubboServiceResultInfo.isSucess()) {
		                result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
		                result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		            }else{
		                result.setSuccess(MessageInfo.SAVEERROR.isResult());
		                result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		            }
				}else{
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg("请添加公司对照！");
				}
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfoCompany.getExceptionMsg()+"】");
		    }
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用excel导入方法======"+"【"+e.getMessage()+"】");
            result.setSuccess(MessageInfo.GETERROR.isResult());
            result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
        }
        return result;
    }

    /**
     * 导出excel
     * @return
     */
    @RequestMapping(value="/excelExportHelper")
    public void excelExport(HttpServletRequest request, HttpServletResponse response )throws ServletException, IOException {
        try {
        	String excelCompanyId = request.getParameter("excelCompanyId");
            //获取用户对象
            SecurityUserBeanInfo user= LoginUtils.getSecurityUserBeanInfo();
            //用户对象转json
            String userJson = JacksonUtils.toJson(user);

            String sheetName = DateUtil.format(new Date(), "yyyyMMddHHmmssSS");
            String mFilePath = request.getSession().getServletContext().getRealPath("/")+"\\finance\\excel\\凭证信息"+sheetName+".xls";

            File exportFile = new File(mFilePath);
            ExcelHelper exportEh = HssfExcelHelper.getInstance(exportFile);
            //写数据
            writeExcelToMap(userJson,excelCompanyId,exportEh);

            //弹出下载框，下载excle文件
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            try{
                // 取得文件名。
                String filename = exportFile.getName();
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(mFilePath));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename="
                        + new String(filename.getBytes(), "ISO8859-1"));
                response.addHeader("Content-Length", "" + exportFile.length());
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                toClient.write(buffer);
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (toClient != null) {
                    toClient.flush();
                    toClient.close();
                }
                deleteAll(new File(request.getSession().getServletContext().getRealPath("/")+"\\finance\\excel"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用excel导出方法======"+"【"+e.getMessage()+"】");
        }
    }

    public static void deleteAll(File file){

        if(file.isFile() || file.list().length ==0)
        {
            file.delete();
        }else{
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteAll(files[i]);
                files[i].delete();
            }
            // if(file.exists())         //如果文件本身就是目录 ，就要删除目录
            //file.delete();
        }
    }

    public Map<String,Object> readExcelToMap(ExcelHelper eh) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        List<AccountCaptionDto> accountCaptionDtoList = null;
        accountCaptionDtoList = eh.readExcel(AccountCaptionDto.class, VoucherTitleFileArr.captionFiles,0,true);//会计科目
        map.put(ExcelSheet.ACCOUNT_CAPTION.getName(),accountCaptionDtoList);
        List<CashFlowItemDto> cashFlowItemDtoList = null;
        cashFlowItemDtoList = eh.readExcel(CashFlowItemDto.class,VoucherTitleFileArr.cashFlowItemFiles,1,true);//现金流量
        map.put(ExcelSheet.CASH_FLOW_ITEM.getName(),cashFlowItemDtoList);
        List<AssTypeDto> assTypeDtoList = null;
        assTypeDtoList = eh.readExcel(AssTypeDto.class,VoucherTitleFileArr.assTypeFiles,2,true);//辅助核算
        map.put(ExcelSheet.ASS_TYPE.getName(),assTypeDtoList);
        List<AssMappingDto> assMappingDtoList = null;
        assMappingDtoList = eh.readExcel(AssMappingDto.class,VoucherTitleFileArr.assMappingFiles,3,true);//辅助核算明细
        map.put(ExcelSheet.ASS_MAPPING.getName(),assMappingDtoList);
        List<VoucherTemplateTypeDto> voucherTemplateTypeDtoList = null;
        voucherTemplateTypeDtoList = eh.readExcel(VoucherTemplateTypeDto.class,VoucherTitleFileArr.templateTypeFiles,4,true);//凭证模板类型
        map.put(ExcelSheet.VOUCHER_TEMPLATE_TYPE.getName(),voucherTemplateTypeDtoList);
        List<VoucherTemplateDto> voucherTemplateDtoList = null;
        voucherTemplateDtoList = eh.readExcel(VoucherTemplateDto.class,VoucherTitleFileArr.templateFiles,5,true);//凭证模板
        map.put(ExcelSheet.VOUCHER_TEMPLATE.getName(),voucherTemplateDtoList);
        List<VoucherTemplateEntryDto> voucherTemplateEntryDtoList = null;
        voucherTemplateEntryDtoList = eh.readExcel(VoucherTemplateEntryDto.class,VoucherTitleFileArr.templateEntryFiles,6,true);//凭证模板分录
        map.put(ExcelSheet.VOUCHER_TEMPLATE_ENTRY.getName(),voucherTemplateEntryDtoList);
        return map;
    }

    public void writeExcelToMap(String userInfo ,String excelCompanyId,ExcelHelper eh) throws Exception {
        String paramaterJson = "{\"delflag\":0,\"accountSetId\":\""+excelCompanyId+"\"}";
        //会计科目
        String dubboResultInfo=accountCaptionDtoServiceCustomer.queryList(userInfo, paramaterJson);
        DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
        List<AccountCaptionDto> list = new ArrayList<AccountCaptionDto>();
        if(dubboServiceResultInfo.isSucess()) {
            String resultInfo = dubboServiceResultInfo.getResult();
            list = JacksonUtils.fromJson(resultInfo, ArrayList.class, AccountCaptionDto.class);
        }
        String[] acTitles = VoucherTitleFileArr.captionTitle;
        String[] acFieldNames = VoucherTitleFileArr.captionFiles;
        eh.writeExcel(AccountCaptionDto.class, list, acFieldNames, acTitles,"会计科目");
        //现金流量
        String cashDubboResultInfo=cashFlowItemDtoServiceCustomer.queryList(userInfo, paramaterJson);
        DubboServiceResultInfo cashDubboServiceResultInfo= JacksonUtils.fromJson(cashDubboResultInfo, DubboServiceResultInfo.class);
        List<CashFlowItemDto> cashList = new ArrayList<CashFlowItemDto>();
        if(cashDubboServiceResultInfo.isSucess()) {
            String resultInfo = cashDubboServiceResultInfo.getResult();
            cashList = JacksonUtils.fromJson(resultInfo, ArrayList.class, CashFlowItemDto.class);
        }
        String[] cashTitles = VoucherTitleFileArr.cashFlowItemTitle;
        String[] cashFieldNames = VoucherTitleFileArr.cashFlowItemFiles;
        eh.writeExcel(CashFlowItemDto.class, cashList, cashFieldNames, cashTitles,"现金流量");
        //辅助核算
        String assTypeDubboResultInfo=assTypeDtoServiceCustomer.queryList(userInfo, paramaterJson);
        DubboServiceResultInfo assTypeDubboServiceResultInfo= JacksonUtils.fromJson(assTypeDubboResultInfo, DubboServiceResultInfo.class);
        List<AssTypeDto> assTypeList = new ArrayList<AssTypeDto>();
        if(assTypeDubboServiceResultInfo.isSucess()) {
            String resultInfo = assTypeDubboServiceResultInfo.getResult();
            assTypeList = JacksonUtils.fromJson(resultInfo, ArrayList.class, AssTypeDto.class);
        }
        String[] assTypeTitles = VoucherTitleFileArr.assTypeTitle;
        String[] assTypeFieldNames = VoucherTitleFileArr.assTypeFiles;
        eh.writeExcel(AssTypeDto.class, assTypeList, assTypeFieldNames, assTypeTitles,"辅助核算类型");
        //辅助核算明细
        Map<String,Object> map = new HashMap<String,Object>();
        String result = "";
        for(AssTypeDto dto : assTypeList){
			result += dto.getId() + "," ;
		}
        map.put("asstypeids", result);
        String assMappingDubboResultInfo=assMappingDtoServiceCustomer.queryListByAssTypeIds(userInfo, JacksonUtils.toJson(map));
        DubboServiceResultInfo assMappingDubboServiceResultInfo= JacksonUtils.fromJson(assMappingDubboResultInfo, DubboServiceResultInfo.class);
        List<AssMappingDto> assMappingList = new ArrayList<AssMappingDto>();
        if(assMappingDubboServiceResultInfo.isSucess()) {
            String resultInfo = assMappingDubboServiceResultInfo.getResult();
            assMappingList = JacksonUtils.fromJson(resultInfo, ArrayList.class, AssMappingDto.class);
        }
        String[] assMappingTitles = VoucherTitleFileArr.assMappingTitle;
        String[] assMappingFieldNames = VoucherTitleFileArr.assMappingFiles;
        eh.writeExcel(AssMappingDto.class, assMappingList, assMappingFieldNames, assMappingTitles,"辅助核算明细");
        //凭证模板类型
        String templateTypeDubboResultInfo=voucherTemplateTypeDtoServiceCustomer.queryList(userInfo, paramaterJson);
        DubboServiceResultInfo templateTypeDubboServiceResultInfo= JacksonUtils.fromJson(templateTypeDubboResultInfo, DubboServiceResultInfo.class);
        List<VoucherTemplateTypeDto> templateTypeList = new ArrayList<VoucherTemplateTypeDto>();
        if(templateTypeDubboServiceResultInfo.isSucess()) {
            String resultInfo = templateTypeDubboServiceResultInfo.getResult();
            templateTypeList = JacksonUtils.fromJson(resultInfo, ArrayList.class, VoucherTemplateTypeDto.class);
        }
        String[] templateTypeTitles = VoucherTitleFileArr.templateTypeTitle;
        String[] templateTypeFieldNames = VoucherTitleFileArr.templateTypeFiles;
        eh.writeExcel(VoucherTemplateTypeDto.class, templateTypeList, templateTypeFieldNames, templateTypeTitles,"凭证模板-业务类型");
        //凭证模板
        Map<String,Object> templatemap = new HashMap<String,Object>();
        String templateresult = "";
        for(VoucherTemplateTypeDto dto : templateTypeList){
        	templateresult += dto.getId() + "," ;
		}
        templatemap.put("typeId", templateresult);
        String templateDubboResultInfo=voucherTemplateDtoServiceCustomer.queryListByTypeIds(userInfo, JacksonUtils.toJson(templatemap));
        DubboServiceResultInfo templateDubboServiceResultInfo= JacksonUtils.fromJson(templateDubboResultInfo, DubboServiceResultInfo.class);
        List<VoucherTemplateDto> templateList = new ArrayList<VoucherTemplateDto>();
        if(templateDubboServiceResultInfo.isSucess()) {
            String resultInfo = templateDubboServiceResultInfo.getResult();
            templateList = JacksonUtils.fromJson(resultInfo, ArrayList.class, VoucherTemplateDto.class);
        }
        String[] templateTitles = VoucherTitleFileArr.templateTitle;
        String[] templateFieldNames = VoucherTitleFileArr.templateFiles;
        eh.writeExcel(VoucherTemplateDto.class, templateList, templateFieldNames, templateTitles,"凭证模板");
        //凭证模板分录
        Map<String,Object> templateentrymap = new HashMap<String,Object>();
        String templateentryresult = "";
        for(VoucherTemplateDto dto : templateList){
        	templateentryresult += dto.getId() + "," ;
		}
        templateentrymap.put("typeId", templateentryresult);
        String templateEntryDubboResultInfo=voucherTemplateEntryDtoServiceCustomer.queryListByTemplateIds(userInfo, JacksonUtils.toJson(templateentrymap));
        DubboServiceResultInfo templateEntryDubboServiceResultInfo= JacksonUtils.fromJson(templateEntryDubboResultInfo, DubboServiceResultInfo.class);
        List<VoucherTemplateEntryDto> templateEntryList = new ArrayList<VoucherTemplateEntryDto>();
        if(templateEntryDubboServiceResultInfo.isSucess()) {
            String resultInfo = templateEntryDubboServiceResultInfo.getResult();
            templateEntryList = JacksonUtils.fromJson(resultInfo, ArrayList.class, VoucherTemplateEntryDto.class);
        }
        String[] templateEntryTitles = VoucherTitleFileArr.templateEntryTitle;
        String[] templateEntryFieldNames = VoucherTitleFileArr.templateEntryFiles;
        eh.writeExcel(VoucherTemplateEntryDto.class, templateEntryList, templateEntryFieldNames, templateEntryTitles,"凭证模板分录");
    }

}
