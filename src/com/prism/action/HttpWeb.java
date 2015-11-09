package com.prism.action;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpWeb {
	public static void main(String[] args) {
		String html = HttpWeb
				.getGetResponse("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxada2bd7c71ca91b7&secret=95ead5f16779d4374b5c823ad4aa2fed");
		JsonUtil ju = new JsonUtil();

		System.out.println(ju.toObject(html));
	}

	@SuppressWarnings("deprecation")
	public static String getGetResponse(String url, String content) {
		String html = "";
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		// 创建GET方法的实例

		PostMethod postMethod = new UTF8PostMethod(url);
		// 使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		postMethod.setRequestBody(content);
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ postMethod.getStatusLine());
			}
			// 处理内容
			html = postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
		return html;
	}

	public static String getGetResponse(String url) {
		String html = "";
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		// 创建POST方法的实例
		PostMethod getMethod = new UTF8PostMethod(url);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			// 处理内容
			html = getMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return html;
	}

	public static class UTF8PostMethod extends PostMethod {
		public UTF8PostMethod(String url) {
			super(url);
		}

		@Override
		public String getRequestCharSet() {
			// return super.getRequestCharSet();
			return "UTF-8";
		}
	}
}
