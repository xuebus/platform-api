package com.xinleju.platform.sys.sync.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.datasource.DataSourceContextHolder;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.org.dao.OrgnazationDao;
import com.xinleju.platform.sys.org.dao.UserDao;
import com.xinleju.platform.sys.org.dto.SyncUserDto;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.sync.dao.SyncDataDao;
import com.xinleju.platform.sys.sync.entity.SyncData;
import com.xinleju.platform.sys.sync.service.SyncDataService;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.utils.HttpClientUtil;

/**
 * @author admin
 * 
 * 
 */

@Service
public class SyncDataServiceImpl extends  BaseServiceImpl<String,SyncData> implements SyncDataService{
	

	@Autowired
	private SyncDataDao syncDataDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OrgnazationDao orgnazationDao;
	
	@Override
	public Page getBeanPage(Map<String, Object> map)  throws Exception{
		Page page = syncDataDao.getBeanPage(map);
		return page;
	}
	
	@Override
	@Transactional
	public MessageResult syncData(String id) throws Exception {
		MessageResult result = new MessageResult();
		SyncData syncData = syncDataDao.getObjectById(id);
		if(syncData.getStatus()){
			if(Objects.equals(syncData.getDataType(),"user")){//同步用户
				Map paraMap = new HashMap();
				Integer num = userDao.getCount(null);
				for(int i =0 ;i<num; i=i+syncData.getNum()){
					Integer start = i;
					Integer limit = syncData.getNum();
					paraMap.clear();
					paraMap.put("start",i);
					paraMap.put("limit",limit);
					Page page = userDao.getPage(paraMap,start,limit);
					List<User> list = (List<User>)page.getList();
					List<SyncUserDto> postList = new ArrayList<SyncUserDto> ();
					if(list!=null&&!list.isEmpty ()){
                        for(User user:list){
                        	SyncUserDto userDto = new SyncUserDto();
                        	userDto.setThirdId(user.getId());
                        	userDto.setLoginId(user.getLoginName());
                        	if(user.getDelflag()){
                        		userDto.setState("-1");
                        	}else{
                        		if(user.getStatus().equals("1")){
                        			userDto.setState("0");
                        		}else{
                        			userDto.setState("1");
                        		}
                        	}
                        	userDto.setThirdEnterpriseId(user.getTendId());
                        	userDto.setPassword(user.getPassword());
                        	userDto.setName(user.getRealName());
                        	userDto.setMobile(user.getMobile());
                        	userDto.setOrgEmail(user.getEmail());
                        	userDto.setEmail(user.getEmail());
                        	userDto.setOfficePhone(user.getWorkPhone());
                        	if(null == user.getIsMale()){
                        		userDto.setGender(2);
                        	}else{
                        		userDto.setGender(Integer.parseInt(user.getIsMale()));
                        	}
                        	
                        	userDto.setRemark(user.getRemark());
                        	userDto.setBirthday(user.getBirthday());
                        	userDto.setQq("");
                        	userDto.setWechat(user.getWeChat());
                        	userDto.setHomeAddress("");
                        	userDto.setIdentityCard("");
                        	userDto.setJobNumber(user.getJobNumber());
                        	postList.add(userDto);
						}
					}
					
                    paraMap.clear();
					paraMap.put("type",syncData.getDataType());
					paraMap.put("data",JacksonUtils.toJson(postList));
					String content = syncData.getSystem ()+"人员数据同步，接口url："+syncData.getUrl()+"，批次：第【"+(i+1)+"】条至第【"+(i+limit)+"】条";
					String phone = "13718744546";
					new HttpClientUtil(syncData.getUrl(),paraMap, "UTF-8",phone,content).doPost();
//					Thread.sleep(30000);
				}

			}else if(Objects.equals(syncData.getDataType(),"org")){//同步组织机构
				Map paraMap = new HashMap();
				Integer num = orgnazationDao.getCount(null);
				for(int i =0 ;i<num; i=i+syncData.getNum()){
					Integer start = i;
					Integer limit = syncData.getNum();
					paraMap.put("start",i);
					paraMap.put("limit",limit);
					Page page = orgnazationDao.getPage(paraMap,start,limit);
					List list = page.getList();
					paraMap.clear();
					paraMap.put("type",syncData.getDataType());
					paraMap.put("data",JacksonUtils.toJson(list));
					String content = syncData.getSystem ()+"组织机构数据同步，接口url："+syncData.getUrl()+"，第【"+i+"】条至第【"+(i+limit)+"】条";
					String phone = "13718744546";
					new HttpClientUtil(syncData.getUrl(),paraMap, "UTF-8",phone,content).doPost();
				}
			}
			result.setSuccess (true);
			result.setMsg ("数据同步开始");
		}else{
			result.setSuccess(false);
			result.setMsg("数据同步未开启！");
		}
		return result;
	}
	
	@Override
	public MessageResult syncDataOne(String id,String tendCode) throws Exception {
		MessageResult result = new MessageResult();
		Map mapc = new HashMap<>();
		mapc.put("sidx", "id");
		mapc.put("sord", "desc");
		DataSourceContextHolder.clearDataSourceType();
		DataSourceContextHolder.setDataSourceType(tendCode);
		List<SyncData> list_app = syncDataDao.queryList(mapc);
		for(SyncData syncData:list_app){
			if(syncData.getStatus()){
				if(Objects.equals(syncData.getDataType(),"user")){//同步用户
					Map paraMap = new HashMap();
					User user = userDao.getObjectById(id);
					List<SyncUserDto> postList = new ArrayList<SyncUserDto> ();
                	SyncUserDto userDto = new SyncUserDto();
                	userDto.setThirdId(user.getId());
                	userDto.setLoginId(user.getLoginName());
                	if(user.getDelflag()){
                		userDto.setState("-1");
                	}else{
                		if(user.getStatus().equals("1")){
                			userDto.setState("0");
                		}else{
                			userDto.setState("1");
                		}
                	}
                	userDto.setThirdEnterpriseId(user.getTendId());
                	userDto.setPassword(user.getPassword());
                	userDto.setName(user.getRealName());
                	userDto.setMobile(user.getMobile());
                	userDto.setOrgEmail(user.getEmail());
                	userDto.setEmail(user.getEmail());
                	userDto.setOfficePhone(user.getWorkPhone());
                	if(null == user.getIsMale()){
                		userDto.setGender(2);
                	}else{
                		userDto.setGender(Integer.parseInt(user.getIsMale()));
                	}
                	userDto.setRemark(user.getRemark());
                	userDto.setBirthday(user.getBirthday());
                	userDto.setQq("");
                	userDto.setWechat(user.getWeChat());
                	userDto.setHomeAddress("");
                	userDto.setIdentityCard("");
                	userDto.setJobNumber(user.getJobNumber());
                	postList.add(userDto);
                    paraMap.clear();
					paraMap.put("type",syncData.getDataType());
					paraMap.put("data",JacksonUtils.toJson(postList));
					String content = syncData.getSystem ()+"人员数据同步，接口url："+syncData.getUrl()+"，同步用户Id："+id;
					String phone = "13718744546";
					new HttpClientUtil(syncData.getUrl(),paraMap, "UTF-8",phone,content).doPost();
					result.setSuccess (true);
					result.setMsg ("数据同步开始");
				}
			}
		}
		return result;
	}
	
}
