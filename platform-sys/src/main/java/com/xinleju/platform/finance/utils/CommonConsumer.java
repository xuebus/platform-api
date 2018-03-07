package com.xinleju.platform.finance.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.dubbo.config.spring.ReferenceBean;

/**
 * 获取业务接口类
 * @author ly
 * @param <T>
 *
 */
public class CommonConsumer<T> {

	private org.slf4j.Logger logger = LoggerFactory.getLogger(CommonConsumer.class);
    private Map<String,Object> consumerMap = new HashMap<String, Object>();

    public <T> void loadConsumerToMap(T t,String refenceId, HttpServletRequest req){
    	ReferenceBean<T> referenceBean = new ReferenceBean<T>();  
    	HttpSession session =req.getSession();
	    ServletContext context = session.getServletContext();
	    ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	    
    	referenceBean.setApplicationContext(ctx);  
    	String interfaceName = null;
    	if(t.toString().startsWith("interface")){
    		interfaceName = t.toString().substring(10);
    	}else if(t.toString().startsWith("class")){
    		interfaceName = t.toString().substring(6);
    	}
    	referenceBean.setInterface(interfaceName);  
        try {
        	referenceBean.afterPropertiesSet();
        	referenceBean.setId(refenceId);
        	referenceBean.setCheck(false);
        	Object target =  referenceBean.get();
			consumerMap.put(refenceId, target);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用dubbo方法获取服务======"+"【"+e.getMessage()+"】");
		}  
    }
    
    public <T> void loadConsumerToMap1(T t,String refenceId){
    	ReferenceBean<T> referenceBean = new ReferenceBean<T>();  
	    ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {"dubbo-customer-finance.xml"});
	    
    	referenceBean.setApplicationContext(ctx);  
    	String interfaceName = null;
    	if(t.toString().startsWith("interface")){
    		interfaceName = t.toString().substring(10);
    	}else if(t.toString().startsWith("class")){
    		interfaceName = t.toString().substring(6);
    	}
    	referenceBean.setInterface(interfaceName);  
        try {
        	referenceBean.afterPropertiesSet();
        	referenceBean.setId(refenceId);
        	referenceBean.setCheck(false);
        	Object target =  referenceBean.get();
			consumerMap.put(refenceId, target);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用dubbo方法获取服务======"+"【"+e.getMessage()+"】");
		}  
    }
    
	public Map<String,Object> getConsumerMap(){
		return consumerMap;
	}
}