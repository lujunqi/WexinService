package com.prism.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public class WeiXinQYAction extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	private static final String sToken = "RYvyYfIsXb21apVhrEuebbNaD";// 这个Token是随机生成，但是必须跟企业号上的相同
	private static final String sCorpID = "wx970cddf1694f3fc3";// 这里是你企业号的CorpID
	private static final String sEncodingAESKey = "HiWN3UoqCAh0MAoUWdzj5ngsd27Uz82Zib27NryNSnb";// 这个EncodingAESKey是随机生成，但是必须跟企业号上的相同
	private static final String APPSECRET = "oiVl1KmK-G0iwHorQIXjlXqs4Y3klleuzXK6fWvvVW_1fTK3gYu0nq13nNeZifyu";// 管理组的凭证密钥

	public void init() throws ServletException {
		System.out.println("WeiXinction loading..............");
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 微信加密签名
		String sVerifyMsgSig = request.getParameter("msg_signature");
		// 时间戳
		String sVerifyTimeStamp = request.getParameter("timestamp");
		// 随机数
		String sVerifyNonce = request.getParameter("nonce");
		// 随机字符串
		String sVerifyEchoStr = request.getParameter("echostr");
		String sEchoStr; // 需要返回的明文
		PrintWriter out = response.getWriter();
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String name = en.nextElement();
			System.out.println((name + "==" + request.getParameter(name)));
		}

		WXBizMsgCrypt wxcpt;
		try {
			String digest = SHA1.getSHA1(sToken, sVerifyTimeStamp,
					sVerifyNonce, sVerifyMsgSig);
			System.out.println(digest);
			if (digest.equals(sVerifyMsgSig)) {
				if (StringUtils.isNotEmpty(sVerifyEchoStr)) {
					System.out.println("============================");
					wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
					sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
							sVerifyNonce, sVerifyEchoStr);
					out.print(sEchoStr);
					;
					return;
				} else {
					WeiXinResponse wx = new WeiXinResponse();
					String co_access_token = wx.getCoAccessToken(sCorpID,
							APPSECRET);
					System.out.println(co_access_token);
				}
			}

		} catch (AesException e1) {
			e1.printStackTrace();
		}
	}

}
