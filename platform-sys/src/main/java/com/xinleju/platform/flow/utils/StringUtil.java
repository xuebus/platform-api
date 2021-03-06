package com.xinleju.platform.flow.utils;

import com.alibaba.dubbo.common.utils.StringUtils;

public class StringUtil {

	public static String convertSingleQuotes(String value) {
		if(StringUtils.isNotEmpty(value)) {
			if(value.contains("\\")) {
				return value;
			} else {
				return value.replaceAll("\'", "\\\\'");
			}
		}
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(convertSingleQuotes("cs\\' cs"));
	}
}
