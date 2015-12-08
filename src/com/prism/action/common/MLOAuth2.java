package com.prism.action.common;

import com.prism.action.HttpWeb;

public class MLOAuth2 {
	private static final String APPID = "wx71dba835dc454222";
	private static final String APPSECRET = "6710bfe35ed37ec5044d3ee18297db1d";
	
	public String getAccessToken(String code) {
		String url = String
				.format("https://api.weixin.qq.com/sns/oauth2/access_token?"
						+ "appid=%1$s&secret=%2$s&code=%3$s&grant_type=authorization_code"
						,APPID,APPSECRET,code
						);
		String result = HttpWeb.getGetResponse(url);
		return result;
	}
}
