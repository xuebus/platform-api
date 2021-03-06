
package com.xinleju.platform.weixin.utils;

import java.io.Serializable;

/**
 * 云平台token对象
 * @author 马长习
 * @date 2017年6月12日
 */
public class SSOToken implements Serializable {

	private static final long serialVersionUID = 6091650770487171422L;
	
	private String token;
	private String expiredTime;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}
	
}
