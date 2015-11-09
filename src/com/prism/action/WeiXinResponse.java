package com.prism.action;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.prism.action.common.JsonUtil;

public class WeiXinResponse {
	public static void main(String[] args) {
		JsonUtil ju = new JsonUtil();
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("touser", "ljq");
		m.put("msgtype", "text");
		m.put("agentid", "1");
		Map<String, Object> text = new HashMap<String, Object>();
		text.put("content", "你好~");
		m.put("text", text);
		m.put("safe", "0");
		String content = ju.toJson(m);
		System.out.println(content);
		 WeiXinResponse w = new WeiXinResponse();
		 String t = w
		 .getCoAccessToken("wx970cddf1694f3fc3",
		 "oiVl1KmK-G0iwHorQIXjlXqs4Y3klleuzXK6fWvvVW_1fTK3gYu0nq13nNeZifyu");
		 w.coResText(t, content);

	}

	// 获取企业AccessToken
	public String getCoAccessToken(String corpid, String corpsecret) {
		String url = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%1$s&corpsecret=%2$s",
						corpid, corpsecret);
		JsonUtil ju = new JsonUtil();
		@SuppressWarnings("unchecked")
		Map<String, Object> j = (Map<String, Object>) ju.toObject(HttpWeb
				.getGetResponse(url));
		return j.get("access_token") + "";

	}

	public void coResText(String accesstoken, String content) {
		String url = String
				.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%1$s",
						accesstoken);
		// String url =
		// "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=jMd2AbPnWdLRAp8D34Gr9JY1JFQX938gVLVtXzfwlv-s_7IjZdr616moiJoR1aJH";
		JsonUtil ju = new JsonUtil();
		@SuppressWarnings("unchecked")
		Map<String, Object> j = (Map<String, Object>) ju.toObject(HttpWeb
				.getGetResponse(url, content));
		System.out.println(j);
	}

	// 返回文本内容
	public String resText(Map<String, String> reqMap, String conent)
			throws Exception {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");// 添加文档根
		Element ToUserName = root.addElement("ToUserName");
		ToUserName.setText(reqMap.get("FromUserName"));

		Element FromUserName = root.addElement("FromUserName");
		FromUserName.setText(reqMap.get("ToUserName"));

		Element CreateTime = root.addElement("CreateTime");
		CreateTime.setText(reqMap.get("CreateTime"));

		Element MsgType = root.addElement("MsgType");
		MsgType.setText("text");

		Element Content = root.addElement("Content");
		Content.setText(conent);

		return document.asXML();
	}
	// 返回图文内容
	public String resNews(Map<String, String> reqMap, String conent)
			throws Exception {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");// 添加文档根
		Element ToUserName = root.addElement("ToUserName");
		ToUserName.setText(reqMap.get("FromUserName"));

		Element FromUserName = root.addElement("FromUserName");
		FromUserName.setText(reqMap.get("ToUserName"));

		Element CreateTime = root.addElement("CreateTime");
		CreateTime.setText(reqMap.get("CreateTime"));

		Element MsgType = root.addElement("MsgType");
		MsgType.setText("news");

		Element ArticleCount = root.addElement("ArticleCount");
		ArticleCount.setText("1");
		
		Element item = root.addElement("item");
		
		Element Title = item.addElement("Title");
		Title.setText("Title1");
		Element Description = item.addElement("Description");
		Description.setText("Description1");
		Element PicUrl = item.addElement("PicUrl");
		PicUrl.setText("http://mmbiz.qpic.cn/mmbiz/mXh9UNKvAuRWBpf5MaBxm4chwAHY6SRfgCMx1VX4ibzJRHFjdVr2S9iazVgoL9wpO9tm3JibHs49rs7dbj3yIyuNg/0");
		
		Element Url = item.addElement("Url");
		Url.setText("http://mmbiz.qpic.cn/mmbiz/mXh9UNKvAuRWBpf5MaBxm4chwAHY6SRfgCMx1VX4ibzJRHFjdVr2S9iazVgoL9wpO9tm3JibHs49rs7dbj3yIyuNg/0");
		

		return document.asXML();
	}
	
	
}
