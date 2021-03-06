/**
 * 
 */
package com.xinleju.platform.weixin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * 单点登录云平台配置信息
 * @author 马长习
 * @date 2017年6月12日
 */
public class SSOLoginUtils {
	
	private static Logger logger = LoggerFactory.getLogger(SSOLoginUtils.class);
	
	private static final String APPLICATION_JSON = "application/json";

	public static String getIp(){
		return getSsoConfig().getString("ip");
	}
	
	public static String getPort(){
		return getSsoConfig().getString("port");
	}
	
	public static String getDomain(){
		return getSsoConfig().getString("domain");
	}
	
	public static String getAppId(){
		return getSsoConfig().getString("appId");
	}
	
	public static String getAppSecret(){
		return getSsoConfig().getString("appSecret");
	}
	
	public static String getSsoUri(){
		return getSsoConfig().getString("ssoUri");
	}
	
	public static String getTokenUri(){
		return getSsoConfig().getString("tokenUri");
	}
	
	public static String getCountUri(){
		return getSsoConfig().getString("countUri");
	}
	
	public static JSONObject getSsoConfig(){
		InputStream input = SSOLoginUtils.class.getClassLoader().getResourceAsStream("ssoConfig.properties");
		String ssoConfig = replaceBlank(convertStreamToString(input));
		return JSON.parseObject(ssoConfig);
	}
	
	public static String replaceBlank(String str) {
        Pattern p = Pattern.compile("\\s * |\t |\r |\n");
        Matcher m = p.matcher(str);
        String after = m.replaceAll("");
        return after;
    }

	public static String convertStreamToString(InputStream in) {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n, "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();

	}
	
	public static JSONObject getToken(String loginName, String tendId, String tendCode){
		String tokenUrl = getDomain()+getTokenUri()+"?appId="+getAppId()+"&appSecret="+getAppSecret()
		+"&userInfo="+loginName+"&tendId="+tendId+"&tendCode="+tendCode;
		System.out.println("tokenUrl="+tokenUrl);
		return doGet(tokenUrl);
	}
	
	public static JSONObject doGet(String url){
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		JSONObject response = null;
		try {
			System.out.println("httpclient.execute(httpGet) .... 开始执行.....");
			HttpResponse res = httpclient.execute(httpGet);
			System.out.println("返回信息是res="+JacksonUtils.toJson(res));
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String result = EntityUtils.toString(res.getEntity());
				response = JSONObject.parseObject(result);
			}
		} catch (Exception e) {
			logger.error("http请求异常！请求url为："+url, e);
		}
		return response;
	}
	
	public static boolean isValid(SSOToken token){
		String expiredDate = token.getExpiredTime();
		// 设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取当前系统时间
		String nowDate = df.format(new Date());
		int result = compareDate(expiredDate, nowDate);
		if(result <= 0){
			return false;
		}else{
			return true;
		}
	}
	
	private static int compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
	}
}
