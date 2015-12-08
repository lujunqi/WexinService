package com.prism.action;

/**
 * 美伦微信平台
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.prism.action.common.JsonUtil;

//http://58.20.37.144/prism/wx

public class WeiXinMLAction extends HttpServlet {
	private static final long serialVersionUID = 13L;
	private static final String APPID = "wx71dba835dc454222";
	private static final String APPSECRET = "6710bfe35ed37ec5044d3ee18297db1d";
	
	//美伦
//	private static final String APPID = "wx0f044c73bf3010ac";
//	private static final String APPSECRET = "9af838acf6deb98ff7b2f99c7930f9bc";
	private String accesstoken = getAccessToken();

	public void init() throws ServletException {
		System.out.println("WeiXinction loading..............");
		// String[] configLocations = new String[] {"config/baseConfig.xml"};
		// context = new ClassPathXmlApplicationContext(configLocations);
		try {
			setMenu();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

			req.setCharacterEncoding("UTF-8");
			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();
			Enumeration<String> en = req.getParameterNames();
			while (en.hasMoreElements()) {
				String name = en.nextElement();
				System.out.println((name + "==" + req.getParameter(name)));
			}

			String signature = req.getParameter("signature");
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce");
			String echostr = req.getParameter("echostr");
			String token = "132456sdsfdfgfdghhf";
			String[] str = { token, timestamp, nonce };
			Arrays.sort(str); // 字典序排序
			String bigStr = str[0] + str[1] + str[2];
			MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
			md.update(bigStr.getBytes());
			byte[] b = md.digest();

			StringBuilder sbDes = new StringBuilder();
			String tmp = null;
			for (int i = 0; i < b.length; i++) {
				tmp = (Integer.toHexString(b[i] & 0xFF));
				if (tmp.length() == 1) {
					sbDes.append("0");
				}
				sbDes.append(tmp);
			}
			String digest = sbDes.toString();
			if (digest.equals(signature)) {
				if (StringUtils.isNotEmpty(echostr)) {
					out.print(echostr);
					return;
				}

				InputStream stream = req.getInputStream();
				Map<String, String> reqMap = parseXMLData(stream);
				String msgType = reqMap.get("MsgType");// 消息类型
				String fromUserName = reqMap.get("FromUserName");

				System.out.println("fromUserName=" + fromUserName);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMenu() throws UnsupportedEncodingException {
		String url = String
				.format(" https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%1$s",
						accesstoken);

		String content =

		" {\n"
				+ "\"button\":[\n"
				+ "{\n"
				+ "\"name\":\"关于美伦\",\n"
				+ "\"sub_button\":[\n"
				+ "{\n"
				+ "\"type\":\"view\",\n"
				+ "\"name\":\"中心简介\",\n"
				+ "\"url\":\"http://www.gaia-hotel.com/xx.jsp\"\n"
				+ "},\n"
				+ "{\n"
				+ "\"type\":\"view\",\n"
				+ "\"name\":\"美伦服务0\",\n"
				+ "\"url\":\""
				+"https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APPID+"&redirect_uri=http://ws.hnruo.com/xx.jsp&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"
					
				+ "\"},\n" + "{\n" + "\"type\":\"view\",\n"
				+ "\"name\":\"美伦专家\",\n" + "\"url\":\"http://v.qq.com/\"\n"
				+ "},\n" + "{\n" + "\"type\":\"click\",\n"
				+ "\"name\":\"联系客服\",\n" + "\"key\":\"V1001_LX_KF\"\n" + "}]\n"
				+ "},\n" + "{\n" + "\"name\":\"健康中心\",\n"
				+ "\"sub_button\":[\n" + "{\n" + "\"type\":\"view\",\n"
				+ "\"name\":\"美伦健康\",\n" + "\"url\":\"http://www.soso.com/\"\n"
				+ "},\n" + "{\n" + "\"type\":\"view\",\n"
				+ "\"name\":\"卓越环境\",\n" + "\"url\":\"http://v.qq.com/\"\n"
				+ "}]\n" + "},\n" + "{\n" + "\"type\":\"view\",\n"
				+ "\"name\":\"健康商城\",\n" + "\"url\":\"http://v.qq.com/\"\n"
				+ "}\n" + "]}";

		JsonUtil ju = new JsonUtil();
		@SuppressWarnings("unchecked")
		Map<String, Object> j = (Map<String, Object>) ju.toObject(HttpWeb
				.getGetResponse(url, content));
		System.out.println("line151:"+j);
	}

	public String iNull(Object v) {
		if (v == null) {
			return "";
		} else {
			return v + "";
		}
	}

	public String decode(String val) {
		String x = val.replaceAll("\\$", "\\%");
		try {
			return URLDecoder.decode(x, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public String encode(String val) {
		try {
			String x = URLEncoder.encode(val, "UTF-8");
			return x.replaceAll("\\%", "\\$");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	private String getUserGroup(String accessToken) {
		return "";
	}

	private String getAccessToken() {// 获取 access token
		String url = String
				.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%1$s&secret=%2$s",
						APPID, APPSECRET);
		JsonUtil ju = new JsonUtil();
		@SuppressWarnings("unchecked")
		Map<String, Object> j = (Map<String, Object>) ju.toObject(HttpWeb
				.getGetResponse(url));
		System.out.println(j);
		return j.get("access_token") + "";

	}

	public Map<String, String> parseXMLData(InputStream is) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		SAXReader sr = new SAXReader();
		Document doc = sr.read(is);
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		Iterator<Element> it = root.elementIterator();
		Element sonElement = null;

		while (it.hasNext()) {
			sonElement = it.next();
			result.put(sonElement.getName(), sonElement.getText());
		}
		return result;
	}
}
